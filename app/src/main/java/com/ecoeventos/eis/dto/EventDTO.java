/**
 *
 */
package com.ecoeventos.eis.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * @author developer
 */
public class EventDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4367020156515752924L;
    private Integer id;
    private UserDTO userCreated;
    private String name;
    private String description;
    private Date date;
    private String place;
    @JsonIgnore
    private Double rate;

    /**
     *
     */
    public EventDTO() {
        super();
    }

    public EventDTO(Integer id, UserDTO userCreated, String name, String description, Date date, String place, Double rate) {
        this.id = id;
        this.userCreated = userCreated;
        this.name = name;
        this.description = description;
        this.date = date;
        this.place = place;
        this.rate = rate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserDTO getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(UserDTO userCreated) {
        this.userCreated = userCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "id=" + id +
                ", userCreated=" + userCreated +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", place='" + place + '\'' +
                ", rate=" + rate +
                '}';
    }
}
