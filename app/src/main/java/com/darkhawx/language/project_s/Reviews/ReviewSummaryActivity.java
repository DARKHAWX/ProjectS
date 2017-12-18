package com.darkhawx.language.project_s.Reviews;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.darkhawx.language.project_s.CardSet.Card;
import com.darkhawx.language.project_s.CardSet.ViewCardActivity;
import com.darkhawx.language.project_s.R;
import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

import static com.darkhawx.language.project_s.CardSet.ViewCardActivity.CARD_MESSAGE;

public class ReviewSummaryActivity extends AppCompatActivity {

    FlexboxLayout correctListView;
    FlexboxLayout incorrectListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_summary);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        // Obtain stats and card name
        ReviewStats stats = (ReviewStats) intent.getSerializableExtra(ReviewSetActivity.SUMMARY_MESSAGE);
        String cardSetName = intent.getStringExtra(ReviewSetActivity.CARD_SET_NAME_MESSAGE);

        // Setup flexboxes
        correctListView = (FlexboxLayout) findViewById(R.id.correctList);
        incorrectListView = (FlexboxLayout) findViewById(R.id.incorrectList);

        correctListView.setFlexDirection(FlexDirection.ROW);
        correctListView.setFlexWrap(FlexWrap.WRAP);
        correctListView.setJustifyContent(JustifyContent.FLEX_START);
        correctListView.setAlignItems(AlignItems.FLEX_START);
        correctListView.setAlignContent(AlignContent.FLEX_START);
        incorrectListView.setFlexDirection(FlexDirection.ROW);
        incorrectListView.setFlexWrap(FlexWrap.WRAP);
        incorrectListView.setJustifyContent(JustifyContent.FLEX_START);
        incorrectListView.setAlignItems(AlignItems.FLEX_START);
        incorrectListView.setAlignContent(AlignContent.FLEX_START);

        // Display stats
        displayStats(stats, cardSetName);
    }

    protected void displayStats(ReviewStats stats, String cardSetName) {
        ((TextView)findViewById(R.id.numCorrectText)).setText(getString(R.string.review_summary_num_correct, stats.numCorrectCards));
        ((TextView)findViewById(R.id.numIncorrectText)).setText(getString(R.string.review_summary_num_incorrect, stats.numIncorrectCards));
        ((TextView)findViewById(R.id.numTotalAttempted)).setText(getString(R.string.review_summary_total, stats.numCards));
        ((TextView)findViewById(R.id.percentageCorrect)).setText(getString(R.string.review_summary_per_correct, stats.percentage));
        ((TextView)findViewById(R.id.cardSetName)).setText(cardSetName);

        for (int i = 0; i < stats.correctCards.size(); i++) {
            Button button = new Button(this);
            Card card = stats.correctCards.get(i);
            button.setText(card.getDisplayText());
            button.setOnClickListener(new ShowCardOnClickListener(card) {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ReviewSummaryActivity.this, ViewCardActivity.class);
                    intent.putExtra(ViewCardActivity.CARD_MESSAGE, this.card);
                    startActivity(intent);
                }
            });

            correctListView.addView(button);
        }

        for (int i = 0; i < stats.incorrectCards.size(); i++) {
            Button button = new Button(this);
            Card card = stats.incorrectCards.get(i);
            button.setText(card.getDisplayText());
            button.setOnClickListener(new ShowCardOnClickListener(card) {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ReviewSummaryActivity.this, ViewCardActivity.class);
                    intent.putExtra(ViewCardActivity.CARD_MESSAGE, this.card);
                    startActivity(intent);
                }
            });

            incorrectListView.addView(button);
        }
    }
}

abstract class ShowCardOnClickListener implements View.OnClickListener {

    Card card;

    ShowCardOnClickListener(Card card) {
        this.card = card;
    }
}