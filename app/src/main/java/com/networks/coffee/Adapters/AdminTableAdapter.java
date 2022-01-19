package com.networks.coffee.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.networks.coffee.Interfaces.OnTableClickListener;
import com.networks.coffee.R;
import com.networks.coffee.Model.TableModel;

import java.util.List;

public class AdminTableAdapter extends RecyclerView.Adapter<AdminTableAdapter.ViewHolder> {

    private String TAG = "AdminTableAdapter";

    private final OnTableClickListener listener;

    private List<TableModel> tables;

    private LayoutInflater mLayoutInflater;

    private Context context;

    private FirebaseFirestore db;

    String key;

    TableModel table;

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
        db = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bind(tables.get(position));
        holder.delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO DELETE FROM THE TABLE FROM DATABASE
                String s = String.valueOf(tables.get(position).getDocumentId());
                db.collection("Table").document(s)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                Toast.makeText(context.getApplicationContext(), "Record Deleted", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(v.getContext(), AdminTableAdapter.class);
                                v.getContext().startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });


            }
        });
    }


    @Override
    public int getItemCount() {
        return tables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageButton delete;
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
