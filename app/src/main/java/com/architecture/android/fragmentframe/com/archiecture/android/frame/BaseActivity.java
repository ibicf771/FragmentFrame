package com.architecture.android.fragmentframe.com.archiecture.android.frame;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SUZY on 2018/3/20.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_INVALID = -1;

    private FragmentManager mFManager;
    private AtomicInteger mAtomicInteger = new AtomicInteger();
    private List<NodeFragment> mFragmentStack;
    private Map<NodeFragment, FragmentStackEntity> mFragmentEntityMap;
    private FragmentContainerManager mFragmentContainerManager;

    public static class FragmentStackEntity {
        private FragmentStackEntity() {
        }

        public boolean isSticky = false;
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
        mFragmentContainerManager = FragmentContainerManager.getInstance();
        mFragmentStack = mFragmentContainerManager.getFragmentStack();
        mFragmentEntityMap = mFragmentContainerManager.getFragmentEntityMap();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

        FragmentTransaction fragmentTransaction = mFManager.beginTransaction();
        if (thisFragment != null) {
            fragmentTransaction.remove(thisFragment).commit();
            fragmentTransaction.commitNow();
            fragmentTransaction = mFManager.beginTransaction();

            mFragmentEntityMap.remove(thisFragment);
            mFragmentStack.remove(thisFragment);
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
            //增加返回键调用
//            outFragment.onBackPressed();

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
        //onBackPressed先于fragment的操作，如果fragment消费了onBackPressed，则不做后续退出操作
        if(getCurrentFragment() != null){
            if(getCurrentFragment().onBackPressed()){
                return;
            }
        }
        finishFragment();
    }

    public void finishFragment(){
        if (!onBackStackFragment()) {
            exitApp();
        }
    }

    private NodeFragment getCurrentFragment(){
        NodeFragment currentFragment = null;
        if (mFragmentStack.size() > 0) {
            currentFragment = mFragmentStack.get(mFragmentStack.size() - 1);
        }
        return currentFragment;
    }
    /**
     * 双击退出
     */
    private static Boolean isExit = false;
    private void exitApp() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 1500);


        } else {
            finish();
            System.exit(0);
        }
    }

    public List<Fragment> getFragments(){
        return mFManager.getFragments();
    }

    /**
     * Should be returned to display fragments id of {@link android.view.ViewGroup}.
     *
     * @return resource id of {@link android.view.ViewGroup}.
     */
    protected abstract
    @IdRes
    int fragmentLayoutId();

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

}
