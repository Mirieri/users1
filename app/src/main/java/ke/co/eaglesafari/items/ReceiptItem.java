package ke.co.eaglesafari.items;


import ke.co.eaglesafari.constant.Constant;
public class ReceiptItem
	{

		RequestItemReceive			item;
		String							item_1		= "", item_2 = "", number_1 = "",
				number_2 = "", price_1 = "", price_2 = "", sub_1 = "", sub_2 = "";

		public static final String	URL_RECEIPT	= Constant.DOMAIN + "receipt";

		public RequestItemReceive getItem()
			{
				return item;
			}

		public void setItem(RequestItemReceive item)
			{
				this.item = item;
			}

		public String getItem_1()
			{
				return item_1;
			}

		public void setItem_1(String item_1)
			{
				this.item_1 = item_1;
			}

		public String getItem_2()
			{
				return item_2;
			}

		public void setItem_2(String item_2)
			{
				this.item_2 = item_2;
			}

		public String getNumber_1()
			{
				return number_1;
			}

		public void setNumber_1(String number_1)
			{
				this.number_1 = number_1;
			}

		public String getNumber_2()
			{
				return number_2;
			}

		public void setNumber_2(String number_2)
			{
				this.number_2 = number_2;
			}

		public String getPrice_1()
			{
				return price_1;
			}

		public void setPrice_1(String price_1)
			{
				this.price_1 = price_1;
			}

		public String getPrice_2()
			{
				return price_2;
			}

		public void setPrice_2(String price_2)
			{
				this.price_2 = price_2;
			}

		public String getSub_1()
			{
				return sub_1;
			}

		public void setSub_1(String sub_1)
			{
				this.sub_1 = sub_1;
			}

		public String getSub_2()
			{
				return sub_2;
			}

		public void setSub_2(String sub_2)
			{
				this.sub_2 = sub_2;
			}
	}
