package com.example.solution;

import java.time.Clock;
import java.time.Duration;
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
import java.time.temporal.TemporalUnit;

import org.junit.Test;

public class Exercise_Time {

    @Test
    public void exercise1(){
        // Obtain the current time in milliseconds (since Epoch etc...)
        System.out.println(System.currentTimeMillis());
        System.out.println(Clock.systemDefaultZone().millis());
        System.out.println(Clock.systemUTC().millis());
         System.out.println(Clock.systemDefaultZone().instant().toEpochMilli());
        System.out.println(Clock.systemUTC().instant().getEpochSecond());
        System.out.println(Clock.systemUTC().instant().toEpochMilli());
        System.out.println(System.currentTimeMillis());
    }
    
    @Test
    public void exercise2(){
        // Obtain the current date and time
        System.out.println(LocalDateTime.now());
        // Obtain the current date and time in the UTC time zone
        System.out.println(ZonedDateTime.now(Clock.systemUTC()));
    }
    
    @Test
    public void exercise3(){
        // Obtain the number of days in the current month
        System.out.println(LocalDate.now().lengthOfMonth());
        
        // Verify if we are currently in a leap year
        System.out.println(LocalDate.now().isLeapYear());
        
        // Obtain the number of days in the current year
        System.out.println(LocalDate.now().lengthOfYear());
        
        // How many days are there in 2076 ?
        System.out.println(Year.of(2076).length());
        
        // How many days are there in june ?
        System.out.println(Month.JUNE.length(false));
        
        // How many days are there in february 2392 ?
        System.out.println(YearMonth.of(2392, Month.FEBRUARY).lengthOfMonth());
    }
    
    @Test
    public void exercise4(){
        // Create an object that represents your birthday
        MonthDay birthday = MonthDay.of(Month.DECEMBER, 14);
        System.out.println(birthday);
        
        // Calculate your age in days
        long days = birthday.atYear(1981).until(LocalDate.now(), ChronoUnit.DAYS);
        System.out.println(days);
        
        // How long to go until you are 25000 days old ?
        Period period = LocalDate.now().until(birthday.atYear(1981).plus(Period.ofDays(25000)));
        System.out.println(period);
    }
    
}
