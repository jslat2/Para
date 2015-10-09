package quantum.pocketparalegal;

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

/**
 * Created by Gaming on 10/3/2015.
 */
public class ToDos extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.todos, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Cursor cr = MainActivity.db.getClientTodos(MainActivity.db, MainActivity.currentUser, MainActivity.currentClientPosition);

        String[] dbFields = new String[] {"yearDue", "monthDue", "dayDue", "done"};
        int[] fields = new int[] {R.id.yearDue, R.id.monthDue, R.id.dayDue, R.id.done};


        SimpleCursorAdapter c = new SimpleCursorAdapter(getActivity(), R.layout.todolist, cr, dbFields, fields);
        ListView list = (ListView)getActivity().findViewById(R.id.todoList);
        list.setAdapter(c);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                MainActivity.currentToDoPosition = i;
                ToDoEditor t = new ToDoEditor();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, t).addToBackStack(null)
                        .commit();
            }
        });

    }
}