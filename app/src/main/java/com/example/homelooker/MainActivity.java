package com.example.homelooker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    //creacion de variables
    private EditText cuadroCorreo;
    private EditText contrasenia;
    private TextView Registrate;
    private Button botonIniciar;
    private TextView ahoraNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtenemos la vista de los objetos
        this.cuadroCorreo = (EditText) findViewById(R.id.cuadroCorreo);
        this.contrasenia = (EditText) findViewById(R.id.contrasenia);
        this.Registrate = (TextView) findViewById(R.id.Registrate);
        this.botonIniciar =(Button) findViewById(R.id.botonIniciar);
        this.ahoraNo = (TextView) findViewById(R.id.ahoraNo);

        //al pulsar en el boton Iniciar Sesion
        this.botonIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarAcceso(); //se ejecuta el metodo que comprueba los datos
            }
        });

        //al pulsar en el boton registrar
        this.Registrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Registro.class); //intent hacia la ventana de registro
                startActivity(intent); //inicio la actividad del intent
            }
        });

        //al pulsar en el TextView Ahora No
        this.ahoraNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Inicio.class); //intent hacia la ventana de inicio
                Bundle datos = new Bundle(); //bundle para enviar datos
                datos.putString("correo", "Inicia Sesión o Registrate"); //envio como correo este mensaje, para que el usuario si pulsa mas tarde en el vuelva a la ventana principal
                intent.putExtras(datos); //asigno los datos

                startActivity(intent); //inicio la nueva actividad
                finish(); //al pulsar ahora no, cierro esta ventana
            }
        });

        cuadroCorreo.addTextChangedListener(new TextWatcher() {
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
                String email = cuadroCorreo.getText().toString().trim();

                if (!isValidEmail(email)) {
                    cuadroCorreo.setError("Correo electrónico inválido");
                } else {
                    cuadroCorreo.setError(null);
                }
            }
        });

    }

    private boolean isValidEmail(String email) { //comprueba que el email introducido sea valido
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    /**
     * comprueba los datos insertados en los dos campos de texto y si existen en la base de datos, permite el acceso a la ventana de Inicio
     */
    public void comprobarAcceso() {
        //conexion a la tabla user
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(this, "user", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();

        //guardo los valores de los campos
        String correo = cuadroCorreo.getText().toString();
        String contra = contrasenia.getText().toString();

        //consulta que devuelve los datos si coinciden con el correo y contrasenia
        Cursor consulta = bd.rawQuery("SELECT correo, nombre, usuario, contrasenia, rol FROM user WHERE correo=\'"+correo+"\' AND contrasenia=\'"+contra+"\'", null);

        if (consulta.moveToFirst()) { //si la consulta devuelve algun resultado
            Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show(); //muestro mensaje

            Intent intent = new Intent(getBaseContext(), Inicio.class); //intent hacia la nueva ventana
            Bundle datos = new Bundle(); //bundle para almacenar los datos a enviar
            datos.putString("correo", correo); //asigno el correo al bundle
            intent.putExtras(datos); //asigno los datos

            startActivity(intent); //inicio el nuevo activity
            finish(); //al iniciar sesion, cierro esta ventana
        }
        else { //si no devuelve ningun resultado
            Toast.makeText(this, R.string.errorVuelve, Toast.LENGTH_SHORT).show(); //muestro mensaje
            this.contrasenia.setText(""); //dejo el campo de la contrasenia en blanco, porque probablemente este mal escrita
        }

        bd.close(); //cierro la conexion
    }

}