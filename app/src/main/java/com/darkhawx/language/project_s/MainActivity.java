package com.darkhawx.language.project_s;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.darkhawx.language.project_s.API.ProjectSAPI;
import com.darkhawx.language.project_s.CardSet.CardSet;
import com.darkhawx.language.project_s.Reviews.ReviewSetActivity;
import com.darkhawx.language.project_s.Test.DisplayMessageActivity;
import com.darkhawx.language.project_s.ExternalIntegration.WaniKaniIntegration.WaniKaniAPI;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.darkhawx.language.project_s.MESSAGE";
    public static final String SET_ID_MESSAGE = "com.darkhawx.language.project_s.SET_ID";

    public static final String DARKHAWX_API_V1_KEY = "09246629ea90a014b6c99bf66399f5c8";
    public static final String DARKHAWX_API_V2_KEY = "9a9305d7-302d-4f76-afe7-11a23b948c7f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProjectSAPI.addTestSets();
        ProjectSConfig.LoadConfig(this);
        try {
            ((TextView) findViewById(R.id.waniKaniKeyText)).setText(getString(R.string.wanikani_api_key, ProjectSConfig.WANIKANI_ACCOUNT.getWaniKaniAPIKey_V2()));
        } catch (Exception e){
            ((TextView) findViewById(R.id.waniKaniKeyText)).setText(getString(R.string.wanikani_api_key, "Null"));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        ProjectSConfig.SaveConfig(this);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void setWaniKani(View view) {
        ProjectSConfig.WANIKANI_ACCOUNT.setWaniKaniAPIKey_V2(((EditText)findViewById(R.id.waniKaniKeyTextArea)).getText().toString());
        ProjectSConfig.SaveConfig(this);
        ((TextView)findViewById(R.id.waniKaniKeyText)).setText(getString(R.string.wanikani_api_key, ProjectSConfig.WANIKANI_ACCOUNT.getWaniKaniAPIKey_V2()));
    }

    public void testSet(View view) {
        Intent intent = new Intent(this, ReviewSetActivity.class);
        intent.putExtra(SET_ID_MESSAGE,  ProjectSAPI.getCardSet("test"));
        startActivity(intent);
    }

    public void waniKaniSet(View view) {
        CardSet waniKani = ProjectSConfig.WANIKANI_ACCOUNT.getReviewSet();
        ProjectSConfig.SaveConfig(this);
        Intent intent = new Intent(this, ReviewSetActivity.class);
        intent.putExtra(SET_ID_MESSAGE, waniKani);
        startActivity(intent);
    }
}
