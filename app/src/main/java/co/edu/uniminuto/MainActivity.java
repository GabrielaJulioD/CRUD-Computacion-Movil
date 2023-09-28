package co.edu.uniminuto;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText etDocumento;
    private EditText etUsuario;
    private EditText etNombres;
    private EditText etApellidos;
    private EditText etContra;
    private ListView listaUsuarios;

    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializador();
    }

    private void inicializador() {
        etDocumento = findViewById(R.id.ET_documento);
        etUsuario = findViewById(R.id.ET_Usuario);
        etNombres = findViewById(R.id.ET_Nombres);
        etApellidos = findViewById(R.id.ET_apellidos);
        etContra = findViewById(R.id.ET_CONTRA);
        listaUsuarios = findViewById(R.id.LV_lista);
        usuarioDao = new UsuarioDao(this, listaUsuarios);
        listarUsuario();

        cofigurarOnFocus(etDocumento, getString(R.string.et_documento));
        cofigurarOnFocus(etUsuario, getString(R.string.et_usuario));
        cofigurarOnFocus(etNombres, getString(R.string.et_nombres));
        cofigurarOnFocus(etApellidos, getString(R.string.et_apellidos));
        cofigurarOnFocus(etContra, getString(R.string.et_contra));
    }
    private void cofigurarOnFocus(final EditText editText, final String defaultText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editText.setText("");
                } else {
                    if (editText.getText().toString().isEmpty()) {
                        editText.setText(defaultText);
                    }
                }
            }
        });
    }
    private void listarUsuario() {
        ArrayList<Usuario> userList = usuarioDao.getUserList();

        ArrayAdapter<Usuario> adapter = new ArrayAdapter<Usuario>(this, android.R.layout.simple_list_item_1, userList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                Usuario usuario = userList.get(position);
                String formattedDetails = usuario.getFormattedDetails();

                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(formattedDetails);

                return view;
            }
        };

        listaUsuarios.setAdapter(adapter);
    }


    private boolean validarDatos() {
        String documentoStr = etDocumento.getText().toString();
        String usuarioStr = etUsuario.getText().toString();
        String nombresStr = etNombres.getText().toString();
        String apellidosStr = etApellidos.getText().toString();
        String contraStr = etContra.getText().toString();

        // Longitud mínima para el documento (al menos 6 caracteres)
        if (documentoStr.length() < 6) {
            Toast.makeText(this, "El documento debe tener al menos 6 caracteres", Toast.LENGTH_LONG).show();
            return false;
        }

        // Formato del documento (numérico)
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(documentoStr);
        if (!matcher.matches()) {
            Toast.makeText(this, "El documento debe ser numérico", Toast.LENGTH_LONG).show();
            return false;
        }

        //  Seguridad de la contraseña (al menos 8 caracteres, incluyendo números y letras)
        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
        Matcher passwordMatcher = passwordPattern.matcher(contraStr);
        if (!passwordMatcher.matches()) {
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres, incluyendo números y letras", Toast.LENGTH_LONG).show();
            return false;
        }

        //  Nombre y apellido sin números
        if (nombresStr.matches(".*\\d+.*")) {
            Toast.makeText(this, "El nombre no debe contener números", Toast.LENGTH_LONG).show();
            return false;
        }

        if (apellidosStr.matches(".*\\d+.*")) {
            Toast.makeText(this, "El apellido no debe contener números", Toast.LENGTH_LONG).show();
            return false;
        }

        // campos no estén vacíos
        if (documentoStr.isEmpty() || usuarioStr.isEmpty() || nombresStr.isEmpty() || apellidosStr.isEmpty() || contraStr.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }


    public void accionRegistrar(View view) {
        if (validarDatos()) {

            Usuario usuario1 = new Usuario();
            usuario1.setNombre(etNombres.getText().toString());
            usuario1.setApellidos(etApellidos.getText().toString());
            usuario1.setUsuario(etUsuario.getText().toString());
            usuario1.setContra(etContra.getText().toString());
            usuario1.setDocumento(Integer.parseInt(etDocumento.getText().toString()));

            usuarioDao.insertUser(usuario1);
            listarUsuario();
        }
    }
    public void accionBuscar(View view) {
        String documentoStr = etDocumento.getText().toString().trim();

        if (documentoStr.isEmpty()) {
            Toast.makeText(this, "Ingresa un número de documento", Toast.LENGTH_SHORT).show();
            return;
        }

        int documento = Integer.parseInt(documentoStr);

        Usuario usuarioEncontrado = usuarioDao.buscarUsuarioPorDocumento(documento);

        if (usuarioEncontrado != null) {
            String userInfo = "Nombre: " + usuarioEncontrado.getNombre() +
                    "\nApellidos: " + usuarioEncontrado.getApellidos() +
                    "\nUsuario: " + usuarioEncontrado.getUsuario();

            Toast.makeText(this, userInfo, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No se encontró ningún usuario con ese documento", Toast.LENGTH_SHORT).show();
        }
    }

    public void accionActualizar(View view) {
        String documentoStr = etDocumento.getText().toString().trim();
        String usuarioStr = etUsuario.getText().toString().trim();
        String nombresStr = etNombres.getText().toString().trim();
        String apellidosStr = etApellidos.getText().toString().trim();
        String contraStr = etContra.getText().toString().trim();

        if (documentoStr.isEmpty()) {
            Toast.makeText(this, "Seleccione un usuario para actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        int documento = Integer.parseInt(documentoStr);

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setDocumento(documento);
        usuarioActualizado.setUsuario(usuarioStr);
        usuarioActualizado.setNombre(nombresStr);
        usuarioActualizado.setApellidos(apellidosStr);
        usuarioActualizado.setContra(contraStr);

        if (usuarioDao.actualizarUsuario(usuarioActualizado)) {
            Toast.makeText(this, "Usuario actualizado exitosamente", Toast.LENGTH_SHORT).show();
            listarUsuario();
        } else {
            Toast.makeText(this, "Error al actualizar el usuario", Toast.LENGTH_SHORT).show();
        }
    }

}
