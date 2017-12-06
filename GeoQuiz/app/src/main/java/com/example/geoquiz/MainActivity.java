package com.example.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mPrevButton;
    private Button mNextButton;
    private Button mCheatButton;
    private TextView mQuestionView;
    private boolean mIsCheater;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia,true),
            new Question(R.string.question_oceans,true),
            new Question(R.string.question_mideast,false),
            new Question(R.string.question_africa,false),
            new Question(R.string.question_americas,true),
            new Question(R.string.question_asia,true)
    };

    private int mCurrentIndex = 0;
    private int mTrueAnswerNum = 0;
    private int mAnswerNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mPrevButton = findViewById(R.id.prev_button);
        mNextButton = findViewById(R.id.next_button);
        mQuestionView = findViewById(R.id.question_view);
        mQuestionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNextButton.performClick();
            }
        });
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mQuestionBank[mCurrentIndex].setAnswered(true);
                closeAnswerButton();
               checkAnswer(true);
            }
        });
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionBank[mCurrentIndex].setAnswered(true);
                closeAnswerButton();
                checkAnswer(false);
            }
        });
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1 + mQuestionBank.length) % mQuestionBank.length;
                updateQuestion();
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsCheater = false;
                mCurrentIndex = (mCurrentIndex+1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
               Intent intent = CheatActivity.newIntent(MainActivity.this,answerIsTrue);
               startActivityForResult(intent,REQUEST_CODE_CHEAT);
            }
        });
        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    private void updateQuestion() {
        Question question = mQuestionBank[mCurrentIndex];
        mQuestionView.setText(question.getTextResId());
        if(question.isAnswered()) {
            closeAnswerButton();
        } else {
            openAnswerButton();
        }
    }

    private void checkAnswer(boolean answer) {
        int id;
        mAnswerNum++;
        if (mIsCheater) {
            id = R.string.judgment_toast;
        } else if(answer == mQuestionBank[mCurrentIndex].isAnswerTrue()) {
            id = R.string.correct_toast;
            mTrueAnswerNum++;
        } else {
            id = R.string.incorrect_toast;
        }
        Toast.makeText(MainActivity.this, id,
                Toast.LENGTH_SHORT).show();
        if(mAnswerNum == mQuestionBank.length) {
            Toast.makeText(MainActivity.this, "you answer " + mAnswerNum
                    + " questions and "+ mTrueAnswerNum + " answers is true.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void openAnswerButton() {
        mTrueButton.setClickable(true);
        mFalseButton.setClickable(true);
    }

    private void closeAnswerButton() {
        mTrueButton.setClickable(false);
        mFalseButton.setClickable(false);
    }
}
