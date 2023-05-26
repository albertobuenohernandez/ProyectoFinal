package com.example.homelooker.ui.favoritos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.homelooker.AdministracionSQLiteOpenHelper;
import com.example.homelooker.Informacion;
import com.example.homelooker.R;
import com.example.homelooker.databinding.FragmentFavoritosBinding;


public class FavoritosFragment extends Fragment {

    //creacion de variables
    private LinearLayout layoutResultados; //es necesario una variable del layout porque se va a crear el contenido a traves de codigo

    private FragmentFavoritosBinding binding;
    private int cod_ubicacion; //guarda el codigo de la ubicacion dependiendo del pais

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FavoritosViewModel galleryViewModel =
                new ViewModelProvider(this).get(FavoritosViewModel.class);

        binding = FragmentFavoritosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //obtenemos la vista de los objetos
        this.layoutResultados = (LinearLayout) root.findViewById(R.id.layoutResultados);

        obtenerFavoritos(); //ejecuto el metodo para mostrar en el layout las estancias favoritas

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * obtiene a traves de una consulta a la tabla favoritos, todas las estancias que lo son,
     * luego hace otra consulta a la tabla estancia para obtener todos sus datos y mostrarlos a traves de codigo java
     */
    public void obtenerFavoritos() {
        //conexion a la tabla favoritos
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(getContext(), "favoritos", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor consulta = bd.rawQuery("SELECT * FROM favoritos", null); //consulta para obtener todos los favoritos

        if (consulta.moveToFirst()) { //si la consulta devuelve algun resultado
            do {
                LinearLayout contenedor = new LinearLayout(getContext()); //crea un nuevo LinearLayout donde se almacenara el contenido
                contenedor.setLayoutParams(new LinearLayout.LayoutParams( //asigno los parametros del layout
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                contenedor.setOrientation(LinearLayout.VERTICAL); //asigno orientacion vertical al layout

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) contenedor.getLayoutParams(); //variable para los parametros
                layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.espacio); //asigno un margin en el bottom
                contenedor.setLayoutParams(layoutParams); //asigno a el layout todos estos parametros

                String correo = consulta.getString(3); //guardo el correo que hay que mandar

                //creacion de un TextView para el nombre
                TextView textViewNombre = new TextView(getContext());
                textViewNombre.setLayoutParams(new LinearLayout.LayoutParams( //asigno parametros
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                String nombre = consulta.getString(1); //obtengo el nombre gracias a la consulta
                textViewNombre.setText(nombre); //asigno a el TextView el valor del nombre
                contenedor.addView(textViewNombre); //aniado al layout el TextView

                //conexion a la tabla estancia
                AdministracionSQLiteOpenHelper adminUbi = new AdministracionSQLiteOpenHelper(getContext(), "estancia", null, 2);
                SQLiteDatabase bdUbi = adminUbi.getWritableDatabase();

                String ubicacion = consulta.getString(2); //obtengo la ubicacion de la estancia favorita, con una consulta a la tabla favoritos

                if (ubicacion.equals("España")) { //si la ubicacion es igual a Espania
                    cod_ubicacion = 1; //asigno a la variable del codigo el valor 1
                }
                else { //si no
                    if (ubicacion.equals("Italia")) { //si es igual a Italia
                        cod_ubicacion = 2; //asigno a la variable del codigo el valor 2
                    }
                    else {
                        if (ubicacion.equals("Portugal")) { //si es igual a Portugal
                            cod_ubicacion = 3; //asigno a la variable del codigo el valor 3
                        }
                        else { //si no
                            cod_ubicacion = 4; //asigno a la variable del codigo el valor 4
                        }
                    }
                }

                //consulta para obtener todos los datos de la tabla ubicacion que tengan un codigo igual al que se pasa en el where
                Cursor consultaUbicacion = bdUbi.rawQuery("SELECT * FROM ubicacion WHERE codigo="+cod_ubicacion, null);

                String ciudad = ""; //variable que mas tarde va a guardar el valor de la ciudad
                if (consultaUbicacion.moveToFirst()) { //si la anterior consulta devuelve algun dato
                    ciudad = consultaUbicacion.getString(1); //asigno a la variable ciudad el valor de el String con indice 1 del la consulta
                }

                TextView textViewUbicacion = new TextView(getContext()); //creo TextView para la ubicacion
                textViewUbicacion.setLayoutParams(new LinearLayout.LayoutParams( //asigno parametros al TextView
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                textViewUbicacion.setText(ciudad+", "+ubicacion); //asigno al TextView su valor
                contenedor.addView(textViewUbicacion); //aniado al layout el TextView


                //conexion a la tabla estancia
                AdministracionSQLiteOpenHelper admin2 = new AdministracionSQLiteOpenHelper(getContext(), "estancia", null, 2);
                SQLiteDatabase bd2 = admin2.getWritableDatabase();

                //consulta que devuelve todos los datos de la estancia que cuyo nombre sea igual al que se pasa como parametro
                Cursor consulta2 = bd2.rawQuery("SELECT * FROM estancia WHERE nombre=\'"+nombre+"\'", null);
                String precio = ""; //variable que guardara el valor del precio
                if (consulta2.moveToFirst()) { //si la anterior consulta devuelve algun dato
                    precio = consulta2.getString(2); //asigno a la variable precio, el valor de el segundo indice de la consulta
                }

                TextView textViewPrecio = new TextView(getContext()); //creo TextView para el precio
                textViewPrecio.setLayoutParams(new LinearLayout.LayoutParams( //asigno parametros al TextView
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                textViewPrecio.setText(precio+"€"); //asigno al TextView el valor que le corresponde
                contenedor.addView(textViewPrecio);

                TextView textViewMetros = new TextView(getContext());
                textViewMetros.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                String metros = consulta2.getString(4);
                textViewMetros.setText(metros+" "+R.string.metros);
                contenedor.addView(textViewMetros); //aniado el TextView al layout

                layoutResultados.addView(contenedor); //finalmente aniado al layout la vista del layout que se ha estado creando

                //al hacer click en cualquier parte del layout (contenedor) que se ha estado creando
                contenedor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), Informacion.class); //intent hacia la ventana de informacion, para mostrar el contenido
                        Bundle datos = new Bundle(); //creo bundle para mandarle los datos necesarios
                        datos.putString("nombre", nombre);
                        datos.putString("ubicacion", ubicacion);
                        datos.putString("correo", correo);
                        intent.putExtras(datos); //asigno los datos anteriores al bundle

                        startActivity(intent); //inicio la actividad del intent
                    }
                });

            } while (consulta.moveToNext()); //mientras que la consulta tenga un siguiente resultado
        }
        //cierro consulta y conexion
        consulta.close();
        bd.close();

    }

}