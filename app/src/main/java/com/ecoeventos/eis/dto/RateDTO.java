/**
 * 
 */
package com.ecoeventos.eis.dto;

import java.io.Serializable;
import java.util.Date;

import com.ecoeventos.eis.dto.EventDTO;

/**
 * @author developer
 *
 */
public class RateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7442854186657149556L;
	/**
	 * 
	 */

	private EventDTO event;
	private UserDTO user;
	private Integer rate;
	private String comment;
	private Date date;

	/**
	 * 
	 */
	public RateDTO() {
		super();
	}

	public RateDTO(EventDTO event, UserDTO user, Integer rate, String comment, Date date) {
		super();
		this.event = event;
		this.user = user;
		this.rate = rate;
		this.comment = comment;
		this.date = date;
	}
	
	public EventDTO getEvent() {
		return event;
	}

	public void setEvent(EventDTO event) {
		this.event = event;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Rate [event=" + event + ", user=" + user + ", rate=" + rate
				+ ", comment=" + comment + ", date=" + date + "]";
	}

}
