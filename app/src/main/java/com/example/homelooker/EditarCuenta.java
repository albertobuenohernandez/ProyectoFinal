package com.example.homelooker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditarCuenta extends AppCompatActivity {

    //creacion de variables
    private EditText cuadroNuevoNombre;
    private EditText cuadroNuevoUsuario;
    private Button botonEditarCuenta;
    private String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cuenta);

        //obtenemos la vista de los objetos
        this.cuadroNuevoNombre = (EditText) findViewById(R.id.cuadroNuevoNombre);
        this.cuadroNuevoUsuario = (EditText) findViewById(R.id.cuadroNuevoUsuario);
        this.botonEditarCuenta = (Button) findViewById(R.id.botonEditarCuenta);


        //Obtenemos el correo con el que hemos accedido
        Intent intentRecibido = getIntent();
        Bundle datos = intentRecibido.getExtras(); //obtengo los extras
        if(datos != null) { //si no esta vacio
            correo = datos.getString("correo"); //asigno a la variable correo el valor del correo que recibe
        }else{
            correo = "";
        }

        //al pulsar en el boton editar cuenta
        this.botonEditarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarCuenta(); //ejecuto el metodo que edita los datos en la cuenta
            }
        });

    }

    /**
     * modifica los datos del user con el que se ha accedido, realizando varias consultas para comrpobar si ya existen anteriormente
     * porque no se puede modificar el mismo dato
     */
    public void editarCuenta() {
        int registrosModificados; //guardara la cantidad de datos que se han modificado
        //conexion a la tabla user
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(this, "user", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();
        //asigno a las variables el valor de los campos de texto
        String nombre = cuadroNuevoNombre.getText().toString();
        String usuario = cuadroNuevoUsuario.getText().toString();

        //asigno los dos valores al registro
        ContentValues registro =new ContentValues();
        registro.put("nombre", nombre);
        registro.put("usuario", usuario);

        //consultas para comprobar si ya existen dicho nombre y usuario en la base de datos
        Cursor consulta1 = bd.rawQuery("SELECT nombre FROM user WHERE nombre=\'"+nombre+"\'", null);
        Cursor consulta2 = bd.rawQuery("SELECT usuario FROM user WHERE usuario=\'"+usuario+"\'", null);

        //si la primera consulta o la segunda devuelve algun valor, es decir que existe dicho user en la tabla
        if (consulta1.moveToFirst() || consulta2.moveToFirst()) {
            //muestro Toast con el mensaje de error y dejo todos los campos en blanco
            Toast.makeText(this, R.string.errorDatosExisten, Toast.LENGTH_SHORT).show();
            this.cuadroNuevoNombre.setText("");
            this.cuadroNuevoUsuario.setText("");

            //recupero el foco para volver a insertar nuevos datos que sean validos
            this.cuadroNuevoNombre.requestFocus();
            this.cuadroNuevoUsuario.requestFocus();
        } else { //si no
            registrosModificados = bd.update("user", registro, "correo=\'"+correo+"\'", null); //actualizo el user en la base de datos

            if (registrosModificados == 1) { //si se ha modificado el dato
                Toast.makeText(this, R.string.datosModificados, Toast.LENGTH_SHORT).show(); //muestro mensaje informativo
                finish(); //cierro la actividad
            }
            else { //si no
                Toast.makeText(this, R.string.errorModificacionDatos, Toast.LENGTH_SHORT).show(); //muestro mensaje informativo
            }

        }
        bd.close(); //cierro conexion
    }

}