package org.codeseoul.event_member_management;

import org.codeseoul.event_member_management.event.Event;
import org.codeseoul.event_member_management.event.EventRepository;
import org.codeseoul.event_member_management.rsvp_state.RsvpState;
import org.codeseoul.event_member_management.rsvp_state.RsvpStateRepository;
import org.codeseoul.event_member_management.series.Series;
import org.codeseoul.event_member_management.series.SeriesRepository;
import org.codeseoul.event_member_management.sns_service.SnsService;
import org.codeseoul.event_member_management.sns_service.SnsServiceRepository;
import org.codeseoul.event_member_management.venue.Venue;
import org.codeseoul.event_member_management.venue.VenueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;

import java.sql.Timestamp;
import java.util.Optional;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
@ConditionalOnProperty(name = "app.seed-database")
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(
            EventRepository eventRepository,
            SeriesRepository seriesRepository,
            SnsServiceRepository snsServiceRepository,
            RsvpStateRepository rsvpStateRepository,
            VenueRepository venueRepository
    ) {

        return args -> {

            Example<Series> exampleSeries = Example.of(new Series("New Series"));
            Optional<Series> seriesExistenceCheck = seriesRepository.findOne(exampleSeries);
            if (seriesExistenceCheck.isPresent()) {
                return;
            }

            Series newSeries = seriesRepository.save(new Series(
                    "New Series"
            ));
            log.info("Preloading " + newSeries);
            log.info("Preloading " + seriesRepository.save(new Series(
                    "Another Series"
            )));

            log.info("Preloading " + eventRepository.save(new Event(
                    "New Event",
                    "such new, so event",
                    Timestamp.valueOf("2022-06-22 08:51:00"),
                    120,
                    "https://imgs.search.brave.com/ETZSXHRxNOTB6w7R9iEP1HFR6ps4blygjf8HHUi-gYk/rs:fit:1200:1200:1/g:ce/aHR0cDovL2kwLmt5/bS1jZG4uY29tL2Vu/dHJpZXMvaWNvbnMv/b3JpZ2luYWwvMDAw/LzAwMC8xMDcvc21p/bHkuanBn",
                    "Yo mama's house",
                    null,
                    newSeries
            )));
            log.info("Preloading " + eventRepository.save(new Event(
                    "Another Event",
                    "This is madness",
                    Timestamp.valueOf("2022-06-23 08:51:00"),
                    120,
                    "https://imgs.search.brave.com/ETZSXHRxNOTB6w7R9iEP1HFR6ps4blygjf8HHUi-gYk/rs:fit:1200:1200:1/g:ce/aHR0cDovL2kwLmt5/bS1jZG4uY29tL2Vu/dHJpZXMvaWNvbnMv/b3JpZ2luYWwvMDAw/LzAwMC8xMDcvc21p/bHkuanBn",
                    "Under da sea",
                    null,
                    newSeries
            )));
            log.info("Preloading " + snsServiceRepository.save(new SnsService(
                    "Meetup",
                    "https://meetup.com/favicon.ico"
            )));
            log.info("Preloading " + snsServiceRepository.save(new SnsService(
                    "Facebook",
                    "https://facebook.com/favicon.ico"
            )));
            log.info("Preloading " + rsvpStateRepository.save(new RsvpState(
                    "yes"
            )));
            log.info("Preloading " + rsvpStateRepository.save(new RsvpState(
                    "no"
            )));
            log.info("Preloading " + rsvpStateRepository.save(new RsvpState(
                    "attended"
            )));
            log.info("Preloading " + venueRepository.save(new Venue(
                    "WCoding",
                    "서울 영등포구 선유로49길 23 11층 1101호",
                    "https://naver.me/FiLsgFXq",
                    "",
                    "https://place.map.kakao.com/1096784914",
                    "https://g.page/Wcoding?share"
            )));
        };
    }
}
