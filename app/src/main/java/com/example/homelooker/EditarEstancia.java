package com.example.homelooker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditarEstancia extends AppCompatActivity {

    //creacion de variables
    private EditText campoEditPrecio;
    private EditText campoEditDesc;
    private EditText campoEditCarac;
    private Button botonEditEstancia;
    private String nombre; //guardara el valor del nombre de la estancia

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_estancia);

        Intent intentRecibido = getIntent(); //obtengo el intent
        Bundle datos = intentRecibido.getExtras(); //bundle que recibe los datos
        if(datos != null) { //si no esta vacio
            nombre = datos.getString("nombre"); //asigno el valor del correo que recibe
        }else{
            nombre = "";
        }

        //obtenemos la vista de los objetos
        this.campoEditPrecio = (EditText) findViewById(R.id.campoEditPrecio);
        this.campoEditDesc = (EditText) findViewById(R.id.campoEditDesc);
        this.campoEditCarac = (EditText) findViewById(R.id.campoEditCarac);
        this.botonEditEstancia = (Button) findViewById(R.id.botonEditEstancia);

        //al pulsar en el boton editar estancia
        this.botonEditEstancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarCuenta(); //se ejecuta el metodo que edita la estancia en la base de datos
            }
        });

    }

    /**
     * obtiene los campos que se han insertado y los modifica en la base de datos
     */
    public void editarCuenta() {
        int registrosModificados; //guardara la cantidad de resultados que se han modificado
        //conexion con la tabla estancia
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(this, "estancia", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();

        //creo variables con el valor del texto que se ha insertado en cada campo
        String precio = campoEditPrecio.getText().toString();
        String descripcion = campoEditDesc.getText().toString();
        String caracteristicas = campoEditCarac.getText().toString();

        //creo el registro para asignarle los valores que se van a actualizar
        ContentValues registro =new ContentValues();
        registro.put("precio", precio);
        registro.put("descripcion", descripcion);
        registro.put("caracteristicas", caracteristicas);

        //ejecuto la modificacion
        registrosModificados = bd.update("estancia", registro, "nombre=\'"+nombre+"\'", null);

        if (registrosModificados == 1) { //si se ha modificado
            Toast.makeText(this, R.string.datosModificados, Toast.LENGTH_SHORT).show(); //muestro mensaje
            finish(); //cierro la actividad actual
        }
        else { //si no
            Toast.makeText(this, R.string.errorModificacionDatos, Toast.LENGTH_SHORT).show(); //muestro mensaje
        }

        bd.close(); //cierro la conexion
    }

}