package com.example.myapp.Quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapp.Common;
import com.example.myapp.Model.Question;
import com.example.myapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PlayQuiz extends AppCompatActivity implements View.OnClickListener {


    int index = 0, score = 0, correct = 0, wrong = 0, thisQuestion = 0, totalQuestion, correctAnswer;

    FirebaseDatabase database;
    DatabaseReference questions;

    ImageView question_image, numline;
    Button btnA, btnB, btnC, btnD, info;
    TextView question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_quiz);

        //Firebase
        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        //Views
        question_text = (TextView)findViewById(R.id.question_text);
        question_image = (ImageView)findViewById(R.id.question_image);

        //Buttons
        info = (Button)findViewById(R.id.info);
        btnA = (Button)findViewById(R.id.answerA);
        btnB = (Button)findViewById(R.id.answerB);
        btnC = (Button)findViewById(R.id.answerC);
        btnD = (Button)findViewById(R.id.answerD);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoAlertDialog();
            }
        });

        //reverseTimer(30, timertxt);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);

    }

    /*private void reverseTimer(int seconds, final TextView tv) {
        new CountDownTimer(seconds * 1000 + 1000,1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tv.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                tv.setText("Completed");
                Intent myIntent = new Intent(PlayQuiz.this, FinishQuiz.class);
                myIntent.putExtra("total", String.valueOf(total));
                myIntent.putExtra("correct", String.valueOf(correct));
                myIntent.putExtra("incorrect", String.valueOf(wrong));
                startActivity(myIntent);
            }
        }.start();

    }*/

    private void infoAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlayQuiz.this);
        alertDialog.setTitle("Need some help?");

        LayoutInflater inflater = this.getLayoutInflater();
        View info = inflater.inflate(R.layout.info,null);

        numline = (ImageView)info.findViewById(R.id.numline);

        alertDialog.setView(info);
        alertDialog.setIcon(R.drawable.information);

        alertDialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }


    @Override
    public void onClick(View view) {

        if(index < totalQuestion) {
            Button clickedButton = (Button)view;
            if(clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer())) {
                score+=10;
                correctAnswer++;
                showQuestion(++index);
            }
            else {
                //Chose Wrong Answer
                Intent intent = new Intent(this, FinishQuiz.class);
                Bundle dataSend = new Bundle();
                dataSend.putInt("Score", score);
                dataSend.putInt("Total", totalQuestion);
                dataSend.putInt("Correct", correctAnswer);
                intent.putExtras(dataSend);
                startActivity(intent);
                finish();
            }

        }

    }

    private void showQuestion(int index) {

        if(index < totalQuestion) {

            thisQuestion++;

            //Checks if question is an image question
            if(Common.questionList.get(index).getIsImageQuestion().equals("true")) {
                Picasso.with(getBaseContext())
                        .load(Common.questionList.get(index).getQuestion())
                        .into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setText(Common.questionList.get(index).getQuestionText());
            }
            else {
                question_image.setVisibility(View.INVISIBLE);
            }

            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());
        }
        else {
            Intent intent = new Intent(this, FinishQuiz.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("Score", score);
            dataSend.putInt("Total", totalQuestion);
            dataSend.putInt("Correct", correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Common.questionList.size();
        showQuestion(++index);
    }

    /*private void updateQuestion() {
        total++;
        if(total > 4) {
            Intent intent = new Intent(PlayQuiz.this, FinishQuiz.class);
            intent.putExtra("total", String.valueOf(total));
            intent.putExtra("correct", String.valueOf(correct));
            intent.putExtra("incorrect", String.valueOf(wrong));
            startActivity(intent);
        }
        else {
            questions = FirebaseDatabase.getInstance().getReference().child("Questions").child(String.valueOf(total));
            questions.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final Question question = dataSnapshot.getValue(Question.class);

                    question_text.setText(question.getQuestionText());
                    if(Common.questionList.get(total).getIsImageQuestion().equals("true")) {
                        Picasso.with(getBaseContext())
                                .load(Common.questionList.get(total).getQuestion())
                                .into(question_image);
                        question_image.setVisibility(View.VISIBLE);
                    }
                    else {
                        question_image.setVisibility(View.INVISIBLE);
                    }

                    btnA.setText(question.getAnswerA());
                    btnB.setText(question.getAnswerB());
                    btnC.setText(question.getAnswerC());
                    btnD.setText(question.getAnswerD());

                    btnA.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(btnA.getText().toString().equals(question.getCorrectAnswer())) {

                                btnA.setBackgroundColor(Color.GREEN);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correct++;
                                        btnA.setBackgroundColor(Color.parseColor("#03A9F4"));

                                        updateQuestion();
                                    }
                                },1500);
                            }
                            else {
                                //Answer is wrong....finds correct answer and turns it green

                                wrong++;
                                btnA.setBackgroundColor(Color.RED);

                                if(btnB.getText().toString().equals(question.getCorrectAnswer())) {
                                    btnB.setBackgroundColor(Color.GREEN);
                                } else if (btnC.getText().toString().equals(question.getCorrectAnswer())) {
                                    btnC.setBackgroundColor(Color.GREEN);
                                } else if (btnD.getText().toString().equals(question.getCorrectAnswer())) {
                                    btnD.setBackgroundColor(Color.GREEN);
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnA.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        btnB.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        btnC.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        btnD.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        updateQuestion();
                                    }
                                },1500);
                            }
                        }
                    });

                    btnB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(btnB.getText().toString().equals(question.getCorrectAnswer())) {

                                btnB.setBackgroundColor(Color.GREEN);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correct++;
                                        btnB.setBackgroundColor(Color.parseColor("#03A9F4"));

                                        updateQuestion();
                                    }
                                },1500);
                            }
                            else {
                                //Answer is wrong....finds correct answer and turns it green

                                wrong++;
                                btnB.setBackgroundColor(Color.RED);

                                if(btnA.getText().toString().equals(question.getCorrectAnswer())) {
                                    btnA.setBackgroundColor(Color.GREEN);
                                } else if (btnC.getText().toString().equals(question.getCorrectAnswer())) {
                                    btnC.setBackgroundColor(Color.GREEN);
                                } else if (btnD.getText().toString().equals(question.getCorrectAnswer())) {
                                    btnD.setBackgroundColor(Color.GREEN);
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnA.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        btnB.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        btnC.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        btnD.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        updateQuestion();
                                    }
                                },1500);
                            }
                        }
                    });

                    btnC.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(btnC.getText().toString().equals(question.getCorrectAnswer())) {

                                btnC.setBackgroundColor(Color.GREEN);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correct++;
                                        btnC.setBackgroundColor(Color.parseColor("#03A9F4"));

                                        updateQuestion();
                                    }
                                },1500);
                            }
                            else {
                                //Answer is wrong....finds correct answer and turns it green

                                wrong++;
                                btnC.setBackgroundColor(Color.RED);

                                if(btnA.getText().toString().equals(question.getCorrectAnswer())) {
                                    btnA.setBackgroundColor(Color.GREEN);
                                } else if (btnB.getText().toString().equals(question.getCorrectAnswer())) {
                                    btnB.setBackgroundColor(Color.GREEN);
                                } else if (btnD.getText().toString().equals(question.getCorrectAnswer())) {
                                    btnD.setBackgroundColor(Color.GREEN);
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnA.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        btnB.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        btnC.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        btnD.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        updateQuestion();
                                    }
                                },1500);
                            }
                        }
                    });

                    btnD.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(btnD.getText().toString().equals(question.getCorrectAnswer())) {

                                btnD.setBackgroundColor(Color.GREEN);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correct++;
                                        btnD.setBackgroundColor(Color.parseColor("#03A9F4"));

                                        updateQuestion();
                                    }
                                },1500);
                            }
                            else {
                                //Answer is wrong....finds correct answer and turns it green

                                wrong++;
                                btnD.setBackgroundColor(Color.RED);

                                if(btnA.getText().toString().equals(question.getCorrectAnswer())) {
                                    btnA.setBackgroundColor(Color.GREEN);
                                } else if (btnB.getText().toString().equals(question.getCorrectAnswer())) {
                                    btnB.setBackgroundColor(Color.GREEN);
                                } else if (btnC.getText().toString().equals(question.getCorrectAnswer())) {
                                    btnC.setBackgroundColor(Color.GREEN);
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnA.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        btnB.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        btnC.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        btnD.setBackgroundColor(Color.parseColor("#03A9F4"));
                                        updateQuestion();
                                    }
                                },1500);
                            }
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }*/



}
