/*
 * No Copyright intended or License applies just for templating.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.fxorder.kafka.gateway;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.fxorder.domain.OrderData;
import com.poc.fxorder.domain.OrderType;
import com.poc.fxorder.dto.OrderString;

/**
 * Class OrderRecordsRepository
 * 
 * @author PM
 *
 */

@Configuration
public class KafkaPubSubHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaPubSubHandler.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private KafkaConsumer<String, String> kafkaConsumer;

	@Value("${kafka.order.capture.topic}")
	private String orderCaptureTopic;

	@Value("${kafka.order.requestOrder.topic}")
	private String orderrequestOrderTopic;

	private CountDownLatch latch = new CountDownLatch(1);

	public CountDownLatch getLatch() {
		return latch;
	}

	/**
	 * OrderRecordsRepository.add(OrderData)
	 * 
	 * @throws none
	 */
	public String add(OrderData order) {

		// Stringify the object
		ObjectMapper mapper = new ObjectMapper();
		String orderString;
		try {
			orderString = mapper.writeValueAsString(order);
		} catch (JsonProcessingException e) {

			LOGGER.error("unable to send message='{}'", order.getId(), e.getMessage());
			return "Failed_To_Record";
		}

		ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(orderCaptureTopic, orderString);

		try {
			
			//Wait for 5 seconds before timing out.
			future.get(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			LOGGER.error("Message publish response failed ='{}'", order.getId(), e.getMessage());
			return "Order_Confirmation_Not_Received";
		} catch (ExecutionException e) {
			LOGGER.error("Message publish response failed ='{}'", order.getId(), e.getMessage());
			return "Order_Confirmation_Not_Received";
		} catch (TimeoutException e) {
			LOGGER.error("Message publish response failed ='{}'", order.getId(), e.getMessage());
			return "Order_Confirmation_Not_Received";
		}

		return "Order_Recorded";
	}

	/**
	 * OrderRecordsRepository.getMatchingOrders()
	 * 
	 * @throws none
	 */
	public Optional<OrderData> getMatchingOrders(OrderString orderID) {

		OrderData order = null;

		// Stringify the object
		ObjectMapper mapper = new ObjectMapper();
		String orderRequest;
		try {
			orderRequest = mapper.writeValueAsString(orderID);
		} catch (JsonProcessingException e) {

			LOGGER.error("unable to send message='{}'", orderID.getId(), e.getMessage());
			return Optional.ofNullable(order);
		}

		kafkaTemplate.send(orderrequestOrderTopic, orderRequest);
		LOGGER.info("*** search request with orderID " + orderID.getId() + " has been publised to kafka request topic",
				orderrequestOrderTopic,
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " *** ");

			// Look for a response for 5 seconds before timing out. We can change it to an optimum response timeout period
			long t = System.currentTimeMillis();
			long end = t + 5000;
			
			kafkaConsumer.subscribe(Collections.singletonList("tradeReply"));

			try {
				while (System.currentTimeMillis() < end) {
					ConsumerRecords<String, String> records = kafkaConsumer.poll(100);

					for (ConsumerRecord<String, String> record : records) {

						if (record.value().contains(orderID.getId())) {

							JSONObject jsonObject = new JSONObject(record.value());
							
							order = new OrderData((String) jsonObject.get("orderId"),
									(String) jsonObject.get("currency"), (float) ((double) jsonObject.get("price")),
									(int) jsonObject.get("amount"),
									(String) jsonObject.get("orderType") == OrderType.ASK.toString() ? OrderType.ASK
											: OrderType.BID,
									(long) jsonObject.get("orderDate"));

							LOGGER.info(
									"*** search request with orderID " + orderID.getId()
											+ " has been retrieved from kafka tradeReply topic",
									order.toString(),
									LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
											+ " *** ");
							end = System.currentTimeMillis() - 5000;
							break;
						}

					}
				}
			} catch (Exception e) {
				LOGGER.error("unable to get a response ", orderID.getId(), e.getMessage());
				return Optional.ofNullable(order);
				
			} finally {
				
				kafkaConsumer.unsubscribe();

				LOGGER.info("*** subcription closed ", orderrequestOrderTopic,
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " *** ");
			}

		return Optional.ofNullable(order);
	}

}
