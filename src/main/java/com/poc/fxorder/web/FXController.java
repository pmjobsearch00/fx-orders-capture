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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.poc.fxorder.dto.OrderDTO;
import com.poc.fxorder.dto.OrderString;
import com.poc.fxorder.exception.InvalidInputException;
import com.poc.fxorder.exception.ServiceException;
import com.poc.fxorder.service.SimpleFXTradingService;

/**
 * FXController for GUI 
 *
 * @author PM
 */
@Controller
public class FXController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FXController.class);
	
	@Autowired
	private SimpleFXTradingService service;

	/**
	 * Landing home page
	 */
	@RequestMapping("/")
	public String home() {
    	return "home";
    }	
	
	/**
	 * Page for a new FX order to be created
	 */
	@RequestMapping("/newOrder")
	public String newOrder(Model model){
		model.addAttribute("orderDTO", new OrderDTO());
    	return "newOrder";
    }	
	
	/**
	 * Stores a new FX order
	 */
	@RequestMapping(value = "/saveOrder", method = RequestMethod.POST)
    public String save(OrderDTO orderDTO, BindingResult result, Model model){
		
		model.addAttribute("orderDTO", new OrderDTO());
		
	    if ((null == result) || (null == orderDTO) || (result.hasErrors())) {
	        model.addAttribute("orderMsg", "Order could not be saved!");
	        return "newOrder";
	    } else if ((null == orderDTO.getCurrency()) || (orderDTO.getCurrency().trim().length() < 7)) {
	        model.addAttribute("orderMsg", "Valid Currency pair is mandatory!");
	        return "newOrder";
	    } else if (null == orderDTO.getBidOrAsk()) {
	        model.addAttribute("orderMsg", "Order type is mandatory!");
	        return "newOrder";
	    } else if (orderDTO.getPrice() <= 0) {
	        model.addAttribute("orderMsg", "Valid Price is mandatory!");
	        return "newOrder";
	    } else if (orderDTO.getAmount() <= 0) {
	        model.addAttribute("orderMsg", "Valid Amount is mandatory!");
	        return "newOrder";
	    }
	    
	    try {
	        String status = service.saveOrder(orderDTO.getCurrency(), orderDTO.getPrice(), orderDTO.getBidOrAsk(), 
	        		orderDTO.getAmount());
	        
	            model.addAttribute("orderMsg", status);
	        
	    } catch (InvalidInputException ei ) {
	    	LOGGER.debug("++++++++++++++++++++++++++++: " + ei.getMessage());
            model.addAttribute("orderMsg", ei.getMessage());
	    } catch (ServiceException es) {
	    	LOGGER.debug("++++++++++++++++++++++++++++: " + es.getMessage());
            model.addAttribute("orderMsg", es.getMessage());
	    }
	    
        return "newOrder";

    }
	
	
	/**
	 * Page for a search FX order page landing
	 */
	@RequestMapping("/searchOrders")
	public String searchOrders(Model model){
		model.addAttribute("orderString", new OrderString());
    	return "matchedOrders";
    }
	
	/**
	 * Action for a cancel order
	 */
    @RequestMapping(value = "/cancelOrder/{id}", method = RequestMethod.GET)
    public String cancelOrder(@PathVariable("id") String Id, Model model) {
		model.addAttribute("orderString", new OrderString());
		model.addAttribute("orderMsg", "Function not yet implented!");
    	return "redirect:/cancelOrder";
    }
    
	/**
	 * Action for a cancel order page landing
	 */
	@RequestMapping("/cancelOrder")
	public String cancelOrder(Model model){
		model.addAttribute("orderString", new OrderString());
		model.addAttribute("orderMsg", "Function not implented!");
    	return "matchedOrders";
    }
	
	/**
	 * Returns matching FX order
	 */
	@RequestMapping("/searchOrder")
	public String matchedOrders(OrderString orderString, BindingResult result, Model model) {
		
		model.addAttribute("orderString", orderString);
		
	    if ((null == result) || (null == orderString) || (result.hasErrors())) {
	        model.addAttribute("orderMsg", "Order ID invalid!");
	        return "matchedOrders";
	    } 
	    
	    try {
	    	
	    	if (service.matchingOrders(orderString).isPresent()) {
	    		
	    		model.addAttribute("matchedOrder", service.matchingOrders(orderString).get());
	    	} else {
	    		model.addAttribute("orderMsg", "Order ID does not exists!");
	    	}
	    	
	        
	    } catch (InvalidInputException ei ) {
	    	LOGGER.debug("++++++++++++++++++++++++++++: " + ei.getMessage());
            model.addAttribute("orderMsg", ei.getMessage());
	    } catch (ServiceException es) {
	    	LOGGER.debug("++++++++++++++++++++++++++++: " + es.getMessage());
            model.addAttribute("orderMsg", es.getMessage());
	    }


    	return "matchedOrders";
    }	
	
	
}
