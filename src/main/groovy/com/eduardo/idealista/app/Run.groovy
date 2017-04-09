package com.eduardo.idealista.app

import com.eduardo.idealista.mail.MailSender
import com.eduardo.idealista.model.Flat;
import com.eduardo.idealista.model.SearchTerms;
import com.eduardo.idealista.scrapper.SimpleIdealistaScrapper;

import java.util.Map;

/**
 * Created by hedu on 9/04/17.
 */
public class Run {

    private static Set<Flat> previousFlats = new HashSet<>()
    private static final long MILLIS = 60000

    public static void main(String[] args) {

        def zones = ['madrid': ['retiro', 'chamartin', 'chamberi', 'salamanca']]

        SearchTerms st = new SearchTerms(zones, 900, 1, false, true, SearchTerms.PublishedPeriod.lastDay)
        SimpleIdealistaScrapper sis = new SimpleIdealistaScrapper([st])

        MailSender mailSender = new MailSender("lfmbmail@gmail.com", "lfmbpass");

        while (true ) {
            def flats = sis.searchFlats();
            def mailContent = ""

            flats.each { flat ->
                if (previousFlats.add(flat)) {
                    println(flat)
                    mailContent <<= flat.toString() << "\n"
                } else println "Flat: ${flat.url} already exists"
            }

            if (mailContent != "") {
                mailSender.sendMail('eperezghedu@gmail.com', "PISOS", mailContent.toString())
            }
            Thread.sleep(MILLIS)
        }

    }
}
