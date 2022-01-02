package com.networks.coffee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNewItem extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private static final String TAG = "ADD new item";

    //ADD item fields
    private String coffee_name;
    private String overview;
    private String price;
    private String photo_url;
    private String max_count;
    private Boolean alcoholic;
    private Boolean popular;
    private Boolean today;
    private Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        add_button=findViewById(R.id.add_button_id);
        add_button.setOnClickListener(this);

        View clickView = this.findViewById(R.id.back);
        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        NewItemInitialize();
        if (checkNewItem()) {
            switch (view.getId()) {
                case R.id.add_button_id:
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> item = new HashMap<>();
                    item.put("count", max_count);
                    item.put("name", coffee_name);
                    item.put("overview", overview);
                    item.put("alcoholic",alcoholic);
                    item.put("popular", popular);
                    item.put("price", price);
                    item.put("today", today);
                    item.put("url", photo_url);

                    db.collection("Items")
                            .add(item)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                    break;
            }
        }
    }

    public void NewItemInitialize(){
        coffee_name = ((TextView) findViewById(R.id.add_coffee_name_id)).getText().toString();
        overview = ((TextView) findViewById(R.id.add_overview_id)).getText().toString();
        price = ((TextView) findViewById(R.id.add_price_id)).getText().toString();
        max_count = ((TextView) findViewById(R.id.add_count_id)).getText().toString();
        photo_url=((TextView) findViewById(R.id.add_photo_id)).getText().toString();
        alcoholic=RadioGroupInitialize(R.id.add_alcoholic_radioGroup);
        popular = RadioGroupInitialize(R.id.popular_radioGroup);
        today = RadioGroupInitialize(R.id.today_radioGroup);

        if (TextUtils.isEmpty(coffee_name)) {
            Toast.makeText(this, "Enter Coffee name", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(overview)) {
            Toast.makeText(this, "Enter overview", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Enter price", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(max_count)) {
            Toast.makeText(this, "Enter max_count", Toast.LENGTH_LONG).show();
            return;
        }

    }


    public Boolean RadioGroupInitialize(int resource) {
        RadioGroup rg = (RadioGroup) findViewById(resource);
        String rb = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        Log.d(TAG, "RadioGroupInitialize: "+rb);
        if (rb.equals("Yes")){
            return true;
        }
        return false;
    }

    public Boolean checkNewItem(){
        if (!coffee_name.equals("")&&!overview.equals("")&&!price.equals("")
                &&!photo_url.equals("")&&!max_count.equals("")){
            return true;
        }

        return false;
    }


}