package com.example.heung;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.example.heung.databinding.ActivityPostcontBindingImpl;
import com.example.heung.databinding.ItemCallistBindingImpl;
import com.example.heung.databinding.ItemPostcommentBindingImpl;
import com.example.heung.databinding.ItemPostcontBindingImpl;
import com.example.heung.databinding.ItemReplyBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYPOSTCONT = 1;

  private static final int LAYOUT_ITEMCALLIST = 2;

  private static final int LAYOUT_ITEMPOSTCOMMENT = 3;

  private static final int LAYOUT_ITEMPOSTCONT = 4;

  private static final int LAYOUT_ITEMREPLY = 5;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(5);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.example.heung.R.layout.activity_postcont, LAYOUT_ACTIVITYPOSTCONT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.example.heung.R.layout.item_callist, LAYOUT_ITEMCALLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.example.heung.R.layout.item_postcomment, LAYOUT_ITEMPOSTCOMMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.example.heung.R.layout.item_postcont, LAYOUT_ITEMPOSTCONT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.example.heung.R.layout.item_reply, LAYOUT_ITEMREPLY);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYPOSTCONT: {
          if ("layout/activity_postcont_0".equals(tag)) {
            return new ActivityPostcontBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_postcont is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMCALLIST: {
          if ("layout/item_callist_0".equals(tag)) {
            return new ItemCallistBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_callist is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMPOSTCOMMENT: {
          if ("layout/item_postcomment_0".equals(tag)) {
            return new ItemPostcommentBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_postcomment is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMPOSTCONT: {
          if ("layout/item_postcont_0".equals(tag)) {
            return new ItemPostcontBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_postcont is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMREPLY: {
          if ("layout/item_reply_0".equals(tag)) {
            return new ItemReplyBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_reply is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(1);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(5);

    static {
      sKeys.put("layout/activity_postcont_0", com.example.heung.R.layout.activity_postcont);
      sKeys.put("layout/item_callist_0", com.example.heung.R.layout.item_callist);
      sKeys.put("layout/item_postcomment_0", com.example.heung.R.layout.item_postcomment);
      sKeys.put("layout/item_postcont_0", com.example.heung.R.layout.item_postcont);
      sKeys.put("layout/item_reply_0", com.example.heung.R.layout.item_reply);
    }
  }
}
