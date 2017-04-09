package com.eduardo.idealista.model

import spock.lang.Specification;
/**
 * Created by hedu on 9/04/17.
 */
class SearchTermsSpec extends Specification{

    void 'SearchTerms constructor and getters are working properly'() {

        given: 'The values we will use to create the SearchTerms object'

        Map<String, List<String>> zones = ['madrid' : ['chamberi', 'chamartin', 'salamanca'],
                                           'oviedo' : ['centro-casco-historico']]
        int maxPrice = 700
        int minRooms = 1
        boolean includeGroundFloor = false
        boolean picturesRequired = true
        SearchTerms.PublishedPeriod publishedPeriod = SearchTerms.PublishedPeriod.lastDay

        when : 'we create the SearchTerms object'

        SearchTerms searchTerms = new SearchTerms(
                zones, maxPrice, minRooms, includeGroundFloor,
                picturesRequired, publishedPeriod)

        then: 'The SearchTerms object contains the same values'
            zones == searchTerms.getZones()

        and:

        searchTerms.getMaxPrice() == maxPrice
        searchTerms.getMinRooms() == minRooms
        searchTerms.isIncludeGroundFloor() == includeGroundFloor
        searchTerms.isPicturesRequired() == picturesRequired
        searchTerms.getPublishedPeriod() == publishedPeriod

    }
}
