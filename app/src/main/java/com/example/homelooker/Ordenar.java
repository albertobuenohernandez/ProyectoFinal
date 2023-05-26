package com.example.homelooker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Ordenar extends AppCompatActivity {

    //creacion de variables
    private TextView textoCancelar;
    private Button botonNombre;
    private Button botonPrecio;
    private Button botonMetros;
    private Button botonDormitorios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenar);

        //obtenemos la vista de los objetos
        this.textoCancelar = (TextView) findViewById(R.id.textoCancelar);
        this.botonNombre = (Button) findViewById(R.id.botonNombre);
        this.botonPrecio = (Button) findViewById(R.id.botonPrecio);
        this.botonMetros = (Button) findViewById(R.id.botonMetros);
        this.botonDormitorios = (Button) findViewById(R.id.botonDormitorios);

        //al pulsar en el TextView de cancelar
        this.textoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); //cierro la actividad actual
            }
        });

        //al pulsar en el boton Nombre
        this.botonNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultadoIntent = new Intent(); //creo un intent
                resultadoIntent.putExtra("orden", "nombre"); //asigno como orden el nombre
                setResult(Activity.RESULT_OK, resultadoIntent);
                finish(); //cierro la actividad actual
            }
        });

        //al pulsar en el boton Precio
        this.botonPrecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultadoIntent = new Intent(); //creo un intent
                resultadoIntent.putExtra("orden", "precio"); //asigno como orden el precio
                setResult(Activity.RESULT_OK, resultadoIntent);
                finish(); //cierro la actividad actual
            }
        });

        //al pulsar en el boton Metros
        this.botonMetros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultadoIntent = new Intent(); //creo un intent
                resultadoIntent.putExtra("orden", "metros"); //asigno como orden los metros
                setResult(Activity.RESULT_OK, resultadoIntent);
                finish(); //cierro la actividad actual
            }
        });

        //al pulsar en el boton Dormitorios
        this.botonDormitorios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultadoIntent = new Intent(); //creo un intent
                resultadoIntent.putExtra("orden", "dormitorios"); //asigno como orden los dormitorios
                setResult(Activity.RESULT_OK, resultadoIntent);
                finish(); //cierro la actividad actual
            }
        });

    }
}