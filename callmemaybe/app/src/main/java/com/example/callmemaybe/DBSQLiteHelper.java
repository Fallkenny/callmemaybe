package com.example.callmemaybe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DBSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsDatabase";
    private static final String CONTACTS_TABLENAME = "contact";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PHONENUMBER = "phonenumber";
    private static final String OBSERVATION = "obs";
    private static final String PROFILEPICTURE = "profilepicture";
    private static final String[] COLUMNS = {ID, NAME, PHONENUMBER, OBSERVATION, PROFILEPICTURE};


    public DBSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "+CONTACTS_TABLENAME+" ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                 NAME+" TEXT,"+
                 PHONENUMBER+" INTEGER,"+
                OBSERVATION+" INTEGER," +
                PROFILEPICTURE+" BLOB)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ CONTACTS_TABLENAME);
        this.onCreate(db);
    }

    private Bitmap createBitmapFromBlob(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    private byte[] createByteArrayFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(bitmap == null)
            return  new byte[]{};
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }


    private Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact();
        contact.setId(Integer.parseInt(cursor.getString(0)));
        contact.setName(cursor.getString(1));
        contact.setPhoneNumber(cursor.getString(2));
        contact.setObservation(cursor.getString(3));
        contact.setProfilePicture(createBitmapFromBlob(cursor.getBlob(4)));
        return contact;
    }

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, contact.getName());
        values.put(PHONENUMBER, contact.getPhoneNumber());
        values.put(OBSERVATION, contact.getObservation());
        values.put(PROFILEPICTURE, createByteArrayFromBitmap(contact.getProfilePicture()));
        db.insert(CONTACTS_TABLENAME, null, values);
        db.close();
    }

    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(CONTACTS_TABLENAME,
                COLUMNS,
                " id = ?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);
        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            Contact contact = cursorToContact(cursor);
            return contact;
        }
    }

    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        String query = "SELECT * FROM " + CONTACTS_TABLENAME + " ORDER BY " + NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Contact contact = cursorToContact(cursor);
                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        return contacts;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, contact.getName());
        values.put(PHONENUMBER, contact.getPhoneNumber());
        values.put(OBSERVATION, contact.getObservation());
        values.put(PROFILEPICTURE, createByteArrayFromBitmap(contact.getProfilePicture()) );
        int i = db.update(CONTACTS_TABLENAME,
                values,
                ID+" = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
        return i;
    }

    public int deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(CONTACTS_TABLENAME,
                ID+" = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
        return i;
    }

}
