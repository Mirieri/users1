package ke.co.eaglesafari.views;

import android.app.ProgressDialog;
import android.content.Context;

public class PDialog {
    Context context;
    ProgressDialog pdialog;

    public PDialog() {
        // TODO Auto-generated constructor stub
    }

    public PDialog(Context context) {
        this.context = context;
    }

    public void start(String text) {
        pdialog = new ProgressDialog(context);
        pdialog.setCancelable(true);
        pdialog.setMessage(text);
        pdialog.show();

    }

    public void update(String text) {
        pdialog.setMessage(text);
    }

    public void end() {
        pdialog.dismiss();
    }

}
