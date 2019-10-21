package mn.zaal.zaal.tuslah;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "LoginInfo";

    // Login table name
    private static final String TABLE_LOGIN = "login";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PHONENUMBER = "phone_number";
    private static final String KEY_CREATED_AT = "created_at";
    private SQLiteDatabase mDefaultWritableDatabase = null;
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Table Create Statements
    private static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_USERNAME + " TEXT,"
            + KEY_PHONENUMBER + " TEXT,"
            + KEY_CREATED_AT + ")";

    @Override
    public SQLiteDatabase getWritableDatabase() {
        final SQLiteDatabase db;
        if(mDefaultWritableDatabase != null){
            db = mDefaultWritableDatabase;
        } else {
            db = super.getWritableDatabase();
        }
        return db;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.mDefaultWritableDatabase = db;
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.mDefaultWritableDatabase = db;
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String email, String name, String username, String phone_number, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, email);
        values.put(KEY_NAME, name);
        values.put(KEY_USERNAME, username);
        values.put(KEY_PHONENUMBER, phone_number);
        values.put(KEY_CREATED_AT, created_at);

        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<>();
        String selectQuery = "SELECT * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("email", cursor.getString(1));
            user.put("name", cursor.getString(2));
            user.put("username", cursor.getString(3));
            user.put("phone_number", cursor.getString(4));
            user.put("created_at", cursor.getString(5));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
