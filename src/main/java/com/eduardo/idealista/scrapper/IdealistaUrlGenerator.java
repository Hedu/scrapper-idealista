package com.eduardo.idealista.scrapper;

import com.eduardo.idealista.model.SearchTerms;
import org.jsoup.nodes.Document;

/**
 * Created by hedu on 9/04/17.
 */
public class IdealistaUrlGenerator {


    private final static String BASIC_URL = "https://www.idealista.com";
    private final static String RENT = "/alquiler-viviendas";
    private final static String MAX_PRICE_PARAM = "con-precio-hasta_";
    private final static String ZERO_ROOM_PARAM = "estudios,de-un-dormitorio";
    private final static String ONE_ROOM_PARAM = "de-un-dormitorio";
    private final static String TWO_ROOM_PARAM = "de-dos-dormitorios";
    private final static String THREE_ROOM_PARAM = "de-tres-dormitorios";
    private final static String FOUR_ROOM_PARAM = "de-cuatro-cinco-habitaciones-o-mas";
    private final static String NON_GROUND_FLOOR = "ultimas-plantas,plantas-intermedias";
    private final static String MEDIA_IS_REQUIRED = "fotos-o-video";
    private final static String LD_PERIOD = "publicado_ultimas-24-horas";
    private final static String LW_PERIOD = "publicado_ultima-semana";
    private final static String LM_PERIOD = "publicado_ultimo-mes";

    public static String generateSearchUrl(String city, String neighborhood, int maxPrice, int minRooms,
                                     boolean includeGroundFloor, boolean picturesRequired,
                                     SearchTerms.PublishedPeriod publishedPeriod) {

        StringBuilder sb = new StringBuilder(BASIC_URL);
        sb.append(RENT);
        addParameter(sb, city);
        addParameter(sb, neighborhood);

        boolean newParam = true;
        if (maxPrice != -1) {
            newParam = addOrContinueParam(sb, MAX_PRICE_PARAM, true);
            sb.append(maxPrice);
        }

        switch (minRooms) {
            case 0:
                newParam = addOrContinueParam(sb, ZERO_ROOM_PARAM,newParam);
            case 1:
                newParam = addOrContinueParam(sb, ONE_ROOM_PARAM,newParam);
            case 2:
                newParam = addOrContinueParam(sb, TWO_ROOM_PARAM,newParam);
            case 3:
                newParam = addOrContinueParam(sb, THREE_ROOM_PARAM,newParam);
            case 4:
                newParam = addOrContinueParam(sb, FOUR_ROOM_PARAM,newParam);
        }

        if (!includeGroundFloor) {
            newParam = addOrContinueParam(sb, NON_GROUND_FLOOR,newParam);
        }

        if (picturesRequired) {
            newParam = addOrContinueParam(sb, MEDIA_IS_REQUIRED,newParam);
        }

        switch (publishedPeriod) {
            case lastDay:
                addOrContinueParam(sb, LD_PERIOD,newParam);
                break;
            case lastWeek:
                addOrContinueParam(sb, LW_PERIOD, newParam);
                break;
            case lastMonth:
                addOrContinueParam(sb, LM_PERIOD, newParam);
        }

        return sb.toString();
    }

    private static boolean addOrContinueParam(StringBuilder sb, String parameter, boolean newParam) {
        if (newParam) {
            addParameter(sb, parameter);
        }
        else {
            continueParameter(sb, parameter);
        }
        return false;
    }

    private static void addParameter(StringBuilder sb, String parameter) {
        sb.append("/" + parameter);
    }

    private static void continueParameter(StringBuilder sb, String parameter) {
        sb.append("," + parameter);
    }

    public static String getFlatUrl(String flatHref) {
        return BASIC_URL + flatHref;
    }

}
