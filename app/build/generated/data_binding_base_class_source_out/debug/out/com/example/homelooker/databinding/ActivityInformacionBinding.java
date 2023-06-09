// Generated by view binder compiler. Do not edit!
package com.example.homelooker.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.homelooker.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityInformacionBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final ScrollView ScrollView;

  @NonNull
  public final Button botonContacto;

  @NonNull
  public final Button botonEditarEstancia;

  @NonNull
  public final Button botonEliminarEstancia;

  @NonNull
  public final ImageButton botonFav;

  @NonNull
  public final ImageView imagenEstancia;

  @NonNull
  public final LinearLayout layoutPrincipal;

  @NonNull
  public final TextView textoCaracteristicas;

  @NonNull
  public final TextView textoDescripcion;

  @NonNull
  public final TextView textoDormitorios;

  @NonNull
  public final TextView textoMetros;

  @NonNull
  public final TextView textoNombre;

  @NonNull
  public final TextView textoPrecio;

  @NonNull
  public final TextView textoUbicacion;

  @NonNull
  public final TextView textoUbicacionSuperior;

  private ActivityInformacionBinding(@NonNull ScrollView rootView, @NonNull ScrollView ScrollView,
      @NonNull Button botonContacto, @NonNull Button botonEditarEstancia,
      @NonNull Button botonEliminarEstancia, @NonNull ImageButton botonFav,
      @NonNull ImageView imagenEstancia, @NonNull LinearLayout layoutPrincipal,
      @NonNull TextView textoCaracteristicas, @NonNull TextView textoDescripcion,
      @NonNull TextView textoDormitorios, @NonNull TextView textoMetros,
      @NonNull TextView textoNombre, @NonNull TextView textoPrecio,
      @NonNull TextView textoUbicacion, @NonNull TextView textoUbicacionSuperior) {
    this.rootView = rootView;
    this.ScrollView = ScrollView;
    this.botonContacto = botonContacto;
    this.botonEditarEstancia = botonEditarEstancia;
    this.botonEliminarEstancia = botonEliminarEstancia;
    this.botonFav = botonFav;
    this.imagenEstancia = imagenEstancia;
    this.layoutPrincipal = layoutPrincipal;
    this.textoCaracteristicas = textoCaracteristicas;
    this.textoDescripcion = textoDescripcion;
    this.textoDormitorios = textoDormitorios;
    this.textoMetros = textoMetros;
    this.textoNombre = textoNombre;
    this.textoPrecio = textoPrecio;
    this.textoUbicacion = textoUbicacion;
    this.textoUbicacionSuperior = textoUbicacionSuperior;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityInformacionBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityInformacionBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_informacion, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityInformacionBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      ScrollView ScrollView = (ScrollView) rootView;

      id = R.id.botonContacto;
      Button botonContacto = ViewBindings.findChildViewById(rootView, id);
      if (botonContacto == null) {
        break missingId;
      }

      id = R.id.botonEditarEstancia;
      Button botonEditarEstancia = ViewBindings.findChildViewById(rootView, id);
      if (botonEditarEstancia == null) {
        break missingId;
      }

      id = R.id.botonEliminarEstancia;
      Button botonEliminarEstancia = ViewBindings.findChildViewById(rootView, id);
      if (botonEliminarEstancia == null) {
        break missingId;
      }

      id = R.id.botonFav;
      ImageButton botonFav = ViewBindings.findChildViewById(rootView, id);
      if (botonFav == null) {
        break missingId;
      }

      id = R.id.imagenEstancia;
      ImageView imagenEstancia = ViewBindings.findChildViewById(rootView, id);
      if (imagenEstancia == null) {
        break missingId;
      }

      id = R.id.layoutPrincipal;
      LinearLayout layoutPrincipal = ViewBindings.findChildViewById(rootView, id);
      if (layoutPrincipal == null) {
        break missingId;
      }

      id = R.id.textoCaracteristicas;
      TextView textoCaracteristicas = ViewBindings.findChildViewById(rootView, id);
      if (textoCaracteristicas == null) {
        break missingId;
      }

      id = R.id.textoDescripcion;
      TextView textoDescripcion = ViewBindings.findChildViewById(rootView, id);
      if (textoDescripcion == null) {
        break missingId;
      }

      id = R.id.textoDormitorios;
      TextView textoDormitorios = ViewBindings.findChildViewById(rootView, id);
      if (textoDormitorios == null) {
        break missingId;
      }

      id = R.id.textoMetros;
      TextView textoMetros = ViewBindings.findChildViewById(rootView, id);
      if (textoMetros == null) {
        break missingId;
      }

      id = R.id.textoNombre;
      TextView textoNombre = ViewBindings.findChildViewById(rootView, id);
      if (textoNombre == null) {
        break missingId;
      }

      id = R.id.textoPrecio;
      TextView textoPrecio = ViewBindings.findChildViewById(rootView, id);
      if (textoPrecio == null) {
        break missingId;
      }

      id = R.id.textoUbicacion;
      TextView textoUbicacion = ViewBindings.findChildViewById(rootView, id);
      if (textoUbicacion == null) {
        break missingId;
      }

      id = R.id.textoUbicacionSuperior;
      TextView textoUbicacionSuperior = ViewBindings.findChildViewById(rootView, id);
      if (textoUbicacionSuperior == null) {
        break missingId;
      }

      return new ActivityInformacionBinding((ScrollView) rootView, ScrollView, botonContacto,
          botonEditarEstancia, botonEliminarEstancia, botonFav, imagenEstancia, layoutPrincipal,
          textoCaracteristicas, textoDescripcion, textoDormitorios, textoMetros, textoNombre,
          textoPrecio, textoUbicacion, textoUbicacionSuperior);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
