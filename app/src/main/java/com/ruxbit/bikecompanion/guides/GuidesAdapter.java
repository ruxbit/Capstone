package com.ruxbit.bikecompanion.guides;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ruxbit.bikecompanion.R;
import com.ruxbit.bikecompanion.model.Guide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuidesAdapter extends RecyclerView.Adapter<GuidesAdapter.GuidesAdapterViewHolder> {
    private List<Guide> guides;
    private Context context;

    private final GuidesAdapterOnClickHandler clickHandler;

    public interface GuidesAdapterOnClickHandler {
        void onClick(Guide guide);
    }

    public GuidesAdapter(GuidesAdapterOnClickHandler clickHandler, Context context) {
        this.clickHandler = clickHandler;
        this.context = context;
    }

    public class GuidesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_lig_pic) ImageView ivGuide;
        @BindView(R.id.tv_lig_name) TextView tvName;

        public GuidesAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Guide guide = guides.get(adapterPosition);
            clickHandler.onClick(guide);
        }
    }

    @Override
    public GuidesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.list_item_guide;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new GuidesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuidesAdapterViewHolder guidesAdapterViewHolder, int position) {
        Guide guide = guides.get(position);

        Glide.with(context).load(guide.getImageUri()).centerCrop().placeholder(R.drawable.youtube_placeholder).into(guidesAdapterViewHolder.ivGuide);
        guidesAdapterViewHolder.tvName.setText(guide.getName());
    }

    @Override
    public int getItemCount() {
        if (guides == null) {
            return 0;
        } else {
            return guides.size();
        }
    }

    public void setGuidesData(List<Guide> guides) {
        this.guides = guides;
        notifyDataSetChanged();
    }
}