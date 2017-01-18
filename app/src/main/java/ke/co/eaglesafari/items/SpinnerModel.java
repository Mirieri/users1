package ke.co.eaglesafari.items;

public class SpinnerModel
	{
		public SpinnerModel()
			{

			}

		public SpinnerModel(String id, String value)
			{
				this.id = id;
				this.value = value;
			}

		String	value;
		String	id;

		public String getValue()
			{
				return value;
			}

		public SpinnerModel setValue(String value)
			{
				this.value = value;
				return this;
			}

		public String getId()
			{
				return id;
			}

		public SpinnerModel setId(String id)
			{
				this.id = id;
				return this;
			}

	}