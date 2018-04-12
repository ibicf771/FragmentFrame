package com.architecture.android.fragmentframe.com.archiecture.android.frame;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by yangsimin on 2018/4/12.
 */

public class FragmentContainerManager {

    private static FragmentContainerManager sFragmentContainerManager;

    private List<NodeFragment> mFragmentStack = new ArrayList<>();

    private Map<NodeFragment, BaseActivity.FragmentStackEntity> mFragmentEntityMap = new HashMap<>();

    protected static FragmentContainerManager getInstance() {
        synchronized (FragmentContainerManager.class) {
            if (sFragmentContainerManager == null) {
                sFragmentContainerManager = new FragmentContainerManager();
            }
            return sFragmentContainerManager;
        }
    }

    protected List<NodeFragment> getFragmentStack(){
        return mFragmentStack;
    }

    protected Map<NodeFragment, BaseActivity.FragmentStackEntity> getFragmentEntityMap(){
        return mFragmentEntityMap;
    }


    protected void reSetFrameStack(FragmentManager fragmentManager){
        mFragmentStack.clear();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            mFragmentStack.add((NodeFragment) fragment);
        }
    }
}
