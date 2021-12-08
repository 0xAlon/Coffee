package com.networks.coffee;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    private final List<ItemModel> items;
    private LayoutInflater mLayoutInflater;
    private Context context;
    private String TAG = "ViewAdapter";

    public ViewAdapter(List<ItemModel> items,
                       Context context) {
        this.items = items;
        mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(items.get(position));
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.count.setText(String.valueOf((Integer.parseInt(holder.count.getText().toString()) + 1)));
                holder.total.setText(String.valueOf(Integer.parseInt(holder.total.getText().toString()) + Integer.parseInt(items.get(holder.getAdapterPosition()).getPrice())));
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(holder.count.getText().toString()) > 0) {
                    holder.count.setText(String.valueOf((Integer.parseInt(holder.count.getText().toString()) - 1)));
                    holder.total.setText(String.valueOf(Integer.parseInt(holder.total.getText().toString()) - Integer.parseInt(items.get(holder.getAdapterPosition()).getPrice())));

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView image;
        public final TextView title;
        public final TextView overview;
        public final ImageButton add;
        public final ImageButton remove;
        public final TextView count;
        public final TextView total;
        public final TextView price;


        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.item_name);
            price = view.findViewById(R.id.item_price);
            overview = view.findViewById(R.id.overview);
            add = view.findViewById(R.id.add);
            remove = view.findViewById(R.id.remove);
            count = view.findViewById(R.id.count);
            total = ((Activity) context).findViewById(R.id.total);

        }

        public void bind(ItemModel movieModel) {
            Glide
                    .with(context)
                    .load("https://" + movieModel.getUrl())
                    .into(image);

            title.setText(movieModel.getName());
            price.setText(movieModel.getPrice()+"₪");
            overview.setText(movieModel.getOverview());
        }
    }
}