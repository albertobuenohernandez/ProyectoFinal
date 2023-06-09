// Generated by view binder compiler. Do not edit!
package com.example.homelooker.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public final class FragmentFavoritosBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final ScrollView ScrollView;

  @NonNull
  public final LinearLayout layoutPrincipal;

  @NonNull
  public final LinearLayout layoutResultados;

  @NonNull
  public final LinearLayout layoutSuperior;

  @NonNull
  public final TextView tituloFavoritos;

  private FragmentFavoritosBinding(@NonNull ScrollView rootView, @NonNull ScrollView ScrollView,
      @NonNull LinearLayout layoutPrincipal, @NonNull LinearLayout layoutResultados,
      @NonNull LinearLayout layoutSuperior, @NonNull TextView tituloFavoritos) {
    this.rootView = rootView;
    this.ScrollView = ScrollView;
    this.layoutPrincipal = layoutPrincipal;
    this.layoutResultados = layoutResultados;
    this.layoutSuperior = layoutSuperior;
    this.tituloFavoritos = tituloFavoritos;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentFavoritosBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentFavoritosBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_favoritos, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentFavoritosBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      ScrollView ScrollView = (ScrollView) rootView;

      id = R.id.layoutPrincipal;
      LinearLayout layoutPrincipal = ViewBindings.findChildViewById(rootView, id);
      if (layoutPrincipal == null) {
        break missingId;
      }

      id = R.id.layoutResultados;
      LinearLayout layoutResultados = ViewBindings.findChildViewById(rootView, id);
      if (layoutResultados == null) {
        break missingId;
      }

      id = R.id.layoutSuperior;
      LinearLayout layoutSuperior = ViewBindings.findChildViewById(rootView, id);
      if (layoutSuperior == null) {
        break missingId;
      }

      id = R.id.tituloFavoritos;
      TextView tituloFavoritos = ViewBindings.findChildViewById(rootView, id);
      if (tituloFavoritos == null) {
        break missingId;
      }

      return new FragmentFavoritosBinding((ScrollView) rootView, ScrollView, layoutPrincipal,
          layoutResultados, layoutSuperior, tituloFavoritos);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
