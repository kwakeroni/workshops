package com.example;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.HijrahChronology;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

import org.junit.Test;

// java.time API
// New expressive date+time API
// Immutable
// Strongly influenced by Joda-Time (and by the same teamlead)
public class TimeAPI {

	// Clock - source of time information
	@Test
	public void clock(){
		Clock clock = Clock.systemDefaultZone();
		Clock testingClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
		
		long timeInMillis = clock.millis();
		Instant now = clock.instant(); 
		
		System.out.println("Clock == " + clock);
		
	}
	
	// Instant - an instant in time
	@Test
	public void instant(){
		Instant epoch = Instant.EPOCH;
		Instant now = Instant.now();
		System.out.println("Instant.now == " + now);
	}
	
	// Locals - a date and/or time without zoning information (most common use)
	@Test
	public void locals(){
		LocalDate date = LocalDate.now();
		LocalDate date2 = LocalDate.of(2014, 12, 14); // 1-based
		LocalDate date3 = LocalDate.of(2014, Month.DECEMBER, 14);
		LocalDate date4 = LocalDate.ofYearDay(2014, 348);
		System.out.println("Day 348 in 2014 == " + date4);
		
		LocalTime time = LocalTime.now();
		LocalTime time2 = LocalTime.of(15, 17);
		LocalTime time3 = LocalTime.of(15,  17, 30, 511);
		System.out.println("15:17 == " + time2);
		
		LocalDateTime dateTime = date.atStartOfDay();
		LocalDateTime dateTime2 = date2.atTime(time2);
		LocalDateTime dateTime3 = time3.atDate(date3);
		System.out.println("14/12/2014 15:17 == " + dateTime2);
		
		LocalDate january7 = LocalDate.of(2015, Month.JANUARY, 7);
		LocalDate deadline = LocalDate.now().plus(Period.ofDays(15));
	}
	
	// Date Time in a certain zone
	@Test
	public void zoned(){
		ZonedDateTime dateTime = Instant.now().atZone(ZoneId.of("UTC"));
		ZonedDateTime dateTime2 = Instant.now().atZone(ZoneId.of("UTC+1"));
		System.out.println(dateTime + " <--> " + dateTime2);
		
		LocalDateTime _1517 = LocalDate.of(2014, 12, 14).atTime(15, 17);
		ZonedDateTime date = _1517.atZone(ZoneId.systemDefault());	
		ZonedDateTime dateUtc = LocalDate.of(2014, 12, 14).atTime(14, 17).atZone(ZoneId.of("UTC"));
		
		System.out.println(date + " <--> " + dateUtc);
		System.out.println(date.toInstant().equals(dateUtc.toInstant()));
		
		Instant instant = Instant.now();
		OffsetDateTime offset = instant.atOffset(ZoneOffset.ofHours(1));
		ZonedDateTime zoned = instant.atZone(ZoneId.of("UTC+01"));
		System.out.println(offset + " <--> " + zoned);
		System.out.println(offset.equals(zoned));
		System.out.println(offset.toInstant().equals(zoned.toInstant()));
		
		
		
	}
	
	// Classes to contain partial date information
	// No more need for "empty" fields (and errors due to time information)
	@Test
	public void partials(){
		MonthDay birthDay = MonthDay.of(Month.DECEMBER, 14);
		System.out.println(birthDay);
		LocalDate thisYear = birthDay.atYear(2014);
		System.out.println(thisYear);
		
		YearMonth yearMonth = YearMonth.of(2014, Month.DECEMBER);
		System.out.println(yearMonth);
		LocalDate _14th = yearMonth.atDay(14);
		LocalDate end = yearMonth.atEndOfMonth();
		System.out.println(YearMonth.of(2014,  2).atEndOfMonth() + " <--> "
						 + YearMonth.of(2016,  2).atEndOfMonth());
	}
	

	
	// Period in dates
	@Test
	public void period(){
		Period period = Period.ofDays(7);
		Period period2 = Period.ofMonths(3);
		
		Period period3 = period2.minus(period);
		Period period4 = Period.between(LocalDate.of(2014, 01, 15), LocalDate.of(2014, 12, 14));
		System.out.println(period2);
		System.out.println(period3);
		System.out.println(period4);
		System.out.println(period2.addTo(LocalDate.of(2014,  01,  15)));
	}
	
	// Duration in time
	@Test
	public void duration(){
		Duration duration = Duration.ofHours(8);
		Duration duration2 = Duration.ofDays(15);
		// no Duration duration3 = Duration.of(3, ChronoUnit.MONTHS);
		
		Duration duration4 = duration2.plus(duration);
		Duration duration5 = Duration.between(LocalTime.of(15, 17), LocalDateTime.now().toLocalTime());
		System.out.println(duration2);
		System.out.println(duration4);
		System.out.println(duration5);
		
		System.out.println(Duration.ofSeconds(2).addTo(LocalDateTime.of(2014, 12, 14, 23, 59, 59)));
	}
	
	// Other Calendars
	@Test
	public void noniso(){
		ChronoLocalDate iso = LocalDate.now();
		ChronoLocalDate japanese = JapaneseChronology.INSTANCE.dateNow();
		ChronoLocalDate buddhist = ThaiBuddhistChronology.INSTANCE.dateNow();
		ChronoLocalDate hijrah = HijrahChronology.INSTANCE.dateNow();
		
		System.out.println(iso);
		System.out.println(japanese);
		System.out.println(buddhist);
		System.out.println(hijrah);
		
	}
}
