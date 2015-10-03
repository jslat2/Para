package quantum.para;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class BasicInfoEditor extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.basicinfoeditor, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EditText firstName = ((EditText) getActivity().findViewById(R.id.firstName));
        EditText lastName = ((EditText) getActivity().findViewById(R.id.lastName));
        EditText email = ((EditText) getActivity().findViewById(R.id.email));
        EditText phone = ((EditText) getActivity().findViewById(R.id.phone));
        EditText address = ((EditText) getActivity().findViewById(R.id.address));
        EditText city = ((EditText) getActivity().findViewById(R.id.city));
        EditText state = ((EditText) getActivity().findViewById(R.id.state));
        EditText zip = ((EditText) getActivity().findViewById(R.id.zip));

        Cursor cr = MainActivity.db.getCurrentClient(MainActivity.db, MainActivity.currentUser, MainActivity.currentClientPosition);
        cr.moveToFirst();

        firstName.setText(cr.getString(3));
        lastName.setText(cr.getString(4));
        email.setText(cr.getString(5));
        phone.setText(cr.getString(6));
        address.setText(cr.getString(7));
        city.setText(cr.getString(8));
        state.setText(cr.getString(9));
        zip.setText(cr.getString(10));
    }
}
