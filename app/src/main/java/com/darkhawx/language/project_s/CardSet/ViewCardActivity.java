package com.darkhawx.language.project_s.CardSet;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.darkhawx.language.project_s.R;
import com.darkhawx.language.project_s.Reviews.ReviewSetActivity;
import com.darkhawx.language.project_s.Reviews.ReviewStats;

public class ViewCardActivity extends AppCompatActivity {

    public static final String CARD_MESSAGE = "com.darkhawx.language.project_s.card.CARD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card);

        Intent intent = getIntent();
        Card card = (Card) intent.getSerializableExtra(CARD_MESSAGE);

        displayCard(card);
    }

    protected void displayCard(Card card) {
        ((TextView)findViewById(R.id.cardName)).setText(card.getDisplayText());
    }

//    @Override
//    public void onBackPressed() {
//        NavUtils.navigateUpFromSameTask(this);
//    }
}
