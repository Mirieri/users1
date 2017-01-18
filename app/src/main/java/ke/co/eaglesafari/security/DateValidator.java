package ke.co.eaglesafari.security;

import org.joda.time.LocalDate;
import org.joda.time.Years;

public class DateValidator
	{
		public DateValidator()
			{

			}

		public boolean is_above(int year, int month, int date, int years)
			{
				Years age = null;
				try
					{
						LocalDate birthdate = null;

						birthdate = new LocalDate(year, month, date);

						LocalDate now = new LocalDate();
						age = Years.yearsBetween(birthdate, now);
					} catch (IllegalArgumentException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				int a = age.getYears();
				return a >= 18;

			}

	}
