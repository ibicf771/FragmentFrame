package com.architecture.android.fragmentframe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.architecture.android.fragmentframe.com.archiecture.android.frame.NodeFragment;

/**
 * Created by yangsimin on 2018/3/21.
 */

public class Fragment01 extends NodeFragment{
    Button mButton;
    Button mBack;
    TextView mShowText;

    @Override
    public void onAttach(Context context) {
        Log.d("MainActivity", getClass().getSimpleName() + "onAttach");
        super.onAttach(context);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("MainActivity", getClass().getSimpleName() + "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment01);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d("MainActivity", getClass().getSimpleName() + "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        mShowText = findViewById(R.id.show_text);
        mButton = (Button)findViewById(R.id.fragment_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "第一页按钮被按下");
//                startFragment(Fragment02.class);

                startFragmentForResult(Fragment02.class, 1);
                printsDelay();
            }
        });
        mBack = findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("key", "我是第一页返回的信息");
                setResult(Activity.RESULT_OK, bundle);
                finish();
            }
        });
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, @Nullable Bundle result) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            String text = result.getString("key");
            mShowText.setText(text);
        }
        super.onFragmentResult(requestCode, resultCode, result);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("MainActivity", getClass().getSimpleName() + "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        Log.d("MainActivity", getClass().getSimpleName() + "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("MainActivity", getClass().getSimpleName() + "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("MainActivity", getClass().getSimpleName() + "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("MainActivity", getClass().getSimpleName() + "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d("MainActivity", getClass().getSimpleName() + "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d("MainActivity", getClass().getSimpleName() + "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("MainActivity", getClass().getSimpleName() + "onDetach");
        super.onDetach();
    }

    Handler mHandler = new Handler();

    private void printsDelay(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                FragmentManager FM = ((MainActivity)getActivity()).getMainFragmentManager();
//
//                Log.d("MainActivity", "delay fragment size" + FM.getFragments().size());
//                Log.d("MainActivity", "delay FM.getBackStackEntryCount() " + FM.getBackStackEntryCount());
//                for (Fragment fragment: FM.getFragments()){
//                    Log.d("MainActivity", "delay fragment name: " + fragment.getClass().getSimpleName());
//                }
            }
        }, 1000);

    }
}
