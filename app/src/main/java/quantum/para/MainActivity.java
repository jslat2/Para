package quantum.para;

import android.app.Dialog;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static Database db;
    public static int currentUser = -1;
    public static int currentClientPosition = -1;
    public static int currentBillPosition = -1;
    public static String currentDirectory = Environment.getExternalStorageDirectory().toString();
    public static String[] currentDirectoryList;

    //variable representing whether currently navigating to client directory
    //for use in navigating out of files_nav fragment
    public static boolean filesActive = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new Database(this);

        Login l = new Login();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, l)
                .commit();
    }

    public void openBilling(View v){
        Billing b = new Billing();
        switchFrag(b);
    }

    public void openBill(View v){
        Bill b = new Bill();
        switchFrag(b);
    }

    public void openHome(View v){
        currentClientPosition = -1;
        Home h = new Home();
        switchFrag(h);
    }

    public void openFileNav(View v){
        FileNav f = new FileNav();
        switchFrag(f);
    }

    public void fileNavUpLevel(View v){

        for(int i = currentDirectory.length()-1; i >= 0; i--){
            if(currentDirectory.equals(Environment.getExternalStorageDirectory().toString())){
                Toast.makeText(this, "Cannot move up a level", Toast.LENGTH_LONG).show();
                break;
            }

            currentDirectory = currentDirectory.substring(0, i);
            if(currentDirectory.charAt(i-1) == '/'){
                currentDirectory = currentDirectory.substring(0, i-1);
                break;
            }
        }

        FileNav n = new FileNav();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, n).addToBackStack(null)
                .commit();
    }



    public void saveBill(View v){
        String service = ((EditText)findViewById(R.id.serviceRendered)).getText().toString();
        float amountDue = Float.parseFloat(((EditText) findViewById(R.id.amountDue)).getText().toString());
        int year = ((DatePicker)findViewById(R.id.dateRendered)).getYear();
        int month = ((DatePicker)findViewById(R.id.dateRendered)).getMonth();
        int day = ((DatePicker)findViewById(R.id.dateRendered)).getDayOfMonth();

        db.saveNewBill(db, currentUser, currentClientPosition, service, amountDue, year, month, day);
    }

    public void editBill(View v){
        String service = ((EditText)findViewById(R.id.serviceRendered)).getText().toString();
        float amountDue = Float.parseFloat(((EditText) findViewById(R.id.amountDue)).getText().toString());
        int year = ((DatePicker)findViewById(R.id.dateRendered)).getYear();
        int month = ((DatePicker)findViewById(R.id.dateRendered)).getMonth();
        int day = ((DatePicker)findViewById(R.id.dateRendered)).getDayOfMonth();

        db.editBill(db, currentUser, currentClientPosition, currentBillPosition, service, amountDue, year, month, day);
    }

    public void saveBasicInfo(View v) {
        String firstName = ((EditText) findViewById(R.id.firstName)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.lastName)).getText().toString();
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
        String address = ((EditText) findViewById(R.id.address)).getText().toString();
        String city = ((EditText) findViewById(R.id.city)).getText().toString();
        String state = ((EditText) findViewById(R.id.state)).getText().toString();
        String zip = ((EditText) findViewById(R.id.zip)).getText().toString();

        db.addBasicInfo(db, currentUser, firstName, lastName, email, phone, address, city, state, zip);
    }

    public void editBasicInfo(View v){
        String firstName = ((EditText) findViewById(R.id.firstName)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.lastName)).getText().toString();
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
        String address = ((EditText) findViewById(R.id.address)).getText().toString();
        String city = ((EditText) findViewById(R.id.city)).getText().toString();
        String state = ((EditText) findViewById(R.id.state)).getText().toString();
        String zip = ((EditText) findViewById(R.id.zip)).getText().toString();

        db.editBasicInfo(db, currentUser, currentClientPosition, firstName, lastName, email, phone, address, city, state, zip);
    }

    public void openCreateClient(View v){
        BasicInfo b = new BasicInfo();
        switchFrag(b);
    }

    public void openToDo(View v){
        ToDo t = new ToDo();
        switchFrag(t);
    }

    public void openTodoEditor(View v){
        ToDoEditor t = new ToDoEditor();
        switchFrag(t);
    }

    public void login(View v){
        String user = ((EditText) findViewById(R.id.username)).getText().toString();
        String pass = ((EditText) findViewById(R.id.password)).getText().toString();

        if(db.isValidLogin(db, user, pass)) {
            currentUser = db.getUserId(db, user);
            Home h = new Home();
            switchFragNoBackStack(h);
        } else{
            Toast.makeText(this, "Invalid Login", Toast.LENGTH_LONG).show();
        }
    }

    public void logout(View v){
        currentUser = -1;
        currentClientPosition = -1;
        currentBillPosition = -1;

        Login l = new Login();
        switchFragNoBackStack(l);
    }

    public void openPaymentLog(View v){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.paymentdialog);

        Button saveButton = (Button)dialog.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText amountText = (EditText) dialog.findViewById(R.id.paymentAmount);
                if (amountText.getText().toString().trim().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Cannot log payment of $0", Toast.LENGTH_LONG).show();
                } else {
                    float paymentAmount = Float.parseFloat((amountText).getText().toString());

                    int year = ((DatePicker)dialog.findViewById(R.id.datePaid)).getYear();
                    int month = ((DatePicker)dialog.findViewById(R.id.datePaid)).getMonth();
                    int day = ((DatePicker)dialog.findViewById(R.id.datePaid)).getDayOfMonth();

                    db.logPayment(db, paymentAmount, year, month, day);
                    dialog.dismiss();
                    BillEditor b = new BillEditor();
                    switchFrag(b);
                }

            }
        });

        Button exitButton = (Button)dialog.findViewById(R.id.exitPayment);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void openCreateAccount(View v){
        AccountCreator c = new AccountCreator();
        switchFrag(c);
    }

    public void openBasicInfoEditor(View v){
        BasicInfoEditor b = new BasicInfoEditor();
        switchFrag(b);
    }

    public void openClientHome(View v){
        ClientHome c = new ClientHome();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, c).addToBackStack(null)
                .commit();
    }

    public void createAccount(View v){
        String user = ((EditText) findViewById(R.id.username)).getText().toString();
        String pass = ((EditText) findViewById(R.id.password)).getText().toString();

        if (db.isUniqueUser(db, user)) {
            db.addAccount(db, user, pass);
            Login l = new Login();
            switchFrag(l);
        } else{
            Toast.makeText(this, "User already exists", Toast.LENGTH_LONG).show();
        }
    }

    public void switchFrag(Fragment newFrag){
        FragmentManager frag = getSupportFragmentManager();
        frag.beginTransaction().replace(R.id.container, newFrag, null).addToBackStack(null).commit();
    }


    public void switchFragNoBackStack(Fragment newFrag){
        FragmentManager frag = getSupportFragmentManager();
        frag.beginTransaction().replace(R.id.container, newFrag, null).commit();
    }
}
