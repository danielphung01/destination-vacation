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
        Log.d("DestinationAdapter", "onCreateViewHolder");
        // TODO make item_destination's layout look better
        View destinationView = LayoutInflater.from(context).inflate(R.layout.item_destination, parent, false);
        return new ViewHolder(destinationView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("DestinationAdapter", "onBindViewHolder" + position);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

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
            name = destination.getName();
            categories = destination.getCategories();
            tvDestinationName.setText("Name: " + name);
            tvDestinationCategories.setText("Categories: " + categories);

            // 1. Register click listener on whole row
            container.setOnClickListener(v -> {
                // 2. Put destination's properties into Bundle
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
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
