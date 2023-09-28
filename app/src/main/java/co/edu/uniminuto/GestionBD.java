package co.edu.uniminuto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;

public class GestionBD extends SQLiteOpenHelper {

    private static final String DATABASE_USERS ="dbUsuarios";
    private static final int VERSION = 1;
    private static final String TABLE_USERS = "Usuarios";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE "+TABLE_USERS+" (USU_DOCUMENTO INTEGER" +
            " PRIMARY KEY, USU_USUARIO varchar(50) NOT NULL, USU_NOMBRES varchar(150) NOT NULL," +
            " USU_APELLIDOS varchar(50) NOT NULL, USU_CONTRA varchar(25) NOT NULL)";

    private static final String DELETE_TABLE ="DROP TABLE IF EXISTS "+TABLE_USERS;
    public GestionBD(@Nullable Context context) {
        super(context, DATABASE_USERS, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
        Log.i("BASE DE DATOS", "SE CREÓ LA BASE DE DATOS");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }


}
