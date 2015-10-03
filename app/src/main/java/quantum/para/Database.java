package quantum.para;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    public static final int database_version = 1;

    public Database(Context context) {
        super(context, "PowerPoker", null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE account(ID INTEGER, userName TEXT, password TEXT);");
        db.execSQL("CREATE TABLE client(ID INTEGER, userNum TEXT, firstName TEXT, lastName TEXT, email TEXT, phone TEXT, address TEXT, city TEXT, state TEXT, zip TEXT);");
        db.execSQL("CREATE TABLE bill(ID INTEGER, userNum TEXT, clientNum INTEGER, yearOpened INTEGER, monthOpened INTEGER, dayOpened INTEGER, originalAmountDue REAL, serviceRendered TEXT);");
        db.execSQL("CREATE TABLE payment(ID INTEGER, userNum TEXT, clientNum INTEGER, billNum Integer, yearPaid INTEGER, monthPaid INTEGER, dayPaid INTEGER, amountPaid REAL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //to do
    }

    public boolean clientExists(Database db, int userNum, String firstName, String lastName){
        Cursor cr = getClients(db, userNum);
        while(!cr.isAfterLast()){
            if(cr.getString(3).equals(firstName) && cr.getString(4).equals(lastName)){
                return true;
            }
            cr.moveToNext();
        }
        return false;
    }

    public Cursor getClients(Database db, int user){
        String[] col = {"rowid _id", "ID", "userNum", "firstName", "lastName", "email", "phone", "address", "city", "state", "zip"};
        SQLiteDatabase sq = db.getReadableDatabase();
        return sq.query("client WHERE userNum = " + user, col, null, null, null, null, "lastName asc, firstName asc");
    }

    public Cursor getCurrentClient(Database db, int userNum, int listPosition){
        String[] col = {"rowid _id", "ID", "userNum", "firstName", "lastName", "email", "phone", "address", "city", "state", "zip"};
        SQLiteDatabase sq = db.getReadableDatabase();
        return sq.query("client WHERE userNum = " + userNum + " AND ID = " + getClientID(db, userNum, listPosition), col, null, null, null, null, null);
    }

    public Cursor getClientBills(Database db, int userNum, int listPosition){
        String[] col = {"rowid _id", "ID", "userNum", "clientNum", "yearOpened", "monthOpened", "dayOpened", "originalAmountDue"};
        SQLiteDatabase sq = db.getReadableDatabase();
        return sq.query("bill WHERE userNum = " + userNum + " AND clientNum = " + getClientID(db, userNum, listPosition), col, null, null, null, null, "yearOpened asc, monthOpened asc, dayOpened asc");
    }

    public Cursor getPayments(Database db, int userNum, int currentClientPosition, int currentBillPosition){
        String[] col = {"rowid _id", "ID", "userNum", "clientNum", "billNum", "yearPaid", "monthPaid", "dayPaid", "amountPaid"};
        SQLiteDatabase sq = db.getReadableDatabase();
        return sq.query("payment WHERE userNum = " + userNum + " AND clientNum = " + getClientID(db, userNum, currentClientPosition) + " AND billNum = " + db.getBillID(db, userNum, currentClientPosition, currentBillPosition), col, null, null, null, null, "yearPaid asc, monthPaid asc, dayPaid asc");
    }

    public float getTotalBillsDue(Database db, int currentUserID, int clientListPosition){
        float total = 0;
        SQLiteDatabase sq = db.getReadableDatabase();
        String[] col = {"originalAmountDue"};
        Cursor cr = sq.query("bill WHERE userNum = " + currentUserID + " AND clientNum = " + getClientID(db, currentUserID, clientListPosition), col, null, null, null, null, null);
        cr.moveToFirst();
        while(!cr.isAfterLast()){
            total += cr.getFloat(0);
            cr.moveToNext();
        }
        return total;
    }

    public float getBillAmount(Database db, int currentUser, int clientListPosition, int currentBillPosition){
        float total = 0;
        SQLiteDatabase sq = db.getReadableDatabase();
        String[] col = {"originalAmountDue"};
        Cursor cr = sq.query("bill WHERE userNum = " + currentUser + " AND clientNum = " + getClientID(db, currentUser, clientListPosition), col, null, null, null, null, "yearOpened asc, monthOpened asc, dayOpened asc");
        cr.moveToPosition(currentBillPosition);
        return cr.getFloat(0);
    }

    public String getBillService(Database db, int currentUser, int clientListPosition, int currentBillPosition){
        float total = 0;
        SQLiteDatabase sq = db.getReadableDatabase();
        String[] col = {"serviceRendered"};
        Cursor cr = sq.query("bill WHERE userNum = " + currentUser + " AND clientNum = " + getClientID(db, currentUser, clientListPosition), col, null, null, null, null, "yearOpened asc, monthOpened asc, dayOpened asc");
        cr.moveToPosition(currentBillPosition);
        return cr.getString(0);
    }

    public int getBillYear(Database db, int currentUser, int clientListPosition, int currentBillPosition){
        SQLiteDatabase sq = db.getReadableDatabase();
        String[] col = {"yearOpened"};
        Cursor cr = sq.query("bill WHERE userNum = " + currentUser + " AND clientNum = " + getClientID(db, currentUser, clientListPosition), col, null, null, null, null, "yearOpened asc, monthOpened asc, dayOpened asc");
        cr.moveToPosition(currentBillPosition);
        return cr.getInt(0);
    }

    public int getBillMonth(Database db, int currentUser, int clientListPosition, int currentBillPosition){
        SQLiteDatabase sq = db.getReadableDatabase();
        String[] col = {"monthOpened"};
        Cursor cr = sq.query("bill WHERE userNum = " + currentUser + " AND clientNum = " + getClientID(db, currentUser, clientListPosition), col, null, null, null, null, "yearOpened asc, monthOpened asc, dayOpened asc");
        cr.moveToPosition(currentBillPosition);
        return cr.getInt(0);
    }

    public int getBillDay(Database db, int currentUser, int clientListPosition, int currentBillPosition){
        SQLiteDatabase sq = db.getReadableDatabase();
        String[] col = {"dayOpened"};
        Cursor cr = sq.query("bill WHERE userNum = " + currentUser + " AND clientNum = " + getClientID(db, currentUser, clientListPosition), col, null, null, null, null, "yearOpened asc, monthOpened asc, dayOpened asc");
        cr.moveToPosition(currentBillPosition);
        return cr.getInt(0);
    }

    public void addBasicInfo(Database db, int currentUser, String firstName, String lastName, String email, String phone, String  address, String city, String state, String zip){
        SQLiteDatabase sq = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("ID", getNextClientNum(db));
        cv.put("userNum", currentUser);
        cv.put("firstName", firstName);
        cv.put("lastName", lastName);
        cv.put("email", email);
        cv.put("phone", phone);
        cv.put("address", address);
        cv.put("city", city);
        cv.put("state", state);
        cv.put("zip", zip);

        long k = sq.insert("client", null, cv);
    }

    public void logPayment(Database db, float paymentAmount, int year, int month, int day) {
        SQLiteDatabase sq = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("ID", getNextBillNum(db));
        cv.put("userNum", MainActivity.currentUser);
        cv.put("clientNum", getClientID(db, MainActivity.currentUser, MainActivity.currentClientPosition));
        cv.put("billNum", getBillID(db, MainActivity.currentUser, MainActivity.currentClientPosition, MainActivity.currentBillPosition));
        cv.put("yearPaid", year);
        cv.put("monthPaid", month);
        cv.put("dayPaid", day);
        cv.put("amountPaid", paymentAmount);

        long k = sq.insert("payment", null, cv);
    }

    public void saveNewBill(Database db, int currentUser, int currentClientPosition, String service, float amountDue, int year, int month, int day){
        SQLiteDatabase sq = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("ID", getNextBillNum(db));
        cv.put("userNum", currentUser);
        cv.put("clientNum", getClientID(db, currentUser, currentClientPosition));
        cv.put("yearOpened", year);
        cv.put("monthOpened", month);
        cv.put("dayOpened", day);
        cv.put("originalAmountDue", amountDue);
        cv.put("serviceRendered", service);

        long k = sq.insert("bill", null, cv);
    }

    public void editBill(Database db, int currentUser, int currentClientPosition, int currentBillPosition, String service, float amountDue, int year, int month, int day){
        SQLiteDatabase sq = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("yearOpened", year);
        cv.put("monthOpened", month);
        cv.put("dayOpened", day);
        cv.put("originalAmountDue", amountDue);
        cv.put("serviceRendered", service);

        long k = sq.update("bill", cv, "ID = " + getBillID(db, currentUser, currentClientPosition, currentBillPosition), null);
    }

    public String printTable(Database db, String tableName){
        String outString = "";
        SQLiteDatabase sq = db.getReadableDatabase();
        String[] col = {"ID", "userNum", "clientNum"};
        Cursor cr = sq.query(tableName, col, null, null, null, null, null);
        cr.moveToFirst();
        while(!cr.isAfterLast()){
            outString += " ID " + cr.getString(0) + " userNum " + cr.getString(1) + " clientNum " + cr.getString(2);
            cr.moveToNext();
        }

        return outString;
    }

    public void editBasicInfo(Database db, int currentUser, int currentClient, String firstName, String lastName, String email, String phone, String  address, String city, String state, String zip){
        SQLiteDatabase sq = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("firstName", firstName);
        cv.put("lastName", lastName);
        cv.put("email", email);
        cv.put("phone", phone);
        cv.put("address", address);
        cv.put("city", city);
        cv.put("state", state);
        cv.put("zip", zip);

        long k = sq.update("client", cv, "userNum = " + currentUser + " AND ID = " + getClientID(db, currentUser, currentClient), null);
    }

    public int getClientID(Database db, int user, int listPosition) {
        Cursor cr = db.getClients(db, user);
        cr.moveToPosition(listPosition);
        return cr.getInt(1);
    }

    public int getBillID(Database db, int user, int clientListPosition, int billPosition) {
        Cursor cr = db.getClientBills(db, user, clientListPosition);
        cr.moveToPosition(billPosition);
        return cr.getInt(1);
    }

    public int getUserId(Database db, String userName){
        String[] col = new String[2];
        col[0] = "ID";
        col[1] = "userName";
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.query("account", col, null, null, null, null, null);
        if(cr.moveToFirst()) {
            while(!cr.isAfterLast()){
                if(cr.getString(1).equals(userName)){
                    return cr.getInt(0);
                }
                cr.moveToNext();
            }
        }
        return -1;
    }

    public void addAccount(Database db, String userName, String password){
        SQLiteDatabase sq = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("ID", getNextUserNum(db));
        cv.put("userName", userName);
        cv.put("password", password);

        long k = sq.insert("account", null, cv);
    }

    public int getNextUserNum(Database db){
        int x = 0;
        String[] col = new String[1];
        col[0] = "ID";
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.query("account", col, null, null, null, null, null);
        if(cr.moveToFirst()) {
            while(!cr.isAfterLast()){
                if(cr.getInt(0) > x){
                    x = cr.getInt(0);
                }
                cr.moveToNext();
            }
        }
        x++;
        return x;
    }

    public int getNextBillNum(Database db){
        int x = 0;
        String[] col = new String[1];
        col[0] = "ID";
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.query("bill", col, null, null, null, null, null);
        if(cr.moveToFirst()) {
            while(!cr.isAfterLast()){
                if(cr.getInt(0) > x){
                    x = cr.getInt(0);
                }
                cr.moveToNext();
            }
        }
        x++;
        return x;
    }

    public int getNextClientNum(Database db){
        int x = 0;
        String[] col = new String[1];
        col[0] = "ID";
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.query("client", col, null, null, null, null, null);
        if(cr.moveToFirst()) {
            while(!cr.isAfterLast()){
                if(cr.getInt(0) > x){
                    x = cr.getInt(0);
                }
                cr.moveToNext();
            }
        }
        x++;
        return x;
    }

    public boolean isUniqueUser(Database db, String userName){
        String[] col = new String[1];
        col[0] = "userName";
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.query("account", col, null, null, null, null, null);
        if(cr.moveToFirst()) {
            while(!cr.isAfterLast()){
                if(cr.getString(0).equals(userName)){
                    return false;
                }
                cr.moveToNext();
            }
        }
        return true;
    }

    public boolean isValidLogin(Database db, String userName, String password){
        String[] col = new String[2];
        col[0] = "userName";
        col[1] = "password";
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.query("account", col, null, null, null, null, null);
        if(cr.moveToFirst()) {
            while(!cr.isAfterLast()){
                if(cr.getString(0).equals(userName) && cr.getString(1).equals(password)){
                    return true;
                }
                cr.moveToNext();
            }
        }
        return false;
    }
}