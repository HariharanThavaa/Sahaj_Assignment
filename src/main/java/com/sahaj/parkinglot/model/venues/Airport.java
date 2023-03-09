package com.sahaj.parkinglot.model.venues;

import com.sahaj.parkinglot.model.SpotType;
import com.sahaj.parkinglot.model.Venue;
import com.sahaj.parkinglot.model.VenueType;
import com.sahaj.parkinglot.model.exception.SpotTypeNotSupportedException;

import java.util.Set;

import static com.sahaj.parkinglot.model.SpotType.CAR_OR_SUV;
import static com.sahaj.parkinglot.model.SpotType.MOTORCYCLE_OR_SCOOTER;
import static com.sahaj.parkinglot.model.VenueType.AIRPORT;
import static java.lang.String.format;

public class Airport extends Venue {

    private static final long FIRST_FLAT_RATE_INTERVAL_UPPER_BOUND_FOR_MOTORCYCLE_OR_SCOOTER = 1;
    private static final long FIRST_FLAT_RATE_FOR_MOTORCYCLE_OR_SCOOTER = 0;
    private static final long SECOND_FLAT_RATE_INTERVAL_UPPER_BOUND_FOR_MOTORCYCLE_OR_SCOOTER = 8;
    private static final long SECOND_FLAT_RATE_FOR_MOTORCYCLE_OR_SCOOTER = 40;
    private static final long THIRD_FLAT_RATE_INTERVAL_UPPER_BOUND_FOR_MOTORCYCLE_OR_SCOOTER = 24;
    private static final long THIRD_FLAT_RATE_FOR_MOTORCYCLE_OR_SCOOTER = 60;
    private static final long DAILY_RATE_FOR_MOTORCYCLE_OR_SCOOTER = 80;
    private static final long FIRST_FLAT_RATE_INTERVAL_UPPER_BOUND_FOR_CAR_OR_SUV = 12;
    private static final long FIRST_FLAT_RATE_FOR_CAR_OR_SUV = 60;
    private static final long SECOND_FLAT_RATE_INTERVAL_UPPER_BOUND_FOR_CAR_OR_SUV = 24;
    private static final long SECOND_FLAT_RATE_FOR_CAR_OR_SUV = 80;
    private static final long DAILY_RATE_FOR_CAR_OR_SUV = 100;

    @Override
    public VenueType getType() {
        return AIRPORT;
    }

    @Override
    protected long calculateFee(SpotType spotType, long hours) {
        switch (spotType) {
            case MOTORCYCLE_OR_SCOOTER:
                return calculateFeeForMotorcycleOrScooter(hours);
            case CAR_OR_SUV:
                return calculateFeeForCarOrSUV(hours);
            default:
                throw new SpotTypeNotSupportedException(format("Spot type [%s] is not supported", spotType));
        }
    }

    private long calculateFeeForMotorcycleOrScooter(long hours) {
        if (hours <= FIRST_FLAT_RATE_INTERVAL_UPPER_BOUND_FOR_MOTORCYCLE_OR_SCOOTER) {
            return FIRST_FLAT_RATE_FOR_MOTORCYCLE_OR_SCOOTER;
        } else if (hours <= SECOND_FLAT_RATE_INTERVAL_UPPER_BOUND_FOR_MOTORCYCLE_OR_SCOOTER) {
            return SECOND_FLAT_RATE_FOR_MOTORCYCLE_OR_SCOOTER;
        } else if (hours <= THIRD_FLAT_RATE_INTERVAL_UPPER_BOUND_FOR_MOTORCYCLE_OR_SCOOTER) {
            return THIRD_FLAT_RATE_FOR_MOTORCYCLE_OR_SCOOTER;
        } else {
            return DAILY_RATE_FOR_MOTORCYCLE_OR_SCOOTER * (hours / 24 + 1);
        }
    }

    private long calculateFeeForCarOrSUV(long hours) {
        if (hours <= FIRST_FLAT_RATE_INTERVAL_UPPER_BOUND_FOR_CAR_OR_SUV) {
            return FIRST_FLAT_RATE_FOR_CAR_OR_SUV;
        } else if (hours <= SECOND_FLAT_RATE_INTERVAL_UPPER_BOUND_FOR_CAR_OR_SUV) {
            return SECOND_FLAT_RATE_FOR_CAR_OR_SUV;
        } else {
            return DAILY_RATE_FOR_CAR_OR_SUV * (hours / 24 + 1) ;
        }
    }

    @Override
    protected Set<SpotType> getSupportedSpotType() {
        return Set.of(MOTORCYCLE_OR_SCOOTER, CAR_OR_SUV);
    }
}
