package ke.co.eaglesafari.net.network;

public interface I_netcheck<T, X>
	{
		void onTaskCompleted(T i);

		void onMessage(X x);

	}
