package com.cgp.saral.databaseHelper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by karamjeetsingh on 17/07/15.
 */
public class SqliteDatabasehelper extends SQLiteOpenHelper {

    Context context;
    private static final String ERROR = "SqliteDatabaseHelper";
    private String DB_PATH ;
    public static String DB_NAME = "saral_app.sqlite";
    public static String USERTABLE_NAME = "user";
    public static int VERSION = 4;
     /*  private static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY," + STATE
            + " TEXT," + U5MR_FIRST + " TEXT," + U5_children_SECOND + " TEXT," + ORS_by_ASHA_THIRD
            + " TEXT" + zinc_ORS_FORTH + " TEXT," + ORS_Zinc_corners_FIFTH + " TEXT," + NHF_ORT_corners_SIX +
            " TEXT," + schools_part_SEVEN + " TEXT," + PRI_meet_EIGHT + " TEXT," + ")";*/


    SQLiteDatabase database;


    public SqliteDatabasehelper(Context context) {
        super(context,DB_NAME,null,VERSION);
        this.context = context;
        DB_PATH = "/data/data/com.cgp.saral/databases/";
       //DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
    }
/*
* Copy DB
* */

   /* public void createDataBase() throws IOException{
        boolean dbExist = checkDataBase();
        if(dbExist){

        }else{

            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error al copiar la base de datos");
            }
        }

    }
*/


    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{

        boolean dbExist = checkDataBase();

        if(dbExist){
            //Do nothing
            this.getWritableDatabase();
        }

        dbExist = checkDataBase();

        if(!dbExist){

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }
        this.close();
    }


    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);

            int i = checkDB.getVersion();
            int a = i;

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    public void copyDataBase() throws IOException{
        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DB_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        Log.e("Chcek Database", ""+"Database copy done");
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

  /*  public boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            Log.e("Chcek Database", ""+"Database is exist");
        }catch(SQLiteException e){
            //database does't exist yet.
            Log.e("Chcek Database", ""+"Database not exist"+e);
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }
*/
    public void openDataBase() throws SQLException{

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);

    }

    @Override
    public synchronized void close() {
        if(database != null)
            database.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    }
