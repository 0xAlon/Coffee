package com.networks.coffee;

import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class tableManagement extends BaseActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private String TAG = "tableManagement";
    String userType;
    FloatingActionButton fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(R.layout.activity_table_management, frameLayout);

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
        startActivity(intent);
    }


    public void button(View view) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("userType", userType);
        startActivity(intent);
    }

}