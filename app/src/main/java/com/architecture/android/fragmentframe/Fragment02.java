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
import com.architecture.android.fragmentframe.util.LeakCanaryUtil;

import org.w3c.dom.Text;

/**
 * Created by yangsimin on 2018/3/21.
 */

public class Fragment02 extends NodeFragment{
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

        setContentView(R.layout.fragment02);
        mTestLeak = new TestLeak();
    }

    static TestLeak mTestLeak;

    class TestLeak{}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d("MainActivity", getClass().getSimpleName() + "onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        mShowText = findViewById(R.id.show_text);
        mButton = (Button)getView().findViewById(R.id.fragment_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "第二页按钮被按下");
//                startFragment(Fragment01.class);
                startFragmentForResult(Fragment01.class, 2);
//                printsDelay();
            }
        });

        mBack = findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("key", "我是第二页返回的信息");
                setResult(Activity.RESULT_OK, bundle);
                finish();
            }
        });

        Button button2 = getView().findViewById(R.id.replace_fragment_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment01 fragment01 = new Fragment01();
                replaceFragment(fragment01);
//                printsDelay();
            }
        });
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, @Nullable Bundle result) {
        if(requestCode == 2 && resultCode == RESULT_OK){
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
        LeakCanaryUtil.watch(this);
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
                FragmentManager FM = ((MainActivity)getActivity()).getMainFragmentManager();

                Log.d("MainActivity", "delay fragment size" + FM.getFragments().size());
                Log.d("MainActivity", "delay FM.getBackStackEntryCount() " + FM.getBackStackEntryCount());
                for (Fragment fragment: FM.getFragments()){
                    Log.d("MainActivity", "delay fragment name: " + fragment.getClass().getSimpleName());
                }
            }
        }, 1000);

    }
}
