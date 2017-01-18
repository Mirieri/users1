package ke.co.eaglesafari.net.network;


public interface I_Response<T, X> {
    void onTaskCompleted(T i);

    void onTaskCommpletedMessage(X x);

    void onData(X x, T t);

}
