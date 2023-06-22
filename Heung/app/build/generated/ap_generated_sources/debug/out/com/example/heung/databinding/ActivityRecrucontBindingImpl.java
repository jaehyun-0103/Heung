package com.example.heung.databinding;
import com.example.heung.R;
import com.example.heung.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityRecrucontBindingImpl extends ActivityRecrucontBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.conslayout, 1);
        sViewsWithIds.put(R.id.app, 2);
        sViewsWithIds.put(R.id.recruit_reply_delete, 3);
        sViewsWithIds.put(R.id.post_line2, 4);
        sViewsWithIds.put(R.id.recruit_content_button_back, 5);
        sViewsWithIds.put(R.id.userinfo_conslayout, 6);
        sViewsWithIds.put(R.id.account_iv_profile, 7);
        sViewsWithIds.put(R.id.recruit_content_author, 8);
        sViewsWithIds.put(R.id.recruit_content_date, 9);
        sViewsWithIds.put(R.id.boardcontent_conslayout, 10);
        sViewsWithIds.put(R.id.recruit_content_title, 11);
        sViewsWithIds.put(R.id.recruit_content_content, 12);
        sViewsWithIds.put(R.id.recruit_content_type, 13);
        sViewsWithIds.put(R.id.recruit_content_endDate, 14);
        sViewsWithIds.put(R.id.recruit_content_max, 15);
        sViewsWithIds.put(R.id.recruit_content_curr, 16);
        sViewsWithIds.put(R.id.recruit_type, 17);
        sViewsWithIds.put(R.id.recruit_contact, 18);
        sViewsWithIds.put(R.id.post_line, 19);
        sViewsWithIds.put(R.id.textView, 20);
        sViewsWithIds.put(R.id.textView2, 21);
        sViewsWithIds.put(R.id.textView4, 22);
        sViewsWithIds.put(R.id.content_conslayout, 23);
        sViewsWithIds.put(R.id.recruit_curr, 24);
        sViewsWithIds.put(R.id.complete, 25);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityRecrucontBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 26, sIncludes, sViewsWithIds));
    }
    private ActivityRecrucontBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.ImageView) bindings[7]
            , (android.widget.TextView) bindings[2]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[10]
            , (android.widget.Button) bindings[25]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[1]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[23]
            , (android.view.View) bindings[19]
            , (android.view.View) bindings[4]
            , (android.widget.TextView) bindings[18]
            , (android.widget.TextView) bindings[8]
            , (android.widget.ImageButton) bindings[5]
            , (android.widget.TextView) bindings[12]
            , (android.widget.TextView) bindings[16]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[14]
            , (android.widget.TextView) bindings[15]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[13]
            , (android.widget.NumberPicker) bindings[24]
            , (android.widget.Button) bindings[3]
            , (android.widget.TextView) bindings[17]
            , (android.widget.TextView) bindings[20]
            , (android.widget.TextView) bindings[21]
            , (android.widget.TextView) bindings[22]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[6]
            );
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}