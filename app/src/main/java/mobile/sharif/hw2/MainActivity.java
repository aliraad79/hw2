package mobile.sharif.hw2;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.List;

import mobile.sharif.hw2.Fragment.BookmarkFragment;
import mobile.sharif.hw2.Fragment.HeadlessFragment;
import mobile.sharif.hw2.Fragment.ModalFragment;
import mobile.sharif.hw2.Fragment.SettingFragment;

import static mobile.sharif.hw2.Fragment.HeadlessFragment.HEADLESS_FRAGMENT_TAG;
import static mobile.sharif.hw2.Fragment.ModalFragment.LATITUDE;
import static mobile.sharif.hw2.Fragment.ModalFragment.LONGITUDE;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener , GPSCallback{

    private HeadlessFragment headlessFragment;
    private BookmarkFragment bookmarkFragment = new BookmarkFragment();
    private SettingFragment settingFragment = new SettingFragment();
    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private static final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    SymbolManager symbolManager;
    private final MainActivity.LocationChangeListeningActivityLocationCallback callback =
            new LocationChangeListeningActivityLocationCallback(this);

    // speed meter fields
    private GPSManager gpsManager = null;
    private double speed = 0.0;
    Boolean isGPSEnabled=false;
    LocationManager locationManager;
    double currentSpeed,kmphSpeed;
    TextView txtview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_main);
        txtview=(TextView)findViewById(R.id.speed);
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        getCurrentSpeed(txtview);

        BottomNavigationView navbar = findViewById(R.id.bottom_navigation);
        navbar.getMenu().getItem(1).setChecked(true);
        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bookmark:
                        if (menuItem.isChecked()) break;
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, bookmarkFragment).commit();
                        break;

                    case R.id.setting:
                        if (menuItem.isChecked()) break;
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, settingFragment).commit();
                        break;

                    case R.id.map:
                        if (menuItem.isChecked()) break;
                        getSupportFragmentManager()
                                .beginTransaction()
                                .remove(bookmarkFragment).commit();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .remove(settingFragment).commit();
                        break;
                }
                return true;
            }
        });

        // Headless fragment for passing database between fragments and configuration changes
        headlessFragment = (HeadlessFragment) getSupportFragmentManager()
                .findFragmentByTag(HEADLESS_FRAGMENT_TAG);
        if (headlessFragment == null) {
            headlessFragment = new HeadlessFragment();
            getSupportFragmentManager().beginTransaction().add(headlessFragment, HEADLESS_FRAGMENT_TAG).commit();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double lat = mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude();
                double lon = mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude();
                travelToLocation(lat, lon);
            }
        });

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }


    private void travelToLocation(double lat, double lon) {
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(lat, lon)) // Sets the new camera position
                .zoom(15) // Sets the zoom
                .tilt(30) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder

        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 5000);
    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.TRAFFIC_NIGHT,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        mapboxMap.addOnMapLongClickListener(point -> {
                            Bundle bundle = new Bundle();
                            bundle.putDouble(LONGITUDE, point.getLongitude());
                            bundle.putDouble(LATITUDE, point.getLatitude());
                            ModalFragment fragment = new ModalFragment();
                            fragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_modal, fragment).commit();
                            return true;
                        });

                        // Set non-data-driven properties.
                        symbolManager = new SymbolManager(mapView, mapboxMap, style);
                        symbolManager.setIconAllowOverlap(true);
                        symbolManager.setIconTranslate(new Float[]{-4f, 5f});

                        enableLocationComponent(style);
                    }
                });
    }

    private void make_marker(LatLng point) {
        SymbolOptions symbolOptions = new SymbolOptions()
                .withLatLng(new LatLng(point.getLatitude(), point.getLongitude()))
                .withIconImage("fire-station-15")
                .withIconSize(1.3f)
                .withSymbolSortKey(10.0f);
        symbolManager.create(symbolOptions);
    }

    /**
     * Initialize the Maps SDK's LocationComponent
     */
    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Set the LocationComponent activation options
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();

            // Activate with the LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            initLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
        }
    }

    private static class LocationChangeListeningActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<MainActivity> mainActivityWeakReference;

        LocationChangeListeningActivityLocationCallback(MainActivity mainActivity) {
            this.mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            MainActivity mainActivity = mainActivityWeakReference.get();

            if (mainActivity != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    return;
                }

                // Pass the new location to the Maps SDK's LocationComponent
                if (mainActivity.mapboxMap != null && result.getLastLocation() != null) {
                    mainActivity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can't be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (mainActivity != null) {
                Toast.makeText(mainActivity, exception.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    // speedMeter methods
    public void getCurrentSpeed(View view){
        txtview.setText(getString(R.string.speed));
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        gpsManager = new GPSManager(MainActivity.this);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnabled) {
            gpsManager.startListening(getApplicationContext());
            gpsManager.setGPSCallback(this);
        } else {
            gpsManager.showSettingsAlert();
        }
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onGPSUpdate(Location location) {
        speed = location.getSpeed();
        currentSpeed = round(speed,3, BigDecimal.ROUND_HALF_UP);
        kmphSpeed = round((currentSpeed*3.6),3,BigDecimal.ROUND_HALF_UP);
        txtview.setText(kmphSpeed+"km/h");
    }
    public static double round(double unrounded, int precision, int roundingMode) {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.i("Info", "START");
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Info", "RESUME");
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Info", "PAUSE");
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Info", "STOP");
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("Info", "SAVE");
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gpsManager.stopListening();
        gpsManager.setGPSCallback(null);
        gpsManager = null;
        // Prevent leaks
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }
        Log.i("Info", "DESTROY");
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i("Info", "LOW MEMORY");
        mapView.onLowMemory();

    }
}