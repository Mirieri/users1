package ke.co.eaglesafari.items;

public class MyAccountItem
	{
		public static final String	TABLE_NAME	= "mYaCCOUNT";
		public static final String	DB_NAME		= "DBNAMEaCCOUNT";

		public class Const
			{
				public static final String	KEY_NAME			= "name";
				public static final String	KEY_TIME			= "time";
				public static final String	KEY_BALANCE		= "balance";
				public static final String	KEY_POINTS		= "points";
				public static final String	KEY_TELEPHONE	= "telephone";

			}

		String	name	= "", time = "", balance = "", points = "",
				telephone = "";

		public String getName()
			{
				return name;
			}

		public void setName(String name)
			{
				this.name = name;
			}

		public String getTime()
			{
				return time;
			}

		public void setTime(String time)
			{
				this.time = time;
			}

		public String getBalance()
			{
				return balance;
			}

		public void setBalance(String balance)
			{
				this.balance = balance;
			}

		public String getPoints()
			{
				return points;
			}

		public void setPoints(String points)
			{
				this.points = points;
			}

		public String getTelephone()
			{
				return telephone;
			}

		public void setTelephone(String telephone)
			{
				this.telephone = telephone;
			}

	}
