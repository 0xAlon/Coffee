package com.networks.coffee;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditItem extends Fragment implements View.OnClickListener{

    String TAG="EditItem";

    private final FragmentManager fragmentManager;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    //
    private View binding;
    private View view;

    //
    ItemModel item;
    private String coffee_name;
    private String overview;
    private String price;
    private String photo_url;
    private String max_count;
    private Boolean popular;
    private Boolean today;
    private Boolean alcoholic;

    public EditItem(FragmentManager fragmentManager, ItemModel item) {
        this.fragmentManager = fragmentManager;
        this.item = item;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = inflater.inflate(R.layout.fragment_edit_item, container, false);
        view = binding.getRootView();
        DocumentReference docRef = db.collection("Items").document(item.getDocumentId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        ((EditText) view.findViewById(R.id.edit_coffee_name_id)).setText(document.getString("name"));
                        ((EditText) view.findViewById(R.id.edit_overview_id)).setText(document.getString("overview"));
                        ((EditText) view.findViewById(R.id.edit_price_id)).setText(document.getString("price"));
                        ((EditText) view.findViewById(R.id.edit_count_id)).setText(document.getString("count"));
                        ((EditText) view.findViewById(R.id.edit_photo_id)).setText(document.getString("url"));
                        //radio button
                        if (document.getBoolean("alcoholic")==true){
                            ((RadioGroup) view.findViewById(R.id.edit_alcoholic_radioGroup)).check(R.id.edit_alcoholic_id);
                        }
                        else{
                            ((RadioGroup) view.findViewById(R.id.edit_alcoholic_radioGroup)).check(R.id.edit_no_alcoholic_id);
                        }
                        if (document.getBoolean("popular")==true){
                            ((RadioGroup) view.findViewById(R.id.edit_popular_radioGroup)).check(R.id.edit_popular_id);
                        }
                        else{
                            ((RadioGroup) view.findViewById(R.id.edit_popular_radioGroup)).check(R.id.edit_no_popular_id);
                        }
                        if (document.getBoolean("today")==true){
                            ((RadioGroup) view.findViewById(R.id.edit_today_radioGroup)).check(R.id.edit_today_id);
                        }
                        else{
                            ((RadioGroup) view.findViewById(R.id.edit_today_radioGroup)).check(R.id.edit_no_today_id);
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        view.findViewById(R.id.edit_apply_button_id).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        InitializeItemsNewFields();
        if (checkNewItem()){
            db.collection("Items").document(item.getDocumentId())
                    .update(
                            "name",coffee_name,
                            "count",max_count,
                            "overview",overview,
                            "popular",popular,
                            "price",price,
                            "alcoholic",alcoholic,
                            "today",today,
                            "url",photo_url
                    );
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(getActivity(), "Some filed is valid", Toast.LENGTH_LONG).show();
        }
    }

    public void InitializeItemsNewFields(){

        coffee_name = ((TextView) view.findViewById(R.id.edit_coffee_name_id)).getText().toString();
        overview =((TextView) view.findViewById(R.id.edit_overview_id)).getText().toString();
        price = ((TextView) view.findViewById(R.id.edit_price_id)).getText().toString();
        max_count = ((TextView) view.findViewById(R.id.edit_count_id)).getText().toString();
        photo_url= ((TextView) view.findViewById(R.id.edit_photo_id)).getText().toString();
        alcoholic =RadioGroupInitialize(R.id.edit_alcoholic_radioGroup);
        popular = RadioGroupInitialize(R.id.edit_popular_radioGroup);
        today = RadioGroupInitialize(R.id.edit_today_radioGroup);


        if (TextUtils.isEmpty(coffee_name)) {
            Toast.makeText(getActivity(), "Enter Coffee name", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(overview)) {
            Toast.makeText(getActivity(), "Enter overview", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(price)) {
            Toast.makeText(getActivity(), "Enter price", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(max_count)) {
            Toast.makeText(getActivity(), "Enter max_count", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public Boolean RadioGroupInitialize(int resource) {
        RadioGroup rg = (RadioGroup) view.findViewById(resource);
        String rb = ((RadioButton) view.findViewById(rg.getCheckedRadioButtonId())).getText().toString();
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



