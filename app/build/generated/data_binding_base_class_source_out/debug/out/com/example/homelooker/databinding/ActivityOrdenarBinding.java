// Generated by view binder compiler. Do not edit!
package com.example.homelooker.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.homelooker.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityOrdenarBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button botonDormitorios;

  @NonNull
  public final Button botonMetros;

  @NonNull
  public final Button botonNombre;

  @NonNull
  public final Button botonPrecio;

  @NonNull
  public final TextView textoCancelar;

  private ActivityOrdenarBinding(@NonNull LinearLayout rootView, @NonNull Button botonDormitorios,
      @NonNull Button botonMetros, @NonNull Button botonNombre, @NonNull Button botonPrecio,
      @NonNull TextView textoCancelar) {
    this.rootView = rootView;
    this.botonDormitorios = botonDormitorios;
    this.botonMetros = botonMetros;
    this.botonNombre = botonNombre;
    this.botonPrecio = botonPrecio;
    this.textoCancelar = textoCancelar;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityOrdenarBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityOrdenarBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_ordenar, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityOrdenarBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.botonDormitorios;
      Button botonDormitorios = ViewBindings.findChildViewById(rootView, id);
      if (botonDormitorios == null) {
        break missingId;
      }

      id = R.id.botonMetros;
      Button botonMetros = ViewBindings.findChildViewById(rootView, id);
      if (botonMetros == null) {
        break missingId;
      }

      id = R.id.botonNombre;
      Button botonNombre = ViewBindings.findChildViewById(rootView, id);
      if (botonNombre == null) {
        break missingId;
      }

      id = R.id.botonPrecio;
      Button botonPrecio = ViewBindings.findChildViewById(rootView, id);
      if (botonPrecio == null) {
        break missingId;
      }

      id = R.id.textoCancelar;
      TextView textoCancelar = ViewBindings.findChildViewById(rootView, id);
      if (textoCancelar == null) {
        break missingId;
      }

      return new ActivityOrdenarBinding((LinearLayout) rootView, botonDormitorios, botonMetros,
          botonNombre, botonPrecio, textoCancelar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}