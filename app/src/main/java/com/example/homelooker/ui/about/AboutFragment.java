package com.example.homelooker.ui.about;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.homelooker.R;
import com.example.homelooker.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {

    //creacion de variables
    private Button botonContacto;

    private FragmentAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        com.example.homelooker.ui.about.AboutViewModel aboutViewModel =
                new ViewModelProvider(this).get(AboutViewModel.class);

        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //obtenemos la vista de los objetos
        this.botonContacto = (Button) root.findViewById(R.id.botonContacto);

        this.botonContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactar(); //al pulsar en el boton se ejecutara el metodo contactar
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
     * crea un redireccion a correco electronico a traves de un Intent para contactar con el creador de la app
     */
    public void contactar() {
        Intent intent = new Intent(Intent.ACTION_SEND); //Intent con la accion de enviar
        intent.setType("message/rfc822"); //el tipo sera mensaje
        //asigno destinatario, asunto y mensaje
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"abuenoh02@informatica.iesvalledeljerteplasencia.es"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Incidencia o duda con la app.");
        intent.putExtra(Intent.EXTRA_TEXT, "Hola Alberto, me pongo en contacto contigo porque estoy utilizando tu app y me ha surgido la siguiente duda:");

        try {
            startActivity(Intent.createChooser(intent, "Enviar correo electrónico")); //inicio la actividad del correo electronico
        } catch (ActivityNotFoundException ex) { //capturo la excepcion si no encuentra la nueva actividad
            //mensaje de error si salta la excepcion
            Toast.makeText(getContext(), R.string.errorCorreo, Toast.LENGTH_SHORT).show();
        }
    }

}