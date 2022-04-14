package com.firesafetysci.FireSci.Main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firesafetysci.FireSci.R;

import java.util.List;

public class DeviceTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<String> dataSet;
    private final String selectedDeviceType;
    private final OnDeviceTypeClickListener onClickListener;
    Context context;

    public DeviceTypeAdapter(List<String> data, String selectedDeviceType, Context context, OnDeviceTypeClickListener onClickListener) {
        dataSet = data;
        this.context = context;
        this.selectedDeviceType = selectedDeviceType;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_type_layout,
                parent, false);
        return new ViewHolder(v, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int listPosition) {
        String deviceType = dataSet.get(listPosition);

        switch (deviceType) {
            case "dar":
                ((ViewHolder) holder).deviceTypeImageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.direct_fire_suppression));
                if (selectedDeviceType.equals("dar")) {
                    ((ViewHolder) holder).chevronRightImageView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_checked));
                    ((ViewHolder) holder).viewBg.setBackground(ContextCompat.getDrawable(context, R.drawable.system_row_bg));
                }
                break;

            case "indi":
                ((ViewHolder) holder).deviceTypeImageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.indirect_fire_suppression));
                if (selectedDeviceType.equals("indi")) {
                    ((ViewHolder) holder).chevronRightImageView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_checked));
                    ((ViewHolder) holder).viewBg.setBackground(ContextCompat.getDrawable(context, R.drawable.system_row_bg));
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface OnDeviceTypeClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView deviceTypeImageView;
        private final ImageView chevronRightImageView;
        private final ConstraintLayout constraintLayout;
        private final View viewBg;
        OnDeviceTypeClickListener clickListener;

        public ViewHolder(View itemView, OnDeviceTypeClickListener clickListener) {
            super(itemView);
            this.deviceTypeImageView = itemView.findViewById(R.id.deviceTypeImageView);
            this.constraintLayout = itemView.findViewById(R.id.deviceTypeConstraintLayout);
            this.chevronRightImageView = itemView.findViewById(R.id.chevronRightImageView);
            this.viewBg = itemView.findViewById(R.id.viewBg);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition());
        }
    }
}
