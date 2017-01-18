package ke.co.eaglesafari.items;


import ke.co.eaglesafari.constant.Constant;

public class FeedBackItem
	{
		public static final String	KEY_STAR			= "star";
		public static final String	KEY_FEED_TEXT	= "feed_back";
		public static final String	KEY_REQUEST_ID	= "request_id";
		public static final String	URL_FEEDBACK	= Constant.DOMAIN + "feed_back";
		String							star				= "";
		String							request_id		= "";
		String							feed_text		= "";

		public String getStar()
			{
				return star;
			}

		public void setStar(String star)
			{
				this.star = star;
			}

		public String getRequest_id()
			{
				return request_id;
			}

		public void setRequest_id(String request_id)
			{
				this.request_id = request_id;
			}

		public String getFeed_text()
			{
				return feed_text;
			}

		public void setFeed_text(String feed_text)
			{
				this.feed_text = feed_text;
			}

		/**
		 * @return
		 * @see String#isEmpty()
		 */
		public boolean isEmpty()
			{
				return star.isEmpty() && feed_text.isEmpty();

			}

	}
