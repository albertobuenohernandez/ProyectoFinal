package com.example.homelooker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CambiarContrasenia extends AppCompatActivity {

    //creacion de variables
    private EditText cuadroAntigua;
    private EditText cuadroNueva;
    private EditText cuadroRepiteNueva;
    private Button botonCambiar;
    private String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasenia);

        //obtenemos la vista de los objetos
        this.cuadroAntigua = (EditText) findViewById(R.id.cuadroAntigua);
        this.cuadroNueva = (EditText) findViewById(R.id.cuadroNueva);
        this.cuadroRepiteNueva = (EditText) findViewById(R.id.cuadroRepiteNueva);
        this.botonCambiar = (Button) findViewById(R.id.botonCambiar);

        //Obtenemos el correo con el que hemos accedido
        Intent intentRecibido = getIntent();
        Bundle datos = intentRecibido.getExtras(); //obtengo los extras
        if(datos != null) { //si no esta vacio
            correo = datos.getString("correo"); //asigno a la variable correo el valor del correo que recibe
        }else{
            correo = "";
        }

        //al pulsar en el boton cambiar contrasenia
        this.botonCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarContra(); //ejecuto el metodo que cambia la contraseña
            }
        });

    }

    /**
     * se conecta a la base de datos y comprueba mediante un consulta, que el valor de la contrasenia actual existe
     * y si los otros dos campos coinciden, se modificará la contraseña en la base de datos a la insertada en los campos
     */
    public void cambiarContra() {
        int registrosModificados; //guardara la cantidad de resultados modificados
        //conexion a la tabla user
        AdministracionSQLiteOpenHelper admin = new AdministracionSQLiteOpenHelper(this, "user", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();

        //obtengo el valor de los campos y se lo asigno a las variables
        String contrasenia = cuadroAntigua.getText().toString();
        String nuevaContra = cuadroNueva.getText().toString();
        String repiteContra = cuadroRepiteNueva.getText().toString();

        ContentValues registro =new ContentValues();
        registro.put("contrasenia", nuevaContra); //asigno el valor de la contrasenia

        //consulta para comprobar si la contraseña en el primer campo existe en la tabla
        Cursor consulta1 = bd.rawQuery("SELECT contrasenia FROM user WHERE contrasenia=\'"+contrasenia+"\' AND correo=\'"+correo+"\'", null);

        if (!consulta1.moveToFirst()) { //si la consulta no devuelve ningun valor, es decir que no existe dicho user con esa contrasenia en la tabla
            //muestro Toast con el mensaje de error y dejo todos los campos en blanco
            Toast.makeText(this, R.string.errorContraNoIgual, Toast.LENGTH_LONG).show();
            this.cuadroAntigua.setText("");
            this.cuadroNueva.setText("");
            this.cuadroRepiteNueva.setText("");

            //recupero el foco para volver a insertar nuevos datos que sean validos
            this.cuadroAntigua.requestFocus();
            this.cuadroNueva.requestFocus();
            this.cuadroRepiteNueva.requestFocus();

        } else { //si no
            if (nuevaContra.equals(repiteContra)) { //si las nuevas contrasenias coinciden
                registrosModificados = bd.update("user", registro, "correo=\'"+correo+"\'", null); //actualizo
                if (registrosModificados == 1) { //si ha modificado el dato
                    Toast.makeText(this, R.string.contraModificada, Toast.LENGTH_SHORT).show(); //muestro mensaje
                    cerrarYReiniciarApp(); //ejecuto el metodo que reinicia la app y vuelve a la pantalla principal
                    finish(); //cierro la ventana
                }
                else { //si no muestro mensaje de error
                    Toast.makeText(this, R.string.errorModificacionDatos, Toast.LENGTH_SHORT).show();
                }
            }
            else { //si no coinciden las contrasenias
                Toast.makeText(this, R.string.errorContraNoCoincide, Toast.LENGTH_SHORT).show(); //muestro mensaje
                this.cuadroNueva.setText("");
                this.cuadroRepiteNueva.setText("");
            }

        }
        bd.close(); //cierro conexion
    }

    /**
     * crea un intent a la ventana principal, crea una nueva tarea y limpia todas las anteriores,
     * finalmente cierra lo anterior
     */
    public void cerrarYReiniciarApp() {
        Intent intent = new Intent(this, MainActivity.class); //intent a la actividad principal
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //agrego al intent la flag de nueva tarea y limpiar las anteriores
        startActivity(intent); //inicio el intent
        finishAffinity(); //ejecuto el metodo que finaliza todas las actividades en la pila
    }

}