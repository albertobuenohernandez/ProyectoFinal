package com.example.homelooker.ui.ajustes;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.homelooker.AdministracionSQLiteOpenHelper;
import com.example.homelooker.CambiarContrasenia;
import com.example.homelooker.EditarCuenta;
import com.example.homelooker.MainActivity;
import com.example.homelooker.R;
import com.example.homelooker.databinding.ActivityAjustesBinding;

public class AjustesFragment extends Fragment {

    //creacion de variables
    private Button botonEditar;
    private Button botonCambiarContra;
    private Button botonEliminar;
    private String correo;

    private ActivityAjustesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        com.example.homelooker.ui.ajustes.AjustesViewModel ajustesViewModel =
                new ViewModelProvider(this).get(AjustesViewModel.class);

        binding = ActivityAjustesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //obtenemos la vista de los objetos
        this.botonEditar = (Button) root.findViewById(R.id.botonEditar);
        this.botonCambiarContra = (Button) root.findViewById(R.id.botonCambiarContra);
        this.botonEliminar = (Button) root.findViewById(R.id.botonEliminar);

        //Obtenemos el correo con el que hemos accedido
        Intent intentRecibido = getActivity().getIntent();
        Bundle datos = intentRecibido.getExtras();
        if(datos != null){ //si los datos no son nulos
            correo = datos.getString("correo");
        }else{ //si no
            correo = ""; //el correo se queda vacio
        }

        //al hacer click en el boton editar
        this.botonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditarCuenta.class); //intent para abrir la ventana de editar la cuenta
                Bundle datos = new Bundle(); //nuevo bundle para enviar datos a la ventana que se va a abrir
                datos.putString("correo", correo); //asigno el dato que se va a enviar
                intent.putExtras(datos); //asigno al intent los datos extras que se van a enviar

                startActivity(intent); //inicio de la nueva actividad
            }
        });

        //al hacer click en el boton cambiar contrasenia
        this.botonCambiarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CambiarContrasenia.class); //intent para abrir la ventana de cambiar contrasenia

                Bundle datos = new Bundle(); //nuevo bundle para enviar datos a la ventana que se va a abrir
                datos.putString("correo", correo); //asigno el dato que se va a enviar
                intent.putExtras(datos); //asigno al intent los datos extras que se van a enviar

                startActivity(intent); //inicio la nueva actividad
            }
        });

        //al hacer click en el boton eliminar cuenta
        this.botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCuadroDialogoConfirm(); //se ejecuta el metodo que crea el cuadro de dialogo de confirmacion
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
     * crea un cuadro de dialogo de confirmacion para asegurar que el usuario de verdad quiere eliminar su cuenta
     */
    private void crearCuadroDialogoConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); //se crea un AlertDialog con el context actual
        builder.setMessage(R.string.seguroEliminarCuenta); //mensaje que se muestra en el cuadro
        builder.setTitle(R.string.eliminarCuenta); //titulo del cuadro de dialogo
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() { //opcion positiva del cuadro con el mensaje Si
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eliminarUsuario(); //ejecutara el metodo que elimina el user de la base de datos
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() { //opcion negativa del cuadro con el mensaje No
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //No ocurre nada
            }
        });
        builder.show(); //se muestra el cuadro
    }

    /**
     * accede a la base de datos del proyecto para eliminar el usuario que corresponda con el correo que recibe
     */
    public void eliminarUsuario() {
        int registrosBorrados; //guarda la cantidad de users eliminados, así se confirma que se ha eliminado correctamente
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(getContext(), "user", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String where = "correo = ?"; //clausula where de la sentencia sql
        String[] args = {correo}; //parametros que recibe en el where

        registrosBorrados = bd.delete("user", where, args); //ejecuto un delete en la base de datos para eliminar el usuario

        if (registrosBorrados == 1) { //si la variable ha obtenido el valor 1
            Toast.makeText(getContext(), R.string.borradoExito, Toast.LENGTH_SHORT).show(); //muestro mensaje de exito
            cerrarYReiniciarApp(); //ejecuto el metodo que cierra todas las ventanas y vuelve al inicio
        }
        else { //si no
            Toast.makeText(getContext(), R.string.noExisteUser, Toast.LENGTH_SHORT).show(); //muestro mensaje informando del error
        }
        bd.close(); //cierro la conexion
    }

    /**
     * crea un intent a la ventana principal, crea una nueva tarea y limpia todas las anteriores,
     * finalmente cierra lo anterior
     */
    public void cerrarYReiniciarApp() {
        Intent intent = new Intent(getContext(), MainActivity.class); //intent a la actividad principal
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //agrego al intent la flag de nueva tarea y limpiar las anteriores
        startActivity(intent); //inicio el intent
        getActivity().finishAffinity(); //ejecuto el metodo que finaliza todas las actividades en la pila
    }

}