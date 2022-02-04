package com.firesafetysci.FireSci.Main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.firesafetysci.FireSci.R;

import java.util.List;

public class SystemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<System> dataSet;
    Context context;
    private final OnEditButtonPressedListener onEditClickListener;
    private final OnDeleteButtonPressedListener onDeleteClickListener;
    private final OnSystemClickListener onClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        TextView systemNameTextView;
        ImageView systemTypeImageView;
        private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
        private final TextView detailsTextView;
        private final TextView editTextView;
        private final TextView deleteTextView;
        private final SwipeRevealLayout swipeRevealLayout;

        public ViewHolder(View itemView, OnSystemClickListener clickListener) {
            super(itemView);
            this.systemNameTextView = itemView.findViewById(R.id.systemNameTextView);
            this.systemTypeImageView = itemView.findViewById(R.id.systemTypeImageView);
            this.detailsTextView = itemView.findViewById(R.id.detailsTextView);
            this.editTextView = itemView.findViewById(R.id.editTextView);
            this.deleteTextView = itemView.findViewById(R.id.deleteTextView);
            this.swipeRevealLayout = itemView.findViewById(R.id.swipeLayout);
        }
    }

    public SystemAdapter(List<System> data, Context context, OnEditButtonPressedListener onEditClickListener,
                         OnDeleteButtonPressedListener onDeleteClickListener, OnSystemClickListener onClickListener) {
        dataSet = data;
        this.context = context;
        this.onEditClickListener = onEditClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.system_row_layout,
                parent, false);
        return new ViewHolder(v, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int listPosition) {
        System system = dataSet.get(listPosition);

        ((ViewHolder) holder).viewBinderHelper.bind(((ViewHolder) holder).swipeRevealLayout, String.valueOf(system.getId()));
        ((ViewHolder) holder).viewBinderHelper.closeLayout(String.valueOf(system.getId()));

        ((ViewHolder) holder).systemNameTextView.setText(system.getDeviceName());
        String systemType = system.getSystemType();

        switch (systemType) {
            case "dar":
                ((ViewHolder) holder).systemTypeImageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.dar_icon));
                break;
            case "indi":
                ((ViewHolder) holder).systemTypeImageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.indi_icon));
                break;
            default:
                ((ViewHolder) holder).systemTypeImageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.default_icon));
                break;
        }
        ((ViewHolder) holder).detailsTextView.setOnClickListener(v -> {
            onClickListener.onItemClick(listPosition);
        });

        ((ViewHolder) holder).editTextView.setOnClickListener(v -> {
            onEditClickListener.onEditClick(listPosition);
        });

        ((ViewHolder) holder).deleteTextView.setOnClickListener(v -> {
            onDeleteClickListener.onDeleteClick(listPosition);
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void removeItem(int position) {
        dataSet.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnEditButtonPressedListener {
        void onEditClick(int position);
    }

    public interface OnDeleteButtonPressedListener {
        void onDeleteClick(int position);
    }

    public interface OnSystemClickListener {
        void onItemClick(int position);
    }
}
