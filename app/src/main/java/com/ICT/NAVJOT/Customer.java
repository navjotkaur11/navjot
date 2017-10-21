package com.ICT.NAVJOT;

/**
 * Created by Android on 11-10-2017.
 */

public class Customer {
    String Title;
    String Place;
    String Date;
    String ID;

    public Customer(String title, String place, String date, String ID) {
        Title = title;
        Place = place;
        Date = date;
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
