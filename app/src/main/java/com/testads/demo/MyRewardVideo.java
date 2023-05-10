package com.testads.demo;

import android.app.Activity;

import com.ironsource.mediationsdk.adunit.adapter.BaseAdapter;
import com.ironsource.mediationsdk.adunit.adapter.BaseRewardedVideo;
import com.ironsource.mediationsdk.adunit.adapter.listener.RewardedVideoAdListener;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdData;
import com.ironsource.mediationsdk.model.NetworkSettings;

public class MyRewardVideo extends BaseRewardedVideo<BaseAdapter>
{
    public MyRewardVideo(NetworkSettings networkSettings) {
        super(networkSettings);
    }

    @Override
    public void showAd(AdData adData, RewardedVideoAdListener listener) {

    }

    @Override
    public boolean isAdAvailable(AdData adData) {
        return false;
    }

    @Override
    public void loadAd(AdData adData, Activity activity, RewardedVideoAdListener rewardedVideoAdListener) {

    }
}
