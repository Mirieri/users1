package ke.co.eaglesafari.net.network;

import android.util.Log;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ke.co.eaglesafari.utilities.NameValuePair;


 public class JSONParserV2 implements  IParser<ResponseModel,String>
    {

        public ResponseModel requestGETObject(String uri, ArrayList<NameValuePair> map)
        {
            ResponseModel resp=new ResponseModel();
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(uri).newBuilder();

            for(int i=0;i<map.size();i++)
            {
                urlBuilder.addQueryParameter(map.get(i).key, map.get(i).value);
            }

            String url = urlBuilder.build().toString();
            Log.e("Uri",url);

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response=null;
            try
            {
                response= client.newCall(request).execute();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            try
            {
                if(response.isSuccessful())
                {
                    Log.e("Response Body",response.toString());
                    resp.setIs_logged_in(true);
                    resp.setStatus_code(response.code());

                    try
                    {
                        resp.setJson(new JSONObject(response.body().string()));

                    }
                    catch (JSONException y)
                    {

                        y.printStackTrace();
                    }
                    catch (IOException io)
                    {
                        io.printStackTrace();
                    }
                    catch (IllegalStateException h)
                    {
                        h.printStackTrace();
                    }


                }
            } catch (NullPointerException e)
            {
                e.printStackTrace();
            }

            return resp;
        }



        @Override
    public ResponseModel requestGET(String uri, ArrayList<NameValuePair> map)
        {
        ResponseModel resp=new ResponseModel();
        OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder=null;
            try {
                 urlBuilder = HttpUrl.parse(uri).newBuilder();
            }catch (NullPointerException e)
            {
                return resp;
            }

        for(int i=0;i<map.size();i++)
            {
            urlBuilder.addQueryParameter(map.get(i).key, map.get(i).value);
            }

        String url = urlBuilder.build().toString();
            Log.e("Get URL",url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response=null;
        try
            {
            response= client.newCall(request).execute();
            } catch (IOException e)
            {
            e.printStackTrace();
            }

          try
          {
              Log.e("Response",response.toString());
          }catch (NullPointerException n)
          {
              n.printStackTrace();
          }

        try
            {
            if(response.isSuccessful())
                {
                resp.setIs_logged_in(true);
                resp.setStatus_code(response.code());

                try
                    {
                    resp.setJarray(new JSONArray(response.body().string()));
                    }
                catch (JSONException j)
                    {
                    j.printStackTrace();
                    }
                catch (IOException io)
                    {
                    io.printStackTrace();
                    }
                catch (IllegalStateException h)
                    {
                    h.printStackTrace();
                    }

                }
            } catch (NullPointerException e)
            {
            e.printStackTrace();
            }

        return resp;
        }

    @Override
    public ResponseModel requestPOST(String uri, ArrayList<NameValuePair> map,String token)
        {



        ResponseModel resp=new ResponseModel();
        OkHttpClient client = new OkHttpClient();

        MultipartBuilder builder=new MultipartBuilder();
        builder.type(MultipartBuilder.FORM);

       for(int i=0;i<map.size();i++)
           {
           builder.addFormDataPart(map.get(i).key, map.get(i).value);

           }


        RequestBody requestBody=   builder.build();
if(!token.isEmpty())
{
    uri=uri+"?token="+token;
}

        Request request = new Request.Builder()
                .url(uri)
                .post(requestBody)
                .build();
        Response response=null;
        try
            {
            response= client.newCall(request).execute();
            } catch (IOException e)
            {
            resp.setStatus_code(404);
            e.printStackTrace();
            }

        try
            {
            if(response.isSuccessful())
                {
                resp.setIs_logged_in(true);
                resp.setStatus_code(response.code());
                try
                    {
                    resp.setJson( new JSONObject(response.body().string()));
                    } catch (JSONException e)
                    {

                    e.printStackTrace();
                    } catch (IOException e)
                    {
                    e.printStackTrace();
                    }

                }
            else
                {
                resp.setStatus_code(response.code());
                try
                    {
                    resp.setJson( new JSONObject(response.body().string()));
                    } catch (JSONException e)
                    {

                    e.printStackTrace();
                    } catch (IOException e)
                    {
                    e.printStackTrace();
                    }
                }
            } catch (NullPointerException e)
            {
            e.printStackTrace();
            }
Log.e("Response",resp.toString());
        return resp;
        }
        public ResponseModel requestPATCHJSON(String uri, String json)
        {

            Log.e("Url being updated",uri);
            ResponseModel resp=new ResponseModel();
            OkHttpClient client = new OkHttpClient();
            final MediaType JSON
                    = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, json);
            Log.e("JSON Sent", json);
            Request request = new Request.Builder()
                    .url(uri).patch(body)
                    .build();
            Response response=null;
            try
            {
                response= client.newCall(request).execute();
            } catch (IOException e)
            {
                resp.setStatus_code(404);
                e.printStackTrace();
            }

            try
            {
                if(response.isSuccessful())
                {
                    resp.setIs_logged_in(true);
                    resp.setStatus_code(response.code());
                    try
                    {
                        resp.setJson( new JSONObject(response.body().string()));
                    } catch (JSONException e)
                    {

                        e.printStackTrace();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
                else
                {
                    resp.setStatus_code(response.code());
                    try
                    {
                        resp.setJson( new JSONObject(response.body().string()));
                    } catch (JSONException e)
                    {

                        e.printStackTrace();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            } catch (NullPointerException e)
            {
                e.printStackTrace();
            }

            return resp;
        }





        public ResponseModel requestPUTJSON(String uri, String json)
        {

            Log.e("Url being updated",uri);
        ResponseModel resp=new ResponseModel();
        OkHttpClient client = new OkHttpClient();
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Log.e("JSON Sent", json);
        Request request = new Request.Builder()
                .url(uri).put(body)
                .build();
        Response response=null;
        try
            {
            response= client.newCall(request).execute();
            } catch (IOException e)
            {
            resp.setStatus_code(404);
            e.printStackTrace();
            }

        try
            {
            if(response.isSuccessful())
                {
                resp.setIs_logged_in(true);
                resp.setStatus_code(response.code());
                try
                    {
                    resp.setJson( new JSONObject(response.body().string()));
                    } catch (JSONException e)
                    {

                    e.printStackTrace();
                    } catch (IOException e)
                    {
                    e.printStackTrace();
                    }

                }
            else
                {
                resp.setStatus_code(response.code());
                try
                    {
                    resp.setJson( new JSONObject(response.body().string()));
                    } catch (JSONException e)
                    {

                    e.printStackTrace();
                    } catch (IOException e)
                    {
                    e.printStackTrace();
                    }
                }
            } catch (NullPointerException e)
            {
            e.printStackTrace();
            }

        return resp;
        }



    public ResponseModel requestPOSTJSON(String uri, String json)
        {
        ResponseModel resp=new ResponseModel();
        OkHttpClient client = new OkHttpClient();
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Log.e("JSON Sent", json);
        Request request = new Request.Builder()
                .url(uri)
                .post(body)
                .build();
        Response response=null;
        try
            {
            response= client.newCall(request).execute();
            } catch (IOException e)
            {
            resp.setStatus_code(404);
            e.printStackTrace();
            }

        try
            {
            if(response.isSuccessful())
                {
                resp.setIs_logged_in(true);
                resp.setStatus_code(response.code());
                try
                    {
                    resp.setJson( new JSONObject(response.body().string()));
                    } catch (JSONException e)
                    {

                    e.printStackTrace();
                    } catch (IOException e)
                    {
                    e.printStackTrace();
                    }

                }
            else
                {
                resp.setStatus_code(response.code());
                try
                    {
                    resp.setJson( new JSONObject(response.body().string()));
                    } catch (JSONException e)
                    {

                    e.printStackTrace();
                    } catch (IOException e)
                    {
                    e.printStackTrace();
                    }
                }
            } catch (NullPointerException e)
            {
            e.printStackTrace();
            }
Log.e("Response",resp.toString());
        return resp;
        }


    @Override
    public ResponseModel requestDELETE(String uri, HashMap<String, String> map)
        {
        ResponseModel resp=new ResponseModel();
        OkHttpClient client = new OkHttpClient();
         final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        MultipartBuilder builder=new MultipartBuilder();
        builder.type(MultipartBuilder.FORM);

        Set set = map.entrySet();
        // Get an iterator
        Iterator i = set.iterator();
        //add to params
        while(i.hasNext()) {
        Map.Entry param = (Map.Entry)i.next();
        builder.addFormDataPart(param.getKey().toString(), param.getValue().toString());

        }
        RequestBody requestBody=   builder.build();



        Request request = new Request.Builder()
                .url(uri)
                .delete(requestBody)
                .build();
        Response response=null;
        try
            {
            response= client.newCall(request).execute();
            } catch (IOException e)
            {
            e.printStackTrace();
            }

        if(response.isSuccessful())
            {
            resp.setIs_logged_in(true);
            resp.setStatus_code(response.code());
            try
                {
                resp.setJson( new JSONObject(response.body().string()));
                } catch (JSONException e)
                {

                e.printStackTrace();
                } catch (IOException e)
                {
                e.printStackTrace();
                }

            }

        return resp;
        }

    @Override
    public ResponseModel requestPUT(String uri, HashMap<String,String> map)
        {
        ResponseModel resp=new ResponseModel();
        OkHttpClient client = new OkHttpClient();

        MultipartBuilder builder=new MultipartBuilder();
        builder.type(MultipartBuilder.FORM);

        Set set = map.entrySet();
        // Get an iterator
        Iterator i = set.iterator();
        //add to params
        while(i.hasNext()) {
        Map.Entry param = (Map.Entry)i.next();
        builder.addFormDataPart(param.getKey().toString(), param.getValue().toString());

        }
        RequestBody requestBody=   builder.build();



        Request request = new Request.Builder()
                .url(uri)
                .put(requestBody)
                .build();
        Response response=null;
        try
            {
            response= client.newCall(request).execute();
            } catch (IOException e)
            {
            e.printStackTrace();
            }

        if(response.isSuccessful())
            {
            resp.setIs_logged_in(true);
            resp.setStatus_code(response.code());
            try
                {
                resp.setJson( new JSONObject(response.body().string()));
                } catch (JSONException e)
                {

                e.printStackTrace();
                } catch (IOException e)
                {
                e.printStackTrace();
                }

            }

        return resp;
        }

        @Override
        public ResponseModel requestPATCH(String t, HashMap<String, String> m) {
            return null;
        }


    }
