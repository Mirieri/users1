package ke.co.eaglesafari.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ke.co.eaglesafari.R;
import ke.co.eaglesafari.items.ApplicationItem;
import ke.co.eaglesafari.items.SpinnerModel;
import ke.co.eaglesafari.net.BeDriverPartneProcess;
import ke.co.eaglesafari.net.network.I_Response;
import ke.co.eaglesafari.views.I_SpinnerValue;
import ke.co.eaglesafari.views.PDialog;
import ke.co.eaglesafari.views.Spiner;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Application.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Application#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Application extends Fragment {
    EditText editFirstName, editLastName, editEmail, editPhone;
    Spinner spinneType;
    Button btnApply;
    ApplicationItem applicationItem = new ApplicationItem();
    private OnFragmentInteractionListener mListener;
    PDialog pDialog;

    public Application() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Application.
     */
    // TODO: Rename and change types and number of parameters
    public static Application newInstance(String param1, String param2) {
        Application fragment = new Application();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    class SendComplete implements I_Response<Boolean, String> {

        @Override
        public void onTaskCompleted(Boolean i) {
            pDialog.end();
            if (i) {
                editFirstName.setText("");
                editLastName.setText("");
                editEmail.setText("");
                editPhone.setText("");
            }
        }

        @Override
        public void onTaskCommpletedMessage(String s) {
            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onData(String s, Boolean aBoolean) {
        }
    }

    class SpinnerResponse implements I_SpinnerValue<String> {

        @Override
        public void SpinnerValue(String y) {
            applicationItem.setType(y);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.application, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);
        pDialog = new PDialog(getActivity());
        editFirstName = (EditText) getActivity().findViewById(R.id.txt_first_name);
        editLastName = (EditText) getActivity().findViewById(R.id.txt_last_name);
        editEmail = (EditText) getActivity().findViewById(R.id.txt_email);
        editPhone = (EditText) getActivity().findViewById(R.id.txt_phone);
        spinneType = (Spinner) getActivity().findViewById(R.id.spinner_type);
        btnApply = (Button) getActivity().findViewById(R.id.btn_apply);
        ArrayList<SpinnerModel> spinnerModels = new ArrayList<>();
        spinnerModels.add(new SpinnerModel().setId("0").setValue("Driver"));
        spinnerModels.add(new SpinnerModel().setId("1").setValue("Partner"));
        new Spiner(getActivity(), spinneType, spinnerModels, new SpinnerResponse()).setup();

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applicationItem.setFirst_name(editFirstName.getText().toString());
                applicationItem.setLast_name(editLastName.getText().toString());
                applicationItem.setEmail(editEmail.getText().toString());
                applicationItem.setTelephone(editPhone.getText().toString());

                if (applicationItem.isEmpty()) {
                    Toast.makeText(getActivity(), "Complete all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                //send
                pDialog.start("Sending application...");
                new BeDriverPartneProcess(getActivity(), new SendComplete(), applicationItem).execute();

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
