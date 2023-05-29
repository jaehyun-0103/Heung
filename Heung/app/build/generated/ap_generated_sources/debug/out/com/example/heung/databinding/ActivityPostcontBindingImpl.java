package com.example.heung.databinding;
import com.example.heung.R;
import com.example.heung.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityPostcontBindingImpl extends ActivityPostcontBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.userinfo_conslayout, 1);
        sViewsWithIds.put(R.id.account_iv_profile, 2);
        sViewsWithIds.put(R.id.tv_author, 3);
        sViewsWithIds.put(R.id.tv_date, 4);
        sViewsWithIds.put(R.id.boardcontent_conslayout, 5);
        sViewsWithIds.put(R.id.tv_title, 6);
        sViewsWithIds.put(R.id.tv_content, 7);
        sViewsWithIds.put(R.id.content_photo_recycler, 8);
        sViewsWithIds.put(R.id.like_btn, 9);
        sViewsWithIds.put(R.id.likeCnt, 10);
        sViewsWithIds.put(R.id.comment_btn, 11);
        sViewsWithIds.put(R.id.CommentCnt, 12);
        sViewsWithIds.put(R.id.edt_comment, 13);
        sViewsWithIds.put(R.id.comment_layout, 14);
        sViewsWithIds.put(R.id.comment_textview, 15);
        sViewsWithIds.put(R.id.btn_ctv, 16);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityPostcontBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 17, sIncludes, sViewsWithIds));
    }
    private ActivityPostcontBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[12]
            , (android.widget.ImageView) bindings[2]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[5]
            , (android.widget.Button) bindings[16]
            , (android.widget.ImageView) bindings[11]
            , (com.google.android.material.textfield.TextInputLayout) bindings[14]
            , (com.google.android.material.textfield.TextInputEditText) bindings[15]
            , (androidx.recyclerview.widget.RecyclerView) bindings[8]
            , (android.widget.LinearLayout) bindings[13]
            , (android.widget.ImageView) bindings[9]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[6]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[1]
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