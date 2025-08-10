package com.appmux.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appmux.R;

import java.util.List;

public class ActionItemAdapter extends RecyclerView.Adapter<ActionItemAdapter.VH> {

    public interface OnActionClickListener {
        void onActionClicked(@NonNull String label);
    }

    private final List<String> items;
    private final OnActionClickListener listener;

    public ActionItemAdapter(@NonNull List<String> items, @NonNull OnActionClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_action_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        String label = items.get(position);
        holder.label.setText(label);
        holder.itemView.setOnClickListener(v -> listener.onActionClicked(label));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView label;
        VH(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.text);
        }
    }
}


