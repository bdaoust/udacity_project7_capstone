package org.bdaoust.project7capstone.activities;

import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.bdaoust.project7capstone.R;

public class MainActivity extends BaseMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (isLargeLayout()) {
            AdView adView;
            AdRequest adRequest;

            MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

            adView = (AdView) findViewById(R.id.adView);
            adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
    }
}
