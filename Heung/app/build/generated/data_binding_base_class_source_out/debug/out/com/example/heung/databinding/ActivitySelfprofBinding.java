// Generated by view binder compiler. Do not edit!
package com.example.heung.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.heung.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySelfprofBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final BottomNavigationLayoutBinding bottomNavigation;

  @NonNull
  public final FloatingActionButton btnEdit;

  @NonNull
  public final Button btnQuit;

  @NonNull
  public final Button logout;

  @NonNull
  public final Button nickChange;

  @NonNull
  public final TextView nickname;

  @NonNull
  public final TextView post;

  @NonNull
  public final ImageView selfProfile;

  @NonNull
  public final RecyclerView selfRecycler;

  private ActivitySelfprofBinding(@NonNull ConstraintLayout rootView,
      @NonNull BottomNavigationLayoutBinding bottomNavigation,
      @NonNull FloatingActionButton btnEdit, @NonNull Button btnQuit, @NonNull Button logout,
      @NonNull Button nickChange, @NonNull TextView nickname, @NonNull TextView post,
      @NonNull ImageView selfProfile, @NonNull RecyclerView selfRecycler) {
    this.rootView = rootView;
    this.bottomNavigation = bottomNavigation;
    this.btnEdit = btnEdit;
    this.btnQuit = btnQuit;
    this.logout = logout;
    this.nickChange = nickChange;
    this.nickname = nickname;
    this.post = post;
    this.selfProfile = selfProfile;
    this.selfRecycler = selfRecycler;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySelfprofBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySelfprofBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_selfprof, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySelfprofBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bottom_navigation;
      View bottomNavigation = ViewBindings.findChildViewById(rootView, id);
      if (bottomNavigation == null) {
        break missingId;
      }
      BottomNavigationLayoutBinding binding_bottomNavigation = BottomNavigationLayoutBinding.bind(bottomNavigation);

      id = R.id.btn_edit;
      FloatingActionButton btnEdit = ViewBindings.findChildViewById(rootView, id);
      if (btnEdit == null) {
        break missingId;
      }

      id = R.id.btn_quit;
      Button btnQuit = ViewBindings.findChildViewById(rootView, id);
      if (btnQuit == null) {
        break missingId;
      }

      id = R.id.logout;
      Button logout = ViewBindings.findChildViewById(rootView, id);
      if (logout == null) {
        break missingId;
      }

      id = R.id.nick_change;
      Button nickChange = ViewBindings.findChildViewById(rootView, id);
      if (nickChange == null) {
        break missingId;
      }

      id = R.id.nickname;
      TextView nickname = ViewBindings.findChildViewById(rootView, id);
      if (nickname == null) {
        break missingId;
      }

      id = R.id.post;
      TextView post = ViewBindings.findChildViewById(rootView, id);
      if (post == null) {
        break missingId;
      }

      id = R.id.self_profile;
      ImageView selfProfile = ViewBindings.findChildViewById(rootView, id);
      if (selfProfile == null) {
        break missingId;
      }

      id = R.id.self_recycler;
      RecyclerView selfRecycler = ViewBindings.findChildViewById(rootView, id);
      if (selfRecycler == null) {
        break missingId;
      }

      return new ActivitySelfprofBinding((ConstraintLayout) rootView, binding_bottomNavigation,
          btnEdit, btnQuit, logout, nickChange, nickname, post, selfProfile, selfRecycler);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
