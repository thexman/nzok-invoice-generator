package com.a9ski;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Month;

import org.junit.Test;

public class PersonalIdentifierUtilsTest {

	@Test
	public void testParseBirthDate() {
		assertEquals(LocalDate.of(1982, Month.JANUARY, 22), PersonalIdentifierUtils.parseBirthDate("8201220000"));
		assertEquals(LocalDate.of(2000, Month.JANUARY, 22), PersonalIdentifierUtils.parseBirthDate("0041220000"));
		assertEquals(LocalDate.of(1914, Month.DECEMBER, 31), PersonalIdentifierUtils.parseBirthDate("1412310000"));
		assertEquals(LocalDate.of(2014, Month.DECEMBER, 31), PersonalIdentifierUtils.parseBirthDate("1452310000"));
	}

}
