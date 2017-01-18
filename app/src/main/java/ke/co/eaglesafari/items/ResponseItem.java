package ke.co.eaglesafari.items;

public class ResponseItem
	{
		String	success	= "0", extra = "";

		public String getMessage()
			{
				return success;
			}

		public void setSuccess(String success)
			{
				this.success = success;
			}

		public String getExtra()
			{
				return extra;
			}

		public void setExtra(String extra)
			{
				this.extra = extra;
			}

	}
