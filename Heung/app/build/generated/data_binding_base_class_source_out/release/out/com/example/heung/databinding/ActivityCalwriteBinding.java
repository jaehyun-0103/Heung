// Generated by view binder compiler. Do not edit!
package com.example.heung.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public final class ActivityCalwriteBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageButton btnBack;

  @NonNull
  public final EditText calLocation;

  @NonNull
  public final EditText calMemo;

  @NonNull
  public final Button calSave;

  @NonNull
  public final EditText calTitle;

  @NonNull
  public final ConstraintLayout clTimepicker1;

  @NonNull
  public final ConstraintLayout clTimepicker2;

  @NonNull
  public final ConstraintLayout constraintLayout;

  @NonNull
  public final TextView endDate;

  @NonNull
  public final TextView endTime;

  @NonNull
  public final FrameLayout flAddTimebox;

  @NonNull
  public final ImageView ivScheduleTimeEnd;

  @NonNull
  public final ImageView ivScheduleTimeStart;

  @NonNull
  public final TextView startDate;

  @NonNull
  public final TextView startTime;

  @NonNull
  public final TextView tvCalAddLocation;

  @NonNull
  public final TextView tvCalAddMemo;

  @NonNull
  public final TextView tvScheduleAddDescription;

  @NonNull
  public final TextView tvScheduleAddMemoCondition;

  @NonNull
  public final TextView tvScheduleAddTime;

  @NonNull
  public final TextView tvScheduleAddWhat;

  private ActivityCalwriteBinding(@NonNull ConstraintLayout rootView, @NonNull ImageButton btnBack,
      @NonNull EditText calLocation, @NonNull EditText calMemo, @NonNull Button calSave,
      @NonNull EditText calTitle, @NonNull ConstraintLayout clTimepicker1,
      @NonNull ConstraintLayout clTimepicker2, @NonNull ConstraintLayout constraintLayout,
      @NonNull TextView endDate, @NonNull TextView endTime, @NonNull FrameLayout flAddTimebox,
      @NonNull ImageView ivScheduleTimeEnd, @NonNull ImageView ivScheduleTimeStart,
      @NonNull TextView startDate, @NonNull TextView startTime, @NonNull TextView tvCalAddLocation,
      @NonNull TextView tvCalAddMemo, @NonNull TextView tvScheduleAddDescription,
      @NonNull TextView tvScheduleAddMemoCondition, @NonNull TextView tvScheduleAddTime,
      @NonNull TextView tvScheduleAddWhat) {
    this.rootView = rootView;
    this.btnBack = btnBack;
    this.calLocation = calLocation;
    this.calMemo = calMemo;
    this.calSave = calSave;
    this.calTitle = calTitle;
    this.clTimepicker1 = clTimepicker1;
    this.clTimepicker2 = clTimepicker2;
    this.constraintLayout = constraintLayout;
    this.endDate = endDate;
    this.endTime = endTime;
    this.flAddTimebox = flAddTimebox;
    this.ivScheduleTimeEnd = ivScheduleTimeEnd;
    this.ivScheduleTimeStart = ivScheduleTimeStart;
    this.startDate = startDate;
    this.startTime = startTime;
    this.tvCalAddLocation = tvCalAddLocation;
    this.tvCalAddMemo = tvCalAddMemo;
    this.tvScheduleAddDescription = tvScheduleAddDescription;
    this.tvScheduleAddMemoCondition = tvScheduleAddMemoCondition;
    this.tvScheduleAddTime = tvScheduleAddTime;
    this.tvScheduleAddWhat = tvScheduleAddWhat;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCalwriteBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCalwriteBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_calwrite, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCalwriteBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_back;
      ImageButton btnBack = ViewBindings.findChildViewById(rootView, id);
      if (btnBack == null) {
        break missingId;
      }

      id = R.id.cal_location;
      EditText calLocation = ViewBindings.findChildViewById(rootView, id);
      if (calLocation == null) {
        break missingId;
      }

      id = R.id.cal_memo;
      EditText calMemo = ViewBindings.findChildViewById(rootView, id);
      if (calMemo == null) {
        break missingId;
      }

      id = R.id.cal_save;
      Button calSave = ViewBindings.findChildViewById(rootView, id);
      if (calSave == null) {
        break missingId;
      }

      id = R.id.cal_title;
      EditText calTitle = ViewBindings.findChildViewById(rootView, id);
      if (calTitle == null) {
        break missingId;
      }

      id = R.id.cl_timepicker1;
      ConstraintLayout clTimepicker1 = ViewBindings.findChildViewById(rootView, id);
      if (clTimepicker1 == null) {
        break missingId;
      }

      id = R.id.cl_timepicker2;
      ConstraintLayout clTimepicker2 = ViewBindings.findChildViewById(rootView, id);
      if (clTimepicker2 == null) {
        break missingId;
      }

      id = R.id.constraintLayout;
      ConstraintLayout constraintLayout = ViewBindings.findChildViewById(rootView, id);
      if (constraintLayout == null) {
        break missingId;
      }

      id = R.id.endDate;
      TextView endDate = ViewBindings.findChildViewById(rootView, id);
      if (endDate == null) {
        break missingId;
      }

      id = R.id.endTime;
      TextView endTime = ViewBindings.findChildViewById(rootView, id);
      if (endTime == null) {
        break missingId;
      }

      id = R.id.fl_add_timebox;
      FrameLayout flAddTimebox = ViewBindings.findChildViewById(rootView, id);
      if (flAddTimebox == null) {
        break missingId;
      }

      id = R.id.iv_schedule_time_end;
      ImageView ivScheduleTimeEnd = ViewBindings.findChildViewById(rootView, id);
      if (ivScheduleTimeEnd == null) {
        break missingId;
      }

      id = R.id.iv_schedule_time_start;
      ImageView ivScheduleTimeStart = ViewBindings.findChildViewById(rootView, id);
      if (ivScheduleTimeStart == null) {
        break missingId;
      }

      id = R.id.startDate;
      TextView startDate = ViewBindings.findChildViewById(rootView, id);
      if (startDate == null) {
        break missingId;
      }

      id = R.id.startTime;
      TextView startTime = ViewBindings.findChildViewById(rootView, id);
      if (startTime == null) {
        break missingId;
      }

      id = R.id.tv_cal_add_location;
      TextView tvCalAddLocation = ViewBindings.findChildViewById(rootView, id);
      if (tvCalAddLocation == null) {
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

      id = R.id.tv_schedule_add_memo_condition;
      TextView tvScheduleAddMemoCondition = ViewBindings.findChildViewById(rootView, id);
      if (tvScheduleAddMemoCondition == null) {
        break missingId;
      }

      id = R.id.tv_schedule_add_time;
      TextView tvScheduleAddTime = ViewBindings.findChildViewById(rootView, id);
      if (tvScheduleAddTime == null) {
        break missingId;
      }

      id = R.id.tv_schedule_add_what;
      TextView tvScheduleAddWhat = ViewBindings.findChildViewById(rootView, id);
      if (tvScheduleAddWhat == null) {
        break missingId;
      }

      return new ActivityCalwriteBinding((ConstraintLayout) rootView, btnBack, calLocation, calMemo,
          calSave, calTitle, clTimepicker1, clTimepicker2, constraintLayout, endDate, endTime,
          flAddTimebox, ivScheduleTimeEnd, ivScheduleTimeStart, startDate, startTime,
          tvCalAddLocation, tvCalAddMemo, tvScheduleAddDescription, tvScheduleAddMemoCondition,
          tvScheduleAddTime, tvScheduleAddWhat);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
