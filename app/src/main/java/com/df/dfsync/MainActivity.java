package com.df.dfsync;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import im.actor.sdk.ActorSDK;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActorSDK.sharedActor().waitForReady();
        ActorSDK.sharedActor().startMessagingApp(this);
        setContentView(R.layout.activity_login);
    }
}
