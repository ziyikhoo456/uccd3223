package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;


public class SQLiteAdapter {

    //SQL Commands
    private static final String DATABASE_NAME = "SPLIT_BILL_RECORD";
    private static final String DATABASE_TABLE = "BILL_TABLE"; //table
    private static final int DATABASE_VERSION = 1; //version

    //columns
    private static final String CATEGORY = "Category";
    private static final String DATE = "Date"; //column
    private static final String TIME = "Time"; //column
    public static final String PEOPLE = "People";
    public static final String AMOUNT = "Amount";


    //sql command to create the table with the column
    //ID, Content, Ingredients, Price
    private static final String SCRIPT_CREATE_DATABASE =
            "create table "+DATABASE_TABLE+
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    DATE + " text not null, "+
                    TIME + " text, "+
                    CATEGORY + " text, "+
                    PEOPLE + " text, "+
                    AMOUNT + " text);";

    //variables
    private Context context;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    ///constructor
    public SQLiteAdapter(Context c) {
        context = c;
    }

    //Get the number of rows inside the database
    public int getCount() throws android.database.SQLException{
        int count = 0;
        String[] columns = new String[] {DATE,TIME,CATEGORY,PEOPLE,AMOUNT};
        Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE, columns, null,null,null,null,null);
        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
            count++;
        }

        return count;
    }

    //open the database to insert data/to write data
    public SQLiteAdapter openToWrite() throws android.database.SQLException{

        //create a table with MYDATABASE_NAME and
        //the version of MYDATABASE_VERSION
        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

        //open to write
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        return this;
    }

    //open the database to read
    public SQLiteAdapter openToRead() throws android.database.SQLException {

        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

        //open to read
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();

        return this;
    }



    //Insert content into the columns
    public long insert(String date, String time, String category, String people, String amount){

        ContentValues contentValues = new ContentValues();

        contentValues.put(DATE, date);
        contentValues.put(TIME, time);
        contentValues.put(CATEGORY,category);
        contentValues.put(PEOPLE,people);
        contentValues.put(AMOUNT, amount);

        return sqLiteDatabase.insert(DATABASE_TABLE,null,contentValues);

    }



    public String[][] queueSome(String selection, String[] args) {

        int index = 0;
        int size = this.getCount();
        String[][] results = new String[size][5];
        String[] columns = new String[]{DATE,TIME,CATEGORY,PEOPLE,AMOUNT};; // Specify columns you want to retrieve

        Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE, columns, selection,args,null,null,null);

        int index_CONTENT = cursor.getColumnIndex(DATE);
        int index_CONTENT_2 = cursor.getColumnIndex(TIME);
        int index_CONTENT_3 = cursor.getColumnIndex(CATEGORY);
        int index_CONTENT_4 = cursor.getColumnIndex(PEOPLE);
        int index_CONTENT_5 = cursor.getColumnIndex(AMOUNT);

        for(cursor.moveToLast(); !(cursor.isBeforeFirst()); cursor.moveToPrevious()){
            results[index][0] = cursor.getString(index_CONTENT);
            results[index][1] = cursor.getString(index_CONTENT_2);
            results[index][2] = cursor.getString(index_CONTENT_3);
            results[index][3] = cursor.getString(index_CONTENT_4);
            results[index][4] = cursor.getString(index_CONTENT_5);
            index++;
        }

        cursor.close();
        return results;
    }

    //close the database
    public void close(){
        sqLiteHelper.close();
    }

    //delete all the content in the table/ delete the table
    public int deleteAll(){
        return sqLiteDatabase.delete(DATABASE_TABLE,null,null);
    }


    //superclass of SQLiteOpenHelper --> implement both the
    //override methods which creates the database
    public class SQLiteHelper extends SQLiteOpenHelper {

        //Constructor with 4 parameters
        public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //to create the database
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE);
        }

        //version control
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE);
        }
    }

}
