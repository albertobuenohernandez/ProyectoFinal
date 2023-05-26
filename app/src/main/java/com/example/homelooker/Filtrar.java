package com.example.homelooker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Filtrar extends AppCompatActivity {

    //creacion de variables
    private TextView tituloCancelar;
    private Spinner spinnerPrecioMin;
    private Spinner spinnerPrecioMax;
    private Spinner spinnerMetrosMin;
    private Spinner spinnerMetrosMax;
    private RadioGroup radioGroupDormitorios;
    private Button botonMostrar;

    private String precioMin, precioMax, metrosMin, metrosMax; //guardan el valor del texto insertado en cada campo
    private String numDorm = ""; //guarda el numero de dormitorios seleccionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar);

        //obtenemos la vista de los objetos
        this.tituloCancelar = (TextView) findViewById(R.id.tituloCancelar);
        this.spinnerPrecioMin = (Spinner) findViewById(R.id.spinnerPrecioMin);
        this.spinnerPrecioMax = (Spinner) findViewById(R.id.spinnerPrecioMax);
        this.spinnerMetrosMin = (Spinner) findViewById(R.id.spinnerMetrosMin);
        this.spinnerMetrosMax = (Spinner) findViewById(R.id.spinnerMetrosMax);
        this.radioGroupDormitorios = (RadioGroup) findViewById(R.id.radioGroupDormitorios);
        this.botonMostrar = (Button) findViewById(R.id.botonMostrar);

        //al pulsar en el TextView cancelar
        this.tituloCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); //cierro la actividad acutal
            }
        });

        //al seleccionar el precio minimo en el spinner
        spinnerPrecioMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el elemento seleccionado del Spinner
                precioMin = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //al seleccionar el precio maximo en el spinner
        spinnerPrecioMax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el elemento seleccionado del Spinner
                precioMax = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //al seleccionar los metros minimos en el spinner
        spinnerMetrosMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el elemento seleccionado del Spinner
                metrosMin = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //al seleccionar los metros maximos en el spinner
        spinnerMetrosMax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el elemento seleccionado del Spinner
                metrosMax = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //al seleccionar un elemento del radioGroup de dormitorios
        radioGroupDormitorios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId); //creamos un RadioButton que sea el que tenemos seleccionado
                if (radioButton != null) { //si no esta vacio
                    numDorm = radioButton.getText().toString(); //asignamos el valor del radioButton seleccionado
                }
                else { //si no
                    numDorm = "1"; //asignamos el valor de 1
                }
            }
        });

        //al pulsar en el boton mostrar
        this.botonMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultadoIntent = new Intent(); //se crea un nuevo intent

                //compruebo si alguno esta vacio y muestro un mensaje avisando
                if (precioMin.isEmpty() || precioMax.isEmpty() || metrosMin.isEmpty() || metrosMax.isEmpty() || numDorm.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Error, tiene que rellenar todos los campos", Toast.LENGTH_LONG).show(); //muestro mensaje
                }
                else { //si no le asigno el valor que le corresponde si es min o max y envio los datos
                    if (precioMin.equals("Min") || precioMin.equals("Max")) {
                        precioMin = "0";
                    }
                    if (precioMax.equals("Min") || precioMax.equals("Max")) {
                        precioMax = "999999";
                    }
                    if (metrosMin.equals("Min") || metrosMin.equals("Max")) {
                        metrosMin = "0";
                    }
                    if (metrosMax.equals("Min") || metrosMax.equals("Max")) {
                        metrosMax = "999999";
                    }

                    //se asignan todos los datos que se van a mandar
                    resultadoIntent.putExtra("precioMin", precioMin);
                    resultadoIntent.putExtra("precioMax", precioMax);
                    resultadoIntent.putExtra("metrosMin", metrosMin);
                    resultadoIntent.putExtra("metrosMax", metrosMax);
                    resultadoIntent.putExtra("numDorm", numDorm.substring(0,1)); //obtengo solo la primera letra, que será un 4
                    resultadoIntent.putExtra("orden", ""); //envio el orden vacio, porque no se esta ordenando y asi evito errores de codigo

                    setResult(Activity.RESULT_OK, resultadoIntent); //se asignae l resultado, ya que se ejecutara al cerrarse esta ventana
                    finish(); //cierro activity actual
                }

            }
        });

    }
}