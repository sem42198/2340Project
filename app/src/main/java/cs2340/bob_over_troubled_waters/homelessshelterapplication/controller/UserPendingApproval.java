package cs2340.bob_over_troubled_waters.homelessshelterapplication.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cs2340.bob_over_troubled_waters.homelessshelterapplication.R;

public class UserPendingApproval extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pending_approval);
    }

    public void logoutButtonAction(View view) {
        finish();
    }
}