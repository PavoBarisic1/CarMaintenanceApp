package ba.sum.fsre.carmaintenanceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private List<Service> serviceList;
    private OnServiceActionListener actionListener;

    public ServiceAdapter(List<Service> serviceList, OnServiceActionListener actionListener) {
        this.serviceList = serviceList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.tvServiceType.setText(service.getServiceType());
        holder.tvServiceDate.setText(service.getServiceDate());
        holder.tvMileage.setText(service.getMileage());
        holder.tvCost.setText(service.getCost());
        holder.tvNotes.setText(service.getNotes());

        holder.btnEdit.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onEditService(service);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDeleteService(service);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceType, tvServiceDate, tvMileage, tvCost, tvNotes;
        ImageView btnEdit, btnDelete;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            tvServiceType = itemView.findViewById(R.id.tvServiceType);
            tvServiceDate = itemView.findViewById(R.id.tvServiceDate);
            tvMileage = itemView.findViewById(R.id.tvMileage);
            tvCost = itemView.findViewById(R.id.tvCost);
            tvNotes = itemView.findViewById(R.id.tvNotes);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface OnServiceActionListener {
        void onEditService(Service service);
        void onDeleteService(Service service);
    }
}
