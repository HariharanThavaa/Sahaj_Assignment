package com.sahaj.parkinglot.model;

import com.sahaj.parkinglot.model.exception.NoSpaceAvailableException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.sahaj.parkinglot.model.SpotType.*;
import static com.sahaj.parkinglot.model.VenueType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class VenueTest {

    @Test
    void testInitialiseVenue() {
        Venue venue = Venue.initialiseVenue(MALL, Map.of(MOTORCYCLE_OR_SCOOTER, 2));
        assertThat(venue.getType()).isEqualTo(MALL);
        Set<SpotType> expected = new HashSet<>(Arrays.asList(MOTORCYCLE_OR_SCOOTER, CAR_OR_SUV,
                BUS_OR_TRUCK));
        assertThat(venue.getSupportedSpotType()).isEqualTo(expected);
    }

    @Test
    void testExample1() throws NoSpaceAvailableException {
        Venue venue = Venue.initialiseVenue(MALL, Map.of(MOTORCYCLE_OR_SCOOTER, 2));
        // Park motorcycle
        ParkingTicket ticket1 = venue.park(MOTORCYCLE_OR_SCOOTER, LocalDateTime.of(2022, 5, 29, 14, 4, 7));
        assertThat(ticket1.toString())
                .isEqualTo("Parking Ticket:\n" +
                        " Ticket Number: 001\n" +
                        " Spot Number: 1\n" +
                        " Entry Date-time: 29-May-2022 14:04:07");

        // Park Scooter
        ParkingTicket ticket2 = venue.park(MOTORCYCLE_OR_SCOOTER, LocalDateTime.of(2022, 5, 29, 14, 44, 7));
        assertThat(ticket2.toString())
                .isEqualTo("Parking Ticket:\n" +
                        " Ticket Number: 002\n" +
                        " Spot Number: 2\n" +
                        " Entry Date-time: 29-May-2022 14:44:07");

        // Park Scooter
        assertThatExceptionOfType(NoSpaceAvailableException.class)
                .isThrownBy(() -> venue.park(MOTORCYCLE_OR_SCOOTER, LocalDateTime.now()))
                .withMessage("No space available");

        // Move out scooter, ticket number 002
        ParkingReceipt receipt1 = venue.unpark(MOTORCYCLE_OR_SCOOTER, ticket2,
                LocalDateTime.of(2022, 5, 29, 15, 40, 7));
        assertThat(receipt1.toString())
                .isEqualTo("Parking Receipt:\n" +
                        " Receipt Number: R-001\n" +
                        " Entry Date-time: 29-May-2022 14:44:07\n" +
                        " Exit Date-time: 29-May-2022 15:40:07\n" +
                        " Fees: 10");

        // Park motorcycle
        ParkingTicket ticket3 = venue.park(MOTORCYCLE_OR_SCOOTER, LocalDateTime.of(2022, 5, 29, 15, 59, 7));
        assertThat(ticket3.toString())
                .isEqualTo("Parking Ticket:\n" +
                        " Ticket Number: 003\n" +
                        " Spot Number: 2\n" +
                        " Entry Date-time: 29-May-2022 15:59:07");

        // Move out motorcycle, ticket number 001
        ParkingReceipt receipt2 = venue.unpark(MOTORCYCLE_OR_SCOOTER, ticket1,
                LocalDateTime.of(2022, 5, 29, 17, 44, 7));
        assertThat(receipt2.toString())
                .isEqualTo("Parking Receipt:\n" +
                        " Receipt Number: R-002\n" +
                        " Entry Date-time: 29-May-2022 14:04:07\n" +
                        " Exit Date-time: 29-May-2022 17:44:07\n" +
                        " Fees: 40");
    }

    @Test
    void testExample2() throws NoSpaceAvailableException {
        // Mall parking lot
        Venue venue = Venue.initialiseVenue(MALL,
                Map.of(MOTORCYCLE_OR_SCOOTER, 100, CAR_OR_SUV, 80, BUS_OR_TRUCK, 10));

        // Motorcycle parked for 3 hours and 30 mins. Fees: 40
        ParkingTicket ticket1 = venue.park(MOTORCYCLE_OR_SCOOTER, LocalDateTime.of(2022, 5, 29, 14, 0, 0));
        ParkingReceipt receipt1 = venue.unpark(MOTORCYCLE_OR_SCOOTER, ticket1,
                LocalDateTime.of(2022, 5, 29, 17, 30, 0));
        assertThat(receipt1.getFees()).isEqualTo(40);

        // Car parked for 6 hours and 1 min. Fees: 140
        ParkingTicket ticket2 = venue.park(CAR_OR_SUV, LocalDateTime.of(2022, 5, 30, 14, 0, 0));
        ParkingReceipt receipt2 = venue.unpark(CAR_OR_SUV, ticket2,
                LocalDateTime.of(2022, 5, 30, 20, 1, 0));
        assertThat(receipt2.getFees()).isEqualTo(140);

        // Truck parked for 1 hour and 59 mins. Fees: 100
        ParkingTicket ticket3 = venue.park(BUS_OR_TRUCK, LocalDateTime.of(2022, 5, 28, 14, 0, 0));
        ParkingReceipt receipt3 = venue.unpark(BUS_OR_TRUCK, ticket3,
                LocalDateTime.of(2022, 5, 28, 15, 59, 0));
        assertThat(receipt3.getFees()).isEqualTo(100);
    }

    @Test
    void testExample3() throws NoSpaceAvailableException {
        // Stadium parking lot
        Venue venue = Venue.initialiseVenue(STADIUM,
                Map.of(MOTORCYCLE_OR_SCOOTER, 1000, CAR_OR_SUV, 1500));

        // Motorcycle parked for 3 hours and 40 mins. Fees: 30
        ParkingTicket ticket1 = venue.park(MOTORCYCLE_OR_SCOOTER, LocalDateTime.of(2022, 5, 1, 14, 0, 0));
        ParkingReceipt receipt1 = venue.unpark(MOTORCYCLE_OR_SCOOTER, ticket1,
                LocalDateTime.of(2022, 5, 1, 17, 40, 0));
        assertThat(receipt1.getFees()).isEqualTo(30);

        // Motorcycle parked for 14 hours and 59 mins. Fees: 390
        ParkingTicket ticket2 = venue.park(MOTORCYCLE_OR_SCOOTER, LocalDateTime.of(2022, 5, 2, 0, 0, 0));
        ParkingReceipt receipt2 = venue.unpark(MOTORCYCLE_OR_SCOOTER, ticket2,
                LocalDateTime.of(2022, 5, 2, 14, 59, 0));
        assertThat(receipt2.getFees()).isEqualTo(390);

        // Electric SUV parked for 11 hours and 30 mins. Fees: 180.
        ParkingTicket ticket3 = venue.park(CAR_OR_SUV, LocalDateTime.of(2022, 5, 3, 0, 0, 0));
        ParkingReceipt receipt3 = venue.unpark(CAR_OR_SUV, ticket3,
                LocalDateTime.of(2022, 5, 3, 11, 30, 0));
        assertThat(receipt3.getFees()).isEqualTo(180);

        // SUV parked for 13 hours and 5 mins. Fees: 580.
        ParkingTicket ticket4 = venue.park(CAR_OR_SUV, LocalDateTime.of(2022, 5, 4, 0, 0, 0));
        ParkingReceipt receipt4 = venue.unpark(CAR_OR_SUV, ticket4,
                LocalDateTime.of(2022, 5, 4, 13, 5, 0));
        assertThat(receipt4.getFees()).isEqualTo(580);
    }

    @Test
    void testExample4() throws NoSpaceAvailableException {
        // Airport parking lot
        Venue venue = Venue.initialiseVenue(AIRPORT,
                Map.of(MOTORCYCLE_OR_SCOOTER, 200, CAR_OR_SUV, 500));

        // Motorcycle parked for 55 mins. Fees: 0
        ParkingTicket ticket1 = venue.park(MOTORCYCLE_OR_SCOOTER, LocalDateTime.of(2022, 6, 1, 0, 0, 0));
        ParkingReceipt receipt1 = venue.unpark(MOTORCYCLE_OR_SCOOTER, ticket1,
                LocalDateTime.of(2022, 6, 1, 0, 55, 0));
        assertThat(receipt1.getFees()).isEqualTo(0);

        // Motorcycle parked for 14 hours and 59 mins. Fees: 60
        ParkingTicket ticket2 = venue.park(MOTORCYCLE_OR_SCOOTER, LocalDateTime.of(2022, 6, 2, 0, 0, 0));
        ParkingReceipt receipt2 = venue.unpark(MOTORCYCLE_OR_SCOOTER, ticket2,
                LocalDateTime.of(2022, 6, 2, 14, 59, 0));
        assertThat(receipt2.getFees()).isEqualTo(60);

        // Motorcycle parked for 1 day and 12 hours. Fees: 160
        ParkingTicket ticket3 = venue.park(MOTORCYCLE_OR_SCOOTER, LocalDateTime.of(2022, 6, 3, 0, 0, 0));
        ParkingReceipt receipt3 = venue.unpark(MOTORCYCLE_OR_SCOOTER, ticket3,
                LocalDateTime.of(2022, 6, 4, 12, 0, 0));
        assertThat(receipt3.getFees()).isEqualTo(160);

        // Car parked for 50 mins. Fees: 60
        ParkingTicket ticket4 = venue.park(CAR_OR_SUV, LocalDateTime.of(2022, 6, 5, 0, 0, 0));
        ParkingReceipt receipt4 = venue.unpark(CAR_OR_SUV, ticket4,
                LocalDateTime.of(2022, 6, 5, 0, 50, 0));
        assertThat(receipt4.getFees()).isEqualTo(60);

        // SUV parked for 23 hours and 59 mins. Fees: 80
        ParkingTicket ticket5 = venue.park(CAR_OR_SUV, LocalDateTime.of(2022, 6, 6, 0, 0, 0));
        ParkingReceipt receipt5 = venue.unpark(CAR_OR_SUV, ticket5,
                LocalDateTime.of(2022, 6, 6, 23, 59, 0));
        assertThat(receipt5.getFees()).isEqualTo(80);

        // Car parked for 3 days and 1 hour. Fees: 400
        ParkingTicket ticket6 = venue.park(CAR_OR_SUV, LocalDateTime.of(2022, 6, 7, 0, 0, 0));
        ParkingReceipt receipt6 = venue.unpark(CAR_OR_SUV, ticket6,
                LocalDateTime.of(2022, 6, 10, 1, 0, 0));
        assertThat(receipt6.getFees()).isEqualTo(400);
    }

}