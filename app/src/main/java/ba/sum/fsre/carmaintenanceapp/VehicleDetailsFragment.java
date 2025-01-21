package ba.sum.fsre.carmaintenanceapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class VehicleDetailsFragment extends Fragment implements ServiceAdapter.OnServiceActionListener {

    private static final String ARG_LICENSE_PLATE = "license_plate";

    private ImageView vehicleImageView;
    private TextView vehicleNameTextView, vehicleTypeView, licensePlateTextView, mileageTextView, fuelTypeTextView, modelYearTextView;
    private Button addServiceButton;

    private RecyclerView recyclerViewServices;
    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList = new ArrayList<>();

    public static VehicleDetailsFragment newInstance(String licensePlate) {
        VehicleDetailsFragment fragment = new VehicleDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LICENSE_PLATE, licensePlate);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle_details, container, false);

        vehicleImageView = view.findViewById(R.id.vehicleImage);
        vehicleNameTextView = view.findViewById(R.id.vehicleName);
        licensePlateTextView = view.findViewById(R.id.licensePlate);
        mileageTextView = view.findViewById(R.id.mileage);
        fuelTypeTextView = view.findViewById(R.id.fuelType);
        modelYearTextView = view.findViewById(R.id.modelYear);
        vehicleTypeView = view.findViewById(R.id.vehicle_type);
        addServiceButton = view.findViewById(R.id.addServiceButton);

        recyclerViewServices = view.findViewById(R.id.recyclerViewServices);
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(getContext()));

        serviceAdapter = new ServiceAdapter(serviceList, this);
        recyclerViewServices.setAdapter(serviceAdapter);

        ImageView backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        if (getArguments() != null) {
            String licensePlate = getArguments().getString(ARG_LICENSE_PLATE);
            fetchVehicleDetails(licensePlate);
            fetchServices(licensePlate);
        }

        ImageView editButton = view.findViewById(R.id.edit_button);
        editButton.setOnClickListener(v -> navigateToEditDetailsFragment());

        addServiceButton.setOnClickListener(v -> {
            String licensePlate = licensePlateTextView.getText().toString();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            AddServiceFragment addServiceFragment = AddServiceFragment.newInstance(licensePlate);
            transaction.replace(R.id.fragment_container, addServiceFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        ImageView deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());

        return view;
    }

    private void fetchVehicleDetails(String licensePlate) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("vehicles").document(licensePlate).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Vehicle vehicle = documentSnapshot.toObject(Vehicle.class);
                        if (vehicle != null) {
                            populateVehicleDetails(vehicle);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });
    }

    private void fetchServices(String licensePlate) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("ServiceQuery", "Fetching services for vehicle with license plate: " + licensePlate);

        db.collection("vehicles").document(licensePlate).collection("services")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        Log.d("ServiceData", "Number of documents fetched: " + queryDocumentSnapshots.size());
                        serviceList.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Log.d("ServiceData", "Document data: " + documentSnapshot.getData());
                            Service service = documentSnapshot.toObject(Service.class);
                            if (service != null) {
                                serviceList.add(service);
                            }
                        }
                        serviceAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("ServiceData", "No documents found in the 'services' collection for vehicle.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ServiceData", "Error fetching services: " + e.getMessage());
                });
    }

    private void populateVehicleDetails(Vehicle vehicle) {
        vehicleNameTextView.setText(vehicle.getManufacturer() + " " + vehicle.getModel());
        licensePlateTextView.setText(vehicle.getLicensePlate());
        mileageTextView.setText(String.valueOf(vehicle.getMileage()));
        fuelTypeTextView.setText(vehicle.getFuelType());
        modelYearTextView.setText(vehicle.getModelYear());
        vehicleTypeView.setText(vehicle.getVehicleType());
    }

    private void navigateToEditDetailsFragment() {
        if (getActivity() != null) {
            EditDetailsFragment editFragment = EditDetailsFragment.newInstance(licensePlateTextView.getText().toString());
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, editFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void deleteVehicle() {
        String licensePlate = licensePlateTextView.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("vehicles").document(licensePlate).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Vehicle deleted", Toast.LENGTH_SHORT).show();
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to delete vehicle", Toast.LENGTH_SHORT).show();
                });
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage("Jeste li sigurni da želite izbrisati vozilo?")
                .setCancelable(false)
                .setPositiveButton("Da", (dialog, id) -> deleteVehicle())
                .setNegativeButton("Ne", (dialog, id) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onEditService(Service service) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        EditServiceFragment editFragment = EditServiceFragment.newInstance(service);
        transaction.replace(R.id.fragment_container, editFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDeleteService(Service service) {
        new AlertDialog.Builder(getContext())
                .setMessage("Jeste li sigurni da želite izbrisati servis?")
                .setPositiveButton("Da", (dialog, which) -> deleteService(service))
                .setNegativeButton("Ne", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteService(Service service) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("vehicles").document(licensePlateTextView.getText().toString())
                .collection("services").document(service.getServiceDate())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        db.collection("vehicles").document(licensePlateTextView.getText().toString())
                                .collection("services").document(service.getServiceDate())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    serviceList.remove(service);
                                    serviceAdapter.notifyDataSetChanged();
                                    Toast.makeText(getContext(), "Servis obrisan", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FirestoreDelete", "Neuspješno brisanje servisa", e);
                                    Toast.makeText(getContext(), "Neuspješno brisanje servisa", Toast.LENGTH_SHORT).show();
                                });
                    }
                });




    }
}
