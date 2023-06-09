// Generated by view binder compiler. Do not edit!
package com.example.heung.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.heung.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemSelfprofBinding implements ViewBinding {
  @NonNull
  private final TableLayout rootView;

  @NonNull
  public final TextView content;

  @NonNull
  public final TextView title;

  private ItemSelfprofBinding(@NonNull TableLayout rootView, @NonNull TextView content,
      @NonNull TextView title) {
    this.rootView = rootView;
    this.content = content;
    this.title = title;
  }

  @Override
  @NonNull
  public TableLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemSelfprofBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemSelfprofBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_selfprof, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemSelfprofBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.content;
      TextView content = ViewBindings.findChildViewById(rootView, id);
      if (content == null) {
        break missingId;
      }

      id = R.id.title;
      TextView title = ViewBindings.findChildViewById(rootView, id);
      if (title == null) {
        break missingId;
      }

      return new ItemSelfprofBinding((TableLayout) rootView, content, title);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
