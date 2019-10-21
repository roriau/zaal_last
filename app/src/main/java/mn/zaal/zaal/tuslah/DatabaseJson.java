package mn.zaal.zaal.tuslah;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mn.zaal.zaal.models.zaalModel;
import timber.log.Timber;

public class DatabaseJson extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "Zaal";

    private static final String TABLE_ZAAL = "zaal_list";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_CITY = "city";
    private static final String KEY_DISTRICT = "district";
    private static final String KEY_KHOROO = "khoroo";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PRICE_TAL = "price_tal";
    private static final String KEY_PRICE_BUTEN = "price_buten";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_MAP_LAT = "map_lat";
    private static final String KEY_MAP_LNG = "map_lng";
    private static final String KEY_ZAAL_PIC = "zaal_pic";
    private SQLiteDatabase mDefaultWritableDatabase = null;
    public DatabaseJson(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATE_ZAAL_TABLE = "CREATE TABLE " + TABLE_ZAAL + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT UNIQUE,"
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_CITY + " TEXT,"
            + KEY_DISTRICT + " TEXT,"
            + KEY_KHOROO + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_PRICE_TAL + " TEXT,"
            + KEY_PRICE_BUTEN + " TEXT,"
            + KEY_PHONE + " TEXT,"
            + KEY_MAP_LAT + " TEXT,"
            + KEY_MAP_LNG + " TEXT,"
            + KEY_ZAAL_PIC + ")";
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

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.mDefaultWritableDatabase = db;
        db.execSQL(CREATE_ZAAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.mDefaultWritableDatabase = db;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZAAL);
        onCreate(db);
    }


    public void addZaal(List<zaalModel> zaalModelList) {
         SQLiteDatabase db = this.getWritableDatabase();
         db.beginTransaction();
         try {
             ContentValues values = new ContentValues();
             for (zaalModel model: zaalModelList) {
                 values.put(KEY_NAME, model.getName());
                 values.put(KEY_DESCRIPTION, model.getDescription());
                 values.put(KEY_CITY, model.getCity());
                 values.put(KEY_DISTRICT, model.getDistrict());
                 values.put(KEY_KHOROO, model.getKhoroo());
                 values.put(KEY_ADDRESS, model.getAddress());
                 values.put(KEY_PRICE_TAL, model.getPrice_tal());
                 values.put(KEY_PRICE_BUTEN, model.getPrice_buten());
                 values.put(KEY_PHONE, model.getPhone());
                 values.put(KEY_MAP_LAT, model.getMap_lat());
                 values.put(KEY_MAP_LNG, model.getMap_lng());
                 values.put(KEY_ZAAL_PIC, model.getZaal_pic());
                 db.insert(TABLE_ZAAL, null, values);
             }
             db.setTransactionSuccessful();
         } finally {
             db.endTransaction();
         }
    }

    public List<zaalModel> allZaal() {
        List<zaalModel> models = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ZAAL;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                zaalModel model = new zaalModel();
                model.setName(cursor.getString(1));
                model.setDescription(cursor.getString(2));
                model.setCity(cursor.getString(3));
                model.setDistrict(cursor.getString(4));
                model.setKhoroo(cursor.getString(5));
                model.setAddress(cursor.getString(6));
                model.setPrice_tal(cursor.getString(7));
                model.setPrice_buten(cursor.getString(8));
                model.setPhone(cursor.getString(9));
                model.setMap_lat(cursor.getString(10));
                model.setMap_lng(cursor.getString(11));
                model.setZaal_pic(cursor.getString(12));
                models.add(model);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return models;
    }

    public int getRowCount() {
        String countQuery = "SELECT * FROM " + TABLE_ZAAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        return rowCount;
    }

    public void resetTables(){
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.delete(TABLE_ZAAL, null, null);
        } catch (SQLException e) {
            Timber.d(e.toString());
        }

    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
