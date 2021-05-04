package mobile.sharif.hw2.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import mobile.sharif.hw2.DbHelper;
import mobile.sharif.hw2.R;

import static mobile.sharif.hw2.Fragment.HeadlessFragment.HEADLESS_FRAGMENT_TAG;

public class SettingFragment extends Fragment {

    private DbHelper dbHelper;
    private SQLiteDatabase db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HeadlessFragment headlessFragment = (HeadlessFragment) getFragmentManager()
                .findFragmentByTag(HEADLESS_FRAGMENT_TAG);
        if (headlessFragment != null) {
            dbHelper = headlessFragment.dbHelper;
            db = headlessFragment.db;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        FrameLayout f = view.findViewById(R.id.delete_data_frame);

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity()).setMessage(R.string.clear_database)
                        .setPositiveButton(R.string.yes, (dialog, id) -> delete()).setNegativeButton(R.string.no, leave()).show();
            }

            private DialogInterface.OnClickListener leave() {
                return null;
            }
        });
        return view;
    }

    private void delete() {
        dbHelper.deleteAllData(db);
    }

}