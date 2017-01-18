package ke.co.eaglesafari.views;

import android.widget.DatePicker;

public class DatePickerProcessor
	{
		DatePicker	dp;

		public DatePickerProcessor(DatePicker dp)
			{
				this.dp = dp;

			}

		public String getDate()
			{
				String date = String.valueOf(dp.getYear())
						+ "-"
						+ String.valueOf(dp.getMonth() + "-"
								+ String.valueOf(dp.getDayOfMonth()));
				return date;
			}

	}
