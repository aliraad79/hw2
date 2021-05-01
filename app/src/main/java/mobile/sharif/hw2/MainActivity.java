package mobile.sharif.hw2;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.mapboxsdk.Mapbox;

import mobile.sharif.hw2.Fragment.BookmarkFragment;
import mobile.sharif.hw2.Fragment.HeadlessFragment;
import mobile.sharif.hw2.Fragment.MapFragment;
import mobile.sharif.hw2.Fragment.SettingFragment;

import static mobile.sharif.hw2.Fragment.HeadlessFragment.HEADLESS_FRAGMENT_TAG;

public class MainActivity extends AppCompatActivity {

    private HeadlessFragment headlessFragment;
    private MapFragment mapFragment = new MapFragment();
    private BookmarkFragment bookmarkFragment = new BookmarkFragment();
    private SettingFragment settingFragment = new SettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_main);
        mapFragment = new MapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mapFragment)
                .commit();

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
                                .replace(R.id.fragment_container, mapFragment).commit();
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
    }
}