// Generated by data binding compiler. Do not edit!
package com.example.heung.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.example.heung.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ItemCallistBinding extends ViewDataBinding {
  @NonNull
  public final ImageView accountIvProfile;

  @NonNull
  public final ConstraintLayout calList;

  @NonNull
  public final TextView performEnd;

  @NonNull
  public final TextView performName;

  @NonNull
  public final TextView performStart;

  protected ItemCallistBinding(Object _bindingComponent, View _root, int _localFieldCount,
      ImageView accountIvProfile, ConstraintLayout calList, TextView performEnd,
      TextView performName, TextView performStart) {
    super(_bindingComponent, _root, _localFieldCount);
    this.accountIvProfile = accountIvProfile;
    this.calList = calList;
    this.performEnd = performEnd;
    this.performName = performName;
    this.performStart = performStart;
  }

  @NonNull
  public static ItemCallistBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_callist, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ItemCallistBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ItemCallistBinding>inflateInternal(inflater, R.layout.item_callist, root, attachToRoot, component);
  }

  @NonNull
  public static ItemCallistBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_callist, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ItemCallistBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ItemCallistBinding>inflateInternal(inflater, R.layout.item_callist, null, false, component);
  }

  public static ItemCallistBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static ItemCallistBinding bind(@NonNull View view, @Nullable Object component) {
    return (ItemCallistBinding)bind(component, view, R.layout.item_callist);
  }
}
