package com.example.myapp.Quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapp.Common;
import com.example.myapp.Model.QuestionScore;
import com.example.myapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FinishQuiz extends AppCompatActivity {

    Button playAgain;
    TextView txtResultScore, getTextResultQuestion;

    FirebaseDatabase database;
    DatabaseReference question_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_quiz);

        playAgain = (Button) findViewById(R.id.playAgain);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        txtResultScore = (TextView)findViewById(R.id.txtTotalScore);
        getTextResultQuestion = (TextView)findViewById(R.id.txtTotalQuestion);

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishQuiz.this, Start.class);
                startActivity(intent);
                finish();
            }
        });

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            txtResultScore.setText(String.format("SCORE : %d",score));
            getTextResultQuestion.setText(String.format("PASSED : %d / %d", correctAnswer, totalQuestion));

            question_score.child(String.format("%s_%s", Common.currentUser.getName(),Common.levelId))
                                    .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getName(),Common.levelId),
                                            Common.currentUser.getName(),
                                            String.valueOf(score)));
        }
        /*t1 = (TextView)findViewById(R.id.textView6);
        t2 = (TextView)findViewById(R.id.textView7);
        t3 = (TextView)findViewById(R.id.textView8);

        Intent i = getIntent();

        String questions = i.getStringExtra("total");
        String correct = i.getStringExtra("correct");
        String wrong = i.getStringExtra("incorrect");

        t1.setText(questions);
        t2.setText(correct);
        t3.setText(wrong);*/

    }
}
