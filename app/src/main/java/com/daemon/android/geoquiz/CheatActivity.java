package com.daemon.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CheatActivity extends AppCompatActivity {
    //extra constant key
    private static final String EXTRA_ANSWER_IS_TRUE  =
            "com.daemon.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN =
            "com.daemon.android.geoquiz.answer_shown";
    private static final String KEY_ANSWER_SHOWN = "shown";

    private static final String TAG = "CheatActivity";

    private boolean mAnswerShown;//标识，是否查看过答案
    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button   mShowAnswer;
    private TextView mShowAPILevel;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent i = new Intent(packageContext , CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        return i ;
    }

    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);

        mAnswerTextView = (TextView)findViewById(R.id.answer_text_view);
        mShowAnswer = (Button)findViewById(R.id.show_answer_button);
        mShowAPILevel = (TextView)findViewById(R.id.show_build_version_text_view);

        mShowAPILevel.setText("API level "+Build.VERSION.SDK_INT);

        if(savedInstanceState !=null){
            if(savedInstanceState.getBoolean(KEY_ANSWER_SHOWN,false)){//查看过
                Log.d(TAG,"after rotation,mAnswerShown:"+mAnswerShown);
                //再次赋值，因为转屏后，mAnswerShown变为false。
                mAnswerShown = true;
                showAnswerText();//显示答案
                //再次把extra设为true，转屏后（activity destroied），activity的变量都会重新设定
                setAnswerShownResult(true);
            }
        }



        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnswerShown = true;
                showAnswerText();
                setAnswerShownResult(true);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//API>=21
                    //animation code
                    int cx = mShowAnswer.getWidth() / 2;
                    int cy = mShowAnswer.getHeight() / 2;
                    float radius = mShowAnswer.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswer, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mAnswerTextView.setVisibility(View.VISIBLE);
                            mShowAnswer.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                }else{
                    mAnswerTextView.setVisibility(View.VISIBLE);
                    mShowAnswer.setVisibility(View.INVISIBLE);
                }
            }


        });
    }
    //显示答案
    private void showAnswerText() {
        if(mAnswerIsTrue){
            mAnswerTextView.setText(R.string.true_button);
        }else{
            mAnswerTextView.setText(R.string.false_button);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"onSaveInstanceState() called and mAnswerShown:"+mAnswerShown);
        // 保存是否查看过答案的标识
        outState.putBoolean(KEY_ANSWER_SHOWN,mAnswerShown);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        setResult(RESULT_OK,data);
    }
}
