package com.networks.coffee;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class ItemDescription extends Fragment {

    private FragmentManager fragmentManager;

    private View binding;

    public ImageView image;

    public TextView title;
    public TextView overview;
    public TextView price;

    ItemModel item;

    public ItemDescription(FragmentManager fragmentManager, ItemModel item, String userType) {
        this.fragmentManager = fragmentManager;
        this.item = item;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = inflater.inflate(R.layout.fragment_item_description, container, false);
        View view = binding.getRootView();

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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}