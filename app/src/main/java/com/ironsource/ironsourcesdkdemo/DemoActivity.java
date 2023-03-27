package com.ironsource.ironsourcesdkdemo;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.ironsource.adapters.supersonicads.SupersonicConfig;
import com.ironsource.adqualitysdk.sdk.ISAdQualityConfig;
import com.ironsource.adqualitysdk.sdk.ISAdQualityInitError;
import com.ironsource.adqualitysdk.sdk.ISAdQualityInitListener;
import com.ironsource.adqualitysdk.sdk.ISAdQualityLogLevel;
import com.ironsource.adqualitysdk.sdk.IronSourceAdQuality;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.impressionData.ImpressionData;
import com.ironsource.mediationsdk.impressionData.ImpressionDataListener;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.LevelPlayBannerListener;
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener;
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoListener;
import com.ironsource.mediationsdk.sdk.OfferwallListener;
import com.ironsource.mediationsdk.utils.IronSourceUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DemoActivity extends Activity implements LevelPlayRewardedVideoListener, OfferwallListener, ImpressionDataListener {

    private final String TAG = "DemoActivity";
    private final String APP_KEY = "85460dcd";
//    private final String APP_KEY = "17a70d3c5";
//    private final String APP_KEY = "90a24db5";
    private final String AQ_USER_ID = "86421357";
    private Button mVideoButton;
    private Button mOfferwallButton;
    private Button mInterstitialLoadButton;
    private Button mInterstitialShowButton;
    private static final int  a = 904;

    private static final int b = 856;
    private Placement mPlacement;

    private FrameLayout mBannerParentLayout;
    private IronSourceBannerLayout mIronSourceBannerLayout;
    private Button isTestSuite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);


        Map<String, String> rvParams = new HashMap<>();
        rvParams.put("ip", "123.4.56.78");
        IronSource.setRewardedVideoServerParameters(rvParams);

        //The integrationHelper is used to validate the integration. Remove the integrationHelper before going live!
        IntegrationHelper.validateIntegration(this);


//        PendingIntent pendingIntent;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            pendingIntent = PendingIntent.getActivity(this, 0, getIntent(), PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
//        }else{
//            pendingIntent = PendingIntent.getActivity(this,
//                    0, getIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
//
//        }

        ISAdQualityConfig.Builder builder = new ISAdQualityConfig.Builder().setAdQualityInitListener(new ISAdQualityInitListener() {

            @Override
            public void adQualitySdkInitSuccess() {
                Log.d("AdQualityInitListener", "adQualitySdkInitSuccess");
            }

            @Override
            public void adQualitySdkInitFailed(ISAdQualityInitError error, String message) {
                Log.d("AdQualityInitListener", "adQualitySdkInitFailed " + error + " message: " + message);
            }
        });
        builder.setTestMode(true);
        builder.setLogLevel(ISAdQualityLogLevel.VERBOSE);
//        builder.setUserId(AQ_USER_ID);
        ISAdQualityConfig adQualityConfig = builder.build();

        IronSourceAdQuality.getInstance().initialize(this, APP_KEY, adQualityConfig);


        IronSource.setAdaptersDebug(true);


        //Network Connectivity Status
        IronSource.shouldTrackNetworkState(this, true);
        initUIElements();
        startIronSourceInitTask();
        IronSource.getAdvertiserId(this);



    }

    private void startIronSourceInitTask() {
        String advertisingId = IronSource.getAdvertiserId(DemoActivity.this);
        // we're using an advertisingId as the 'userId'
        initIronSource(APP_KEY, advertisingId);

    }

    private void initIronSource(String appKey, String userId) {
        // Be sure to set a listener to each product that is being initiated
        // set the IronSource rewarded video listener
        // set the IronSource offerwall listener
        IronSource.setOfferwallListener(this);
        // set client side callbacks for the offerwall
        SupersonicConfig.getConfigObj().setClientSideCallbacks(true);
        // set the interstitial listener
//        IronSource.setInterstitialListener(this);
        IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
            @Override
            public void onAdReady(AdInfo adInfo) {
                handleInterstitialShowButtonState(true);
            }

            @Override
            public void onAdLoadFailed(IronSourceError ironSourceError) {
                handleInterstitialShowButtonState(false);
            }

            @Override
            public void onAdOpened(AdInfo adInfo) {
                // called when the video has started
                Log.d(TAG, "onAdOpened ----- " + adInfo.toString());
            }

            @Override
            public void onAdShowSucceeded(AdInfo adInfo) {
                // called when the video has started
                Log.d(TAG, "onAdShowSucceeded ----- " + adInfo.toString());
            }

            @Override
            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {

            }

            @Override
            public void onAdClicked(AdInfo adInfo) {

            }

            @Override
            public void onAdClosed(AdInfo adInfo) {

            }
        });

        // add the Impression Data listener
        IronSource.addImpressionDataListener(this);
        IronSource.setLevelPlayRewardedVideoListener(this);

        Log.d("TheUserID","userId ===== "+userId);
        // set the IronSource user id
        IronSource.setUserId(userId);
        // init the IronSource SDK
        IronSource.init(this, appKey);
//        IronSource.setManualLoadRewardedVideo(this);
//
//        IronSource.loadRewardedVideo();

//        updateButtonsState();


        // In order to work with IronSourceBanners you need to add Providers who support banner ad unit and uncomment next line
//         createAndloadBanner();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // call the IronSource onResume method
        IronSource.onResume(this);
        updateButtonsState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // call the IronSource onPause method
        IronSource.onPause(this);
        updateButtonsState();
    }

    /**
     * Handle the button state according to the status of the IronSource producs
     */
    private void updateButtonsState() {
        handleVideoButtonState(IronSource.isRewardedVideoAvailable());
        handleOfferwallButtonState(IronSource.isOfferwallAvailable());
        handleLoadInterstitialButtonState(true);
        handleInterstitialShowButtonState(false);
    }

    /**
     * initialize the UI elements of the activity
     */
    private void initUIElements() {
        mVideoButton = (Button) findViewById(R.id.rv_button);
        mVideoButton.setOnClickListener(view -> {
            // check if video is available
            if (IronSource.isRewardedVideoAvailable()){
                IronSource.showRewardedVideo();

            }else{
                IronSource.loadRewardedVideo();
            }
        });

        mOfferwallButton = (Button) findViewById(R.id.ow_button);
        mOfferwallButton.setOnClickListener(view -> {
            //show the offerwall
            if (IronSource.isOfferwallAvailable())
                IronSource.showOfferwall();
        });

        mInterstitialLoadButton = (Button) findViewById(R.id.is_button_1);
        mInterstitialLoadButton.setOnClickListener(view -> IronSource.loadInterstitial());


        mInterstitialShowButton = (Button) findViewById(R.id.is_button_2);
        mInterstitialShowButton.setOnClickListener(view -> {
            // check if interstitial is available
            if (IronSource.isInterstitialReady()) {
                //show the interstitial
                IronSource.showInterstitial();
            }
        });

        TextView versionTV = (TextView) findViewById(R.id.version_txt);
        versionTV.setText(String.format("%s %s", getResources().getString(R.string.version), IronSourceUtils.getSDKVersion()));

        mBannerParentLayout = (FrameLayout) findViewById(R.id.banner_footer);

        isTestSuite = findViewById(R.id.is_test_suite);
        isTestSuite.setOnClickListener(v -> IronSource.launchTestSuite(DemoActivity.this));
    }


    /**
     * Creates and loads IronSource Banner
     */
    private void createAndloadBanner() {
        // choose banner size
        ISBannerSize size = ISBannerSize.BANNER;

        // instantiate IronSourceBanner object, using the IronSource.createBanner API
        mIronSourceBannerLayout = IronSource.createBanner(this, size);

        // add IronSourceBanner to your container
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        mBannerParentLayout.addView(mIronSourceBannerLayout, 0, layoutParams);

        if (mIronSourceBannerLayout != null) {
            // set the banner listener
            mIronSourceBannerLayout.setLevelPlayBannerListener(new LevelPlayBannerListener() {
                @Override
                public void onAdLoaded(AdInfo adInfo) {
                    Log.d(TAG, "onBannerAdLoaded");
                    // since banner container was "gone" by default, we need to make it visible as soon as the banner is ready
                    mBannerParentLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdLoadFailed(IronSourceError ironSourceError) {
                    Log.d(TAG, "onBannerAdLoadFailed" + " " + ironSourceError);

                }

                @Override
                public void onAdClicked(AdInfo adInfo) {
                    Log.d(TAG, "onBannerAdClicked");

                }

                @Override
                public void onAdLeftApplication(AdInfo adInfo) {
                    Log.d(TAG, "onBannerAdLeftApplication");

                }

                @Override
                public void onAdScreenPresented(AdInfo adInfo) {
                    Log.d(TAG, "onBannerAdScreenPresented");

                }

                @Override
                public void onAdScreenDismissed(AdInfo adInfo) {
                    Log.d(TAG, "onBannerAdScreenDismissed");
                }
            });

            // load ad into the created banner
            IronSource.loadBanner(mIronSourceBannerLayout);
        } else {
            Toast.makeText(DemoActivity.this, "IronSource.createBanner returned null", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Destroys IronSource Banner and removes it from the container
     */
    private void destroyAndDetachBanner() {
        IronSource.destroyBanner(mIronSourceBannerLayout);
        if (mBannerParentLayout != null) {
            mBannerParentLayout.removeView(mIronSourceBannerLayout);
        }
    }

    /**
     * Set the Rewareded Video button state according to the product's state
     *
     * @param available if the video is available
     */
    public void handleVideoButtonState(final boolean available) {
        final String text;
        final int color;
        if (available) {
            color = Color.BLUE;
            text = getResources().getString(R.string.show) + " " + getResources().getString(R.string.rv);
        } else {
            color = Color.BLACK;
            text = getResources().getString(R.string.initializing) + " " + getResources().getString(R.string.rv);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mVideoButton.setTextColor(color);
                mVideoButton.setText(text);
                mVideoButton.setEnabled(available);

            }
        });
    }

    /**
     * Set the Rewareded Video button state according to the product's state
     *
     * @param available if the offerwall is available
     */
    public void handleOfferwallButtonState(final boolean available) {
        final String text;
        final int color;
        if (available) {
            color = Color.BLUE;
            text = getResources().getString(R.string.show) + " " + getResources().getString(R.string.ow);
        } else {
            color = Color.BLACK;
            text = getResources().getString(R.string.initializing) + " " + getResources().getString(R.string.ow);
        }
        runOnUiThread(() -> {
            mOfferwallButton.setTextColor(color);
            mOfferwallButton.setText(text);
            mOfferwallButton.setEnabled(available);

        });

    }

    /**
     * Set the Interstitial button state according to the product's state
     *
     * @param available if the interstitial is available
     */
    public void handleLoadInterstitialButtonState(final boolean available) {
        Log.d(TAG, "handleInterstitialButtonState | available: " + available);
        final String text;
        final int color;
        if (available) {
            color = Color.BLUE;
            text = getResources().getString(R.string.load) + " " + getResources().getString(R.string.is);
        } else {
            color = Color.BLACK;
            text = getResources().getString(R.string.initializing) + " " + getResources().getString(R.string.is);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mInterstitialLoadButton.setTextColor(color);
                mInterstitialLoadButton.setText(text);
                mInterstitialLoadButton.setEnabled(available);
            }
        });

    }

    /**
     * Set the Show Interstitial button state according to the product's state
     *
     * @param available if the interstitial is available
     */
    public void handleInterstitialShowButtonState(final boolean available) {
        final int color;
        if (available) {
            color = Color.BLUE;
        } else {
            color = Color.BLACK;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mInterstitialShowButton.setTextColor(color);
                mInterstitialShowButton.setEnabled(available);
            }
        });
    }




    // --------- IronSource Offerwall Listener ---------

    @Override
    public void onOfferwallAvailable(boolean available) {
        handleOfferwallButtonState(available);
    }

    @Override
    public void onOfferwallOpened() {
        // called when the offerwall has opened
        Log.d(TAG, "onOfferwallOpened");
    }

    @Override
    public void onOfferwallShowFailed(IronSourceError ironSourceError) {
        // called when the offerwall failed to show
        // you can get the error data by accessing the IronSourceError object
        ironSourceError.getErrorCode();
        ironSourceError.getErrorMessage();
        Log.d(TAG, "onOfferwallShowFailed" + " " + ironSourceError);
    }

    @Override
    public boolean onOfferwallAdCredited(int credits, int totalCredits, boolean totalCreditsFlag) {
        Log.d(TAG, "onOfferwallAdCredited" + " credits:" + credits + " totalCredits:" + totalCredits + " totalCreditsFlag:" + totalCreditsFlag);
        return false;
    }

    @Override
    public void onGetOfferwallCreditsFailed(IronSourceError ironSourceError) {
        // you can get the error data by accessing the IronSourceError object
        // IronSourceError.getErrorCode();
        // IronSourceError.getErrorMessage();
        Log.d(TAG, "onGetOfferwallCreditsFailed" + " " + ironSourceError);
    }

    @Override
    public void onOfferwallClosed() {
        // called when the offerwall has closed
        Log.d(TAG, "onOfferwallClosed");
    }


    // --------- Impression Data Listener ---------
    @Override
    public void onImpressionSuccess(ImpressionData impressionData) {
        // The onImpressionSuccess will be reported when the rewarded video and interstitial ad is opened.
        // For banners, the impression is reported on load success.
        if (impressionData != null) {
            Locale defaultLocal = Locale.getDefault();


            Locale.setDefault(Locale.CHINA);
//            CultureInfo.DefaultThreadCurrentCulture = CultureInfo.InvariantCulture;
            Log.d(TAG, "onImpressionSuccess ---" + impressionData);
            Locale.setDefault(defaultLocal);
        }
    }

    public void showRewardDialog(Placement placement) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DemoActivity.this);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setTitle(getResources().getString(R.string.rewarded_dialog_header));
        builder.setMessage(getResources().getString(R.string.rewarded_dialog_message) + " " + placement.getRewardAmount() + " " + placement.getRewardName());
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public void onAdAvailable(AdInfo adInfo) {
        Log.d(TAG, Thread.currentThread().getStackTrace()[1].getMethodName() + adInfo.toString());
        if (isRewardVideo(adInfo)){
            handleVideoButtonState(true);
        }else {
            handleInterstitialShowButtonState(true);
        }
    }

    @Override
    public void onAdUnavailable() {
        handleVideoButtonState(false);
    }

    @Override
    public void onAdOpened(AdInfo adInfo) {

    }

    @Override
    public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {

    }

    @Override
    public void onAdClicked(Placement placement, AdInfo adInfo) {

    }

    @Override
    public void onAdRewarded(Placement placement, AdInfo adInfo) {
        if (isRewardVideo(adInfo)) {
            mPlacement = placement;
        }

    }

    @Override
    public void onAdClosed(AdInfo adInfo) {

    }

    private boolean isRewardVideo(AdInfo adInfo){
        return adInfo.getAdUnit().equals("rewardedVideo");
    }
}
