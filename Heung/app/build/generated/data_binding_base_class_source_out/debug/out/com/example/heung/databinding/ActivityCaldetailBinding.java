// Generated by view binder compiler. Do not edit!
package com.example.heung.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.heung.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityCaldetailBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView Date;

  @NonNull
  public final Button calDelect;

  @NonNull
  public final Button calEdit;

  @NonNull
  public final ConstraintLayout clTimepicker1;

  @NonNull
  public final TextView connect;

  @NonNull
  public final ConstraintLayout constraintLayout;

  @NonNull
  public final ConstraintLayout constraintLayout2;

  @NonNull
  public final TextView endTime;

  @NonNull
  public final TextView performLocation;

  @NonNull
  public final TextView performMemo;

  @NonNull
  public final TextView performName;

  @NonNull
  public final TextView startTime;

  @NonNull
  public final TextView tvCalAddMemo;

  @NonNull
  public final TextView tvScheduleAddDescription;

  private ActivityCaldetailBinding(@NonNull ConstraintLayout rootView, @NonNull TextView Date,
      @NonNull Button calDelect, @NonNull Button calEdit, @NonNull ConstraintLayout clTimepicker1,
      @NonNull TextView connect, @NonNull ConstraintLayout constraintLayout,
      @NonNull ConstraintLayout constraintLayout2, @NonNull TextView endTime,
      @NonNull TextView performLocation, @NonNull TextView performMemo,
      @NonNull TextView performName, @NonNull TextView startTime, @NonNull TextView tvCalAddMemo,
      @NonNull TextView tvScheduleAddDescription) {
    this.rootView = rootView;
    this.Date = Date;
    this.calDelect = calDelect;
    this.calEdit = calEdit;
    this.clTimepicker1 = clTimepicker1;
    this.connect = connect;
    this.constraintLayout = constraintLayout;
    this.constraintLayout2 = constraintLayout2;
    this.endTime = endTime;
    this.performLocation = performLocation;
    this.performMemo = performMemo;
    this.performName = performName;
    this.startTime = startTime;
    this.tvCalAddMemo = tvCalAddMemo;
    this.tvScheduleAddDescription = tvScheduleAddDescription;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCaldetailBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCaldetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_caldetail, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCaldetailBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.Date;
      TextView Date = ViewBindings.findChildViewById(rootView, id);
      if (Date == null) {
        break missingId;
      }

      id = R.id.calDelect;
      Button calDelect = ViewBindings.findChildViewById(rootView, id);
      if (calDelect == null) {
        break missingId;
      }

      id = R.id.calEdit;
      Button calEdit = ViewBindings.findChildViewById(rootView, id);
      if (calEdit == null) {
        break missingId;
      }

      id = R.id.cl_timepicker1;
      ConstraintLayout clTimepicker1 = ViewBindings.findChildViewById(rootView, id);
      if (clTimepicker1 == null) {
        break missingId;
      }

      id = R.id.connect;
      TextView connect = ViewBindings.findChildViewById(rootView, id);
      if (connect == null) {
        break missingId;
      }

      id = R.id.constraintLayout;
      ConstraintLayout constraintLayout = ViewBindings.findChildViewById(rootView, id);
      if (constraintLayout == null) {
        break missingId;
      }

      id = R.id.constraintLayout2;
      ConstraintLayout constraintLayout2 = ViewBindings.findChildViewById(rootView, id);
      if (constraintLayout2 == null) {
        break missingId;
      }

      id = R.id.endTime;
      TextView endTime = ViewBindings.findChildViewById(rootView, id);
      if (endTime == null) {
        break missingId;
      }

      id = R.id.performLocation;
      TextView performLocation = ViewBindings.findChildViewById(rootView, id);
      if (performLocation == null) {
        break missingId;
      }

      id = R.id.performMemo;
      TextView performMemo = ViewBindings.findChildViewById(rootView, id);
      if (performMemo == null) {
        break missingId;
      }

      id = R.id.performName;
      TextView performName = ViewBindings.findChildViewById(rootView, id);
      if (performName == null) {
        break missingId;
      }

      id = R.id.startTime;
      TextView startTime = ViewBindings.findChildViewById(rootView, id);
      if (startTime == null) {
        break missingId;
      }

      id = R.id.tv_cal_add_memo;
      TextView tvCalAddMemo = ViewBindings.findChildViewById(rootView, id);
      if (tvCalAddMemo == null) {
        break missingId;
      }

      id = R.id.tv_schedule_add_description;
      TextView tvScheduleAddDescription = ViewBindings.findChildViewById(rootView, id);
      if (tvScheduleAddDescription == null) {
        break missingId;
      }

      return new ActivityCaldetailBinding((ConstraintLayout) rootView, Date, calDelect, calEdit,
          clTimepicker1, connect, constraintLayout, constraintLayout2, endTime, performLocation,
          performMemo, performName, startTime, tvCalAddMemo, tvScheduleAddDescription);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
