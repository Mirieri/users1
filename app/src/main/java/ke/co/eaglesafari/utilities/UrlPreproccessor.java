package ke.co.eaglesafari.utilities;

import android.content.Context;

import ke.co.eaglesafari.auth.UserDetails;
import ke.co.eaglesafari.constant.Constant;
import ke.co.eaglesafari.constant.ModelConstants;
import ke.co.eaglesafari.db.PaginationDb;
import ke.co.eaglesafari.items.Pagination;

public class UrlPreproccessor {

    Context context;
    PaginationDb paginationDb;
    String token;
    String and_token;

    public UrlPreproccessor(Context context)
    {
        this.context=context;
        paginationDb=PaginationDb.getInstance(context);
        paginationDb.getWritableDatabase();
        UserDetails usd = new UserDetails(context);

        token="?token="+usd.getToken();
        and_token="&token="+usd.getToken();
    }

    public String getHistoryUrl(Boolean next)
    {

            if (paginationDb.isExist(ModelConstants.History)) {
                Pagination pagination = paginationDb.getPagination(ModelConstants.History);
                if(next) {

                    if (pagination.getPrev_page_url() == "null" || pagination.getPrev_page_url() == null)
                        return Constant.DOMAIN+"request"+token;
                    else
                        return pagination.getPrev_page_url()+and_token;

                }else {

                    if (pagination.getNext_page_url() == "null" || pagination.getNext_page_url() == null)
                        return Constant.DOMAIN+"request"+token;
                    else
                        return pagination.getNext_page_url()+and_token;
                }
            }
        return Constant.DOMAIN+"request"+token;
    }
}
