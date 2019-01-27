package tmsva.org.free.planificate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import tmsva.org.free.planificate.R;
import tmsva.org.free.planificate.data.network.Arrival;

public class QueryServicesAdapter extends RecyclerView.Adapter<QueryServicesAdapter.ViewHolder> {
    private Context mContext;
    private List<Arrival> mArrivals;

    public QueryServicesAdapter( Context context, List<Arrival> arrivals) {
        this.mContext = context;
        this.mArrivals = arrivals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.arrival_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        final Arrival arrival = mArrivals.get(pos);

        h.txtService.setText(arrival.getRouteId());
        h.txtPlate.setText(arrival.getBusPlateNumber());
        h.txtEta.setText(arrival.getArrivalEstimation());
        h.txtDistance.setText(arrival.getBusDistance());
    }

    @Override
    public int getItemCount() {
        return mArrivals == null ? 0 : mArrivals.size();
    }

    public void updateArrivals(List<Arrival> arrivals) {
        if(mArrivals != null) {
            mArrivals.clear();
            mArrivals.addAll(arrivals);
        } else {
            mArrivals = arrivals;
        }
        notifyDataSetChanged();
    }

    public void clear() {
        int listSize = mArrivals.size();
        mArrivals.clear();
        notifyItemRangeRemoved(0, listSize);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView txtService, txtPlate, txtEta, txtDistance;
        ViewHolder(@NonNull View v) {
            super(v);
            txtService = v.findViewById(R.id.txt_service_header);
            txtPlate = v.findViewById(R.id.txt_bus_plate_header);
            txtEta = v.findViewById(R.id.txt_eta_header);
            txtDistance = v.findViewById(R.id.txt_distance_header);
        }
    }
}
