package ke.co.eaglesafari.views;

import android.content.Context;
import android.widget.Toast;

public class Tos
	{
		Context	context;

		public Tos(Context context)
			{
				this.context = context;
			}

		public void s(String text)
			{
				Toast.makeText(context, text, 300).show();
			}

	}
