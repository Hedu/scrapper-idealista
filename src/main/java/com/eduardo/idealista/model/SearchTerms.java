package com.eduardo.idealista.model;

import java.util.List;
import java.util.Map;

/**
 * Created by hedu on 9/04/17.
 */
public class SearchTerms {

    public enum PublishedPeriod {all, lastDay, lastWeek, lastMonth};

    private Map<String, List<String>> zones;
    private int maxPrice;
    private int minRooms;
    private boolean includeGroundFloor;
    private boolean picturesRequired;
    private PublishedPeriod publishedPeriod;

    public SearchTerms(Map<String, List<String>> zones, int maxPrice,
                       int minRooms, boolean includeGroundFloor,
                       boolean picturesRequired, PublishedPeriod publishedPeriod) {

        this.zones = zones;
        this.maxPrice = maxPrice;
        this.minRooms = minRooms;
        this.includeGroundFloor = includeGroundFloor;
        this.picturesRequired = picturesRequired;
        this.publishedPeriod = publishedPeriod;
    }

    public Map<String, List<String>> getZones() {
        return zones;
    }

    public void setZones(Map<String, List<String>> zones) {
        this.zones = zones;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getMinRooms() {
        return minRooms;
    }

    public void setMinRooms(int minRooms) {
        this.minRooms = minRooms;
    }

    public boolean isIncludeGroundFloor() {
        return includeGroundFloor;
    }

    public void setIncludeGroundFloor(boolean includeGroundFloor) {
        this.includeGroundFloor = includeGroundFloor;
    }

    public boolean isPicturesRequired() {
        return picturesRequired;
    }

    public void setPicturesRequired(boolean picturesRequired) {
        this.picturesRequired = picturesRequired;
    }

    public PublishedPeriod getPublishedPeriod() {
        return publishedPeriod;
    }

    public void setPublishedPeriod(PublishedPeriod publishedPeriod) {
        this.publishedPeriod = publishedPeriod;
    }

    public static PublishedPeriod getPublishedPeriod(String period) {
        switch (period) {
            case "day":
                return PublishedPeriod.lastDay;
            case "week":
                return  PublishedPeriod.lastWeek;
            case "month" :
                return PublishedPeriod.lastMonth;
            default:
                return PublishedPeriod.all;
        }
    }
}
