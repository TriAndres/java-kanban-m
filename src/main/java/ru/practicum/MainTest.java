package ru.practicum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainTest {
    public static void main(String[] args) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("0");

        LocalDateTime localDateTime = LocalDateTime.now();
        String time1 = localDateTime.format(formatter);

        String time2 = localDateTime.plusDays(55).format(formatter);


        Time time = new Time("1", time1, time2);

        System.out.println(time.toString());

        LocalDateTime localDateTime1 = LocalDateTime.parse(time.getStartTime(), formatter);
        System.out.println(localDateTime1.format(formatter2));


    }
}

class Time {
    private String id;
    private String startTime;
    private String endTime;

    public Time(String id, String startTime, String endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return id + "/" + startTime + "/" + endTime;
    }
}