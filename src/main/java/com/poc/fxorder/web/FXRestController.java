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

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.poc.fxorder.domain.OrderData;
import com.poc.fxorder.dto.OrderDTO;
import com.poc.fxorder.dto.OrderString;
import com.poc.fxorder.exception.InvalidInputException;
import com.poc.fxorder.exception.ServiceException;
import com.poc.fxorder.service.SimpleFXTradingService;

/**
 * Rest Controller for FX services.
 *
 * @author PM
 */
@RestController
public class FXRestController {

	private final SimpleFXTradingService service;

	private static final Logger LOGGER = LoggerFactory.getLogger(FXRestController.class);

	@Autowired
	public FXRestController(final SimpleFXTradingService service) {
		this.service = service;
	}

	/**
	 * Stores a new FX order
	 */
	@RequestMapping(value = "/v1/createOrder", method = RequestMethod.POST)
	public ResponseEntity<?> createOrder(@RequestBody(required = true) OrderDTO orderDTO) {

		try {
			String status = service.saveOrder(orderDTO.getCurrency(), orderDTO.getPrice(), orderDTO.getBidOrAsk(),
					orderDTO.getAmount());

				return new ResponseEntity<>(status, HttpStatus.OK);

		} catch (InvalidInputException ei) {
			LOGGER.debug("++++++++++++++++++++++++++++: " + ei.getMessage());
			return new ResponseEntity<>(ei.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (ServiceException es) {
			LOGGER.debug("++++++++++++++++++++++++++++: " + es.getMessage());
			return new ResponseEntity<>(es.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	
	/**
	 * Returns matching FX order
	 */
	@RequestMapping(value = "/v1/searchOrders", method = RequestMethod.POST)
	public ResponseEntity<?> matchedOrders(@RequestBody(required = true) OrderString orderString) {

		try {
			
			Optional<OrderData> order = service.matchingOrders(orderString);
			if (order.isPresent()) {
				return new ResponseEntity<>(service.matchingOrders(orderString), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(orderString.getId()+": Order_not_exists", HttpStatus.OK);
			}
			
		} catch (InvalidInputException ei ) {
			LOGGER.debug("++++++++++++++++++++++++++++: " + ei.getMessage());
            return new ResponseEntity<>(ei.getMessage(), HttpStatus.BAD_REQUEST);
	    } catch (ServiceException es) {
	    	LOGGER.debug("++++++++++++++++++++++++++++: " + es.getMessage());
            return new ResponseEntity<>(es.getMessage(), HttpStatus.BAD_REQUEST);
	    }

	}
	

}
