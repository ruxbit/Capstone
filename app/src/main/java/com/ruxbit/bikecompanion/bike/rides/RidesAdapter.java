package com.ruxbit.bikecompanion.bike.rides;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ruxbit.bikecompanion.R;
import com.ruxbit.bikecompanion.model.Ride;

import java.text.DateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.RidesAdapterViewHolder> {
    private List<Ride> rides;

    private final RidesAdapter.RidesAdapterOnClickHandler clickHandler;
    private final Resources resources;

    public interface RidesAdapterOnClickHandler {
        void onClick(Ride ride);
    }

    public RidesAdapter(RidesAdapter.RidesAdapterOnClickHandler clickHandler, Resources resources) {
        this.clickHandler = clickHandler;
        this.resources = resources;
    }

    public class RidesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_lir_name) TextView tvName;
        @BindView(R.id.tv_lir_distance) TextView tvDistance;
        @BindView(R.id.tv_lir_moving_time) TextView tvMovingTime;
        @BindView(R.id.tv_lir_start_date) TextView tvStartDate;

        public RidesAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Ride ride = rides.get(adapterPosition);
            clickHandler.onClick(ride);
        }
    }

    @Override
    public RidesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.list_item_ride;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new RidesAdapter.RidesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RidesAdapterViewHolder ridesAdapterViewHolder, int position) {
        Ride ride = rides.get(position);

        int hours = ride.getMoving_time() / 3600;
        int minutes = (ride.getMoving_time() % 3600) / 60;
        int seconds = ride.getMoving_time() % 60;

        ridesAdapterViewHolder.tvName.setText(ride.getName());
        ridesAdapterViewHolder.tvDistance.setText(resources.getString(R.string.distance, ride.getDistance() / 1000));
        ridesAdapterViewHolder.tvMovingTime.setText(resources.getString(R.string.moving_time, hours, minutes, seconds));
        ridesAdapterViewHolder.tvStartDate.setText(resources.getString(R.string.start_time, DateFormat.getDateTimeInstance().format(ride.getStart_date())));
    }

    @Override
    public int getItemCount() {
        if (rides == null) {
            return 0;
        } else {
            return rides.size();
        }
    }

    public void setRidesData(List<Ride> rides) {
        this.rides = rides;
        notifyDataSetChanged();
    }
}
