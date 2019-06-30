package com.ruxbit.bikecompanion.bikes;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ruxbit.bikecompanion.model.Bike;
import com.ruxbit.bikecompanion.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BikesAdapter extends RecyclerView.Adapter<BikesAdapter.BikesAdapterViewHolder> {
    private List<Bike> bikes;
    private Resources resources;

    private final BikesAdapterOnClickHandler clickHandler;

    public interface BikesAdapterOnClickHandler {
        void onClick(Bike bike);
    }

    public BikesAdapter(BikesAdapterOnClickHandler clickHandler, Resources resources) {
        this.clickHandler = clickHandler;
        this.resources = resources;
    }

    public class BikesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_lib_bike) ImageView ivBike;
        @BindView(R.id.tv_lib_name) TextView tvName;
        @BindView(R.id.tv_lib_brand) TextView tvBrand;
        @BindView(R.id.tv_lib_model) TextView tvModel;

        public BikesAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Bike bike = bikes.get(adapterPosition);
            clickHandler.onClick(bike);
        }
    }

    @Override
    public BikesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.list_item_bike;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new BikesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BikesAdapterViewHolder bikesAdapterViewHolder, int position) {
        Bike bike = bikes.get(position);
        if (bike.getImage() == null) {
            bikesAdapterViewHolder.ivBike.setImageResource(R.drawable.ic_bike);
        } else {
            bikesAdapterViewHolder.ivBike.setImageBitmap(bike.getImage());
        }
        bikesAdapterViewHolder.tvBrand.setText(bike.getBrand_name());
        bikesAdapterViewHolder.tvModel.setText(bike.getModel_name());
        bikesAdapterViewHolder.tvName.setText(bike.getName());
    }

    @Override
    public int getItemCount() {
        if (bikes == null) {
            return 0;
        } else {
            return bikes.size();
        }
    }

    public void setBikesData(List<Bike> bikes) {
        this.bikes = bikes;
        notifyDataSetChanged();
    }
}

