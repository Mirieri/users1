package ke.co.eaglesafari.views;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import java.util.ArrayList;
import java.util.List;

import ke.co.eaglesafari.items.SpinnerModel;

public class Spiner {
    Spinner spinner;
    Activity activity;

    public Spiner() {

    }

    ArrayList<SpinnerModel> array;
    I_SpinnerValue<String> spinvalue;

    public Spiner(Activity activity, Spinner spiner, ArrayList<SpinnerModel> array,
                  I_SpinnerValue<String> value) {
        this.activity = activity;
        spinner = spiner;
        this.array = array;
        this.spinvalue = value;

    }

    public void setup() {
        final List<String> value = new ArrayList<String>();
        final List<String> id_a = new ArrayList<String>();
        value.add("---Select---");
        id_a.add("0");
        for (int x = 0; x < array.size(); x++) {
            value.add(array.get(x).getValue());
            id_a.add(array.get(x).getId());
        }

        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(activity,
                android.R.layout.simple_list_item_1, value);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adp1);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                // TODO Auto-generated method stub

                spinvalue.SpinnerValue(id_a
                        .get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

    }


}
