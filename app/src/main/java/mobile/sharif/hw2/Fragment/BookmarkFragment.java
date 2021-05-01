package mobile.sharif.hw2.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import mobile.sharif.hw2.DbHelper;
import mobile.sharif.hw2.MyLocation;
import mobile.sharif.hw2.MyRecyclerViewAdapter;
import mobile.sharif.hw2.R;

import static mobile.sharif.hw2.Fragment.HeadlessFragment.HEADLESS_FRAGMENT_TAG;

public class BookmarkFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {
    private MyRecyclerViewAdapter adapter;
    private RecyclerView bookmarks;
    private SearchView searchView;
    private static ArrayList<MyLocation> locations = new ArrayList<>();
    private DbHelper dbHelper;
    private SQLiteDatabase db;

//    public static class MyHandler extends Handler {
//        private final WeakReference<MainActivity> mainActivityWeakReference;
//
//        public MyHandler(MainActivity mainActivity) {
//            this.mainActivityWeakReference = new WeakReference<>(mainActivity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            MainActivity mainActivity = mainActivityWeakReference.get();
//            switch (msg.what) {
//                case INITIALIZE_RECYCLERVIEW:
//                    mainActivity.adapter = new MyRecyclerViewAdapter(mainActivity, coins);
//                    mainActivity.adapter.setClickListener(mainActivity);
//                    recyclerView.setAdapter(mainActivity.adapter);
//                    break;
//                case CLEAR_LIST:
//                    Log.i("start", mainActivity.api.getStart() + "");
//                    mainActivity.adapter.notifyDataSetChanged();
//                    mainActivity.api.resetStart();
//                    break;
//                case RELOAD:
//                    Log.i("start1", mainActivity.api.getStart() + "");
//                    mainActivity.progressBar.setProgress(0);
//                    mainActivity.adapter.notifyDataSetChanged();
//                    break;
//            }
//        }
//    }
//    private MyHandler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HeadlessFragment headlessFragment = (HeadlessFragment) getFragmentManager()
                .findFragmentByTag(HEADLESS_FRAGMENT_TAG);
        if (headlessFragment != null) {
            dbHelper = headlessFragment.dbHelper;
            db = headlessFragment.db;
            locations = dbHelper.getAllLocations(db);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        bookmarks = view.findViewById(R.id.bookmarks);
        searchView = view.findViewById(R.id.bookmark_search_view);
        bookmarks.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyRecyclerViewAdapter(getActivity(), locations);
        adapter.setClickListener(this);
        bookmarks.setAdapter(adapter);
        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getActivity(), "Item " + position + " clicked", Toast.LENGTH_SHORT).show();
    }
}