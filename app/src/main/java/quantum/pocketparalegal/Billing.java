package quantum.pocketparalegal;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Billing extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.billing, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String amtDue = Float.toString(MainActivity.db.getTotalBillsDue(MainActivity.db, MainActivity.currentUser, MainActivity.currentClientPosition));
        ((EditText) getActivity().findViewById(R.id.due)).setText(amtDue);

        Cursor cr = MainActivity.db.getClientBills(MainActivity.db, MainActivity.currentUser, MainActivity.currentClientPosition);

        String[] dbFields = new String[] {"yearOpened", "originalAmountDue"};
        int[] fields = new int[] {R.id.lastName, R.id.firstName};


        SimpleCursorAdapter c = new SimpleCursorAdapter(getActivity(), R.layout.client_list, cr, dbFields, fields);
        ListView list = (ListView)getActivity().findViewById(R.id.bills);
        list.setAdapter(c);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                MainActivity.currentBillPosition = i;
                BillEditor b = new BillEditor();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, b).addToBackStack(null)
                        .commit();
            }
        });

    }
}