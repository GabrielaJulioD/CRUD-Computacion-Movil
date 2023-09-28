package co.edu.uniminuto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;

public class UsuarioDao {

    private GestionBD gestionBD;
    private Context context;
    private ListView listaUsuarios;

    public UsuarioDao(Context context, ListView listView) {
        this.context = context;
        this.listaUsuarios = listView;
        gestionBD = new GestionBD(this.context);
    }

    public void insertUser(Usuario usuario) {
        try {
            SQLiteDatabase sqLiteDatabase = gestionBD.getWritableDatabase();
            if (sqLiteDatabase != null) {
                ContentValues values = new ContentValues();
                values.put("USU_DOCUMENTO", usuario.getDocumento());
                values.put("USU_USUARIO", usuario.getUsuario());
                values.put("USU_NOMBRES", usuario.getNombre());
                values.put("USU_APELLIDOS", usuario.getApellidos());
                values.put("USU_CONTRA", usuario.getContra());
                long response = sqLiteDatabase.insert("Usuarios", null, values);
                Snackbar.make(listaUsuarios, "Se ha registrado el usuario: " + response, Snackbar.LENGTH_LONG).show();
                sqLiteDatabase.close();
            } else {
                Snackbar.make(listaUsuarios, "No se ha registrado el usuario", Snackbar.LENGTH_LONG).show();
            }
        } catch (SQLException sqlException) {
            Log.i("DB", "" + sqlException);
        }
    }

    public ArrayList<Usuario> getUserList() {
        SQLiteDatabase sqLiteDatabase = gestionBD.getReadableDatabase();
        String query = "SELECT * FROM Usuarios";
        ArrayList<Usuario> userList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Usuario usuario = new Usuario();
                usuario.setDocumento(cursor.getInt(0));
                usuario.setUsuario(cursor.getString(1));
                usuario.setNombre(cursor.getString(2));
                usuario.setApellidos(cursor.getString(3));
                usuario.setContra(cursor.getString(4));
                userList.add(usuario);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return userList;
    }

    public Usuario buscarUsuarioPorDocumento(int documento) {
        SQLiteDatabase sqLiteDatabase = gestionBD.getReadableDatabase();
        String query = "SELECT * FROM Usuarios WHERE USU_DOCUMENTO = ?";
        String[] args = {String.valueOf(documento)};

        Cursor cursor = sqLiteDatabase.rawQuery(query, args);
        Usuario usuarioEncontrado = null;

        if (cursor.moveToFirst()) {
            usuarioEncontrado = new Usuario();
            usuarioEncontrado.setDocumento(cursor.getInt(0));
            usuarioEncontrado.setUsuario(cursor.getString(1));
            usuarioEncontrado.setNombre(cursor.getString(2));
            usuarioEncontrado.setApellidos(cursor.getString(3));
            usuarioEncontrado.setContra(cursor.getString(4));
        }

        cursor.close();
        sqLiteDatabase.close();
        return usuarioEncontrado;
    }

    public boolean actualizarUsuario(Usuario usuario) {
        try {
            SQLiteDatabase sqLiteDatabase = gestionBD.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("USU_USUARIO", usuario.getUsuario());
            values.put("USU_NOMBRES", usuario.getNombre());
            values.put("USU_APELLIDOS", usuario.getApellidos());
            values.put("USU_CONTRA", usuario.getContra());

            int rowsAffected = sqLiteDatabase.update("Usuarios", values,
                    "USU_DOCUMENTO = ?", new String[]{String.valueOf(usuario.getDocumento())});

            sqLiteDatabase.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            Log.e("DB", "Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
}
