package com.sahaj.parkinglot.model;

import com.sahaj.parkinglot.model.exception.NoSpaceAvailableException;
import com.sahaj.parkinglot.model.exception.SpotTypeNotSupportedException;
import com.sahaj.parkinglot.model.exception.VenueTypeNotSupportedException;
import com.sahaj.parkinglot.model.venues.Airport;
import com.sahaj.parkinglot.model.venues.Mall;
import com.sahaj.parkinglot.model.venues.Stadium;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static com.sahaj.parkinglot.model.Venue.SpotAvailability.AVAILABLE;
import static com.sahaj.parkinglot.model.Venue.SpotAvailability.OCCUPIED;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public abstract class Venue {
    private static final Map<SpotType, Map<SpotAvailability, List<Spot>>> SPOTS = new HashMap<>();

    public abstract VenueType getType();

    public static Venue initialiseVenue(VenueType venueType, Map<SpotType, Integer> spots) {
        Venue venue = createValue(venueType);
        spots.forEach(venue::addSpots);
        return venue;
    }

    public synchronized ParkingTicket park(SpotType spotType, LocalDateTime entryTimestamp)
            throws NoSpaceAvailableException {
        if (SPOTS.get(spotType).get(AVAILABLE).isEmpty()) {
            throw new NoSpaceAvailableException("No space available");
        }

        Spot allocatedSpot = SPOTS.get(spotType).get(AVAILABLE).remove(0);

        SPOTS.get(spotType).get(OCCUPIED).add(allocatedSpot);

        return new ParkingTicket(allocatedSpot.getSpotNumber(), entryTimestamp);
    }

    public synchronized ParkingReceipt unpark(SpotType spotType, ParkingTicket ticket,
                                              LocalDateTime exitTimestamp) {

        Spot spot = new Spot(spotType, ticket.getSpotNumber());

        SPOTS.get(spotType).get(OCCUPIED).remove(spot);

        SPOTS.get(spotType).get(AVAILABLE).add(spot);

        long fee = calculateFee(spotType, ChronoUnit.HOURS.between(ticket.getEntryTimestamp(), exitTimestamp) + 1);

        return new ParkingReceipt(ticket.getEntryTimestamp(), exitTimestamp, fee);
    }

    protected abstract long calculateFee(SpotType spotType, long hours);

    protected abstract Set<SpotType> getSupportedSpotType();

    private static Venue createValue(VenueType venueType) {
        switch (venueType) {
            case MALL:
                return new Mall();
            case STADIUM:
                return new Stadium();
            case AIRPORT:
                return new Airport();
        }

        throw new VenueTypeNotSupportedException(format("Venue type [%s] is not supported", venueType));
    }

    private void addSpots(SpotType spotType, int noOfSpots) {
        if (!getSupportedSpotType().contains(spotType)) {
            throw new SpotTypeNotSupportedException(format("Spot Type [%s] is not supported by venue type [%s]", spotType, getType()));
        }

        List<Spot> availableSpots = IntStream.range(1, noOfSpots + 1)
                .boxed()
                .map(spotNumber -> new Spot(spotType, spotNumber))
                .collect(toList());

        SPOTS.put(
                spotType,
                Map.of(AVAILABLE, availableSpots, OCCUPIED, new ArrayList<>()));

    }

    protected enum SpotAvailability {
        AVAILABLE,
        OCCUPIED
    }
}
