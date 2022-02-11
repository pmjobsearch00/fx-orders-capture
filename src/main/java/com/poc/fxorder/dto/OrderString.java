package com.poc.fxorder.dto;

import javax.validation.constraints.NotBlank;

/**
 * Class OrderString
 * 
 * It is the DTO class to be able to deal with search request
 * 
 * @author PM
 *
 */
public class OrderString {
	
	@NotBlank(message = "Order ID is mandatory")
	private String id;

	/**
	 * @return the id
	 */
	public String getId() {
		return id==null?id:id.trim();
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id==null?id:id.trim();;
	}	
	
}
