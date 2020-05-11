package com.df.dfsync;

import im.actor.sdk.ActorSDK;
import im.actor.sdk.ActorSDKApplication;
import im.actor.sdk.ActorStyle;

public class MessageApplication extends ActorSDKApplication {
    @Override
    public void onConfigureActorSDK() {
        // ActorSDK.sharedActor().setEndpoints(new String[]{"YOUR_ENDPOINT_HERE"});
        ActorStyle style = ActorSDK.sharedActor().style;
        // Customize your colors here
    }
}
