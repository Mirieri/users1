package ke.co.eaglesafari.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLngBounds;
import ke.co.eaglesafari.R;



/**
 * Created by George on 4/22/2016.
 */
public class PlaceAutoComp extends PlaceAutocompleteFragment {
    private View zzaRh;
    private View zzaRi;
    private TextView textViewSearchInput;
    private TextView txt_label;
    @Nullable
    private LatLngBounds zzaRk;
    @Nullable
    private AutocompleteFilter zzaRl;
    @Nullable
    private PlaceSelectionListener zzaRm;

    String label="",place_name="";

    public PlaceAutoComp() {

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View var4 = inflater.inflate(R.layout.autocompleteview, container, false);


        this.zzaRh = var4.findViewById(R.id.autocomplete_search_button);
        this.zzaRi = var4.findViewById(R.id.autocomplete_clear_button);
        this.textViewSearchInput = (TextView)var4.findViewById(R.id.place_autocomplete_search_input);
        this.txt_label = (TextView)var4.findViewById(R.id.txt_label);

        View.OnClickListener var5 = new View.OnClickListener() {
            public void onClick(View view) {
                PlaceAutoComp.this.zzzG();
            }
        };
        this.zzaRh.setOnClickListener(var5);
        this.textViewSearchInput.setOnClickListener(var5);
        this.zzaRi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PlaceAutoComp.this.setText("");
            }
        });
        this.zzzF();
        return var4;
    }

    public void onDestroyView() {
        this.zzaRh = null;
        this.zzaRi = null;
        this.textViewSearchInput = null;
        super.onDestroyView();
    }

    public void setBoundsBias(@Nullable LatLngBounds bounds) {
        this.zzaRk = bounds;
    }

    public void setFilter(@Nullable AutocompleteFilter filter) {
        this.zzaRl = filter;
    }

    public void setText(CharSequence text) {
        this.textViewSearchInput.setText(text);
        this.zzzF();
    }

    public void setHint(CharSequence hint) {
        this.textViewSearchInput.setHint(hint);
        this.zzaRh.setContentDescription(hint);
    }

    public void setOnPlaceSelectedListener(PlaceSelectionListener listener) {
        this.zzaRm = listener;
    }

    public void start(String label,String value) {
        txt_label.setText(label);
        textViewSearchInput.setText(value);
    }
    private void zzzF() {
        boolean var1 = !this.textViewSearchInput.getText().toString().isEmpty();
        if (var1) this.zzaRi.setSystemUiVisibility(0);
        else this.zzaRi.setSystemUiVisibility(8);
    }

    private void zzzG() {
        int var1 = -1;

        try {
            Intent var2 = (new PlaceAutocomplete.IntentBuilder(2)).setBoundsBias(this.zzaRk).setFilter(this.zzaRl).zzeR(this.textViewSearchInput.getText().toString()).zziH(1).build(this.getActivity());
            this.startActivityForResult(var2, 1);
        } catch (GooglePlayServicesRepairableException var3) {
            var1 = var3.getConnectionStatusCode();
            Log.e("Places", "Could not open autocomplete activity", var3);
        } catch (GooglePlayServicesNotAvailableException var4) {
            var1 = var4.errorCode;
            Log.e("Places", "Could not open autocomplete activity", var4);
        }

        if(var1 != -1) {
            GoogleApiAvailability var5 = GoogleApiAvailability.getInstance();
            var5.showErrorDialogFragment(this.getActivity(), var1, 2);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if(resultCode == -1) {
                Place var4 = PlaceAutocomplete.getPlace(this.getActivity(), data);
                if(this.zzaRm != null) {
                    this.zzaRm.onPlaceSelected(var4);
                }

                this.setText(var4.getName().toString());
            } else if(resultCode == 2) {
                Status var5 = PlaceAutocomplete.getStatus(this.getActivity(), data);
                if(this.zzaRm != null) {
                    this.zzaRm.onError(var5);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
