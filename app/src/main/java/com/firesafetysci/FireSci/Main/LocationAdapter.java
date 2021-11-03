package com.firesafetysci.FireSci.Main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firesafetysci.FireSci.R;

import java.util.List;
import java.util.Locale;

public class LocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<Location> dataSet;
    Context context;
    private final OnLocationClickListener onClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView companyNameTextView, addressTextView, cityStateZipcodeTextView, countryTextView;
        OnLocationClickListener clickListener;

        public ViewHolder(View itemView, OnLocationClickListener clickListener) {
            super(itemView);
            this.companyNameTextView = itemView.findViewById(R.id.companyNameLocationRow);
            addressTextView = itemView.findViewById(R.id.addressLocationRow);
            cityStateZipcodeTextView = itemView.findViewById(R.id.cityStateZipcodeTextViewRow);
            countryTextView = itemView.findViewById(R.id.countryTextViewRow);

            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition());
        }
    }


    public LocationAdapter(List<Location> data, Context context, OnLocationClickListener onClickListener) {
        dataSet = data;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    public static Location getLocationAt(int position) {
        return dataSet.get(position);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_row_layout,
                parent, false);
        return new ViewHolder(v, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int listPosition) {
        Location location = dataSet.get(listPosition);

        ((ViewHolder) holder).companyNameTextView.setText(location.getCompanyName());
        ((ViewHolder) holder).addressTextView.setText(location.getAddress());
        ((ViewHolder) holder).cityStateZipcodeTextView.setText(String.format(Locale.US, "%s, %s %s",
                location.getCity(), location.getStateProvince(), location.getZipcode()));
        ((ViewHolder) holder).countryTextView.setText(location.getCountry());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface OnLocationClickListener {
        void onItemClick(int position);
    }

}
