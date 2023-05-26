package com.example.homelooker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homelooker.databinding.ActivityInicioBinding;

public class Inicio extends AppCompatActivity {

    //creacion de variables
    private ImageButton imagenLogo;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityInicioBinding binding;
    private TextView correoNav; //TextView de el navigation drawer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarInicio.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_busqueda, R.id.nav_favoritos)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //----------------------------------

        View headerView = navigationView.getHeaderView(0); //obtengo la vista de la cabecera
        this.correoNav = (TextView) headerView.findViewById(R.id.correoNav); //busco la vista del TextView

        String correo, rol; //para guardar el valor que recibe del MainActivity
        Intent intentRecibido = getIntent(); //obtengo el intent que recibe
        Bundle datos = intentRecibido.getExtras(); //obtengo los datos que recibe
        if(datos != null){ //si los datos recibidos no son nulos
            correo = datos.getString("correo"); //asigno a la variable correo el valor que recibe

            Bundle bundle = new Bundle(); //bundle para enviar los datos
            bundle.putString("correo", correo); //asigno el correo al bundle

            NavController navControllerr = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
            navControllerr.navigate(R.id.nav_busqueda, bundle);

        }else{ //si no
            correo = ""; //le asigno valor vacio
        }

        this.correoNav.setText(correo); //escribo el nuevo valor al TextView

        //al pulsar en el TextView del navigation drawer
        this.correoNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (correoNav.getText().toString().startsWith("Inicia")) { //si el texto que recibe empieza por Inicia, es decir que ha accedido mediante ahora no
                    //abrimos de nuevo el Intent de MainActivity y cerramos el actual, porque vamos a iniciar sesion o hacer un registro
                    Intent intent = new Intent(getBaseContext(), MainActivity.class); //intent de el MainActivity
                    startActivity(intent); //inicio el intent
                    finish(); //cierro el activity actual
                }
            }
        });



    }
    //Metodo para el menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.inicio, menu);
        return true;

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.inicio, menu);
        //return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_ajustes: //si pulsa en la opcion Ajustes
                Intent intent = new Intent(getBaseContext(), Ajustes.class); //intent a la ventana de ajustes
                Bundle datos = new Bundle(); //buendle para enviar datos
                datos.putString("correo", correoNav.getText().toString()); //asigno el valor del TerxtView de correoNav
                intent.putExtras(datos); //asigno los extras

                startActivity(intent); //inicio la nueva activity
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public Context getActivityContext() {
        return this;
    }

}