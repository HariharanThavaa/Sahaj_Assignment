package com.sahaj.parkinglot.model;

import lombok.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.String.format;

@Value
public class ParkingReceipt {

    private static int receiptCounter = 0;
    String receiptNumber;
    LocalDateTime entryTimestamp;
    LocalDateTime exitTimestamp;
    long fees;

    public ParkingReceipt(LocalDateTime entryTimestamp, LocalDateTime exitTimestamp, long fees) {
        this.receiptNumber = format("R-%03d", ++receiptCounter);
        this.entryTimestamp = entryTimestamp;
        this.exitTimestamp = exitTimestamp;
        this.fees = fees;
    }

    @Override
    public String toString() {
        return format("Parking Receipt:\n" +
                " Receipt Number: %s\n" +
                " Entry Date-time: %s\n" +
                " Exit Date-time: %s\n" +
                " Fees: %d", receiptNumber,
                entryTimestamp.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss")),
                exitTimestamp.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss")),
                fees);
    }
}
