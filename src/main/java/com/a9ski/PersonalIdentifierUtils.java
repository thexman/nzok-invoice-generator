package com.a9ski;

import java.time.LocalDate;

public class PersonalIdentifierUtils {
	public static LocalDate parseBirthDate(String personIdentifier) {
		int year = Integer.parseInt(personIdentifier.substring(0, 2));
		int month = Integer.parseInt(personIdentifier.substring(2,4));
		int date = Integer.parseInt(personIdentifier.substring(4,6));
		if (month > 40) {
			year += 2000;
			month -= 40;
		} else {
			year += 1900;
		}
		final LocalDate d = LocalDate.of(year, month, date);
		return d;
	}
}
