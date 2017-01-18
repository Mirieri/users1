package ke.co.eaglesafari.items;



import ke.co.eaglesafari.constant.Constant;
public class RequestItemReceive
	{
		String	name	= "", destination = "", telephone = "", pick_up = "",
				request_id = "", created_at = "", service = "", status = "",
				amount = "", profile_picture = "";

		public String getProfile_picture()
			{
				return profile_picture;
			}

		public void setProfile_picture(String profile_picture)
			{
				this.profile_picture = profile_picture;
			}

		public String getService()
			{
				return service;
			}

		public void setService(String service)
			{
				this.service = service;
			}

		public String getName()
			{
				return name;
			}

		public void setName(String name)
			{
				this.name = name;
			}

		public String getDestination()
			{
				return destination;
			}

		public void setDestination(String destination)
			{
				this.destination = destination;
			}

		public String getTelephone()
			{
				return telephone;
			}

		public void setTelephone(String telephone)
			{
				this.telephone = telephone;
			}

		public String getPick_up()
			{
				return pick_up;
			}

		public void setPick_up(String pick_up)
			{
				this.pick_up = pick_up;
			}

		public String getRequest_id()
			{
				return request_id;
			}

		public void setRequest_id(String request_id)
			{
				this.request_id = request_id;
			}

		public String getCreated_at()
			{
				return created_at;
			}

		public void setCreated_at(String created_at)
			{
				this.created_at = created_at;
			}

		public boolean isEmpty()
			{
				if (created_at.isEmpty())
					return true;
				if (request_id.isEmpty())
					return true;
				if (pick_up.isEmpty())
					return true;
				if (telephone.isEmpty())
					return true;
				if (destination.isEmpty())
					return true;
				return name.isEmpty();

			}

		public String getStatus()
			{
				return status;
			}

		public void setStatus(String status)
			{
				this.status = status;
			}

		public String getAmount()
			{
				return amount;
			}

		public void setAmount(String amount)
			{
				this.amount = amount;
			}

		public class Cons
			{

				public static final String	KEY_NAME					= "name";
				public static final String	KEY_DESTINATION		= "destination";
				public static final String	KEY_TELEPHONE			= "telephone";
				public static final String	KEY_PICK_UP				= "pick_up";
				public static final String	KEY_REQUEST_ID			= "request_id";
				public static final String	KEY_CREATED_AT			= "created_at";
				public static final String	KEY_SERVICE				= "service";
				public static final String	KEY_STATUS				= "status";
				public static final String	KEY_AMOUNT				= "amount";
				public static final String	KEY_PROFILE_PICTURE	= "profile_picture";
				public static final String	URL_REQUEST				= Constant.DOMAIN
																						+ "request";
				public static final String	URL_MARK_FINISHED		= Constant.DOMAIN
																						+ "mark_finished";

			}

	}
