package ba.sum.fsre.carmaintenanceapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class EditDetailsFragment extends Fragment {

    private static final String ARG_LICENSE_PLATE = "license_plate";

    private EditText vehicleTypeEditText, manufacturerEditText, modelEditText, modelYearEditText, licensePlateEditText, fuelTypeEditText, mileageEditText;

    public static EditDetailsFragment newInstance(String licensePlate) {
        EditDetailsFragment fragment = new EditDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LICENSE_PLATE, licensePlate);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_details, container, false);

        vehicleTypeEditText = view.findViewById(R.id.vehicle_type);
        manufacturerEditText = view.findViewById(R.id.manufacturer);
        modelEditText = view.findViewById(R.id.model);
        modelYearEditText = view.findViewById(R.id.model_year);
        licensePlateEditText = view.findViewById(R.id.license_plate);
        fuelTypeEditText = view.findViewById(R.id.fuel_type);
        mileageEditText = view.findViewById(R.id.mileage);

        ImageView backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        if (getArguments() != null) {
            String licensePlate = getArguments().getString(ARG_LICENSE_PLATE);
            fetchVehicleDetails(licensePlate);
        }

        // Spremanje podataka
        view.findViewById(R.id.save_button).setOnClickListener(v -> saveVehicleDetails());

        // Inside onCreateView method
        view.findViewById(R.id.cancel_button).setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed(); // This will simulate the back press, effectively canceling the action
            }
        });

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
                    Toast.makeText(getContext(), "Failed to fetch vehicle details", Toast.LENGTH_SHORT).show();
                });
    }

    private void populateVehicleDetails(Vehicle vehicle) {
        vehicleTypeEditText.setText(vehicle.getVehicleType());
        manufacturerEditText.setText(vehicle.getManufacturer());
        modelEditText.setText(vehicle.getModel());
        modelYearEditText.setText(vehicle.getModelYear());
        licensePlateEditText.setText(vehicle.getLicensePlate());
        fuelTypeEditText.setText(vehicle.getFuelType());
        mileageEditText.setText(vehicle.getMileage()); // mileage ostaje String
    }

    private void saveVehicleDetails() {
        String type = vehicleTypeEditText.getText().toString();
        String manufacturer = manufacturerEditText.getText().toString();
        String model = modelEditText.getText().toString();
        String modelYear = modelYearEditText.getText().toString();
        String licensePlate = licensePlateEditText.getText().toString();
        String fuelType = fuelTypeEditText.getText().toString();
        String mileage = mileageEditText.getText().toString();

        // Provjera je li mileage broj
        if (mileage.isEmpty()) {
            Toast.makeText(getContext(), "Mileage cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Spremanje vozila u Firestore - ažuriranje postojećeg vozila
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Vehicle updatedVehicle = new Vehicle(type, manufacturer, model, modelYear, licensePlate, fuelType, mileage);

        // Ažuriranje postojećeg dokumenta prema licensePlate
        db.collection("vehicles").document(licensePlate).set(updatedVehicle, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Vehicle details updated", Toast.LENGTH_SHORT).show();
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to update vehicle details", Toast.LENGTH_SHORT).show();
                });
    }
}
