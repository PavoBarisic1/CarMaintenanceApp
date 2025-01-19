package ba.sum.fsre.carmaintenanceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    private List<Vehicle> vehicleList;
    private OnItemClickListener listener;

    public VehicleAdapter(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public interface OnItemClickListener {
        void onItemClick(String licensePlate);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle vehicle = vehicleList.get(position);
        holder.bind(vehicle);
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    class VehicleViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewLicensePlate;
        private final TextView textViewModel;

        private final TextView textViewMileage;

        private final TextView textViewModelYear;


        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLicensePlate = itemView.findViewById(R.id.license_plate);
            textViewModel = itemView.findViewById(R.id.model);
            textViewMileage = itemView.findViewById(R.id.mileage);
            textViewModelYear = itemView.findViewById(R.id.model_year);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(vehicleList.get(getAdapterPosition()).getLicensePlate());
                }
            });
        }

        public void bind(Vehicle vehicle) {
            textViewLicensePlate.setText(vehicle.getLicensePlate());
            textViewModel.setText(vehicle.getManufacturerAndModel());
            textViewMileage.setText(vehicle.getMileage());
            textViewModelYear.setText(vehicle.getModelYear());
        }
    }
}