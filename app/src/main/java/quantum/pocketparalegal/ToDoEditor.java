package quantum.pocketparalegal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * Created by Gaming on 10/3/2015.
 */
public class ToDoEditor extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.todoeditor, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        String completed = MainActivity.db.getToDoComplete(MainActivity.db, MainActivity.currentUser, MainActivity.currentClientPosition, MainActivity.currentToDoPosition);
        int year = MainActivity.db.getToDoYear(MainActivity.db, MainActivity.currentUser, MainActivity.currentClientPosition, MainActivity.currentToDoPosition);
        int month = MainActivity.db.getToDoMonth(MainActivity.db, MainActivity.currentUser, MainActivity.currentClientPosition, MainActivity.currentToDoPosition);
        int day = MainActivity.db.getToDoDay(MainActivity.db, MainActivity.currentUser, MainActivity.currentClientPosition, MainActivity.currentToDoPosition);
        String task = MainActivity.db.getToDoTask(MainActivity.db, MainActivity.currentUser, MainActivity.currentClientPosition, MainActivity.currentToDoPosition);

        if(completed.equals("Complete")){
            ((CheckBox)getActivity().findViewById(R.id.completed)).setChecked(true);
        }
        ((DatePicker)getActivity().findViewById(R.id.dueDate)).updateDate(year, month, day);
        ((EditText)getActivity().findViewById(R.id.task)).setText(task);
    }
}

