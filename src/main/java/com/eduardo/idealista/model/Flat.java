package com.eduardo.idealista.model;

import java.util.Objects;

/**
 * Created by hedu on 6/04/17.
 */
public class Flat {
    private String url;
    private String title;
    private int price;
    private int size;
    private int rooms;
    private int floor;

    public Flat(String url, String title, int price, int size, int rooms, int floor) {
        this.url = url;
        this.title = title;
        this.price = price;
        this.size = size;
        this.rooms = rooms;
        this.floor = floor;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public int getSize() {
        return size;
    }

    public int getRooms() {
        return rooms;
    }

    public int getFloor() {
        return floor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Url: ").append(url);
        sb.append("; Title: ").append(title);
        sb.append("; Price: ").append(price);
        sb.append("; Size: ").append(size);
        sb.append("; Rooms: ").append(rooms);
        sb.append("; Floor: ").append(floor);

        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof Flat)) {
            return false;
        }
        Flat flat = (Flat) other;
        return Objects.equals(url, flat.getUrl()) && Objects.equals(title, flat.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, title);
    }


}
