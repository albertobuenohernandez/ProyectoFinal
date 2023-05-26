package com.example.homelooker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class Resultados extends AppCompatActivity {

    //creacion de variables
    private String tipo;
    private String ubicacion;
    private String correo = "";
    private int cod_ubicacion;
    private LinearLayout layoutResultados;
    private Button botonFiltrar;
    private Button botonOrdenar;
    private ImageView imagenEstancia;
    private TextView contResultados;

    private int CODIGO_DE_SOLICITUD;
    private boolean mMethodExecuted = false; //comprueba la ejecucion del metodo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        //Obtenemos el codigo que acabamos de crear en la ventana de registro
        Intent intentRecibido = getIntent();
        Bundle datos = intentRecibido.getExtras();
        if(datos != null) { //si no esta vacio
            tipo = datos.getString("tipo");
            ubicacion = datos.getString("ubicacion");
            correo = datos.getString("correo");
        }else{
            tipo = "";
            ubicacion = "";
            correo = "";
        }

        //obtenemos la vista de los objetos
        this.imagenEstancia = (ImageView) findViewById(R.id.imagenEstancia);
        this.contResultados = (TextView) findViewById(R.id.contResultados);
        this.botonFiltrar = (Button) findViewById(R.id.botonFiltrar);
        this.botonOrdenar = (Button) findViewById(R.id.botonOrdenar);

        //al pulsar en el boton Filtrar
        this.botonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CODIGO_DE_SOLICITUD = 10; //asigno un codigo de solicitud
                Intent intent = new Intent(getBaseContext(), Filtrar.class); //creo un intent hacia la actividad de filtrar
                startActivityForResult(intent, CODIGO_DE_SOLICITUD); //ejecuto el metodo que se ejecuta cuando la actividad del intent finaliza, para recibir los datos
            }
        });

        //al pulsar en el boton Ordenar
        this.botonOrdenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Ordenar.class); //intent hacia la ventana de Ordenar
                startActivity(intent); //ejecuto el metodo que se ejecuta cuando la actividad del intent finaliza, para recibir los datos
            }
        });

        if (!mMethodExecuted) { //si el metodo no se ha ejecutado, es decir, que la variable siga en falso
            obtenerEstancia(); //ejecuto el metodo que obtiene la estancia y la muestra
            mMethodExecuted = true; //asigno la variable a true
        }

    }


    /**
     * se ejecurta cuando finalize la actividad este abierta, es necesaria para esta clase porque tiene que recibir datos de
     * las ventanas Filtrar y Ordenar. Este metodo creara el layout y lo mostrara teniendo en cuenta los valores que recibe al finalizar
     * la ventana anterior
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODIGO_DE_SOLICITUD) { //si el codigo de solicitud es igual al que se pasa por parametro
            if (resultCode == RESULT_OK) { //si el codigo de resultado es el mismo
                layoutResultados.removeAllViews(); //elimina todas las vistas que esten dibujadas en este layout, como TextView, Spinner, etc

                MyTask task = new MyTask(this);
                task.execute();

                //obtenemos los datos de resultado del Intent que acaba de finalizar
                String precioMin = data.getStringExtra("precioMin");
                String precioMax = data.getStringExtra("precioMax");
                String metrosMin = data.getStringExtra("metrosMin");
                String metrosMax = data.getStringExtra("metrosMax");
                String numDorm = data.getStringExtra("numDorm");
                String orden = data.getStringExtra("orden");

                //guarda el valor de el precio minimo y maximo pasado a entero
                int precioMinInt = Integer.parseInt(precioMin);
                int precioMaxInt = Integer.parseInt(precioMax);
                int metrosMinInt = Integer.parseInt(metrosMin);
                int metrosMaxInt = Integer.parseInt(metrosMax);
                int dormInt = Integer.parseInt(numDorm);

                //conexion a la tabla estancia de la base de datos
                AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(this, "estancia", null, 2);
                SQLiteDatabase bd = admin.getWritableDatabase();
                Cursor consulta; //creo un cursor para la consulta en cada caso

                if (orden.equals("") || orden == null) { //si el orden que recibe no esta vacio
                    //realizo la consulta ordenada por el orden
                    consulta = bd.rawQuery("SELECT * FROM estancia WHERE tipo=\'"+tipo+"\' AND ubicacion_cod="+cod_ubicacion+" AND precio >= "+precioMinInt+" AND precio <= "+precioMaxInt+" AND metros >= "+metrosMinInt+" AND metros <= "+metrosMaxInt+" AND dormitorios=\'"+dormInt+"\'", null);
                }
                else { //si no se ejecuta sin ordenar
                    //consulta = bd.rawQuery("SELECT * FROM estancia WHERE tipo=\'"+tipo+"\' AND ubicacion_cod="+cod_ubicacion+" AND precio >= "+precioMinInt+" AND precio <= "+precioMaxInt+" AND metros >= "+metrosMinInt+" AND metros <= "+metrosMaxInt+" AND dormitorios=\'"+dormInt+"\' ORDER BY "+orden, null);

                    String query = "SELECT * FROM estancia WHERE tipo=? AND ubicacion_cod=? AND precio >= ? AND precio <= ? AND metros >= ? AND metros <= ? AND dormitorios=? ORDER BY ?";
                    String[] args = new String[] { tipo, String.valueOf(cod_ubicacion), String.valueOf(precioMinInt), String.valueOf(precioMaxInt), String.valueOf(metrosMinInt), String.valueOf(metrosMaxInt), String.valueOf(dormInt), orden};

                    consulta = bd.rawQuery(query, args);
                }

                this.contResultados.setText(consulta.getCount()+" resultados encontrados"); //asigno a el TextView la cantidad de resultados que devuelve la consulta

                this.layoutResultados = (LinearLayout) findViewById(R.id.layoutResultados); //obtengo la vista del layout

                if (consulta.moveToFirst() && consulta.getCount() != 0) { //si la consulta devuelve algun resultado y el numero de resultados no es igual a 0
                    do {
                        LinearLayout contenedor = new LinearLayout(this); //creo un LinearLayout que funcionara como contenedor
                        contenedor.setLayoutParams(new LinearLayout.LayoutParams( //asigno parametros
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        contenedor.setOrientation(LinearLayout.VERTICAL); //asigno orientacion vertical

                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) contenedor.getLayoutParams(); //obtengo los parametros
                        layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.espacio); //establezco un margen en el bottom
                        contenedor.setLayoutParams(layoutParams); //asigno los parametros

                        ImageView imageView = new ImageView(this); //creo un objeto ImageView
                        imageView.setLayoutParams(new LinearLayout.LayoutParams( //asigno parametros
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                (int) getResources().getDimension(R.dimen.image_height)));

                        contenedor.addView(imageView); //aniado el imageView a el contenedor

                        String url = consulta.getString(8); //obtengo el valor con indice 8 (la url de la imagen)

                        Picasso.get().load(url).into(imageView); //dibujo la imagen mediante la libreria Picasso

                        //Mostramos el nombre
                        TextView textViewNombre = new TextView(this); //creo TextView para el nombre
                        textViewNombre.setLayoutParams(new LinearLayout.LayoutParams( //asigno parametros
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(25, 0, 0, 0); //establezco un margen isquiero de 20
                        textViewNombre.setLayoutParams(params);

                        String nombre = consulta.getString(1); //obtengo el valor con indice 1
                        textViewNombre.setText(nombre); //asigno el valor al TextView
                        contenedor.addView(textViewNombre); //aniado el TextView al contenedor


                        //Consulta a la base de datos para obtener la ciudad
                        AdministracionSQLiteOpenHelper admin2 = new AdministracionSQLiteOpenHelper(this, "ubicacion", null, 2);
                        SQLiteDatabase bd2 = admin2.getWritableDatabase();
                        Cursor consultaUbicacion = bd2.rawQuery("SELECT * FROM ubicacion WHERE codigo="+cod_ubicacion, null);
                        String ciudad = "";
                        if (consultaUbicacion.moveToFirst()) {
                            //System.out.println(consultaUbicacion.getString(1));
                            ciudad = consultaUbicacion.getString(1);  //obtengo el valor con indice 1 de la consulta anterior
                        }

                        TextView textViewUbicacion = new TextView(this); //creo TextView para la ubicacion
                        textViewUbicacion.setLayoutParams(new LinearLayout.LayoutParams( //asigno parametros
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        ViewGroup.MarginLayoutParams paramsUbi = new ViewGroup.MarginLayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        paramsUbi.setMargins(25, 0, 0, 0); //establezco un margen isquiero de 20
                        textViewUbicacion.setLayoutParams(params);

                        textViewUbicacion.setText(ciudad+", "+ubicacion);
                        contenedor.addView(textViewUbicacion); //aniado el TextView al contenedor


                        TextView textViewPrecio = new TextView(this); //creo TextView para el precio
                        textViewPrecio.setLayoutParams(new LinearLayout.LayoutParams( //asigno parametros
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        ViewGroup.MarginLayoutParams paramsPrecio = new ViewGroup.MarginLayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        paramsPrecio.setMargins(25, 0, 0, 0); //establezco un margen isquiero de 20
                        textViewPrecio.setLayoutParams(params);

                        String precio = consulta.getString(2);  //obtengo el valor con indice 2
                        textViewPrecio.setText(precio+"€"); //asigno el valor al TextView
                        contenedor.addView(textViewPrecio); //aniado el TextView al contenedor


                        TextView textViewHabitaciones = new TextView(this); //creo TextView para el numero de dormitorios
                        textViewHabitaciones.setLayoutParams(new LinearLayout.LayoutParams( //asigno parametros
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        ViewGroup.MarginLayoutParams paramsHab = new ViewGroup.MarginLayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        paramsHab.setMargins(25, 0, 0, 0); //establezco un margen isquiero de 20
                        textViewHabitaciones.setLayoutParams(params);

                        String habitaciones = consulta.getString(5);  //obtengo el valor con indice 5
                        textViewHabitaciones.setText(habitaciones+" Dormitorios"); //asigno el valor al TextView
                        contenedor.addView(textViewHabitaciones); //aniado el TextView al contenedor


                        TextView textViewMetros = new TextView(this); //creo TextView para los metros cuadrados
                        textViewMetros.setLayoutParams(new LinearLayout.LayoutParams( //asigno parametros
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        ViewGroup.MarginLayoutParams paramsMetros = new ViewGroup.MarginLayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        paramsMetros.setMargins(25, 0, 0, 0); //establezco un margen isquiero de 20
                        textViewMetros.setLayoutParams(params);

                        String metros = consulta.getString(4);  //obtengo el valor con indice 4
                        textViewMetros.setText(metros+" m\u00B2"); //asigno el valor al TextView
                        contenedor.addView(textViewMetros); //aniado el TextView al contenedor

                        layoutResultados.addView(contenedor); //aniado al layout el contenedor que se ha estado creando

                        //al pulsar en cualquier parte del contenedor
                        contenedor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getBaseContext(), Informacion.class); //intent hacia la ventana de informacion
                                Bundle datos = new Bundle(); //bundle para enviar los datos
                                datos.putString("nombre", nombre);
                                datos.putString("ubicacion", ubicacion);
                                datos.putString("correo", correo);
                                intent.putExtras(datos); //asigno los datos a enviar

                                startActivity(intent); //inicio el nuevo activity
                            }
                        });

                        bd2.close();

                    } while (consulta.moveToNext()); //mientras que la consulta devuelva un siguiente resultado
                }

                //cierro consulta y conexion
                consulta.close();
                bd.close();
            }
        }
    }

    /**
     * genera a traves del codigo un contenedor donde se muestran todos los datos de cada uno de las estancia, como se
     * ha realizado en el metod anterior pero esta vez sin ejecutarse al finalizar la actividad, por lo que no necesita limpiar las vistas anteriores
     */
    public void obtenerEstancia() {
        if (ubicacion.equals("España")) { //si la ubicacion que recibe es igual a Espania
            cod_ubicacion = 1; //asigno al codigo el valor de 1
        }
        else {
            if (ubicacion.equals("Italia")) { //si la ubicacion que recibe es igual a Italia
                cod_ubicacion = 2; //asigno al codigo el valor de 2
            }
            else {
                if (ubicacion.equals("Portugal")) { //si la ubicacion que recibe es igual a Portugal
                    cod_ubicacion = 3; //asigno al codigo el valor de 3
                }
                else {
                    cod_ubicacion = 4; //asigno al codigo el valor de 4
                }
            }
        }

        //conexion a la tabla estancia
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(this, "estancia", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();

        //consulta para obtener todos los datos cuyo tipo y codigo sea igual a los que recibe
        Cursor consulta = bd.rawQuery("SELECT * FROM estancia WHERE tipo=\'"+tipo+"\' AND ubicacion_cod="+cod_ubicacion, null);

        this.contResultados.setText(consulta.getCount()+" resultados encontrados"); //asigno el valor al TextView para mostrar la cantidad de resultados

        this.layoutResultados = (LinearLayout) findViewById(R.id.layoutResultados); //asigno la vista al LinearLayout de resultados

        MyTask task = new MyTask(this);
        task.execute();

        if (consulta.moveToFirst()) { //si la consulta devuelve algun dato
            do {
                LinearLayout contenedor = new LinearLayout(this); //creo un contenedor donde se aniade el siguiente contenido
                contenedor.setLayoutParams(new LinearLayout.LayoutParams( //aniado parametros
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                contenedor.setOrientation(LinearLayout.VERTICAL); //asigno orientacion vertical

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) contenedor.getLayoutParams(); //obtengo los parametros
                layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.espacio); //asigno un margen en el bottom
                contenedor.setLayoutParams(layoutParams); //asigno al contenedor los parametros creados

                ImageView imageView = new ImageView(this); //creo un ImageView
                imageView.setLayoutParams(new LinearLayout.LayoutParams( //asigno parametros
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) getResources().getDimension(R.dimen.image_height)));

                contenedor.addView(imageView); //aniado el ImageView al contenedor

                String url = consulta.getString(8); //obtengo el valor del octavo indice (la url de la imagen)

                Picasso.get().load(url).into(imageView); //dibujo la imagen a traves de la libreria Picasso

                //Mostramos el nombre
                TextView textViewNombre = new TextView(this); //creo un TextView para el nombre de la estancia
                textViewNombre.setLayoutParams(new LinearLayout.LayoutParams( //asigno parametros
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(25, 0, 0, 0); //establezco un margen isquiero de 20
                textViewNombre.setLayoutParams(params);

                String nombre = consulta.getString(1); //asigno a la variable el valor de primer indice que decuelve la consulta
                textViewNombre.setText(nombre); //establezco dicho valor a el TextView de nombre
                contenedor.addView(textViewNombre); //aniado al contenedor el nuevo TextView


                //Consulta a la base de datos para obtener la ciudad
                AdministracionSQLiteOpenHelper admin2 = new AdministracionSQLiteOpenHelper(this, "ubicacion", null, 2);
                SQLiteDatabase bd2 = admin2.getWritableDatabase();
                Cursor consultaUbicacion = bd2.rawQuery("SELECT * FROM ubicacion WHERE codigo="+cod_ubicacion, null);
                String ciudad = "";
                if (consultaUbicacion.moveToFirst()) { //si la consulta devuelve algun resultado
                    ciudad = consultaUbicacion.getString(1); //asigno el valor del primer indice
                }

                TextView textViewUbicacion = new TextView(this); //creo un TextView para la ubicacion de la estancia
                textViewUbicacion.setLayoutParams(new LinearLayout.LayoutParams( //asigno parametros
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                ViewGroup.MarginLayoutParams paramsUbi = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                paramsUbi.setMargins(25, 0, 0, 0); //establezco un margen isquiero de 20
                textViewUbicacion.setLayoutParams(params);

                textViewUbicacion.setText(ciudad+", "+ubicacion);
                contenedor.addView(textViewUbicacion); //aniado al contenedor el nuevo TextView


                TextView textViewPrecio = new TextView(this); //creo un TextView para el precio de la estancia
                textViewPrecio.setLayoutParams(new LinearLayout.LayoutParams( //asigno parametros
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                ViewGroup.MarginLayoutParams paramsPrecio = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                paramsPrecio.setMargins(25, 0, 0, 0); //establezco un margen isquiero de 20
                textViewPrecio.setLayoutParams(params);

                String precio = consulta.getString(2); //obtengo el valor del segundo indice
                textViewPrecio.setText(precio+"€");
                contenedor.addView(textViewPrecio); //aniado al contenedor el nuevo TextView


                TextView textViewHabitaciones = new TextView(this); //creo un TextView para los dormitorios de la estancia
                textViewHabitaciones.setLayoutParams(new LinearLayout.LayoutParams( //asigno parametros
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                ViewGroup.MarginLayoutParams paramsHab = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                paramsHab.setMargins(25, 0, 0, 0); //establezco un margen isquiero de 20
                textViewHabitaciones.setLayoutParams(params);

                String habitaciones = consulta.getString(5); //obtengo el valor del quinto indice
                textViewHabitaciones.setText(habitaciones+" Dormitorios");
                contenedor.addView(textViewHabitaciones); //aniado al contenedor el nuevo TextView


                TextView textViewMetros = new TextView(this); //creo un TextView para los metros de la estancia
                textViewMetros.setLayoutParams(new LinearLayout.LayoutParams( //asigno parametros
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                ViewGroup.MarginLayoutParams paramsMetros = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                paramsMetros.setMargins(25, 0, 0, 0); //establezco un margen isquiero de 20
                textViewMetros.setLayoutParams(params);

                String metros = consulta.getString(4); //obtengo el valor del cuarto indice
                textViewMetros.setText(metros+" m\u00B2");
                contenedor.addView(textViewMetros); //aniado al contenedor el nuevo TextView


                layoutResultados.addView(contenedor); //anaido al LinearLayout el contenedor que se ha ido creando

                //al pulsar en cualquier parte del contenedor
                contenedor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), Informacion.class); //intent hacia la ventana de informacion
                        Bundle datos = new Bundle(); //bundle para enviar los datos
                        datos.putString("nombre", nombre);
                        datos.putString("ubicacion", ubicacion);
                        datos.putString("correo", correo);
                        intent.putExtras(datos); //asigno los datos al bundle

                        startActivity(intent); //inicio la nueva actividad
                    }
                });

                bd2.close();

            } while (consulta.moveToNext()); //mientras que la consulta devuelva un siguiente resultado
        }

        //cierro la consulta y la conexion
        consulta.close();
        bd.close();
    }

}