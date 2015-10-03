package quantum.para;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;


public class FileNav extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.filesActive = true;
        return inflater.inflate(R.layout.filenav, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        File f = new File(MainActivity.currentDirectory);
        f.mkdirs();
        String[] list = f.list();

        if(!(list == null)) {
            MainActivity.currentDirectoryList = new String[list.length];

            for(int i = 0; i < list.length; i++){
                MainActivity.currentDirectoryList[i] = list[i];
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.list, list);
            ListView l = (ListView) getView().findViewById(R.id.listView);
            l.setAdapter(adapter);

            l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> av, View view, int i, long l) {

                    String nextFolder = MainActivity.currentDirectory + "/" + MainActivity.currentDirectoryList[i];
                    File nextFile = new File(nextFolder);

                    if(nextFile.isDirectory()) {
                        MainActivity.currentDirectory = nextFolder;

                        FileNav n = new FileNav();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, n)
                                .commit();
                    }

                    else{
                        FileNameMap fileNameMap = URLConnection.getFileNameMap();
                        String type = fileNameMap.getContentTypeFor(nextFolder);

                        Uri path = Uri.fromFile(nextFile);
                        Intent openIntent = new Intent(Intent.ACTION_VIEW);
                        openIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        openIntent.setDataAndType(path, type);
                        try {
                            startActivity(openIntent);
                        } catch (ActivityNotFoundException e) {

                        }
                    }



                }
            });
        }

        else{
            String[] none = new String[1];
            none[0] = "";

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.list, none);
            ListView l = (ListView) getView().findViewById(R.id.listView);
            l.setAdapter(adapter);
        }
    }
}