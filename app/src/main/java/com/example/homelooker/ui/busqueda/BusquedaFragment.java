package com.example.homelooker.ui.busqueda;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.homelooker.AdministracionSQLiteOpenHelper;
import com.example.homelooker.Inicio;
import com.example.homelooker.NuevaEstancia;
import com.example.homelooker.R;
import com.example.homelooker.Resultados;
import com.example.homelooker.databinding.FragmentBusquedaBinding;

public class BusquedaFragment extends Fragment {

    //creacion de variables
    private Spinner spinnerTipo;
    private EditText cuadroUbicacion;
    private Button botonBuscar;
    private Button botonAniadirBusqueda;
    private TextView textoUsuario;

    private String correo = ""; //guardara el valor del correo
    private String rol = ""; //guardara el valor del rol con el que se inicia

    private FragmentBusquedaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BusquedaViewModel BusquedaViewModel =
                new ViewModelProvider(this).get(BusquedaViewModel.class);

        binding = FragmentBusquedaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //obtenemos la vista de los objetos
        this.spinnerTipo = (Spinner) root.findViewById(R.id.spinnerTipo);
        this.cuadroUbicacion = (EditText) root.findViewById(R.id.cuadroUbicacion);
        this.botonBuscar = (Button) root.findViewById(R.id.botonBuscar);
        this.botonAniadirBusqueda = (Button) root.findViewById(R.id.botonAniadirBusqueda);
        this.textoUsuario = (TextView) root.findViewById(R.id.textoUsuario);

        Context context = ((Inicio) getActivity()).getActivityContext(); //se obtiene el context de la actividad actual, porque es un fragment

        //se cargan los datos en un hilo separado, porque sino da errores al iniciar la actividad y cargar los datos
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadData(); //ejecucion del metodo que carga los datos

                //se actualiza la vista en el hilo principal
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        comprobarRol(context, correo); //ejecucion del metodo que comprueba el rol con el que se accede a la app
                        asignarUsuario(context);
                    }
                });
            }
        }).start(); //inicia

        //al hacer click en el boton de buscar
        this.botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Resultados.class); //creo un nuevo intent para ir a la ventana de Resultados

                //creo variables para los datos que voy a enviar
                String tipo = spinnerTipo.getSelectedItem().toString();
                String ubicacion = cuadroUbicacion.getText().toString();

                if (tipo.equals("Tipo de busqueda")) {
                    Toast.makeText(getContext(), R.string.errorSpinnerBusqueda, Toast.LENGTH_LONG).show();
                    spinnerTipo.requestFocus();
                }
                else {
                    if (ubicacion.equals("España") || ubicacion.equals("Italia") || ubicacion.equals("Portugal") || ubicacion.equals("Francia")) {
                        Bundle datos = new Bundle(); //creo el bundle que envia los datos
                        datos.putString("tipo", tipo);
                        datos.putString("ubicacion", ubicacion);
                        datos.putString("correo", correo);
                        intent.putExtras(datos); //asigno los datos al bundle
                        startActivity(intent); //inicio el nuevo Intent
                    }
                    else {
                        Toast.makeText(getContext(), R.string.errorPais, Toast.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "(España, Italia, Portugal o Francia)", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //al hacer click en el boton de aniadir estancia
        this.botonAniadirBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rol.equals("Vendedor")) { //si el rol es Vendedor
                    Intent intent = new Intent(getContext(), NuevaEstancia.class); //creo un intent a la ventana de creacion de estancias
                    startActivity(intent); //inicio la actividad del intent
                }
                else { //si no, muestro mensaje de error informando al usuario
                    Toast.makeText(getContext(), R.string.errorAccesoPagina, Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * comprueba a traves de una consulta a la base de datos, si el rol con el que se accede es vendedor o comprador,
     * y dependiendo de cual sea, se permite mostrar el boton o no
     * @param context
     * @param correo
     */
    public void comprobarRol(Context context, String correo) {
        //conexion a la base de datos, en la tabla user
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(context, "user", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();

        //podemos realizar la siguiente consulta porque solo esta recibiendo un correo de la otra clase
        Cursor consulta = bd.rawQuery("SELECT * FROM user WHERE correo=\'"+correo+"\'", null);
        if (!correo.startsWith("Inicia")) {
            if (consulta.moveToFirst()) { //si devuelve algo la consulta
                rol = consulta.getString(5); //doy valor a la variable que corresponda con el quinto resultado, es decir, el rol
                if (rol.equals("Vendedor")) { //si el rol es igual a Vendedor
                    botonAniadirBusqueda.setVisibility(View.VISIBLE); //muestro el boton
                } else { //si no
                    if (rol.equals("Comprador")) { //si el rol es igual a Comprador
                        botonAniadirBusqueda.clearAnimation(); //limpio la animacion del boton y lo oculto
                        botonAniadirBusqueda.setVisibility(View.GONE);
                    }
                }
            }
        }
        else { //si la consulta no muestra nada
            botonAniadirBusqueda.clearAnimation(); //limpio la animacion del boton y lo oculto
            botonAniadirBusqueda.setVisibility(View.GONE);
        }
        //cierro la consulta y la conexion
        consulta.close();
        bd.close();
    }

    /**
     * este metodo carga los datos que recibe de la clase Inicio.class
     * solo va a recibir un correo
     */
    private void loadData() {
        Bundle datos = getArguments(); //obtengo los argumentos del bundle
        if (datos != null) { //si los datos no son nulos
            correo = datos.getString("correo"); //asigno a la variable correo el valor que recibe
        }
    }

    public void asignarUsuario(Context context) {

        //conexion a la tabla user
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(context, "user", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();

        //consulta que devuelve el usuario
        Cursor consulta = bd.rawQuery("SELECT * FROM user", null);

        if (consulta.moveToFirst()) { //si la consulta devuelve algun resultado
            String usuario = consulta.getString(3); //asigno el valor que corresponde con el usuario
            textoUsuario.setText(" "+usuario); //asigno al TextView el nuevo valor para que muestre el usuario
        }

        bd.close(); //cierro la conexion
    }

}