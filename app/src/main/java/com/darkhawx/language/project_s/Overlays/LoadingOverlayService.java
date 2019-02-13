package com.darkhawx.language.project_s.Overlays;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.darkhawx.language.project_s.API.ProjectSAPI;
import com.darkhawx.language.project_s.CardSet.Card;
import com.darkhawx.language.project_s.CardSet.CardSet;
import com.darkhawx.language.project_s.CardSet.QuestionType;
import com.darkhawx.language.project_s.ExternalIntegration.WaniKaniIntegration.WaniKaniSet;
import com.darkhawx.language.project_s.R;
import com.darkhawx.language.project_s.Tools;

public class LoadingOverlayService extends Service {

    private WindowManager mWindowManager;
    private View mLoadingOverlayView;

    private Card currentCard;
    private QuestionType questionType;

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



    public LoadingOverlayService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // Inflate the chat head layout we created
        mLoadingOverlayView = LayoutInflater.from(this).inflate(R.layout.view_loading_overlay, null);


        // Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        // Specify the chat head position
        // Initially view will be added to top-left corner
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;

        final LoadingOverlayService instance = this;

        // Setup Close Button
        Button button = (Button) mLoadingOverlayView.findViewById(R.id.closeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.onDestroy();
            }
        });

        // Obtain Card and Question Type
        WaniKaniSet set = ((WaniKaniSet)ProjectSAPI.getCardSet("WaniKani"));
        if (set != null) {
            currentCard = set.getReviewCardForOverlay();
            questionType = currentCard.getCardType().getRandomQuestionType();
            Card[] answerCards = set.getDisplayAnswers(currentCard, questionType);
            answerCard1 = answerCards[0];
            answerCard2 = answerCards[1];
            answerCard3 = answerCards[2];
            answerCard4 = answerCards[3];

            // Setup buttons
            answerButton1 = (Button) mLoadingOverlayView.findViewById(R.id.answer1);
            answerButton1.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    pressedAnswer(v, answerCard1);
                }
            });
            answerButton2 = (Button) mLoadingOverlayView.findViewById(R.id.answer2);
            answerButton2.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    pressedAnswer(v, answerCard2);
                }
            });
            answerButton3 = (Button) mLoadingOverlayView.findViewById(R.id.answer3);
            answerButton3.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    pressedAnswer(v, answerCard3);
                }
            });
            answerButton4 = (Button) mLoadingOverlayView.findViewById(R.id.answer4);
            answerButton4.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    pressedAnswer(v, answerCard4);
                }
            });

            // Setup Text
            displayedCardText = (TextView) mLoadingOverlayView.findViewById(R.id.displayedCard);
            questionText = (TextView) mLoadingOverlayView.findViewById(R.id.questionText);
            answerButton1.setText(answerCard1.getAnswerText(questionType));
            answerButton2.setText(answerCard2.getAnswerText(questionType));
            answerButton3.setText(answerCard3.getAnswerText(questionType));
            answerButton4.setText(answerCard4.getAnswerText(questionType));
            displayedCardText.setText(currentCard.getQuestionText(questionType));
            displayedCardText.setBackgroundColor(currentCard.getCardType().getCardColor());
            questionText.setText(currentCard.getQuestion(questionType));
        } else {
            Tools.exceptionToast(this, "Error Loading Overlay - No Set Found");
        }

        // Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mLoadingOverlayView, params);
    }

    public void pressedAnswer(View view, Card card) {
        if (card.equals(currentCard)) {
            // Go To New Card
            stopSelf();
        } else {
            view.setEnabled(false);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoadingOverlayView != null) mWindowManager.removeView(mLoadingOverlayView);
    }
}
