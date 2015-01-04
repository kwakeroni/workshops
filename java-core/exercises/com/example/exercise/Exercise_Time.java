package com.example.exercise;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.Period;
import java.time.ZonedDateTime;

import org.junit.Test;

public class Exercise_Time {

    @Test
    public void exercise1(){
        // Obtain the current time in milliseconds (since Epoch etc...)
        long epochMilli = 0;

        // Verify
        assertTrue(epochMilli + "<= 1420617600000", epochMilli > 1420617600000L);
        assertTrue(epochMilli + ">= 1420635600000", epochMilli < 1420635600000L);
    }
    
    @Test
    public void exercise2(){
        // Obtain the current date and time
        LocalDateTime here = null;
        
        // Obtain the current date and time in the UTC time zone
        ZonedDateTime greenwich = null;
        
        // If it is january 7th 22h30 in New York (EST), what time is it in Moscow (Europe/Moscow) ?
        ZonedDateTime inMoscow = null;
        
        
        // Verify
        assertEquals(-3600, Duration.between(here, greenwich).getSeconds());
        
        assertEquals(LocalDateTime.of(2015, Month.JANUARY, 8, 7, 30), inMoscow.toLocalDateTime());
        // or assertEquals(LocalDateTime.of(2015, Month.JANUARY, 8, 6, 30), inMoscow.toLocalDateTime());
        // See http://blogs.technet.com/b/dst2007/archive/2014/08/22/announcement-update-for-russian-time-zone-changes.aspx
    }
    
    @Test
    public void exercise3(){
        // Obtain the number of days in the current month
        int lengthMonth = 0;
        
        // Verify if we are currently in a leap year
        boolean leap = true;
        
        // Obtain the number of days in the current year
        int lengthYear = 0;
        
        // How many days are there in 2076 ?
        int lengthYear2076 = 0;
        
        // How many days are there in june ?
        int lengthJune = 0;
        
        // How many days are there in february 2392 ?
        int lengthFebruary2392 = 0;
        
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
        int year = 0;
        
        // Create an object that represents your birthday
        MonthDay birthday = null;
        
        // Calculate your age in days
        long days = 0;
        
        // How long to go until you are 25000 days old ?
        Period period = null;

        
        // Verify
        assertEquals(LocalDate.of(year,  birthday.getMonth(), birthday.getDayOfMonth()).toEpochDay() + 25000, LocalDate.now().plus(period).toEpochDay());

    }
    
}
