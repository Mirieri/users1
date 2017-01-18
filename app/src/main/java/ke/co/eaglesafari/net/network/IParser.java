package ke.co.eaglesafari.net.network;




import java.util.ArrayList;
import java.util.HashMap;

import ke.co.eaglesafari.utilities.NameValuePair;


public interface IParser<T,Y>
    {
     T requestGET(Y t, ArrayList<NameValuePair> m);
     T requestPOST(Y t, ArrayList<NameValuePair> m, Y y);
     T requestDELETE(Y t, HashMap<Y, Y> m);
    T requestPUT(Y t, HashMap<Y, Y> m);
    T requestPATCH(Y t, HashMap<Y, Y> m);


    }
