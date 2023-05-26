package com.example.homelooker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class Informacion extends AppCompatActivity {

    //creacion de variables
    private ImageView imagenEstancia;
    private ImageButton botonFav;
    private TextView textoNombre;
    private TextView textoUbicacion;
    private TextView textoPrecio;
    private TextView textoMetros;
    private TextView textoDormitorios;
    private TextView textoDescripcion;
    private TextView textoCaracteristicas;
    private Button botonContacto;
    private Button botonEditarEstancia;
    private Button botonEliminarEstancia;
    private TextView textoUbicacionSuperior;
    private String nombre;
    private String ubicacion;
    private String correo = "";
    private int cod_ubicacion;
    private String rol = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        //obtenemos la vista de los objetos
        this.imagenEstancia = (ImageView) findViewById(R.id.imagenEstancia);
        this.botonFav = (ImageButton) findViewById(R.id.botonFav);
        this.textoNombre = (TextView) findViewById(R.id.textoNombre);
        this.textoUbicacion = (TextView) findViewById(R.id.textoUbicacion);
        this.textoPrecio = (TextView) findViewById(R.id.textoPrecio);
        this.textoMetros = (TextView) findViewById(R.id.textoMetros);
        this.textoDormitorios = (TextView) findViewById(R.id.textoDormitorios);
        this.textoDescripcion = (TextView) findViewById(R.id.textoDescripcion);
        this.textoCaracteristicas = (TextView) findViewById(R.id.textoCaracteristicas);
        this.botonContacto = (Button) findViewById(R.id.botonContacto);
        this.botonEditarEstancia = (Button) findViewById(R.id.botonEditarEstancia);
        this.botonEliminarEstancia = (Button) findViewById(R.id.botonEliminarEstancia);
        this.textoUbicacionSuperior = (TextView) findViewById(R.id.textoUbicacionSuperior);

        Intent intentRecibido = getIntent(); //creamos un nuevo Intent
        Bundle datos = intentRecibido.getExtras(); //bundle que recibe los datos
        if(datos != null) { //si no esta vacio
            nombre = datos.getString("nombre");
            ubicacion = datos.getString("ubicacion");
            correo = datos.getString("correo");
        }else { //si no
            nombre = "";
        }

        //al pulsar en el icono de favoritos
        this.botonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertarFavorito(); //ejecuto el metodo que agrega la estancia a favoritos
            }
        });

        //al pulsar en el boton contacto
        this.botonContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactar(); //ejecuto el metodo que redirecciona a contactar
            }
        });

        comprobarRol(correo);

        //al pulsar en el boton editar estancia
        this.botonEditarEstancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EditarEstancia.class); //creo un intent hacia la gventana EditarEstancia
                Bundle datos = new Bundle(); //creo un bundle para mandar los datos
                datos.putString("nombre", nombre); //asigno el nombre al bundle
                intent.putExtras(datos); //asignos los datos

                startActivity(intent); //inicio la nueva actividad
            }
        });

        ocultarBotonEliminar(); //ejecuto el metodo que oculta el boton de eliminar si es necesario

        this.botonEliminarEstancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCuadroDialogoConfirm();
            }
        });

        rellenarContenido(); //ejecuto el metodo que rellena el contenido de la pagina

    }

    public void ocultarBotonEliminar() {
        //conexion a la base de datos, en la tabla user
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(getBaseContext(), "user", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();

        //podemos realizar la siguiente consulta porque solo esta recibiendo un correo de la otra clase
        Cursor consulta = bd.rawQuery("SELECT * FROM user WHERE correo=\'"+correo+"\'", null);

        if (!correo.startsWith("Inicia")) {
            if (consulta.moveToFirst()) { //si devuelve algo la consulta
                rol = consulta.getString(5); //doy valor a la variable que corresponda con el quinto resultado, es decir, el rol
                if (rol.equals("Vendedor")) { //si el rol es igual a Vendedor
                    botonEliminarEstancia.setVisibility(View.VISIBLE); //muestro el boton
                } else { //si no
                    if (rol.equals("Comprador")) { //si el rol es igual a Comprador
                        botonEliminarEstancia.clearAnimation(); //limpio la animacion del boton y lo oculto
                        botonEliminarEstancia.setVisibility(View.GONE);
                    }
                }
            }
        }
        else { //si la consulta no muestra nada
            botonEliminarEstancia.clearAnimation(); //limpio la animacion del boton y lo oculto
            botonEliminarEstancia.setVisibility(View.GONE);
        }

        bd.close();
    }

    /**
     * rellena el contenido que muestra la ventana actual, se hace mediante el codigo de este metodo
     */
    public void rellenarContenido() {
        if (ubicacion.equals("España")) { //si la ubicacion con la que se accede es Espania
            cod_ubicacion = 1; //asigno el valor para el codigo 1
        }
        else {
            if (ubicacion.equals("Italia")) { //si la ubicacion con la que se accede es Italia
                cod_ubicacion = 2; //asigno el valor para el codigo 2
            }
            else {
                if (ubicacion.equals("Portugal")) { //si la ubicacion con la que se accede es Portugal
                    cod_ubicacion = 3; //asigno el valor para el codigo 3
                }
                else {
                    cod_ubicacion = 4; //asigno el valor para el codigo 4
                }
            }
        }

        //conexion a la tabla estancia
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(this, "estancia", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();

        //consulta para obtener todos los datos de la estancia que tiene el nombre que se pasa en el where
        Cursor consulta = bd.rawQuery("SELECT * FROM estancia WHERE nombre=\'"+nombre+"\'", null);

        if (consulta.moveToFirst()) { //si la consulta devuelve algun resultado
            do {
                String url = consulta.getString(8); //asigno el valor del octavo indice (la imagen)

                Picasso.get().load(url).into(imagenEstancia); //pinto la imagen en el imageView a traves de la url mediante la libreria Picasso

                String nombre = consulta.getString(1); //asigno el valor del primer indice
                textoNombre.setText(nombre); //asigno este texto a el TextView textoNombre

                //Consulta a la tabla ubicacion para obtener la ciudad
                AdministracionSQLiteOpenHelper admin2 = new AdministracionSQLiteOpenHelper(this, "ubicacion", null, 2);
                SQLiteDatabase bd2 = admin2.getWritableDatabase();

                //consulta que devuelve todos los datos de la ubicacion con dicho codigo
                Cursor consultaUbicacion = bd2.rawQuery("SELECT * FROM ubicacion WHERE codigo="+cod_ubicacion, null);
                String ciudad = ""; //guardara el valor de la ciudad
                if (consultaUbicacion.moveToFirst()) { //si la consulta devuelve algun resultado
                    ciudad = consultaUbicacion.getString(1); //asigno a la variable el valor de el primer indice (la ciudad)
                }

                textoUbicacionSuperior.setText(ciudad+", "+ubicacion);
                textoUbicacion.setText(ciudad+", "+ubicacion); //asigno al TextView el valor de la ciudad y el pais

                String precio = consulta.getString(2); //guardo el valor de el segundo indice
                textoPrecio.setText(precio+"€"); //asigno al TextView el valor del precio

                String metros = consulta.getString(4); //guardo el valor de el cuarto indice
                textoMetros.setText(metros+" m\u00B2"); //asigno al TextView el valor de los metros cuadrados

                String habitaciones = consulta.getString(5); //guardo el valor de el quinto indice
                textoDormitorios.setText(habitaciones+" Dormitorios"); //asigno al TextView el valor de los metros cuadrados

                String descripcion = consulta.getString(6); //guardo el valor de el sexto indice
                textoDescripcion.setText(descripcion); //asigno al TextView el valor de la descripcion

                String caracteristicas = consulta.getString(7); //guardo el valor de el septimo indice
                textoCaracteristicas.setText(caracteristicas); //asigno al TextView el valor de las caracteristicas

                bd2.close();

            } while (consulta.moveToNext()); //mientras que la consulta devuelva un siguiente resultado
        }

        //cierro consulta y conexion
        consulta.close();
        bd.close();

    }

    /**
     * inserta en la tabla de favoritos el nombre de la estancia actual, para que en la ventana de favoritos se muestren sus datos
     */
    public void insertarFavorito() {
        //conexion a la tabla favoritos
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(this, "favoritos", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();

        //asigno al contenido los datos que necesita la tabla de favoritos
        ContentValues contenido = new ContentValues();
        contenido.put("nombre", nombre);
        contenido.put("ubicacion", ubicacion);
        contenido.put("correo", correo);

        //consulta para obtener el nombre cuandoe l nombre sea igual al que se pasa por parametro
        Cursor consulta = bd.rawQuery("SELECT nombre FROM favoritos WHERE nombre=\'"+nombre+"\'", null);

        if (consulta.moveToFirst()) { //si la consulta devuelve algun resultado
            int registrosBorrados; //guarda la cantidad de elementos borrados

            String where = "nombre = ?"; //clausula where
            String[] args = {nombre}; //valor que se va a pasar en el where

            registrosBorrados = bd.delete("favoritos", where, args); //elimina el elemento de la tabla favoritos

            if (registrosBorrados == 1) { //si se ha eliminado algun elemento
                Toast.makeText(this, R.string.estanciaEliminada, Toast.LENGTH_SHORT).show(); //muestro mensaje
            }
        }
        else { //si no, se aniade
            Toast.makeText(this, R.string.estanciaGuardadaFav, Toast.LENGTH_LONG).show(); //muestro mensaje
            bd.insert("favoritos", null, contenido); //inserto en la tabla la estancia favorita
        }

        bd.close();
    }

    /**
     * hace una redirecciona a el correo electronico para mandarle uno a el creador de la estancia
     */
    public void contactar() {
        Intent intent = new Intent(Intent.ACTION_SEND); //creo intent con accion de enviar
        intent.setType("message/rfc822"); //asigno el tipo mensaje
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{String.valueOf("abuenoh02@informatica.iesvalledeljerteplasencia.es")}); //asigno destinatario
        intent.putExtra(Intent.EXTRA_SUBJECT, "Estoy interesado en comprar su estancia."); //asigno asunto del correo
        intent.putExtra(Intent.EXTRA_TEXT, "Hola Alberto, me pongo en contacto contigo porque estoy interesado en comprar una vivienda que he visto en la app Homelooker"); //asigno cuerpo del correo electronico

        try {
            startActivity(Intent.createChooser(intent, "Enviar correo electrónico")); //inicio la actividad del intent
        } catch (ActivityNotFoundException ex) { //capturo la excepcion si no encuentra la activity
            Toast.makeText(getBaseContext(), R.string.errorCorreo, Toast.LENGTH_SHORT).show(); //muestro mensaje
        }
    }

    public void comprobarRol(String correo) {
        //conexion a la base de datos, en la tabla user
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(getBaseContext(), "user", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();

        //podemos realizar la siguiente consulta porque solo esta recibiendo un correo de la otra clase
        Cursor consulta = bd.rawQuery("SELECT * FROM user WHERE correo=\'"+correo+"\'", null);
        if (!correo.startsWith("Inicia")) {
            if (consulta.moveToFirst()) { //si devuelve algo la consulta
                rol = consulta.getString(5); //doy valor a la variable que corresponda con el quinto resultado, es decir, el rol
                if (rol.equals("Vendedor")) { //si el rol es igual a Vendedor
                    botonEditarEstancia.setVisibility(View.VISIBLE); //muestro el boton
                } else { //si no
                    if (rol.equals("Comprador")) { //si el rol es igual a Comprador
                        botonEditarEstancia.clearAnimation(); //limpio la animacion del boton y lo oculto
                        botonEditarEstancia.setVisibility(View.GONE);
                    }
                }
            }
        }
        else { //si la consulta no muestra nada
            botonEditarEstancia.clearAnimation(); //limpio la animacion del boton y lo oculto
            botonEditarEstancia.setVisibility(View.GONE);
        }
        //cierro la consulta y la conexion
        consulta.close();
        bd.close();
    }

    private void crearCuadroDialogoConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //creo un AlertDialog para el  cuadro
        builder.setMessage(R.string.seguroEliminarEstancia); //mensaje en el cuadro
        builder.setTitle(R.string.eliminarEstancia); //titulo del cuadro
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() { //opcion positiva
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eliminarEstancia(); //ejecuto el metodo que elimina el user
                finish();
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

    public void eliminarEstancia() {
        int registrosBorrados; //guardara la cantidad de elementos eliminados de la base de datos
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(this, "estancia", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String where = "nombre = ?"; //clausula where
        String[] args = {nombre}; //valores del where

        registrosBorrados = bd.delete("estancia", where, args);

        if (registrosBorrados == 1) { //si se ha eliminado el registro
            Toast.makeText(this, R.string.borradoExito, Toast.LENGTH_SHORT).show(); //muestro mensaje informativo
            //cerrarYReiniciarApp(); //ejecuto el metodo que cierra todas las ventanas y vuelve a la principal
        }
        else {
            Toast.makeText(this, R.string.noExisteEstancia, Toast.LENGTH_SHORT).show();
        }
        bd.close(); //cierro la conexion
    }

}