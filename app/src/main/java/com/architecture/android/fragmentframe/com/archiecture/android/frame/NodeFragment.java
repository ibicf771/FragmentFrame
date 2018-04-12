/*
 * Copyright © Yan Zhenjie. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.architecture.android.fragmentframe.com.archiecture.android.frame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SUZY on 2018/3/20.
 */
public class NodeFragment extends Fragment {

    public static final int RESULT_OK = Activity.RESULT_OK;
    public static final int RESULT_CANCELED = Activity.RESULT_CANCELED;
    public static final int REQUEST_CODE_POI_SEARCH= 100;
    private static final int REQUEST_CODE_INVALID = BaseActivity.REQUEST_CODE_INVALID;

    /**
     * Create a new instance of a Fragment with the given class name.  This is the same as calling its empty constructor.
     *
     * @param context       context.
     * @param fragmentClass class of fragment.
     * @param <T>           subclass of {@link NodeFragment}.
     * @return new instance.
     * @deprecated In {@code Activity} with {@link BaseActivity#fragment(Class)} instead;
     * in the {@code Fragment} width {@link #fragment(Class)} instead.
     */
    @Deprecated
    public static <T extends NodeFragment> T instantiate(Context context, Class<T> fragmentClass) {
        //noinspection unchecked
        return (T) instantiate(context, fragmentClass.getName(), null);
    }

    /**
     * Create a new instance of a Fragment with the given class name.  This is the same as calling its empty constructor.
     *
     * @param context       context.
     * @param fragmentClass class of fragment.
     * @param bundle        argument.
     * @param <T>           subclass of {@link NodeFragment}.
     * @return new instance.
     * @deprecated In {@code Activity} with {@link BaseActivity#fragment(Class, Bundle)} instead;
     * in the {@code Fragment} width {@link #fragment(Class, Bundle)} instead.
     */
    @Deprecated
    public static <T extends NodeFragment> T instantiate(Context context, Class<T> fragmentClass, Bundle bundle) {
        //noinspection unchecked
        return (T) instantiate(context, fragmentClass.getName(), bundle);
    }

    /**
     * Create a new instance of a Fragment with the given class name.  This is the same as calling its empty constructor.
     *
     * @param fragmentClass class of fragment.
     * @param <T>           subclass of {@link NodeFragment}.
     * @return new instance.
     */
    public final <T extends NodeFragment> T fragment(Class<T> fragmentClass) {
        //noinspection unchecked
        return (T) instantiate(getContext(), fragmentClass.getName(), null);
    }

    /**
     * Create a new instance of a Fragment with the given class name.  This is the same as calling its empty constructor.
     *
     * @param fragmentClass class of fragment.
     * @param bundle        argument.
     * @param <T>           subclass of {@link NodeFragment}.
     * @return new instance.
     */
    public final <T extends NodeFragment> T fragment(Class<T> fragmentClass, Bundle bundle) {
        //noinspection unchecked
        return (T) instantiate(getContext(), fragmentClass.getName(), bundle);
    }

    /**
     * Toolbar.
     */
    private Toolbar mToolbar;

    /**
     * CompatActivity.
     */
    private BaseActivity mActivity;

    /**
     * Get BaseActivity.
     *
     * @return {@link BaseActivity}.
     */
    protected final BaseActivity getCompatActivity() {
        return mActivity;
    }

    /**
     * Start activity.
     *
     * @param clazz class for activity.
     * @param <T>   {@link Activity}.
     */
    protected final <T extends Activity> void startActivity(Class<T> clazz) {
        startActivity(new Intent(mActivity, clazz));
    }

    /**
     * Start activity and finish my parent.
     *
     * @param clazz class for activity.
     * @param <T>   {@link Activity}.
     */
    protected final <T extends Activity> void startActivityFinish(Class<T> clazz) {
        startActivity(new Intent(mActivity, clazz));
        mActivity.finish();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    /**
     * Destroy me.
     */
    public void finish() {
        mActivity.onBackPressed();
    }

    /**
     * Set Toolbar.
     *
     * @param toolbar {@link Toolbar}.
     */

    @SuppressLint("RestrictedApi")
    public final void setToolbar(@NonNull Toolbar toolbar) {
        this.mToolbar = toolbar;
        onCreateOptionsMenu(mToolbar.getMenu(), new SupportMenuInflater(mActivity));
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
    }

    /**
     * Display home up button.
     *
     * @param drawableId drawable id.
     */
    public final void displayHomeAsUpEnabled(@DrawableRes int drawableId) {
        displayHomeAsUpEnabled(ContextCompat.getDrawable(mActivity, drawableId));
    }

    /**
     * Display home up button.
     *
     * @param drawable {@link Drawable}.
     */
    public final void displayHomeAsUpEnabled(Drawable drawable) {
        mToolbar.setNavigationIcon(drawable);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!onInterceptToolbarBack())
                    finish();
            }
        });
    }

    /**
     * Override this method, intercept backPressed of ToolBar.
     *
     * @return true, other wise false.
     */
    public boolean onInterceptToolbarBack() {
        return false;
    }

    /**
     * Get Toolbar.
     *
     * @return {@link Toolbar}.
     */
    protected final
    @Nullable
    Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * Set title.
     *
     * @param title title.
     */
    protected void setTitle(CharSequence title) {
        if (mToolbar != null)
            mToolbar.setTitle(title);
    }

    /**
     * Set title.
     *
     * @param titleId string resource id from {@code string.xml}.
     */
    protected void setTitle(int titleId) {
        if (mToolbar != null)
            mToolbar.setTitle(titleId);
    }

    /**
     * Set sub title.
     *
     * @param title sub title.
     */
    protected void setSubtitle(CharSequence title) {
        if (mToolbar != null)
            mToolbar.setSubtitle(title);
    }

    /**
     * Set sub title.
     *
     * @param titleId string resource id from {@code string.xml}.
     */
    protected void setSubtitle(int titleId) {
        if (mToolbar != null)
            mToolbar.setSubtitle(titleId);
    }

    // ------------------------- Stack ------------------------- //

    /**
     * Stack info.
     */
    private BaseActivity.FragmentStackEntity mStackEntity;

    /**
     * Set result.
     *
     * @param resultCode result code, one of {@link NodeFragment#RESULT_OK}, {@link NodeFragment#RESULT_CANCELED}.
     */
    protected final void setResult(@ResultCode int resultCode) {
        mStackEntity.resultCode = resultCode;
    }

    /**
     * Set result.
     *
     * @param resultCode resultCode, use {@link }.
     * @param result     {@link Bundle}.
     */
    protected final void setResult(@ResultCode int resultCode, @NonNull Bundle result) {
        mStackEntity.resultCode = resultCode;
        mStackEntity.result = result;
    }

    /**
     * Get the resultCode for requestCode.
     */
    final void setStackEntity(@NonNull BaseActivity.FragmentStackEntity stackEntity) {
        this.mStackEntity = stackEntity;
    }

    /**
     * You should override it.
     *
     * @param resultCode resultCode.
     * @param result     {@link Bundle}.
     */
    public void onFragmentResult(int requestCode, @ResultCode int resultCode, @Nullable Bundle result) {
    }

    /**
     * Show a fragment.
     *
     * @param clazz fragment class.
     * @param <T>   {@link NodeFragment}.
     */
    public final <T extends NodeFragment> void startFragment(Class<T> clazz) {
        try {
            NodeFragment targetFragment = clazz.newInstance();
            startFragment(targetFragment, true, REQUEST_CODE_INVALID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a fragment.
     *
     * @param clazz
     * @param bundle
     * @param <T>
     */
    public final <T extends NodeFragment> void startFragment(Class<T> clazz, Bundle bundle) {
        try {
            NodeFragment targetFragment = clazz.newInstance();
            startFragment(targetFragment, true, REQUEST_CODE_INVALID, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a fragment.
     *
     * @param clazz       fragment class.
     * @param stickyStack sticky to back stack.
     * @param <T>         {@link NodeFragment}.
     */
    public final <T extends NodeFragment> void startFragment(Class<T> clazz, boolean stickyStack) {
        try {
            NodeFragment targetFragment = clazz.newInstance();
            startFragment(targetFragment, stickyStack, REQUEST_CODE_INVALID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
     * @param <T>            {@link NodeFragment}.
     */
    public final <T extends NodeFragment> void startFragment(T targetFragment) {
        startFragment(targetFragment, true, REQUEST_CODE_INVALID);
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment
     * @param bundle
     * @param <T>
     */
    public final <T extends NodeFragment> void startFragment(T targetFragment, Bundle bundle) {
        startFragment(targetFragment, true, REQUEST_CODE_INVALID, bundle);
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
     * @param stickyStack    sticky back stack.
     * @param <T>            {@link NodeFragment}.
     */
    public final <T extends NodeFragment> void startFragment(T targetFragment, boolean stickyStack) {
        startFragment(targetFragment, stickyStack, REQUEST_CODE_INVALID);
    }

    /**
     * Show a fragment for result.
     *
     * @param clazz       fragment to display.
     * @param requestCode requestCode.
     * @param <T>         {@link NodeFragment}.
     */
    @Deprecated
    public final <T extends NodeFragment> void startFragmentForResult(Class<T> clazz, int requestCode) {
        try {
            NodeFragment targetFragment = clazz.newInstance();
            startFragment(targetFragment, true, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a fragment for result.
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     * @param <T>
     */
    @Deprecated
    public final <T extends NodeFragment> void startFragmentForResult(Class<T> clazz, int requestCode, Bundle bundle) {
        try {
            NodeFragment targetFragment = clazz.newInstance();
            startFragment(targetFragment, true, requestCode, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Show a fragment for result.
     *
     * @param targetFragment fragment to display.
     * @param requestCode    requestCode.
     * @param <T>            {@link NodeFragment}.
     */
    public final <T extends NodeFragment> void startFragmentForResult(T targetFragment, int requestCode) {
        startFragment(targetFragment, true, requestCode);
    }

    /**
     * Show a fragment for result.
     *
     * @param targetFragment
     * @param requestCode
     * @param bundle
     * @param <T>
     */
    public final <T extends NodeFragment> void startFragmentForResult(T targetFragment, int requestCode, Bundle bundle) {
        startFragment(targetFragment, true, requestCode, bundle);
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
     * @param stickyStack    sticky back stack.
     * @param requestCode    requestCode.
     * @param <T>            {@link NodeFragment}.
     */
    private <T extends NodeFragment> void startFragment(T targetFragment, boolean stickyStack, int requestCode) {
        startFragment(targetFragment, stickyStack, requestCode, null);
    }


    /**
     * Show a fragment.
     *
     * @param targetFragment
     * @param stickyStack
     * @param requestCode
     * @param bundle
     * @param <T>
     */
    private <T extends NodeFragment> void startFragment(T targetFragment, boolean stickyStack, int requestCode, Bundle bundle) {
        mActivity.startFragment(this, targetFragment, stickyStack, requestCode, bundle);
    }


    public final <T extends NodeFragment> void replaceFragment(T thatFragment) {
        mActivity.replaceFragment(thatFragment);
    }

    public final <T extends NodeFragment> void replaceFragment(T thatFragment, Bundle bundle) {
        mActivity.replaceFragment(thatFragment, bundle);
    }

    public final <T extends NodeFragment> void replaceFragment(Class<T> clazz) {
        mActivity.replaceFragment(clazz);
    }

    public final <T extends NodeFragment> void replaceFragment(Class<T> clazz, Bundle bundle) {
        mActivity.replaceFragment(clazz, bundle);
    }

    public final NodeFragment getLastFragment() {
        return mActivity.getLastFragment();
    }

    /**
     * get bundle
     *
     * @return
     */
    public Bundle getBundle(){
        return getArguments();
    }


    private @LayoutRes

    int mLayoutResID;

    private View mView;

    public void setContentView(@LayoutRes int layoutResID) {
        mLayoutResID = layoutResID;
    }


    public void setContentView(View view) {
        mView = view;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mLayoutResID > 0){
            return inflater.inflate(mLayoutResID, null);
        }else if(mView != null){
            return mView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public <T extends View> T findViewById(int id){
        if(getView() !=  null){
            return getView().findViewById(id);
        }
        return null;
    }
}
