package com.eduardo.idealista.settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.*;

/**
 * Created by hedu on 10/04/17.
 */
public class Configuration {

    final static String FILE_PATH = "settings.properties";

    public final static String MAIL_RECEIVERS = "mail.receivers";
    public final static String MAIL_FROM = "mail.from";
    public final static String MAIL_PASSWORD = "mail.password";
    public final static String FILTER_ZONES = "filter.zones";
    public final static String FILTER_MAX_PRICE = "filter.max.price";
    public final static String FILTER_MIN_ROOMS = "filter.min.rooms";
    public final static String FILTER_INCLUDE_GROUND_FLOOR = "filter.include.ground.floor";
    public final static String FILTER_PICTURES_REQUIRED = "filter.pictures.required";
    public final static String FILTER_PUBLISHED_PERIOD = "filter.published.period";

    private Properties props;

    public Configuration() {

        props = new Properties();
        //setDefaultValues();

        try {
            // load a properties file
            props.load(getClass().getClassLoader().getResourceAsStream("settings.properties"));

        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
        System.out.println(props.toString());
    }

    private void setDefaultValues() {
        //set default values
        Map<String, List<String>> zones = new HashMap<>();

        List<String> neighborhoods = new ArrayList<>();

        neighborhoods.add("retiro");
        neighborhoods.add("chamartin");
        neighborhoods.add("chamberi");
        neighborhoods.add("salamanca");

        zones.put("madrid", neighborhoods);

        props.put(FILTER_ZONES, zones);
        props.put(FILTER_MAX_PRICE, "650");
        props.put(FILTER_MIN_ROOMS, "1");
        props.put(FILTER_INCLUDE_GROUND_FLOOR, "false");
        props.put(FILTER_PICTURES_REQUIRED, "true");
        props.put(FILTER_PUBLISHED_PERIOD, "day");
    }

    public Map<String, List<String>> getMap(String key) {
        Map<String, List<String>> result = new HashMap<>();
        String[] array = props.getProperty(key).split(";");
        Arrays.stream(array).forEach(str -> {
            String[] list = str.split("=");
            result.put(list[0], toList(list[1]));
        });

        return result;
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    public List<String> getList(String key) {
        return toList(props.getProperty(key));
    }

    private List<String> toList(String value) {
        List<String> result = new ArrayList<>();
        String[] array = value.split(",");
        Arrays.stream(array).forEach(str -> result.add(str));
        return result;
    }

    public boolean getBoolean(String key) {
        return Boolean.valueOf(key);
    }

    public int getInt(String key) {
        int result = 0;
        if (props.containsKey(key)) {
            String value = props.getProperty(key);
            try {
                result = Integer.parseInt(value);
            }
            catch(NumberFormatException nfe) {
                System.out.println("Number Format Exception for key: " + key + " and value: " + value);
            }
        }
        return result;
    }

}
