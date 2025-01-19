package ba.sum.fsre.carmaintenanceapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddVehicleFragment extends Fragment {

    private VehicleViewModel vehicleViewModel;
    private FirebaseFirestore db;
    private EditText vehicleTypeEditText, manufacturerEditText, modelEditText, modelYearEditText, licensePlateEditText, fuelTypeEditText, mileageEditText;
    private MaterialButton save_Button, cancel_Button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicles_registration, container, false);

        // Inicijalizacija Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Inicijalizacija ViewModel-a i drugih UI komponenti...
        vehicleViewModel = new ViewModelProvider(requireActivity()).get(VehicleViewModel.class);
        vehicleTypeEditText = view.findViewById(R.id.vehicle_type);
        manufacturerEditText = view.findViewById(R.id.manufacturer);
        modelEditText = view.findViewById(R.id.model);
        modelYearEditText = view.findViewById(R.id.model_year);
        licensePlateEditText = view.findViewById(R.id.license_plate);
        fuelTypeEditText = view.findViewById(R.id.fuel_type);
        mileageEditText = view.findViewById(R.id.mileage);

        save_Button = view.findViewById(R.id.save_button);
        cancel_Button = view.findViewById(R.id.cancel_button);

        // Spremanje vozila u Firestore
        save_Button.setOnClickListener(v -> {
            String vehicleType = vehicleTypeEditText.getText().toString();
            String manufacturer = manufacturerEditText.getText().toString();
            String model = modelEditText.getText().toString();
            String modelYear = modelYearEditText.getText().toString();
            String licensePlate = licensePlateEditText.getText().toString();
            String fuelType = fuelTypeEditText.getText().toString();
            String mileage = mileageEditText.getText().toString();

            // Kreiranje objekta vozila
            Vehicle newVehicle = new Vehicle(vehicleType, manufacturer, model, modelYear, licensePlate, fuelType, mileage);

            // Dodavanje vozila u Firestore
            db.collection("vehicles")
                    .add(newVehicle)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getContext(), "Vozilo je dodano!", Toast.LENGTH_SHORT).show();
                        // Ažuriranje ViewModel-a ako želite
                        vehicleViewModel.addVehicle(newVehicle);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Pogreška pri dodavanju vozila", Toast.LENGTH_SHORT).show();
                    });

            // Navigacija natrag u VehiclesFragment
            getParentFragmentManager().popBackStack();
        });

        cancel_Button.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}
