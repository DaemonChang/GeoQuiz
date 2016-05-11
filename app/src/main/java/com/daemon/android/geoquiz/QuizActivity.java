package com.daemon.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    //logcat tag
    private static final String TAG = "QuizActivity";
    //a key for the value of mCurrentIndex
    private static final String KEY_INDEX = "index";
    //request code
    private static final int REQUEST_CODE_CHEAT = 0;
    //a key for the value of mIsCheater
    private static final String KEY_ISCHEATER ="isCheater";



    private Button mCheatButton;
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;


    private Question[] mQuestionBank = new Question[]{
        new Question(R.string.question_africa,false) ,
        new Question(R.string.question_americas,true),
        new Question(R.string.question_asia,true),
        new Question(R.string.question_oceans,true),
        new Question(R.string.question_mideast,false)
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    //update the question in different pages
    private void updateQuestion(){
        //stack trace
        //Log.d(TAG,"Updating question text for question #"+ mCurrentIndex,new Exception());
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }
    //check the answer true or false
    private void checkAnswer(boolean userPressTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if(mIsCheater){
           messageResId = R.string.judgment_toast;
        }else{
            if(userPressTrue == answerIsTrue){
               messageResId = R.string.correct_toast;
            }else{
                messageResId = R.string.incorrect_toast;
            }
        }
       Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"OnCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if(savedInstanceState!=null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);

            if(savedInstanceState.getBoolean(KEY_ISCHEATER,false)){//假如是作弊者
                //让作弊者进行到底
               mIsCheater = true;
            }
        }

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);

        //getting references to widgets
        mTrueButton = (Button)findViewById(R.id.true_button);


        mFalseButton = (Button)findViewById(R.id.false_button);
        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mPrevButton = (ImageButton)findViewById(R.id.prev_button);
        mCheatButton = (Button)findViewById(R.id.cheat_button);

        //setting listeners
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex +1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });
        //challenge 2:add a previous Button
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + mQuestionBank.length -1) % mQuestionBank.length;
                updateQuestion();
            }
        });


        //challenge 1:add a Listener to the TextView
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex +1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start CheatActivity
               // Intent i = new Intent(QuizActivity.this,CheatActivity.class);
                //startActivity(i);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this,answerIsTrue);
                startActivityForResult(i,REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT){
           if(data == null){
              return;
           }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG,"onSaveInstanceState() called");
        outState.putInt(KEY_INDEX,mCurrentIndex);
        outState.putBoolean(KEY_ISCHEATER,mIsCheater);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy() called");
    }
}
