package com.example.homelooker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Registro extends AppCompatActivity {

    //creacion de variables
    private EditText cuadroMail;
    private EditText cuadroNombre;
    private EditText cuadroUsuario;
    private EditText cuadroContrasenia;
    private Spinner spinnerRegistro;
    private Button botonRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //obtenemos la vista de los objetos
        this.cuadroMail = (EditText) findViewById(R.id.cuadroMail);
        this.cuadroNombre = (EditText) findViewById(R.id.cuadroNombre);
        this.cuadroUsuario = (EditText) findViewById(R.id.cuadroUsuario);
        this.cuadroContrasenia = (EditText) findViewById(R.id.cuadroContrasenia);
        this.spinnerRegistro = (Spinner) findViewById(R.id.spinnerRegistro);
        this.botonRegistro = (Button) findViewById(R.id.botonRegistro);

        //al pulsar en el boton de Registrar
        this.botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creo variables para los datos que voy a enviar
                String tipo = spinnerRegistro.getSelectedItem().toString();

                if (tipo.equals("Selecciona una opción")) {
                    Toast.makeText(getBaseContext(), R.string.errorSpinnerBusqueda, Toast.LENGTH_LONG).show();
                    spinnerRegistro.requestFocus();
                }
                else {
                    registro(); //ejecuto el metodo para registrar nuevo user
                }

            }
        });

        cuadroMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No se requiere acción antes del cambio de texto
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No se requiere acción durante el cambio de texto
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = cuadroMail.getText().toString().trim();

                if (!isValidEmail(email)) {
                    cuadroMail.setError("Correo electrónico inválido");
                } else {
                    cuadroMail.setError(null);
                }
            }
        });

    }

    private boolean isValidEmail(String email) { //comprueba que el email introducido sea valido
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    /**
     * realiza una conexion con la base de datos, lee los datos que se han insertado en los campos de texto
     * y comprueba de no existan para evitar valores duplicados y luegos los inserta en la base de datos
     */
    public void registro() {
        //ejecuto la conexion a la tabla user
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(this, "user", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();

        //guardo el valor del texto insertado en cada cuadro de texto
        String correo = cuadroMail.getText().toString();
        String nombre = cuadroNombre.getText().toString();
        String usuario = cuadroUsuario.getText().toString();
        String contrasenia = cuadroContrasenia.getText().toString();
        String rol = spinnerRegistro.getSelectedItem().toString();

        //asigno el contenido para que pueda realizarse la consulta y hacer la comprobacion
        ContentValues contenido = new ContentValues();
        contenido.put("correo", correo);
        contenido.put("nombre", nombre);
        contenido.put("usuario", usuario);
        contenido.put("contrasenia", contrasenia);
        contenido.put("rol", rol);

        //ejecuto las siguientes consultas para comprobar si existe el correo y usuario insertados
        Cursor consulta1 = bd.rawQuery("SELECT correo FROM user WHERE correo=\'"+correo+"\'", null);
        Cursor consulta2 = bd.rawQuery("SELECT usuario FROM user WHERE usuario=\'"+usuario+"\'", null);

        //si la primera consulta o la segunda devuelve algun valor, es decir que existe dicho user en la tabla
        if (consulta1.moveToFirst() || consulta2.moveToFirst()) {
            //muestro Toast con el mensaje de error y dejo todos los campos en blanco
            Toast.makeText(this, R.string.errorDatosExisten, Toast.LENGTH_SHORT).show();
            this.cuadroMail.setText("");
            this.cuadroNombre.setText("");
            this.cuadroUsuario.setText("");
            this.cuadroContrasenia.setText("");

            //recupero el foco para volver a insertar nuevos datos que sean validos
            this.cuadroMail.requestFocus();
            this.cuadroNombre.requestFocus();
            this.cuadroUsuario.requestFocus();
            this.cuadroContrasenia.requestFocus();
            this.spinnerRegistro.requestFocus();
        } else { //si no
            //muestro mensaje de que se ha creado el nuevo user
            Toast.makeText(this, R.string.usuarioCreado, Toast.LENGTH_LONG).show();
            bd.insert("user", null, contenido); //inserto en la tabla el nuevo user
            finish(); //cierro la ventana de registro actual
        }

        bd.close(); //cierro la conexion con la base de datos
    }

}