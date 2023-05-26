package com.example.homelooker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Ajustes extends AppCompatActivity {

    //creacion de variables
    private Button botonEditar;
    private Button botonCambiarContra;
    private Button botonEliminar;
    private String correo; //guardara el valor del correo que recibe en el bundle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        //obtenemos la vista de los objetos
        this.botonEditar = (Button) findViewById(R.id.botonEditar);
        this.botonCambiarContra = (Button) findViewById(R.id.botonCambiarContra);
        this.botonEliminar = (Button) findViewById(R.id.botonEliminar);

        //Obtenemos el correo con el que hemos accedido
        Intent intentRecibido = getIntent();
        Bundle datos = intentRecibido.getExtras(); //obtengo los datos
        if(datos != null){ //si no estan vacios
            correo = datos.getString("correo"); //asigno a la variable el valor de el dato correo que recibe
        }else{
            correo = "";
        }

        //al pulsar en el boton editar
        this.botonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EditarCuenta.class); //creo un intent a la ventana de editar cuenta
                Bundle datos = new Bundle(); //creo bundle para enviar los datos
                datos.putString("correo", correo); //asigno el dato
                intent.putExtras(datos); //asignos los extras al bundle

                startActivity(intent); //inicio la nueva actividad
            }
        });

        //al pulsar en el boton cambiar contrasenia
        this.botonCambiarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CambiarContrasenia.class); //creo un intent a la ventana de cambiar contrasenia
                Bundle datos = new Bundle(); //bundle para enviar los datos
                datos.putString("correo", correo); //asigno el dato
                intent.putExtras(datos); //asigno los extras al bundle

                startActivity(intent); //inicio la nueva actividad
            }
        });

        //al pulsar en el boton de eliminar cuenta
        this.botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCuadroDialogoConfirm(); //ejecuto el metodo que muestra el cuadro de dialogo de confirmacion
            }
        });

    }

    /**
     * muestra un cuadro de dialogo de confirmacion para eliminar la cuenta del user actual
     */
    private void crearCuadroDialogoConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //creo un AlertDialog para el  cuadro
        builder.setMessage(R.string.seguroEliminarCuenta); //mensaje en el cuadro
        builder.setTitle(R.string.eliminarCuenta); //titulo del cuadro
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() { //opcion positiva
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eliminarUsuario(); //ejecuto el metodo que elimina el user
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() { //opcion negativa
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //No ocurre nada
            }
        });
        builder.show(); //muestro el cuadro de confirmacion
        }

    /**
     * elimina el user con el que se ha accedido de la base de datos
     */
    public void eliminarUsuario() {
        int registrosBorrados; //guardara la cantidad de elementos eliminados de la base de datos
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(this, "user", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String where = "correo = ?"; //clausula where
        String[] args = {correo}; //valores del where

        registrosBorrados = bd.delete("user", where, args);

        if (registrosBorrados == 1) { //si se ha eliminado el registro
            Toast.makeText(this, R.string.borradoExito, Toast.LENGTH_SHORT).show(); //muestro mensaje informativo
            cerrarYReiniciarApp(); //ejecuto el metodo que cierra todas las ventanas y vuelve a la principal
        }
        else {
            Toast.makeText(this, R.string.noExisteUser, Toast.LENGTH_SHORT).show();
        }
        bd.close(); //cierro la conexion
    }

    /**
     * crea un intent a la ventana principal, crea una nueva tarea y limpia todas las anteriores,
     * finalmente cierra lo anterior
     */
    public void cerrarYReiniciarApp() {
        Intent intent = new Intent(this, MainActivity.class); //intent a la actividad principal
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //agrego al intent la flag de nueva tarea y limpiar las anteriores
        startActivity(intent); //inicio el intent
        finishAffinity(); //ejecuto el metodo que finaliza todas las actividades en la pila
    }

}