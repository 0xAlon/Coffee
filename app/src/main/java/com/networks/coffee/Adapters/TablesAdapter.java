package com.networks.coffee.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.networks.coffee.R;
import com.networks.coffee.Model.TableModel;

import java.util.List;

public class TablesAdapter extends SelectableAdapter<RecyclerView.ViewHolder> {

    private int myTable = -1;
    private String TAG = "TablesAdapter";
    boolean isSelected = false;

    private static class TableViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSeat;
        private final ImageView imgSeatSelected;

        public TableViewHolder(View itemView) {
            super(itemView);

            imgSeat = (ImageView) itemView.findViewById(R.id.img_seat);
            imgSeatSelected = (ImageView) itemView.findViewById(R.id.img_seat_selected);

        }
    }

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private List<TableModel> mItems;

    public TablesAdapter(Context context, List<TableModel> items) {
        //mOnSeatSelected = (OnSeatSelected) context;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mItems = items;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getPlaces();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.list_item_seat, parent, false);
        return new TableViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        TableViewHolder holder = (TableViewHolder) viewHolder;


        holder.imgSeatSelected.setSelected(isSelected);
        if (!mItems.get(position).getStatus()) {
            holder.imgSeatSelected.setSelected(true);
        }

        holder.imgSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myTable == position && mItems.get(position).getStatus() && ViewPagerAdapter.getTempItemPosition() == position) {
                    myTable = -1;
                    ViewPagerAdapter.setTempItemPosition(-1);
                    isSelected = false;
                    toggleSelection(position);
                } else if (myTable == -1 && mItems.get(position).getStatus() && ViewPagerAdapter.getTempItemPosition() == -1) {
                    myTable = position;
                    ViewPagerAdapter.setTempItemPosition(position);
                    isSelected = true;
                    toggleSelection(position);
                    ViewPagerAdapter.setTempItemPosition(position);

                } else if (!mItems.get(position).getStatus()) {
                    Toast.makeText(mContext, "table is occupied",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "you can choose only one table!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (mItems.get(position).getPlaces() == 4 && !holder.imgSeatSelected.isSelected()) {
            holder.imgSeat.setImageResource(R.drawable.visible4);
        } else if (mItems.get(position).getPlaces() == 2 && !holder.imgSeatSelected.isSelected()) {
            holder.imgSeat.setImageResource(R.drawable.visible2);
        } else if (mItems.get(position).getPlaces() == 2 && holder.imgSeatSelected.isSelected()) {
            holder.imgSeat.setImageResource(R.drawable.invisible2);
        } else if (mItems.get(position).getPlaces() == 4 && holder.imgSeatSelected.isSelected()) {
            holder.imgSeat.setImageResource(R.drawable.invisible4);
        }
    }

}
