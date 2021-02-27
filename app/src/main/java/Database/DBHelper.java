package Database;

/* *
    Class to handle all the db operations.
* */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import DataModels.UserModel;

public class DBHelper extends SQLiteOpenHelper {

    //user table variables
    public static final String USER_TABLE = "USER_TABLE";
    public static final String COL_USER_ID = "ID";
    public static final String COL_USER_FNAME = "USER_FNAME";
    public static final String COL_USER_LNAME = "USER_LNAME";
    public static final String COL_USER_EMAIL = "USER_EMAIL";
    public static final String COL_USER_PWORD = "USER_PWORD";

    //project table variables
    public static final String PROJECT_TABLE = "PROJECT_TABLE";
    public static final String COL_PROJECT_ID = "PROJECT_ID";
    public static final String COL_PROJECT_NAME = "PROJECT_NAME";
    public static final String COL_PROJECT_DESC = "PROJECT_DESC";
    public static final String COL_PROJECT_DD= "PROJECT_DD"; //will be stored as text use sqlite datetime functions

    //Chat Table Variables
    public static final String CHAT_TABLE = "CHAT_TABLE";
    public static final String COL_CHAT_ID = "CHAT_ID";
    public static final String COL_CHAT_MESSAGE = "MESSAGE"; //text string to location of json asset
    public static final String COL_CHAT_TIME = "CHAT_TIME";

    //Tasks table Variables
    public static final String TASK_TABLE = "TASK_TABLE";
    public static final String COL_TASK_ID = "TASK_ID";
    public static final String COL_TASK_NAME = "TASK_NAME";
    public static final String COL_TASK_STATUS  = "TASK_STATUS";
    public static final String COL_TASK_DESC = "TASK_DESC";
    public static final String COL_TASK_DD = "TASK_DD"; //will be stored as text use sql data time functions

    //User in project Variables
    public static final String USERS_IN_PROJECT = "USERS_IN_PROJECT";

    //Roles table variables
    public static final String ROLES_TABLE = "ROLES_TABLE";
    public static final String COL_ROLES = "USER_ROLE";

    //Assigned to table variables
    public static final String TASK_ASSIGNMENT = "TASK_ASSIGNMENT";


    public DBHelper(@Nullable Context context) {
        //database name is pm_project.dp
        super(context, "pm_project.db", null, 1);
    }

    //this is called the first time you attempt to access a DB object. Code to generate tables here.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create user table statement
        String createUserTableStatement="CREATE TABLE " + USER_TABLE + "(" + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_USER_FNAME +
                " TEXT, "+ COL_USER_LNAME + " TEXT, " + COL_USER_EMAIL + " TEXT, " + COL_USER_PWORD + " TEXT)";

        //create project table statement
        String createProjectTableStatement = "CREATE TABLE " + PROJECT_TABLE + "(" + COL_PROJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PROJECT_NAME + " TEXT NOT NULL, "+ COL_PROJECT_DESC + " TEXT, " + COL_PROJECT_DD + " TEXT)";

        //create project task statement
        String createTaskTableStatement = "CREATE TABLE " + TASK_TABLE + "(" + COL_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TASK_NAME +
                " TEXT NOT NULL, "+ COL_TASK_DESC + " TEXT, " + COL_TASK_DD +
                " TEXT, " + COL_TASK_STATUS + " TEXT, " + COL_PROJECT_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY("+COL_PROJECT_ID +") REFERENCES " + PROJECT_TABLE + "("+COL_PROJECT_ID+"))";

        //create chat table statement
        String createChatTableStatement = "CREATE TABLE " + CHAT_TABLE + "(" + COL_CHAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_CHAT_MESSAGE +
                " TEXT NOT NULL, "+ COL_CHAT_TIME + " TEXT, " + COL_USER_ID +
                " INTEGER NOT NULL," + COL_PROJECT_ID + " INTEGER NOT NULL, " +
                " FOREIGN KEY("+COL_USER_ID +") REFERENCES " + USER_TABLE + "("+COL_USER_ID+"), " +
                "FOREIGN KEY("+COL_PROJECT_ID +") REFERENCES " + PROJECT_TABLE + "("+COL_PROJECT_ID+"))";

        //create assigned to table
        String createAssignmentTableStatement = "CREATE TABLE " + TASK_ASSIGNMENT + "(" + COL_USER_ID + " INTEGER, "
                + COL_TASK_ID + " INTEGER, PRIMARY KEY (" + COL_USER_ID +", " + COL_TASK_ID +"), " +
                " FOREIGN KEY("+COL_USER_ID +") REFERENCES " + USER_TABLE + "("+COL_USER_ID+"), " +
                "FOREIGN KEY("+COL_TASK_ID +") REFERENCES " + TASK_TABLE + "("+COL_TASK_ID+"))";

        //create users in project table
        String createUsersinProjectTableStatement = "CREATE TABLE " + USERS_IN_PROJECT + "(" + COL_USER_ID + " INTEGER, "
                + COL_PROJECT_ID + " INTEGER, PRIMARY KEY (" + COL_USER_ID +", " + COL_PROJECT_ID +"), " +
                " FOREIGN KEY("+COL_USER_ID +") REFERENCES " + USER_TABLE + "("+COL_USER_ID+"), " +
                "FOREIGN KEY("+COL_PROJECT_ID +") REFERENCES " + PROJECT_TABLE + "("+COL_PROJECT_ID+"))";

        //execute create table statements
        sqLiteDatabase.execSQL(createUserTableStatement);
        sqLiteDatabase.execSQL(createProjectTableStatement);
        sqLiteDatabase.execSQL(createTaskTableStatement);
        sqLiteDatabase.execSQL(createChatTableStatement);
        sqLiteDatabase.execSQL(createAssignmentTableStatement);
        sqLiteDatabase.execSQL(createUsersinProjectTableStatement);

    }

    //this is called when version number of DB changes. Prevents previous users from breaking when you change db design.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean createUser(UserModel user){
        SQLiteDatabase db = this.getWritableDatabase(); //gets the one and only db we are going to write to
        ContentValues cv = new ContentValues(); //special class working like an hash map

        cv.put(COL_USER_FNAME, user.getfName());
        cv.put(COL_USER_LNAME, user.getLname());
        cv.put(COL_USER_EMAIL, user.getEmail());
        cv.put(COL_USER_PWORD, user.getPassword());

        long insert = db.insert(USER_TABLE, null, cv); //positive number successfully insert if negative fail

        if(insert == -1){
            return false;
        }else{
            return true;
        }
    }
}
