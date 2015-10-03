package quantum.para;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Home extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Cursor cr = MainActivity.db.getClients(MainActivity.db, MainActivity.currentUser);

        String[] dbFields = new String[] {"lastName", "firstName"};
        int[] fields = new int[] {R.id.lastName, R.id.firstName};


        SimpleCursorAdapter c = new SimpleCursorAdapter(getActivity(), R.layout.client_list, cr, dbFields, fields);
        ListView list = (ListView)getActivity().findViewById(R.id.clients);
        list.setAdapter(c);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                MainActivity.currentClientPosition = i;
                Toast.makeText(getActivity(), Integer.toString(i), Toast.LENGTH_SHORT).show();
                ClientHome c = new ClientHome();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, c).addToBackStack(null)
                        .commit();
            }
        });

    }
}