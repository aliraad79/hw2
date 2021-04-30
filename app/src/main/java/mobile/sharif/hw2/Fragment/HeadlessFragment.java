package mobile.sharif.hw2.Fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import mobile.sharif.hw2.DbHelper;

public class HeadlessFragment extends Fragment {
    public static String HEADLESS_FRAGMENT_TAG = "headless_fragment";
    public SQLiteDatabase db;
    public DbHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DbHelper(getActivity());
        db = dbHelper.getWritableDatabase();
    }

}
