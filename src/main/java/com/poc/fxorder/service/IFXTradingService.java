/*
 * No Copyright intended or License applies just for templating.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.fxorder.service;

import java.util.Optional;

import com.poc.fxorder.domain.OrderData;
import com.poc.fxorder.domain.OrderType;
import com.poc.fxorder.dto.OrderString;
import com.poc.fxorder.exception.InvalidInputException;
import com.poc.fxorder.exception.ServiceException;

/**
 * Interface IFXTradingService
 * 
 * It provides below four service functionalities
 * 
 * saveOrder()
 * deleteOrder()
 * matchingOrders()
 * unMatchingOrders()
 * 
 * @author PM
 *
 */

public interface IFXTradingService {

	/**
	 * Saves an order.
	 * @param currency price orderType and amount
	 * @return the string status
	 * @throws InvalidInputException, ServiceException
	 */
	String saveOrder(String currency, float price, OrderType orderType, long amount) 
			throws InvalidInputException, ServiceException;


	/**
	 * Returns the matching orders.
	 * @param none
	 * @return the matching OrderData list
	 * @throws InvalidInputException, ServiceException
	 */
	Optional<OrderData> matchingOrders(OrderString orderID) throws InvalidInputException, ServiceException;
	
}