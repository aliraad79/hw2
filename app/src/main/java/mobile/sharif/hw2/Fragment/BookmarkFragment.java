package mobile.sharif.hw2.Fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import mobile.sharif.hw2.DbHelper;
import mobile.sharif.hw2.MyLocation;
import mobile.sharif.hw2.MyRecyclerViewAdapter;
import mobile.sharif.hw2.R;

import static mobile.sharif.hw2.Fragment.HeadlessFragment.HEADLESS_FRAGMENT_TAG;

public class BookmarkFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {
    MyRecyclerViewAdapter adapter;
    static RecyclerView bookmarks;
    public static ArrayList<MyLocation> locations = new ArrayList<>();

//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bookmark);
//
//        HeadlessFragment headlessFragment = (HeadlessFragment) getFragmentManager()
//                .findFragmentByTag(HEADLESS_FRAGMENT_TAG);
//        if (headlessFragment != null) {
//            DbHelper dbHelper = headlessFragment.dbHelper;
//            SQLiteDatabase db = headlessFragment.db;
//            locations = dbHelper.getAllLocations(db);
//            Log.i("Info", String.valueOf(dbHelper.getAllLocations(db)));
//
//        }
//
//        Log.i("Info", String.valueOf(locations.size()));
//
//        bookmarks = findViewById(R.id.bookmarks);
//        bookmarks.setLayoutManager(new LinearLayoutManager(getActivity()));
//        adapter = new MyRecyclerViewAdapter(getActivity(), locations);
//        adapter.setClickListener(this);
//        bookmarks.setAdapter(adapter);
//
//    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getActivity(), "Item " + position + " clicked", Toast.LENGTH_SHORT).show();
    }
}