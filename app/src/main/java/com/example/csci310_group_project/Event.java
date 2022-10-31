package com.example.csci310_group_project;

public class Event {
    private String eventName;
    private String eventDate;
    private String eventOrganizor;
    private String eventDescription;
    private String eventLocation;
    private int eventCost;
    private int img;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
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

    public int getEventCost() {
        return eventCost;
    }

    public void setEventCost(int eventCost) {
        this.eventCost = eventCost;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public Event(String eventName, String eventDate, String eventOrganizor, String eventDescription, String eventLocation, int eventCost, int img) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventOrganizor = eventOrganizor;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.eventCost = eventCost;
        this.img = img;
    }
}
