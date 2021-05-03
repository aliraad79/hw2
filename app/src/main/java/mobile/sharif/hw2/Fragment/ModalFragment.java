package mobile.sharif.hw2.Fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;

import java.util.Objects;

import mobile.sharif.hw2.DbHelper;
import mobile.sharif.hw2.MainActivity;
import mobile.sharif.hw2.MyLocation;
import mobile.sharif.hw2.R;

import static mobile.sharif.hw2.Fragment.HeadlessFragment.HEADLESS_FRAGMENT_TAG;


public class ModalFragment extends Fragment {
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    private ModalFragment modalFragment;
    private Symbol symbol;
    private HeadlessFragment headlessFragment;
    private String locationString;
    private double longitude, latitude;

    public ModalFragment() {
        super();
        modalFragment = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            longitude = getArguments().getDouble(LONGITUDE);
            latitude = getArguments().getDouble(LATITUDE);
            symbol = ((MainActivity) getActivity()).makeMarker(new LatLng(latitude, longitude));
            locationString = "Save Location: (" + String.format("%.2f", longitude) + ", " + String.format("%.2f", latitude) + ")";
        }
        if (getFragmentManager() != null) {
            headlessFragment = (HeadlessFragment) getFragmentManager()
                    .findFragmentByTag(HEADLESS_FRAGMENT_TAG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modal, container, false);
        TextView locationCoordination = view.findViewById(R.id.locationCoordination);
        EditText locationName = view.findViewById(R.id.locationName);
        locationCoordination.setText(locationString);
        Button saveButton = view.findViewById(R.id.saveLocationButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHelper dbHelper = headlessFragment.dbHelper;
                SQLiteDatabase db = headlessFragment.db;
                dbHelper.putLocation(db, new MyLocation(longitude, latitude, locationName.getText().toString()));
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().remove(modalFragment).commit();
                modalFragment = null;
            }
        });
        Button cancelButton = view.findViewById(R.id.cancelSaveLocation);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: put these two lines in onDestroy()
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().remove(modalFragment).commit();
                ((MainActivity) modalFragment.getActivity()).deleteMarker(symbol);
                modalFragment = null;
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        make_marker(point);
    }
}