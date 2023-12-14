package univ.soongsil.undercover.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import univ.soongsil.undercover.R;

public class TravelViewHolder extends RecyclerView.ViewHolder {
    TextView regionText;
    TextView dateText;
    LinearLayout mainFrame;
    public TravelViewHolder(@NonNull View itemView) {
        super(itemView);
        regionText = itemView.findViewById(R.id.regionText);
        dateText = itemView.findViewById(R.id.dateText);
        mainFrame = (LinearLayout) itemView;
    }

    public TextView getRegionText() {
        return regionText;
    }

    public TextView getDateText() {
        return dateText;
    }

    public LinearLayout getMainFrame() {
        return mainFrame;
    }
}
