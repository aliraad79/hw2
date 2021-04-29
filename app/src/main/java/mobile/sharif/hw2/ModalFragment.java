package mobile.sharif.hw2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ModalFragment extends Fragment {

    private static final String ARG_PARAM1 = "latitude";
    private static final String ARG_PARAM2 = "longitude";

    private double lat;
    private double lon;

    public ModalFragment() {
        // Required empty public constructor
    }

    public static ModalFragment newInstance(double param1, double param2) {
        ModalFragment fragment = new ModalFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM1, param1);
        args.putDouble(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lat = getArguments().getDouble(ARG_PARAM1);
            lon = getArguments().getDouble(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modal, container, false);
    }
}