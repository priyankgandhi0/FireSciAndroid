package com.firesafetysci.FireSci.Main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.firesafetysci.FireSci.R;

import java.util.List;

public class DeviceTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<String> dataSet;
    Context context;
    private final String selectedDeviceType;
    private final OnDeviceTypeClickListener onClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView deviceTypeImageView;
        private final ConstraintLayout constraintLayout;
        OnDeviceTypeClickListener clickListener;

        public ViewHolder(View itemView, OnDeviceTypeClickListener clickListener) {
            super(itemView);
            this.deviceTypeImageView = itemView.findViewById(R.id.deviceTypeImageView);
            this.constraintLayout = itemView.findViewById(R.id.deviceTypeConstraintLayout);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition());
        }
    }

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
                    ((ViewHolder) holder).constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.light_grey));

                }
                break;

            case "indi":
                ((ViewHolder) holder).deviceTypeImageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.indirect_fire_suppression));
                if (selectedDeviceType.equals("indi")) {
                    ((ViewHolder) holder).constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
                }
                break;

            case "wind":
                ((ViewHolder) holder).deviceTypeImageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.wind_fire_suppression));
                if (selectedDeviceType.equals("wind")) {
                    ((ViewHolder) holder).constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
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
}
