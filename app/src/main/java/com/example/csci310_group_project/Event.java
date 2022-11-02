package com.example.csci310_group_project;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String eventName;
    private String eventDate;
    private String eventOrganizor;
    private String eventDescription;
    private String eventLocation;
    private String eventType;
    private Integer eventCost;
    private int img;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String[] getEventDateParts(){
        // TODO: divide date via '/'
        String[] parts = eventDate.split("/");
        return parts;
    }

    public Integer getEventYear(){
        String[] components = getEventDateParts();
        return Integer.valueOf(components[2]);
    }

    public Integer getEventMonth(){
        String[] components = getEventDateParts();
        return Integer.valueOf(components[1]);
    }

    public Integer getEventDay(){
        String[] components = getEventDateParts();
        return Integer.valueOf(components[0]);
    }




    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventOrganizor() {
        return eventOrganizor;
    }

    public void setEventOrganizor(String eventOrganizor) {
        this.eventOrganizor = eventOrganizor;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public Integer getEventCost() {
        return eventCost;
    }

    public void setEventCost(Integer eventCost) {
        this.eventCost = eventCost;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public Event(String eventName, String eventType, String eventDate, String eventOrganizor, String eventDescription, String eventLocation, Integer eventCost, int img) {
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.eventOrganizor = eventOrganizor;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.eventCost = eventCost;
        this.img = img;
    }
}
