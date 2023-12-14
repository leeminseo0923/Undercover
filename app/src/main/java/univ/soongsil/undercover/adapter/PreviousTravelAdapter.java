package univ.soongsil.undercover.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import univ.soongsil.undercover.R;
import univ.soongsil.undercover.domain.Route;
import univ.soongsil.undercover.viewholder.TravelViewHolder;

public class PreviousTravelAdapter extends RecyclerView.Adapter<TravelViewHolder> {
    List<Route> routes;

    public PreviousTravelAdapter(List<Route> routes) {
        this.routes = routes;
    }

    @NonNull
    @Override
    public TravelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_travel_view, parent, false);
        return new TravelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelViewHolder holder, int position) {
        TextView regionText = holder.getRegionText();
        TextView dateText = holder.getDateText();

        Route route = routes.get(position);
        regionText.setText(route.getRegion());
        dateText.setText(route.getDate());
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }
}
