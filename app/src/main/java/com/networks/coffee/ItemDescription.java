package com.networks.coffee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ItemDescription extends Fragment implements View.OnClickListener{

    private FragmentManager fragmentManager;

    private View binding;
    public ImageView image;
    public TextView title;
    public TextView overview;
    public TextView price;
    public String userType;
    ItemModel item;
    private Button edit_button;
    private Button delete_button;
    private String TAG="ItemDescription";

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public ItemDescription(FragmentManager fragmentManager, ItemModel item, String userType) {
        this.fragmentManager = fragmentManager;
        this.item = item;
        this.userType=userType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = inflater.inflate(R.layout.fragment_item_description, container, false);
        View view = binding.getRootView();
        toggleInfo(view);
        image = view.findViewById(R.id.image);
        title = view.findViewById(R.id.item_name);
        price = view.findViewById(R.id.item_price);
        overview = view.findViewById(R.id.overview);

        Glide
                .with(this)
                .load("https://" + item.getUrl())
                .into(image);

        title.setText(item.getName());
        price.setText(item.getPrice() + "₪");
        overview.setText(item.getOverview());

        ImageButton btn = (ImageButton) view.findViewById(R.id.back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        return view;
    }



    public void toggleInfo(View view) {
        LinearLayout admin_option = view.findViewById(R.id.admin_layout_id);
        if (this.userType.equals("3")) {
            admin_option.setVisibility(LinearLayout.VISIBLE);
            this.edit_button=view.findViewById(R.id.edit_button_id);
            this.edit_button.setOnClickListener(this);
            this.delete_button=view.findViewById(R.id.delete_button_id);
            this.delete_button.setOnClickListener(this);
        }
        else{
            admin_option.setVisibility(LinearLayout.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_button_id:
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(android.R.id.content, new EditItem(fragmentManager,item))
                        .addToBackStack("main")
                        .commit();
                break;
            case R.id.delete_button_id:
                db.collection("Items").document(item.getDocumentId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                Intent intent = new Intent(view.getContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
                break;

        }
    }
}