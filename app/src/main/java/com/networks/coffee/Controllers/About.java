package com.networks.coffee.Controllers;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;

import com.networks.coffee.R;
import com.networks.coffee.Resources.BaseActivity;

public class About extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(R.layout.activity_about, frameLayout);

        View clickView = rootView.findViewById(R.id.nav);
        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.LEFT);
            }
        });
    }
}