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

    final static String MAIL_RECEIVERS = "mail.receivers";
    final static String MAIL_FROM = "mail.from";
    final static String MAIL_PASSWORD = "mail.password";
    final static String FILTER_ZONES = "filter.zones";
    final static String FILTER_MAX_PRICE = "filter.max.price";
    final static String FILTER_MIN_ROOMS = "filter.min.rooms";
    final static String FILTER_INCLUDE_GROUND_FLOOR = "filter.include.ground.floor";
    final static String FILTER_PICTURES_REQUIRED = "filter.pictures.required";
    final static String FILTER_PUBLISHED_PERIOD = "filter.published.period";

    private Properties props;

    public Configuration() {

        props = new Properties();

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

        try {
            // load a properties file
            props.load(getClass().getClassLoader().getResourceAsStream("settings.properties"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println(props.toString());
    }


    private List<String> parseList(String list) {
        List<String> result = new ArrayList<>();
        String[] array = list.split(",");
        for (String str: array) {
            result.add(str);
        }
        return result;
    }

    private int getInt(String key) {
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
