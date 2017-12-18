package com.darkhawx.language.project_s.Reviews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.darkhawx.language.project_s.CardSet.Card;
import com.darkhawx.language.project_s.CardSet.CardSet;
import com.darkhawx.language.project_s.CardSet.QuestionType;
import com.darkhawx.language.project_s.MainActivity;
import com.darkhawx.language.project_s.R;

public class ReviewSetActivity extends AppCompatActivity {

    public static final String SUMMARY_MESSAGE = "com.darkhawx.language.project_s.review.SUMMARY";
    public static final String CARD_SET_NAME_MESSAGE = "com.darkhawx.language.project_s.review.CARD_SET_NAME";

    public ReviewStats stats;

    public CardSet currentSet;
    public Card currentCard;
    public QuestionType questionType;
    public int complete;
    public int correct;
    private boolean currentlyCorrect;

    private Card answerCard1;
    private Card answerCard2;
    private Card answerCard3;
    private Card answerCard4;

    private Button answerButton1;
    private Button answerButton2;
    private Button answerButton3;
    private Button answerButton4;

    private TextView displayedCardText;
    private TextView questionText;
    private TextView itemsLeftText;
    private TextView itemsCompleteText;
    private TextView percentageCorrectText;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_set);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        currentSet = (CardSet)intent.getSerializableExtra(MainActivity.SET_ID_MESSAGE);

        if (currentSet != null) {
            SetupQuestion();
        } else {
            Log.d(ReviewSetActivity.class.getSimpleName(), "No Set Found");
                    //TODO Open main activity
        }
    }

    public void SetupQuestion() {
        stats = new ReviewStats();

        if (currentSet != null) {
            // Initialise
            currentlyCorrect = true;
            complete = 0;
            correct = 0;
            currentCard = currentSet.next();
            System.out.println(currentCard.getDisplayText());
            questionType = currentCard.getCardType().getRandomQuestionType();
            Card[] answerCards = currentSet.getDisplayAnswers(currentCard, questionType);
            answerCard1 = answerCards[0];
            answerCard2 = answerCards[1];
            answerCard3 = answerCards[2];
            answerCard4 = answerCards[3];

            answerButton1 = (Button) findViewById(R.id.answer1);
            answerButton1.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    pressedAnswer(v, answerCard1);
                }
            });
            answerButton2 = (Button) findViewById(R.id.answer2);
            answerButton2.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    pressedAnswer(v, answerCard2);
                }
            });
            answerButton3 = (Button) findViewById(R.id.answer3);
            answerButton3.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    pressedAnswer(v, answerCard3);
                }
            });
            answerButton4 = (Button) findViewById(R.id.answer4);
            answerButton4.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    pressedAnswer(v, answerCard4);
                }
            });

            displayedCardText = (TextView) findViewById(R.id.displayedCard);
            questionText = (TextView) findViewById(R.id.questionText);
            itemsLeftText = (TextView) findViewById(R.id.itemsLeft);
            itemsCompleteText = (TextView) findViewById(R.id.itemsComplete);
            percentageCorrectText = (TextView) findViewById(R.id.percentageCorrect);

            progressBar = (ProgressBar) findViewById(R.id.mainProgressBar);

            updateDisplay();
        }
    }

    public void finish() {
        Intent intent = new Intent(this, ReviewSummaryActivity.class);
        intent.putExtra(SUMMARY_MESSAGE, stats);
        intent.putExtra(CARD_SET_NAME_MESSAGE, currentSet.getDisplayName());
        startActivity(intent);
    }

    public void updateDisplay() {
        answerButton1.setText(answerCard1.getAnswerText(questionType));
        answerButton2.setText(answerCard2.getAnswerText(questionType));
        answerButton3.setText(answerCard3.getAnswerText(questionType));
        answerButton4.setText(answerCard4.getAnswerText(questionType));
        answerButton1.setEnabled(true);
        answerButton2.setEnabled(true);
        answerButton3.setEnabled(true);
        answerButton4.setEnabled(true);

        displayedCardText.setText(currentCard.getQuestionText(questionType));
        displayedCardText.setBackgroundColor(currentCard.getCardType().getCardColor());
        questionText.setText(currentCard.getQuestion(questionType));
        itemsLeftText.setText(String.valueOf(currentSet.numCards()-complete));
        itemsCompleteText.setText(String.valueOf(complete));
        if (complete == 0) {
            percentageCorrectText.setText(String.valueOf(0));
            progressBar.setProgress(0);
        } else {
            percentageCorrectText.setText(String.valueOf((int)Math.ceil(100.0F * (float) correct / (float) complete)));
            progressBar.setProgress((int)Math.ceil(100.0F * (float)complete / currentSet.numCards()));
        }
    }

    public void pressedAnswer(View view, Card card) {
        if (card.equals(currentCard)) {
            // Go To New Card
            gotoNextCard();
        } else {
            view.setEnabled(false);
            currentlyCorrect = false;
        }
    }

    public void gotoNextCard() {
        if (currentlyCorrect) {
            correct++;
            stats.correct(currentCard);
        } else {
            stats.incorrect(currentCard);
        }

        complete++;
        currentlyCorrect = true;

        if (complete >= currentSet.numCards()) {
            // Finish
            finish();
        } else {
            currentCard = currentSet.next();
            questionType = currentCard.getCardType().getRandomQuestionType();
            Card[] answerCards = currentSet.getDisplayAnswers(currentCard, questionType);
            answerCard1 = answerCards[0];
            answerCard2 = answerCards[1];
            answerCard3 = answerCards[2];
            answerCard4 = answerCards[3];

            updateDisplay();
        }
    }
}
