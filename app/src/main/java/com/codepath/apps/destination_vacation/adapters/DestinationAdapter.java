package com.codepath.apps.destination_vacation.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.apps.destination_vacation.R;
import com.codepath.apps.destination_vacation.fragments.InfoFragment;
import com.codepath.apps.destination_vacation.models.Destination;

import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {

    public static final String TAG = "DestinationAdapter";

    Context context;
    List<Destination> destinations;

    public DestinationAdapter(Context context, List<Destination> destinations) {
        this.context = context;
        this.destinations = destinations;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        // TODO make item_destination's layout look better
        View destinationView = LayoutInflater.from(context).inflate(R.layout.item_destination, parent, false);
        return new ViewHolder(destinationView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder" + position);
        // Get the destination at the passed in position
        Destination destination = destinations.get(position);
        // Bind the movie into the ViewHolder
        holder.bind(destination);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return destinations.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        destinations.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        String xid;
        String name;
        String categories;

        RelativeLayout container;
        TextView tvDestinationName;
        TextView tvDestinationCategories;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDestinationName = itemView.findViewById(R.id.tvDestinationName);
            tvDestinationCategories = itemView.findViewById(R.id.tvDestinationCategories);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(Destination destination) {
            xid = destination.getXid();
            name = destination.getName();
            categories = destination.getCategories();

            // If the destination objects was meant to just show the name, remove the tvDestinationCategories textView
            if (!destination.getJustName()) {
                tvDestinationName.setText("Name: " + name);
                tvDestinationCategories.setText("Categories: " + categories);
            }
            else {
                tvDestinationName.setText(name);
                tvDestinationCategories.setVisibility(View.GONE);
            }


            // 1. Register click listener on whole row
            container.setOnClickListener(v -> {
                // 2. Put destination's properties into bundle
                Bundle bundle = new Bundle();
                bundle.putString("xid", xid);
                bundle.putString("categories", categories);

                InfoFragment infoFragment = new InfoFragment();
                infoFragment.setArguments(bundle);

                // 3. Navigate to info fragment
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContainer, infoFragment)
                        .addToBackStack(null)
                        .commit();
            });
        }
    }
}
