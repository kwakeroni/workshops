package com.example.solution;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.Test;
import static org.junit.Assert.*;

public class Exercise_Time {

    @Test
    public void exercise1(){
        // Obtain the current time in milliseconds (since Epoch etc...)
        long epochMilli = 0;
        epochMilli = System.currentTimeMillis();
        epochMilli = Clock.systemDefaultZone().millis();
        epochMilli = Clock.systemUTC().millis();
        epochMilli = Clock.systemDefaultZone().instant().toEpochMilli();
        epochMilli = Clock.systemUTC().instant().toEpochMilli();
        
        epochMilli = LocalDateTime.of(2015, Month.JANUARY, 7, 11, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        
        // Verify
        assertTrue(epochMilli + "<= 1420617600000", epochMilli > 1420617600000L);
        assertTrue(epochMilli + ">= 1420635600000", epochMilli < 1420635600000L);
    }
    
    @Test
    public void exercise2(){
        Instant now = Instant.now();

        // Obtain the current date and time
        LocalDateTime here = LocalDateTime.now();
        boolean hereSaving = ZoneId.systemDefault().getRules().isDaylightSavings(now);

        // Obtain the current date and time in the UTC time zone
        ZonedDateTime greenwich = ZonedDateTime.now(Clock.systemUTC());
        boolean greenwichSaving = ZoneId.from(greenwich).getRules().isDaylightSavings(now);

        // If it is january 7th 22h30 in New York (America/New_York), what time is it in Moscow (Europe/Moscow) ?
        ZonedDateTime inMoscow = ZonedDateTime.of(LocalDateTime.of(2015, Month.JANUARY, 7, 22, 30), ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.of("Europe/Moscow"));
        boolean moscowSaving =  ZoneId.from(inMoscow).getRules().isDaylightSavings(now);
        
        // Verify
        // Expected value is for Europe/Brussels
        assertEquals((hereSaving ^ greenwichSaving)? -7200 : -3600,
                Duration.between(here, greenwich).getSeconds());

        // Expected value is for Europe/Brussels
        int daylightDiff = (hereSaving ^ moscowSaving)? -1 : 0;
        assertEquals(LocalDateTime.of(2015, Month.JANUARY, 8, 7 + daylightDiff, 30), inMoscow.toLocalDateTime());
        // or assertEquals(LocalDateTime.of(2015, Month.JANUARY, 8, 6, 30), inMoscow.toLocalDateTime());
        // See http://blogs.technet.com/b/dst2007/archive/2014/08/22/announcement-update-for-russian-time-zone-changes.aspx
    }
    
    @Test
    public void exercise3(){
        LocalDate now = LocalDate.now();
        now = LocalDate.of(2015,  Month.JANUARY, 7);
        
        // Obtain the number of days in the current month
        int lengthMonth = now.lengthOfMonth();
        
        // Verify if we are currently in a leap year
        boolean leap = now.isLeapYear();
        
        // Obtain the number of days in the current year
        int lengthYear = now.lengthOfYear();
        
        // How many days are there in 2076 ?
        int lengthYear2076 = Year.of(2076).length();
        
        // How many days are there in june ?
        int lengthJune = Month.JUNE.length(false);
        
        // How many days are there in february 2392 ?
        int lengthFebruary2392 = YearMonth.of(2392, Month.FEBRUARY).lengthOfMonth();
        
        // Verify
        assertEquals(31, lengthMonth);
        assertEquals(false, leap);
        assertEquals(365, lengthYear);
        assertEquals(366, lengthYear2076);
        assertEquals(30, lengthJune);
        assertEquals(29, lengthFebruary2392);
    }
    
    @Test
    public void exercise4(){
        // What year were you born ?
        int year = 1981;
        
        // Create an object that represents your birthday
        MonthDay birthday = MonthDay.of(Month.DECEMBER, 14);
        
        // Calculate your age in days
        long days = birthday.atYear(year).until(LocalDate.now(), ChronoUnit.DAYS);
        
        // How long to go until you are 25000 days old ?
        Period period = LocalDate.now().until(birthday.atYear(year).plus(Period.ofDays(25000)));

        
        // Verify
        assertEquals(LocalDate.of(year,  birthday.getMonth(), birthday.getDayOfMonth()).toEpochDay() + 25000, LocalDate.now().plus(period).toEpochDay());

    }
    
}
