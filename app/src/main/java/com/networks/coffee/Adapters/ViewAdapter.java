package com.networks.coffee.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.networks.coffee.Model.ItemModel;
import com.networks.coffee.Interfaces.OnItemClickListener;
import com.networks.coffee.R;

import java.util.ArrayList;
import java.util.List;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> implements Filterable {

    private String TAG = "ViewAdapter";

    private final OnItemClickListener listener;

    private List<ItemModel> items;
    private List<ItemModel> temp;
    private List<ItemModel> filteredItems;

    private LayoutInflater mLayoutInflater;

    private Context context;

    private SharedPreferences.Editor PrefsEditor;
    private SharedPreferences sharedPreferences;


    public ViewAdapter(final List<ItemModel> items,
                       Context context, OnItemClickListener listener) {
        this.items = items;
        this.temp = items;
        filteredItems = new ArrayList<>(items);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        PrefsEditor = sharedPreferences.edit();


        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    public List<ItemModel> getItems() {
        return items;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(items.get(position));

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(holder.count.getText().toString()) < Integer.parseInt(items.get(holder.getAdapterPosition()).getCount())) {
                    holder.count.setText(String.valueOf((Integer.parseInt(holder.count.getText().toString()) + 1)));
                    holder.total.setText(String.valueOf(Integer.parseInt(holder.total.getText().toString()) + Integer.parseInt(items.get(holder.getAdapterPosition()).getPrice())));


                    PrefsEditor.putInt("total", Integer.parseInt(holder.total.getText().toString())).apply();


                    items.get(holder.getAdapterPosition()).setTotal_count(items.get(holder.getAdapterPosition()).getTotal_count() + 1);
                } else {
                    Toast.makeText(context, "Maximum units", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(holder.count.getText().toString()) > 0) {
                    holder.count.setText(String.valueOf((Integer.parseInt(holder.count.getText().toString()) - 1)));
                    holder.total.setText(String.valueOf(Integer.parseInt(holder.total.getText().toString()) - Integer.parseInt(items.get(holder.getAdapterPosition()).getPrice())));

                    PrefsEditor.putInt("total", Integer.parseInt(holder.total.getText().toString())).apply();


                    items.get(holder.getAdapterPosition()).setTotal_count(items.get(holder.getAdapterPosition()).getTotal_count() - 1);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public Filter getFilter(int type) {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                boolean dataUpdated = true;
                if (type == 1) {
                    filteredItems = filterToday();

                } else {
                    filteredItems = filterPopular();
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = dataUpdated;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                boolean dataUpdated = Boolean.parseBoolean(filterResults.values.toString());
                if (dataUpdated) {
                    temp = new ArrayList<>(items);
                    items.clear();
                    items.addAll(filteredItems);
                    notifyDataSetChanged();
                }
            }
        };
    }

    public void swapData() {
        items = temp;
    }

    private List<ItemModel> filterToday() {
        List<ItemModel> results = new ArrayList<>();
        for (ItemModel item : items) {
            if (null != item.getToday() && item.getToday()) {
                results.add(item);
            }
        }
        return results;
    }

    private List<ItemModel> filterPopular() {
        List<ItemModel> results = new ArrayList<>();
        for (ItemModel item : items) {
            if (null != item.getPopular() && item.getPopular()) {
                results.add(item);
            }
        }
        return results;
    }

    @Override
    public Filter getFilter() { //default
        return null;
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

        public void bind(ItemModel itemModel) {
            Glide
                    .with(context)
                    .load("https://" + itemModel.getUrl())
                    .into(image);

            title.setText(itemModel.getName());
            price.setText(itemModel.getPrice() + "₪");
            overview.setText(itemModel.getOverview());
            count.setText(String.valueOf(itemModel.getTotal_count()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(itemModel);
                }
            });

        }
    }
}