package com.example.homelooker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class MyTask extends AsyncTask<Void, Void, String> {

    private Context mContext;
    private ProgressDialog mProgressDialog;

    public MyTask(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //muestra un cuadro de progreso mientras se está ejecutando la tarea
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Cargando resultados...");
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        //hace una espera de un segundo para el hilo
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //devuelve el resultado de la tarea completada
        return "Tarea completada!";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //una vez acabado este proceso, cierra el cuadro que se ha mostrado
        mProgressDialog.dismiss();
    }

}
