package ba.sum.fsre.carmaintenanceapp;

import android.app.AlertDialog;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;

public class VehicleDetailsFragment extends Fragment {

    private static final String ARG_LICENSE_PLATE = "license_plate";

    private ImageView vehicleImageView;
    private TextView vehicleNameTextView, vehicleTypeView, licensePlateTextView, mileageTextView, fuelTypeTextView, modelYearTextView;
    private Button addServiceButton;

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
        ImageView editButton = view.findViewById(R.id.edit_button);
        editButton.setOnClickListener(v -> navigateToEditDetailsFragment());
        addServiceButton.setOnClickListener(v -> {
            // Dodajte logiku za otvaranje fragmenta za dodavanje usluge
        });

        ImageView deleteButton = view.findViewById(R.id.delete_button); // Pretpostavljam da imate delete_button u XML-u
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
                    // Rukovanje greškama
                });
    }

    private void populateVehicleDetails(Vehicle vehicle) {
        vehicleNameTextView.setText(vehicle.getManufacturer() + " " + vehicle.getModel());
        licensePlateTextView.setText(vehicle.getLicensePlate());
        mileageTextView.setText(String.valueOf(vehicle.getMileage()));
        fuelTypeTextView.setText(vehicle.getFuelType());
        modelYearTextView.setText(vehicle.getModelYear());
        vehicleTypeView.setText(vehicle.getVehicleType());

        // Postavite sliku vozila ako je dostupna
        // Ovo možete proširiti ako koristite Firebase Storage ili neki URL
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

        // Brisanje dokumenta iz Firestore-a
        db.collection("vehicles").document(licensePlate).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Vehicle deleted", Toast.LENGTH_SHORT).show();
                    if (getActivity() != null) {
                        getActivity().onBackPressed(); // Povratak na prethodni fragment
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to delete vehicle", Toast.LENGTH_SHORT).show();
                });
    }

    private void showDeleteConfirmationDialog() {
        // Kreiranje AlertDialog-a za potvrdu brisanja
        new AlertDialog.Builder(getContext())
                .setMessage("Jeste li sigurni da želite izbrisati vozilo?")
                .setCancelable(false) // Onemogućava zatvaranje dijaloga klikom izvan njega
                .setPositiveButton("Da", (dialog, id) -> deleteVehicle()) // Ako korisnik pritisne "Yes", poziva se deleteVehicle()
                .setNegativeButton("Ne", (dialog, id) -> dialog.dismiss()) // Ako korisnik pritisne "No", dijalog se zatvara
                .show();
    }

}
