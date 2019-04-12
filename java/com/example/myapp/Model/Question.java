package com.example.myapp.Model;

public class Question {

    private String Question, AnswerA, AnswerB, AnswerC, AnswerD, CorrectAnswer, LevelId, DifficultyId, IsImageQuestion, QuestionText;

    public Question() {
    }

    public Question(String question, String answerA, String answerB, String answerC, String answerD, String correctAnswer, String levelId, String difficultyId, String isImageQuestion, String questionText) {
        Question = question;
        AnswerA = answerA;
        AnswerB = answerB;
        AnswerC = answerC;
        AnswerD = answerD;
        CorrectAnswer = correctAnswer;
        LevelId = levelId;
        DifficultyId = difficultyId;
        IsImageQuestion = isImageQuestion;
        QuestionText = questionText;
    }

    public String getLevelId() {
        return LevelId;
    }

    public void setLevelId(String levelId) {
        LevelId = levelId;
    }

    public String getDifficultyId() {
        return DifficultyId;
    }

    public void setDifficultyId(String difficultyId) {
        DifficultyId = difficultyId;
    }

    public String getQuestionText() {
        return QuestionText;
    }

    public void setQuestionText(String questionText) {
        QuestionText = questionText;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswerA() {
        return AnswerA;
    }

    public void setAnswerA(String answerA) {
        AnswerA = answerA;
    }

    public String getAnswerB() {
        return AnswerB;
    }

    public void setAnswerB(String answerB) {
        AnswerB = answerB;
    }

    public String getAnswerC() {
        return AnswerC;
    }

    public void setAnswerC(String answerC) {
        AnswerC = answerC;
    }

    public String getAnswerD() {
        return AnswerD;
    }

    public void setAnswerD(String answerD) {
        AnswerD = answerD;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public String getIsImageQuestion() {
        return IsImageQuestion;
    }

    public void setIsImageQuestion(String isImageQuestion) {
        IsImageQuestion = isImageQuestion;
    }
}
