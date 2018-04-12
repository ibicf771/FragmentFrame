package com.architecture.android.fragmentframe.com.archiecture.android.frame;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SUZY on 2018/3/20.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_INVALID = -1;

    private FragmentManager mFManager;
    private AtomicInteger mAtomicInteger = new AtomicInteger();
    private List<NodeFragment> mFragmentStack = new ArrayList<>();
    private Map<NodeFragment, FragmentStackEntity> mFragmentEntityMap = new HashMap<>();

    static class FragmentStackEntity {
        private FragmentStackEntity() {
        }

        private boolean isSticky = false;
        private int requestCode = REQUEST_CODE_INVALID;
        @ResultCode
        int resultCode = RESULT_CANCELED;
        Bundle result = null;
    }

    public final <T extends NodeFragment> T fragment(Class<T> fragmentClass) {
        //noinspection unchecked
        return (T) Fragment.instantiate(this, fragmentClass.getName());
    }

    public final <T extends NodeFragment> T fragment(Class<T> fragmentClass, Bundle bundle) {
        //noinspection unchecked
        return (T) Fragment.instantiate(this, fragmentClass.getName(), bundle);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFManager = getSupportFragmentManager();
        mFragmentStack.clear();
        mFragmentEntityMap.clear();
    }

    /**
     * Show a fragment.
     *
     * @param clazz fragment class.
     */
    public final <T extends NodeFragment> void startFragment(Class<T> clazz) {
        try {
            NodeFragment targetFragment = clazz.newInstance();
            startFragment(null, targetFragment, true, REQUEST_CODE_INVALID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a fragment.
     *
     * @param clazz       fragment class.
     * @param stickyStack sticky to back stack.
     */
    public final <T extends NodeFragment> void startFragment(Class<T> clazz, boolean stickyStack) {
        try {
            NodeFragment targetFragment = clazz.newInstance();
            startFragment(null, targetFragment, stickyStack, REQUEST_CODE_INVALID);
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
        startFragment(null, targetFragment, true, REQUEST_CODE_INVALID);
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
     * @param stickyStack    sticky back stack.
     * @param <T>            {@link NodeFragment}.
     */
    public final <T extends NodeFragment> void startFragment(T targetFragment, boolean stickyStack) {
        startFragment(null, targetFragment, stickyStack, REQUEST_CODE_INVALID);
    }

    /**
     * Show a fragment for result.
     *
     * @param clazz       fragment to display.
     * @param requestCode requestCode.
     * @param <T>         {@link NodeFragment}.
     * @deprecated use {@link #startFragmentForResult(Class, int)} instead.
     */
    @Deprecated
    public final <T extends NodeFragment> void startFragmentForResquest(Class<T> clazz, int requestCode) {
        startFragmentForResult(clazz, requestCode);
    }

    /**
     * Show a fragment for result.
     *
     * @param targetFragment fragment to display.
     * @param requestCode    requestCode.
     * @param <T>            {@link NodeFragment}.
     * @deprecated use {@link #startFragmentForResult(NodeFragment, int)} instead.
     */
    @Deprecated
    public final <T extends NodeFragment> void startFragmentForResquest(T targetFragment, int requestCode) {
        startFragmentForResult(targetFragment, requestCode);
    }

    /**
     * Show a fragment for result.
     *
     * @param clazz       fragment to display.
     * @param requestCode requestCode.
     * @param <T>         {@link NodeFragment}.
     */
    public final <T extends NodeFragment> void startFragmentForResult(Class<T> clazz, int requestCode) {
        if (requestCode == REQUEST_CODE_INVALID)
            throw new IllegalArgumentException("The requestCode must be positive integer.");
        try {
            NodeFragment targetFragment = clazz.newInstance();
            startFragment(null, targetFragment, true, requestCode);
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
        if (requestCode == REQUEST_CODE_INVALID)
            throw new IllegalArgumentException("The requestCode must be positive integer.");
        startFragment(null, targetFragment, true, requestCode);
    }


    protected final <T extends NodeFragment> void startFragment(T thisFragment, T thatFragment,
                                                                boolean stickyStack, int requestCode) {
        startFragment(thisFragment, thatFragment, stickyStack, requestCode, null);
    }

    /**
     * Show a fragment.
     *
     * @param thisFragment Now show fragment, can be null.
     * @param thatFragment fragment to display.
     * @param stickyStack  sticky back stack.
     * @param requestCode  requestCode.
     * @param <T>          {@link NodeFragment}.
     */
    protected final <T extends NodeFragment> void startFragment(T thisFragment, T thatFragment,
                                                                boolean stickyStack, int requestCode, Bundle bundle) {

        FragmentTransaction fragmentTransaction = mFManager.beginTransaction();
        if (thisFragment != null) {
            FragmentStackEntity thisStackEntity = mFragmentEntityMap.get(thisFragment);
            if (thisStackEntity != null) {
                if (thisStackEntity.isSticky) {
                    thisFragment.onPause();
                    thisFragment.onStop();
                    fragmentTransaction.hide(thisFragment);
                } else {
                    fragmentTransaction.remove(thisFragment).commit();
                    fragmentTransaction.commitNow();
                    fragmentTransaction = mFManager.beginTransaction();

                    mFragmentEntityMap.remove(thisFragment);
                    mFragmentStack.remove(thisFragment);
                }
            }
        }

        String fragmentTag = thatFragment.getClass().getSimpleName() + mAtomicInteger.incrementAndGet();
        if(bundle != null){
            thatFragment.setArguments(bundle);
        }
        fragmentTransaction.add(fragmentLayoutId(), thatFragment, fragmentTag);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();

        FragmentStackEntity fragmentStackEntity = new FragmentStackEntity();
        fragmentStackEntity.isSticky = stickyStack;
        fragmentStackEntity.requestCode = requestCode;
        thatFragment.setStackEntity(fragmentStackEntity);
        mFragmentEntityMap.put(thatFragment, fragmentStackEntity);

        mFragmentStack.add(thatFragment);
    }

    public final <T extends NodeFragment> void replaceFragment(Class<T> clazz) {
        try {
            NodeFragment targetFragment = clazz.newInstance();
            replaceFragment(targetFragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final <T extends NodeFragment> void replaceFragment(Class<T> clazz, Bundle bundle) {
        try {
            NodeFragment targetFragment = clazz.newInstance();
            replaceFragment(targetFragment, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final <T extends NodeFragment> void replaceFragment(T thatFragment) {
        replaceFragment(null, thatFragment, true,  REQUEST_CODE_INVALID, null);
    }

    public final <T extends NodeFragment> void replaceFragment(T thatFragment, Bundle bundle) {
        replaceFragment(null, thatFragment, true,  REQUEST_CODE_INVALID, bundle);
    }

    public final <T extends NodeFragment> void replaceFragment(T thisFragment, T thatFragment,
                                                                boolean stickyStack, int requestCode, Bundle bundle) {

        if (mFragmentStack.size() > 0){
            mFManager.popBackStack();
            NodeFragment outFragment = mFragmentStack.get(mFragmentStack.size() - 1);
            mFragmentStack.remove(outFragment);
            mFragmentEntityMap.remove(outFragment);
        }

        FragmentTransaction fragmentTransaction = mFManager.beginTransaction();
        String fragmentTag = thatFragment.getClass().getSimpleName() + mAtomicInteger.incrementAndGet();
        if(bundle != null){
            thatFragment.setArguments(bundle);
        }
        fragmentTransaction.add(fragmentLayoutId(), thatFragment, fragmentTag);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();

        FragmentStackEntity fragmentStackEntity = new FragmentStackEntity();
        fragmentStackEntity.isSticky = stickyStack;
        fragmentStackEntity.requestCode = requestCode;
        thatFragment.setStackEntity(fragmentStackEntity);
        mFragmentEntityMap.put(thatFragment, fragmentStackEntity);
        mFragmentStack.add(thatFragment);
    }



        /**
         * When the back off.
         */
    protected final boolean onBackStackFragment() {
        if (mFragmentStack.size() > 1) {
            mFManager.popBackStack();
            NodeFragment inFragment = mFragmentStack.get(mFragmentStack.size() - 2);

            FragmentTransaction fragmentTransaction = mFManager.beginTransaction();
            fragmentTransaction.show(inFragment);
            fragmentTransaction.commit();

            NodeFragment outFragment = mFragmentStack.get(mFragmentStack.size() - 1);
            inFragment.onResume();

            FragmentStackEntity stackEntity = mFragmentEntityMap.get(outFragment);
            mFragmentStack.remove(outFragment);
            mFragmentEntityMap.remove(outFragment);

            if (stackEntity.requestCode != REQUEST_CODE_INVALID) {
                inFragment.onFragmentResult(stackEntity.requestCode, stackEntity.resultCode, stackEntity.result);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (!onBackStackFragment()) {
            finish();
        }
    }

    /**
     * 获取fragment栈顶的fragment
     *
     * @return
     */
    public NodeFragment getLastFragment(){
        return mFragmentStack.size() == 0 ? null : mFragmentStack.get(mFragmentStack.size() - 1);

    }

    /**
     * 获取fragment栈的所有fragment
     *
     * @return
     */
    public List<NodeFragment> getFragmentList(){
        return mFragmentStack;
    }

    /**
     * Should be returned to display fragments id of {@link android.view.ViewGroup}.
     *
     * @return resource id of {@link android.view.ViewGroup}.
     */
    protected abstract
    @IdRes
    int fragmentLayoutId();
}
