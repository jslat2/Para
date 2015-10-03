package quantum.para;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class BillEditor extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.billeditor, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String currentBillAmountDue = Float.toString(MainActivity.db.getBillAmount(MainActivity.db, MainActivity.currentUser, MainActivity.currentClientPosition, MainActivity.currentBillPosition));
        String currentServiceRendered = MainActivity.db.getBillService(MainActivity.db, MainActivity.currentUser, MainActivity.currentClientPosition, MainActivity.currentBillPosition);
        int currentYear = MainActivity.db.getBillYear(MainActivity.db, MainActivity.currentUser, MainActivity.currentClientPosition, MainActivity.currentBillPosition);
        int currentMonth = MainActivity.db.getBillMonth(MainActivity.db, MainActivity.currentUser, MainActivity.currentClientPosition, MainActivity.currentBillPosition);
        int currentDay = MainActivity.db.getBillDay(MainActivity.db, MainActivity.currentUser, MainActivity.currentClientPosition, MainActivity.currentBillPosition);

        ((EditText)getActivity().findViewById(R.id.amountDue)).setText(currentBillAmountDue);
        ((EditText)getActivity().findViewById(R.id.serviceRendered)).setText(currentServiceRendered);
        ((DatePicker)getActivity().findViewById(R.id.dateRendered)).updateDate(currentYear, currentMonth, currentDay);

        Cursor cr = MainActivity.db.getPayments(MainActivity.db, MainActivity.currentUser, MainActivity.currentClientPosition, MainActivity.currentBillPosition);

        String[] dbFields = new String[] {"yearPaid", "amountPaid"};
        int[] fields = new int[] {R.id.lastName, R.id.firstName};


        SimpleCursorAdapter c = new SimpleCursorAdapter(getActivity(), R.layout.client_list, cr, dbFields, fields);
        ListView list = (ListView)getActivity().findViewById(R.id.payments);
        list.setAdapter(c);
//
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
//                MainActivity.currentBillPosition = i;
//                BillEditor b = new BillEditor();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.container, b).addToBackStack(null)
//                        .commit();
//            }
//        });

    }


}


