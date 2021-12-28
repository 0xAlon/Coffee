package com.networks.coffee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminTableAdapter extends RecyclerView.Adapter<AdminTableAdapter.ViewHolder> {

    private String TAG = "AdminTableAdapter";

    private final OnTableClickListener listener;

    private List<TableModel> tables;

    private LayoutInflater mLayoutInflater;

    private Context context;

    public AdminTableAdapter(final List<TableModel> tables,
                             Context context, OnTableClickListener listener) {
        this.tables = tables;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.table_rows, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.bind(tables.get(position));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO DELETE FROM THE TABLE FROM DATABASE
            }
        });
    }


    @Override
    public int getItemCount() {
        return tables.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public final Button delete;
        public final TextView places;
        public final TextView type;


        public ViewHolder(View view) {
            super(view);
            delete = view.findViewById(R.id.delete);
            type = view.findViewById(R.id.tableType);
            places = view.findViewById(R.id.tablePlace);
        }

        public void bind(TableModel tableModel) {
            places.setText(String.valueOf(tableModel.getPlaces()));
            type.setText(tableModel.getType());

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTableClick(tableModel);
                }
            });

        }
    }
}
