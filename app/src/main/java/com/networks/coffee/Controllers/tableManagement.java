package com.networks.coffee.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.networks.coffee.Adapters.ViewPagerAdapter;
import com.networks.coffee.R;
import com.networks.coffee.Resources.BaseActivity;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.google.android.material.tabs.TabLayout;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class tableManagement extends BaseActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    SharedPreferences sharedPref;

    private String TAG = "tableManagement";
    String userType;
    FloatingActionButton fb;

    // config object for our client ID and the environment (sandbox)
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("AYRMUtOZh23B1ujVeLVziVwBMMi2vHS97DD-6FzUB_wuwCAwA1irzT8u9AZZpq9e5Gk9Paqh_QpbpR9e");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(R.layout.activity_table_management, frameLayout);


        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras.containsKey("userType")) {
            userType = i.getStringExtra("userType");
        }

        fb = rootView.findViewById(R.id.fabButton);
        if (userType.equals("3")) {
            fb.setVisibility(LinearLayout.VISIBLE);
        }

    }

    public void onClickBt(View view) {
        Intent intent = new Intent(this, adminTableManagement.class);
        intent.putExtra("userType",userType);
        startActivity(intent);
    }


    public void button(View view) {

        int total_payment = sharedPref.getInt("total", 0);

        Intent serviceConfig = new Intent(this, PayPalService.class);
        serviceConfig.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(serviceConfig);
        PayPalPayment payment = new PayPalPayment(new BigDecimal(total_payment), "ILS", "Coffee Shop",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
       
        intent.putExtra("userType", userType);
        startActivity(intent);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, 0);

    }


    // Note: PayPal account is not verified so the payments will not be done on my side.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(
                    PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i(TAG, confirm.toJSONObject().toString(4));
                    // TODO: send 'confirm' to your server for verification
                } catch (JSONException e) {
                    Log.e(TAG, "no confirmation data: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i(TAG, "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i(TAG, "Invalid payment / config set");
        }
        // TODO Create thanks page

    }

}