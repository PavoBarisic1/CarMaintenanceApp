package ba.sum.fsre.carmaintenanceapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class Vehicles_registrationFragment extends Fragment {

    private EditText vehicleType, manufacturer, model, modelYear, licensePlate, fuelType, mileage;
    private Button saveButton, cancelButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicles_registration, container, false);

        vehicleType = view.findViewById(R.id.vehicle_type);
        manufacturer = view.findViewById(R.id.manufacturer);
        model = view.findViewById(R.id.model);
        modelYear = view.findViewById(R.id.model_year);
        licensePlate = view.findViewById(R.id.license_plate);
        fuelType = view.findViewById(R.id.fuel_type);
        mileage = view.findViewById(R.id.mileage);

        saveButton = view.findViewById(R.id.save_button);
        cancelButton = view.findViewById(R.id.cancel_button);

        ImageView backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        saveButton.setOnClickListener(v -> saveVehicle());
        cancelButton.setOnClickListener(v -> cancelRegistration());

        return view;
    }

    private void saveVehicle() {
        String type = vehicleType.getText().toString();
        String manu = manufacturer.getText().toString();
        String modelName = model.getText().toString();
        String year = modelYear.getText().toString();
        String plate = licensePlate.getText().toString();
        String fuel = fuelType.getText().toString();
        String mileageValue = mileage.getText().toString();

        // Validacija unosa
        if (type.isEmpty() || manu.isEmpty() || modelName.isEmpty() || year.isEmpty() || plate.isEmpty() || fuel.isEmpty() || mileageValue.isEmpty()) {
            // Možeš prikazati poruku o grešci korisniku
            Toast.makeText(getContext(), "Molimo popunite sva polja!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kreiranje novog objekta Vehicle
        Vehicle vehicle = new Vehicle(type, manu, modelName, year, plate, fuel, mileageValue);

        // Dodavanje vozila u Firestore putem VehicleRepository
        VehicleRepository.getInstance().addVehicle(vehicle)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Vozilo je uspješno dodano!", Toast.LENGTH_SHORT).show();
                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Greška pri dodavanju vozila: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }


    private void cancelRegistration() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
