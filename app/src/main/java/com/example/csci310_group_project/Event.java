package com.example.csci310_group_project;

public class Event {
    private String eventName;
    private String eventDate;
    private String eventOrganizor;
    private String eventDescription;
    private String eventLocation;
    private String eventType;
    private Integer eventCost;
    private int img;
    private boolean registered;
    private boolean favorite;
    private double distanceToUser = 0;
    private double lat;
    private double lng;

    public double getLat(){
        return lat;
    }

    public void setLat(double lat){
        this.lat = lat;
    }

    public double getLng(){
        return lng;
    }

    public void setLng(double lat){
        this.lat = lat;
    }

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
        String[] parts = eventDate.split(",");
        String[] dateParts = parts[0].split("/");
        return dateParts;
    }

    public String[] getEventTimeParts(){
        String[] parts = eventDate.split(", ");
        String[] timeParts = parts[1].split(":");
        return timeParts;
    }


    public Integer getEventYear(){
        String[] components = getEventDateParts();
        return Integer.valueOf(components[2]);
    }

    public Integer getEventMonth(){
        String[] components = getEventDateParts();
        return Integer.valueOf(components[0]);
    }

    public Integer getEventDay(){
        String[] components = getEventDateParts();
        return Integer.valueOf(components[1]);
    }

    public Integer getEventHour(){
        String[] timeParts = getEventTimeParts();
        return Integer.valueOf(timeParts[0]);
    }

    public Integer getEventMinute(){
        String[] timeParts = getEventTimeParts();
        return Integer.valueOf(timeParts[1]);
    }

    public long GetEventTimeCompleteExpr() {
        return (long) getEventYear() * 1296000 + getEventMonth() * 108000 + getEventDay() * 3600 + getEventHour() * 60 + getEventMinute();
    }

    public Boolean getRegistered(){
        return registered;
    }

    public void setRegistered(Boolean b){
        this.registered = b;
    }

    public boolean getFavorite(){return favorite;}
    public void setFavorite(Boolean favorite){this.favorite = favorite;}

    public void setDistanceToUser(double distanceToUser){
        this.distanceToUser = distanceToUser;
    }

    public double getDistanceToUser(){
        return this.distanceToUser;
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

    public Event(String eventName, String eventType, String eventDate, String eventOrganizor, String eventDescription, String eventLocation, Integer eventCost, int img, boolean registered,
                 Boolean favorite, double lat, double lng) {
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.eventOrganizor = eventOrganizor;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.eventCost = eventCost;
        this.img = img;
        this.registered = registered;
        this.favorite = favorite;
        this.lat = lat;
        this.lng = lng;
    }

    public Event() {}
}
