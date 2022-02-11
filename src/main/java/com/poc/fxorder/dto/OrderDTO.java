package com.poc.fxorder.dto;

import javax.validation.constraints.NotBlank;

import com.poc.fxorder.domain.OrderType;

/**
 * Class OrderDTO
 * 
 * It is the DTO class to be able to deal with request for new order.
 * 
 * @author PM
 *
 */
public class OrderDTO {
	
	@NotBlank(message = "Currency is mandatory")
	private String currency;	
	
	@NotBlank(message = "Order type is mandatory")
	private OrderType bidOrAsk;
	
	@NotBlank(message = "Price is mandatory")
	private float price; 
	
	@NotBlank(message = "Amount is mandatory") 
    private long amount;


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
	 * @return the bidOrAsk
	 */
	public OrderType getBidOrAsk() {
		return bidOrAsk;
	}

	/**
	 * @param bidOrAsk the bidOrAsk to set
	 */
	public void setBidOrAsk(OrderType bidOrAsk) {
		this.bidOrAsk = bidOrAsk;
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
	
	
}
