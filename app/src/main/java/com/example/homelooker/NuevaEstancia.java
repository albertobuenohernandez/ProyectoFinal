package com.example.homelooker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NuevaEstancia extends AppCompatActivity {

    //creacion de variables
    private EditText cuadroNombreEstancia;
    private EditText cuadroPrecioEstancia;
    private Spinner spinnerTipoBusq;
    private EditText cuadroMetrosEstancia;
    private EditText cuadroDormitoriosEstancia;
    private EditText textAreaDescripcion;
    private EditText textAreaCaracteristicas;
    private Spinner spinnerUbicacion;
    private EditText cuadroImagen;
    private Button botonInsertarEstancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_estancia);

        //obtenemos la vista de los objetos
        this.cuadroNombreEstancia = (EditText) findViewById(R.id.cuadroNombreEstancia);
        this.cuadroPrecioEstancia = (EditText) findViewById(R.id.cuadroPrecioEstancia);
        this.spinnerTipoBusq = (Spinner) findViewById(R.id.spinnerTipoBusq);
        this.cuadroMetrosEstancia = (EditText) findViewById(R.id.cuadroMetrosEstancia);
        this.cuadroDormitoriosEstancia = (EditText) findViewById(R.id.cuadroDormitoriosEstancia);
        this.textAreaDescripcion = (EditText) findViewById(R.id.textAreaDescripcion);
        this.textAreaCaracteristicas = (EditText) findViewById(R.id.textAreaCaracteristicas);
        this.spinnerUbicacion = (Spinner) findViewById(R.id.spinnerUbicacion);
        this.cuadroImagen = (EditText) findViewById(R.id.cuadroImagen);
        this.botonInsertarEstancia = (Button) findViewById(R.id.botonInsertarEstancia);

        //al pulsar en el boton insertar estancia
        this.botonInsertarEstancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registroEstancia(); //ejecuto el metodo que agrega la nueva estancia a la base de datos
            }
        });

    }

    /**
     * realiza una conexion a la base de datos y aniade una nueva estancia con los datos que inserta el
     * usuario en los diferentes campos de texto y spinners
     */
    public void registroEstancia() {
        //ejecuto la conexion a la tabla estancia
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(this, "estancia", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();

        //guardo el valor del texto insertado en cada cuadro de texto
        String nombre = cuadroNombreEstancia.getText().toString();
        String precio = cuadroPrecioEstancia.getText().toString();
        String tipo = spinnerTipoBusq.getSelectedItem().toString();
        String metros = cuadroMetrosEstancia.getText().toString();
        String dormitorios = cuadroDormitoriosEstancia.getText().toString();
        String descripcion = textAreaDescripcion.getText().toString();
        String caracteristicas = textAreaCaracteristicas.getText().toString();
        String ubicacion = spinnerUbicacion.getSelectedItem().toString();
        String imagen = cuadroImagen.getText().toString();
        int cod_ubicacion; //guarda el valor del codigo de ubicacion

        if (ubicacion.equals("España")) { //si la ubicacion es igual a Espania
            cod_ubicacion = 1; //asigno el valor 1
        }
        else {
            if (ubicacion.equals("Italia")) { //si la ubicacion es igual a Italia
                cod_ubicacion = 2; //asigno el valor 2
            }
            else {
                if (ubicacion.equals("Portugal")) { //si la ubicacion es igual a Portugal
                    cod_ubicacion = 3; //asigno el valor 3
                }
                else {
                    cod_ubicacion = 4; //asigno el valor 4
                }
            }
        }

        //asigno el contenido para que pueda realizarse la consulta y hacer la comprobacion
        ContentValues contenido = new ContentValues();
        contenido.put("nombre", nombre);
        contenido.put("precio", precio);
        contenido.put("tipo", tipo);
        contenido.put("metros", metros);
        contenido.put("dormitorios", dormitorios);
        contenido.put("descripcion", descripcion);
        contenido.put("caracteristicas", caracteristicas);
        contenido.put("imagen", imagen);
        contenido.put("ubicacion_cod", cod_ubicacion);

        //ejecuto la siguiente consulta para comprobar si existe el nombre insertado
        Cursor consulta1 = bd.rawQuery("SELECT nombre FROM estancia WHERE nombre=\'"+nombre+"\'", null);

        //si la primera consulta devuelve algun valor, es decir que existe dicho user en la tabla
        if (consulta1.moveToFirst()) {
            //muestro Toast con el mensaje de error y dejo todos los campos en blanco
            Toast.makeText(this, R.string.errorDatosExisten, Toast.LENGTH_SHORT).show();
            this.cuadroNombreEstancia.setText("");
            this.cuadroPrecioEstancia.setText("");
            this.cuadroMetrosEstancia.setText("");
            this.cuadroDormitoriosEstancia.setText("");
            this.textAreaDescripcion.setText("");
            this.textAreaCaracteristicas.setText("");
            this.cuadroImagen.setText("");

            //recupero el foco para volver a insertar nuevos datos que sean validos
            this.cuadroNombreEstancia.requestFocus();
            this.cuadroPrecioEstancia.requestFocus();
            this.cuadroMetrosEstancia.requestFocus();
            this.cuadroDormitoriosEstancia.requestFocus();
            this.textAreaDescripcion.requestFocus();
            this.textAreaCaracteristicas.requestFocus();
            this.cuadroImagen.requestFocus();

        } else { //si no
            //muestro mensaje de que se ha creado la nueva estancia
            Toast.makeText(this, R.string.estanciaCreada, Toast.LENGTH_LONG).show();
            bd.insert("estancia", null, contenido); //inserto en la tabla la nueva estancia

            finish(); //cierro la ventana de registro actual
        }

        bd.close(); //cierro la conexion con la base de datos
    }

}