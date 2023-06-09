// Generated by view binder compiler. Do not edit!
package com.example.heung.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.heung.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRecrutwriteBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final LinearLayout ContactInformation;

  @NonNull
  public final View postLine;

  @NonNull
  public final RadioGroup radioGroup;

  @NonNull
  public final ImageButton recruitBtnBack;

  @NonNull
  public final EditText recruitContact;

  @NonNull
  public final EditText recruitContent;

  @NonNull
  public final AppCompatRadioButton recruitFilterBusking;

  @NonNull
  public final NumberPicker recruitFilterBuskingCapacity;

  @NonNull
  public final EditText recruitFilterBuskingDate;

  @NonNull
  public final LinearLayout recruitFilterBuskingLayout;

  @NonNull
  public final EditText recruitFilterBuskingSession;

  @NonNull
  public final AppCompatRadioButton recruitFilterClass;

  @NonNull
  public final NumberPicker recruitFilterClassCapacity;

  @NonNull
  public final EditText recruitFilterClassDate2;

  @NonNull
  public final LinearLayout recruitFilterClassLayout;

  @NonNull
  public final EditText recruitFilterClassType;

  @NonNull
  public final View recruitLine;

  @NonNull
  public final TextView recruitPost;

  @NonNull
  public final EditText recruitTitle;

  @NonNull
  public final Button recruitUpload;

  private ActivityRecrutwriteBinding(@NonNull LinearLayout rootView,
      @NonNull LinearLayout ContactInformation, @NonNull View postLine,
      @NonNull RadioGroup radioGroup, @NonNull ImageButton recruitBtnBack,
      @NonNull EditText recruitContact, @NonNull EditText recruitContent,
      @NonNull AppCompatRadioButton recruitFilterBusking,
      @NonNull NumberPicker recruitFilterBuskingCapacity,
      @NonNull EditText recruitFilterBuskingDate, @NonNull LinearLayout recruitFilterBuskingLayout,
      @NonNull EditText recruitFilterBuskingSession,
      @NonNull AppCompatRadioButton recruitFilterClass,
      @NonNull NumberPicker recruitFilterClassCapacity, @NonNull EditText recruitFilterClassDate2,
      @NonNull LinearLayout recruitFilterClassLayout, @NonNull EditText recruitFilterClassType,
      @NonNull View recruitLine, @NonNull TextView recruitPost, @NonNull EditText recruitTitle,
      @NonNull Button recruitUpload) {
    this.rootView = rootView;
    this.ContactInformation = ContactInformation;
    this.postLine = postLine;
    this.radioGroup = radioGroup;
    this.recruitBtnBack = recruitBtnBack;
    this.recruitContact = recruitContact;
    this.recruitContent = recruitContent;
    this.recruitFilterBusking = recruitFilterBusking;
    this.recruitFilterBuskingCapacity = recruitFilterBuskingCapacity;
    this.recruitFilterBuskingDate = recruitFilterBuskingDate;
    this.recruitFilterBuskingLayout = recruitFilterBuskingLayout;
    this.recruitFilterBuskingSession = recruitFilterBuskingSession;
    this.recruitFilterClass = recruitFilterClass;
    this.recruitFilterClassCapacity = recruitFilterClassCapacity;
    this.recruitFilterClassDate2 = recruitFilterClassDate2;
    this.recruitFilterClassLayout = recruitFilterClassLayout;
    this.recruitFilterClassType = recruitFilterClassType;
    this.recruitLine = recruitLine;
    this.recruitPost = recruitPost;
    this.recruitTitle = recruitTitle;
    this.recruitUpload = recruitUpload;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRecrutwriteBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRecrutwriteBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_recrutwrite, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRecrutwriteBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.Contact_information;
      LinearLayout ContactInformation = ViewBindings.findChildViewById(rootView, id);
      if (ContactInformation == null) {
        break missingId;
      }

      id = R.id.post_line;
      View postLine = ViewBindings.findChildViewById(rootView, id);
      if (postLine == null) {
        break missingId;
      }

      id = R.id.radioGroup;
      RadioGroup radioGroup = ViewBindings.findChildViewById(rootView, id);
      if (radioGroup == null) {
        break missingId;
      }

      id = R.id.recruit_btn_back;
      ImageButton recruitBtnBack = ViewBindings.findChildViewById(rootView, id);
      if (recruitBtnBack == null) {
        break missingId;
      }

      id = R.id.recruit_contact;
      EditText recruitContact = ViewBindings.findChildViewById(rootView, id);
      if (recruitContact == null) {
        break missingId;
      }

      id = R.id.recruit_content;
      EditText recruitContent = ViewBindings.findChildViewById(rootView, id);
      if (recruitContent == null) {
        break missingId;
      }

      id = R.id.recruit_filter_busking;
      AppCompatRadioButton recruitFilterBusking = ViewBindings.findChildViewById(rootView, id);
      if (recruitFilterBusking == null) {
        break missingId;
      }

      id = R.id.recruit_filter_busking_capacity;
      NumberPicker recruitFilterBuskingCapacity = ViewBindings.findChildViewById(rootView, id);
      if (recruitFilterBuskingCapacity == null) {
        break missingId;
      }

      id = R.id.recruit_filter_busking_date;
      EditText recruitFilterBuskingDate = ViewBindings.findChildViewById(rootView, id);
      if (recruitFilterBuskingDate == null) {
        break missingId;
      }

      id = R.id.recruit_filter_busking_layout;
      LinearLayout recruitFilterBuskingLayout = ViewBindings.findChildViewById(rootView, id);
      if (recruitFilterBuskingLayout == null) {
        break missingId;
      }

      id = R.id.recruit_filter_busking_session;
      EditText recruitFilterBuskingSession = ViewBindings.findChildViewById(rootView, id);
      if (recruitFilterBuskingSession == null) {
        break missingId;
      }

      id = R.id.recruit_filter_class;
      AppCompatRadioButton recruitFilterClass = ViewBindings.findChildViewById(rootView, id);
      if (recruitFilterClass == null) {
        break missingId;
      }

      id = R.id.recruit_filter_class_capacity;
      NumberPicker recruitFilterClassCapacity = ViewBindings.findChildViewById(rootView, id);
      if (recruitFilterClassCapacity == null) {
        break missingId;
      }

      id = R.id.recruit_filter_class_date2;
      EditText recruitFilterClassDate2 = ViewBindings.findChildViewById(rootView, id);
      if (recruitFilterClassDate2 == null) {
        break missingId;
      }

      id = R.id.recruit_filter_class_layout;
      LinearLayout recruitFilterClassLayout = ViewBindings.findChildViewById(rootView, id);
      if (recruitFilterClassLayout == null) {
        break missingId;
      }

      id = R.id.recruit_filter_class_type;
      EditText recruitFilterClassType = ViewBindings.findChildViewById(rootView, id);
      if (recruitFilterClassType == null) {
        break missingId;
      }

      id = R.id.recruit_line;
      View recruitLine = ViewBindings.findChildViewById(rootView, id);
      if (recruitLine == null) {
        break missingId;
      }

      id = R.id.recruit_post;
      TextView recruitPost = ViewBindings.findChildViewById(rootView, id);
      if (recruitPost == null) {
        break missingId;
      }

      id = R.id.recruit_title;
      EditText recruitTitle = ViewBindings.findChildViewById(rootView, id);
      if (recruitTitle == null) {
        break missingId;
      }

      id = R.id.recruit_upload;
      Button recruitUpload = ViewBindings.findChildViewById(rootView, id);
      if (recruitUpload == null) {
        break missingId;
      }

      return new ActivityRecrutwriteBinding((LinearLayout) rootView, ContactInformation, postLine,
          radioGroup, recruitBtnBack, recruitContact, recruitContent, recruitFilterBusking,
          recruitFilterBuskingCapacity, recruitFilterBuskingDate, recruitFilterBuskingLayout,
          recruitFilterBuskingSession, recruitFilterClass, recruitFilterClassCapacity,
          recruitFilterClassDate2, recruitFilterClassLayout, recruitFilterClassType, recruitLine,
          recruitPost, recruitTitle, recruitUpload);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
