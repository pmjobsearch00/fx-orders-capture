/*
 * No Copyright intended or License applies just for templating.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.fxorder.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.poc.fxorder.domain.OrderData;
import com.poc.fxorder.domain.OrderType;
import com.poc.fxorder.kafka.gateway.KafkaPubSubHandler;

/**
 * JUnit Test class for FXRestController
 * 
 * @author PM
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FXRestControllerTest {

	@Autowired
	WebApplicationContext context;

	@Autowired
	KafkaPubSubHandler repository;

	private MockMvc mvc;

	String oid1 = null;
	String oid2 = null;
	String oid3 = null;
	String oid4 = null;

	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(context).build();

		OrderData order1 = new OrderData(UUID.randomUUID().toString(), "GBP/USD", (float) 2.2222, 2000L, OrderType.ASK,
				LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		OrderData order2 = new OrderData(UUID.randomUUID().toString(), "GBP/USD", (float) 3.3333, 6000L, OrderType.BID,
				LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		OrderData order3 = new OrderData(UUID.randomUUID().toString(), "GBP/USD", (float) 2.2222, 2000L, OrderType.BID,
				LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		OrderData order4 = new OrderData(UUID.randomUUID().toString(), "GBP/USD", (float) 3.3334, 6000L, OrderType.ASK,
				LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		// oid1 would look like 3c37398c-e0c4-44d6-a7cd-b7ef01000733 : Order_Recorded
		oid1 = repository.add(order1);
		oid2 = repository.add(order2);
		oid3 = repository.add(order3);
		oid4 = repository.add(order4);

	}

	@After
	public void cleanUp() {
		repository = null;
	}

	/**
	 * Test method for {@link FXRestController#createOrder(OrderDTO)}. When given a
	 * valid Order DTO
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldCreateAnOrderWithValidDTO() throws Exception {

		String orderJson = "{\"currency\":\"GBP/USD\",\"bidOrAsk\":\"ASK\",\"price\":\"5.5555\",\"amount\":\"4000\"}";

		mvc.perform(post("/v1/createOrder").content(orderJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(containsString("Order_Recorded")));

	}

	/**
	 * Test method for {@link FXRestController#createOrder(OrderDTO)}. When given an
	 * invalid Order DTO with wrong currency pair
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldFailToCreateAnOrderWithWrongCurrencyPair() throws Exception {

		String orderJson = "{\"currency\":\"GBP/EUR\",\"bidOrAsk\":\"ASK\",\"price\":\"5.5555\",\"amount\":\"4000\"}";

		mvc.perform(post("/v1/createOrder").content(orderJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("You have entered invalid currency pair!")));
	}

	/**
	 * Test method for {@link FXRestController#createOrder(OrderDTO)}. When given an
	 * invalid Order DTO with no currency pair
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldFailToCreateAnOrderWithNoCurrencyPair() throws Exception {

		String orderJson = "{\"currency\":\"\",\"bidOrAsk\":\"ASK\",\"price\":\"5.5555\",\"amount\":\"4000\"}";

		mvc.perform(post("/v1/createOrder").content(orderJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("You have entered invalid currency pair!")));
	}

	/**
	 * Test method for {@link FXRestController#createOrder(OrderDTO)}. When given an
	 * invalid Order DTO with negative price
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldFailToCreateAnOrderWithNegativePrice() throws Exception {

		String orderJson = "{\"currency\":\"GBP/USD\",\"bidOrAsk\":\"BID\",\"price\":\"-5.5555\",\"amount\":\"4000\"}";

		mvc.perform(post("/v1/createOrder").content(orderJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("You have entered invalid price range: -5.5555")));
	}

	/**
	 * Test method for {@link FXRestController#createOrder(OrderDTO)}. When given an
	 * invalid Order DTO with wrong price format
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldFailToCreateAnOrderWithWrongPriceFormat() throws Exception {

		String orderJson = "{\"currency\":\"GBP/USD\",\"bidOrAsk\":\"BID\",\"price\":\"5.55556\",\"amount\":\"400\"}";

		mvc.perform(post("/v1/createOrder").content(orderJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(content()
						.string(containsString("You have entered invalid price range. The format is x.xxxx: 5.55556")));
	}

	/**
	 * Test method for {@link FXRestController#createOrder(OrderDTO)}. When given an
	 * invalid Order DTO with no amount
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldFailToCreateAnOrderWithNoAmount() throws Exception {

		String orderJson = "{\"currency\":\"GBP/USD\",\"bidOrAsk\":\"BID\",\"price\":\"5.5555\",\"amount\":\"\"}";

		mvc.perform(post("/v1/createOrder").content(orderJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("You have entered invalid amount range: ")));
	}

	/**
	 * Test method for {@link FXRestController#searchOrders(OrderString)}. When
	 * given a valid OrderString
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldRetrieveMachingOrders() throws Exception {

		String orderSearchJson = "{\"id\":\"" + oid1.replace("Order_Recorded", "").replace(":", "").trim() + "\"}";

		mvc.perform(post("/v1/searchOrders").content(orderSearchJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(
						content().string(containsString(oid1.replace("Order_Recorded", "").replace(":", "").trim())));

	}

	/**
	 * Test method for {@link FXRestController#searchOrders(OrderString)}. When
	 * given an non-existing OrderString sent
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void shouldNotRetrieveInvalidOrder() throws Exception {

		String wrongOrderIdJson = "{\"id\":\"" + "Non-Existing-ID" + "\"}";

		mvc.perform(post("/v1/searchOrders").content(wrongOrderIdJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(containsString("Order_not_exists")));

	}
}
