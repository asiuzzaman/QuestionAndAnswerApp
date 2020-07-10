package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.example.trivia.controller.AppController;
import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.QuestionBank;
import com.example.trivia.data.Score;
import com.example.trivia.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionText,questionCounter,textViewScoreCounter;
    private Button trueButton,falseButton;
    private ImageButton previousImageButton,nextImageButton;
    private int currentQuestionIndex=0;
    private List<Question>questionList;
    private int scoreCounter=0;
    private Score score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        score=new Score();//instantiate our score object;
        initializeField();
        nextImageButton.setOnClickListener(this);
        previousImageButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);

         questionList= new QuestionBank().getQuestion(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionText.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                questionCounter.setText(currentQuestionIndex +"/"+questionArrayList.size());
            }
        });
        //Log.d("Main: ","question: "+res);
    }


    private void initializeField() {
        questionText=findViewById(R.id.question_text);
        questionCounter=findViewById(R.id.question_count);
        textViewScoreCounter=findViewById(R.id.score_counter);
        trueButton=findViewById(R.id.true_button);
        falseButton=findViewById(R.id.false_button);
        previousImageButton=findViewById(R.id.previous_button);
        nextImageButton=findViewById(R.id.next_button);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.previous_button:
                if(currentQuestionIndex<=0) currentQuestionIndex=0;
                else currentQuestionIndex=currentQuestionIndex-1;
                updateQuestion();
                break;
            case R.id.next_button:
                if(currentQuestionIndex>=questionList.size()-1) currentQuestionIndex=questionList.size()-1;
                else currentQuestionIndex+=1;
                updateQuestion();
                break;
            case R.id.true_button:
                checkAnswer(true);
                updateQuestion();
                break;
            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                break;

        }
    }

    private void checkAnswer(boolean userChoose) {
        boolean actualAnswer=questionList.get(currentQuestionIndex).isAnswerTrue();
        int toastMessageId=0;
        if(userChoose==actualAnswer){
            addPoints();
            fadeView();
            toastMessageId=R.string.correct_answer;
        }else {
            deductPoints();
            shakeAnimation();
            toastMessageId=R.string.wrong_answer;
        }
        Toast.makeText(this,toastMessageId,Toast.LENGTH_SHORT).show();
    }

    private void addPoints() {
        scoreCounter+=100;
        score.setScore(scoreCounter);
        textViewScoreCounter.setText(String.valueOf(score.getScore()));
        Log.d("Score","update score "+score.getScore());
    }

    private void deductPoints(){
        scoreCounter-=100;
        if(scoreCounter<0) score.setScore(0);
        else score.setScore(scoreCounter);
        textViewScoreCounter.setText(String.valueOf(score.getScore()));
        Log.d("Score","score after deduction: "+score.getScore());
    }

    private void updateQuestion() {
        //Log.d("AsiuzzamanDebug","CheckQuestionList "+questionList);
        String question=questionList.get(currentQuestionIndex).getAnswer();
        questionText.setText(question);
        questionCounter.setText(currentQuestionIndex+"/"+questionList.size());
    }
    private void fadeView(){
        final CardView cardView=findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    private void shakeAnimation(){
        final CardView cardView=findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}