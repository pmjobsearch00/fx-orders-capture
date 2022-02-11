/*
 * No Copyright intended or License applies just for templating.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.fxorder.domain;

/**
 * Class OrderData
 * 
 * The model class to represent an Order
 * 
 * @author PM
 *
 */
public class OrderData {

	/** The order id. */
	private String id;
	
	/** The currency pair. */
	private String currency;	
	
	/** The trade price. */
	private float price; 
	
	/** The trade amount. */
    private long amount;
    
	/** The BID or ASK. */
	private OrderType orderType;

	/** The date when order recorded. */
	private long orderDate;
	
	
	/**
	 * @param id
	 * @param currency
	 * @param price
	 * @param amount
	 * @param orderType
	 * @param orderDate
	 */
	public OrderData(String id, String currency, float price, long amount, OrderType orderType, long orderDate) {
		super();
		this.id = id;
		this.currency = currency;
		this.price = price;
		this.amount = amount;
		this.orderType = orderType;
		this.orderDate = orderDate;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the price
	 */
	public float getPrice() {	
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * @return the amount
	 */
	public long getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(long amount) {
		this.amount = amount;
	}

	/**
	 * @return the orderType
	 */
	public OrderType getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the orderDate
	 */
	public long getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(long orderDate) {
		this.orderDate = orderDate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrderData [id=" + id + ", currency=" + currency + ", price=" + price + ", amount=" + amount
				+ ", orderType=" + orderType + ", orderDate=" + orderDate + "]";
	}
	
	public String toCustomHash() {
		return currency.trim().replace("/", "")+price+amount;
	}
	
}
