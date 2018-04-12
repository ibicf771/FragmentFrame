package com.architecture.android.fragmentframe;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.architecture.android.fragmentframe.com.archiecture.android.frame.BaseActivity;
import com.architecture.android.fragmentframe.com.archiecture.android.frame.NodeFragment;

import java.util.Map;

public class MainActivity extends BaseActivity {

    Button mButton, mButton2, mButton3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            Log.d("MainActivity", "savedInstanceState == null");
        }else {
            Log.d("MainActivity", "savedInstanceState != null");
        }
        setContentView(R.layout.activity_main);
        mButton = (Button)findViewById(R.id.button);
        mButton2 = (Button)findViewById(R.id.button2);
        mButton3 = (Button)findViewById(R.id.button3);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                //把自己创建好的fragment创建一个对象
                Fragment01 f1 = new Fragment01();
                startFragment(f1);
                printsDelay();
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment02 f2 = new Fragment02();
                startFragment(f2);
                printsDelay();
            }
        });

        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取到FragmentManager，在V4包中通过getSupportFragmentManager，
                //在系统中原生的Fragment是通过getFragmentManager获得的。
//                FragmentManager FM = getSupportFragmentManager();
//                FM.popBackStack("11", 0);
//                printsDelay();
//                Log.d("MainActivity", "getLastFragment " + getLastFragment().getClass().getSimpleName());
                printFragmentStack();
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("MainActivity", "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("MainActivity", "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void printFragmentStack(){
        Log.d("MainActivity", "LIFE_CYCLE printFragmentStack getFragmentList size " + getFragmentList().size());
        for (int i =0;i<getFragmentList().size();i++){
            Log.d("MainActivity stack", getFragmentList().get(i).getClass().getSimpleName());
        }

        Log.d("MainActivity", "LIFE_CYCLE getFragments size " + getFragments().size());
        for(int t=0;t<getFragments().size();t++){
            Log.d("MainActivity", getFragments().get(t).getClass().getSimpleName());
        }


    }

    Handler mHandler = new Handler();

    private void printsDelay(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentManager FM = getSupportFragmentManager();

                Log.d("MainActivity", "delay fragment size" + FM.getFragments().size());
                Log.d("MainActivity", "delay FM.getBackStackEntryCount() " + FM.getBackStackEntryCount());
                for (Fragment fragment: FM.getFragments()){
                    Log.d("MainActivity", "delay fragment name: " + fragment.getClass().getSimpleName());
                }
            }
        }, 1000);

    }

    public FragmentManager getMainFragmentManager(){
        return getSupportFragmentManager();
    }

    @Override
    protected int fragmentLayoutId() {
        return R.id.fragment_test;
    }
}
