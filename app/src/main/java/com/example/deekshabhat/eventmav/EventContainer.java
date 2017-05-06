package com.example.deekshabhat.eventmav;

import android.app.Application;

/**
 * Created by deekshabhat on 4/25/17.
 */

public class EventContainer extends Application{

    private  String eventName;
    private  String eventDate;
    private  String evenLocation;
    private  String evenDescription;
    private  String eventCount;
    private  String eventCategory;
    private String userID;

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

    public String getEvenLocation() {
        return evenLocation;
    }

    public void setEvenLocation(String evenLocation) {
        this.evenLocation = evenLocation;
    }

    public String getEvenDescription() {
        return evenDescription;
    }

    public void setEvenDescription(String evenDescription) {
        this.evenDescription = evenDescription;
    }

    public String getEventCount() {
        return eventCount;
    }

    public void setEventCount(String eventCount) {
        this.eventCount = eventCount;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }






}
