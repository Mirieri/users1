package ke.co.eaglesafari.net.network;

import org.json.JSONArray;
import org.json.JSONObject;


public class ResponseModel
    {
    public JSONObject getJson()
        {
        return json;
        }

    public void setJson(JSONObject json)
        {
        this.json = json;
        }

    JSONObject json=null;
    JSONArray jarray=null;

    public JSONArray getJarray()
        {
        return jarray;
        }

    public void setJarray(JSONArray jarray)
        {
        this.jarray = jarray;
        }

    int status_code =401;

    public int getStatus_code()
        {
        return status_code;
        }

    public void setStatus_code(int status_code)
        {
        this.status_code = status_code;
        }

    public Boolean getIs_logged_in()
        {
        return is_logged_in;
        }

    public void setIs_logged_in(Boolean is_logged_in)
        {
        this.is_logged_in = is_logged_in;
        }

    Boolean is_logged_in=false;

    @Override
    public String toString()
        {
        return "ResponseModel{" +
                "json=" + json +
                ", jarray=" + jarray +
                ", status_code=" + status_code +
                ", is_logged_in=" + is_logged_in +
                '}';
        }
    }
