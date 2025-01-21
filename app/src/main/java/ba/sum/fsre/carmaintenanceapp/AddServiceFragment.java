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
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddServiceFragment extends Fragment {

    private EditText serviceTypeEditText, serviceDateEditText, mileageEditText, costEditText, notesEditText;
    private String licensePlate;

    public static AddServiceFragment newInstance(String licensePlate) {
        AddServiceFragment fragment = new AddServiceFragment();
        Bundle args = new Bundle();
        args.putString("license_plate", licensePlate);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_service, container, false);

        serviceTypeEditText = view.findViewById(R.id.service_type);
        serviceDateEditText = view.findViewById(R.id.service_date);
        mileageEditText = view.findViewById(R.id.service_mileage);
        costEditText = view.findViewById(R.id.service_cost);
        notesEditText = view.findViewById(R.id.service_notes);

        MaterialButton saveButton = view.findViewById(R.id.save_button);
        MaterialButton cancelButton = view.findViewById(R.id.cancel_button);
        ImageView backButton = view.findViewById(R.id.back_button);

        if (getArguments() != null) {
            licensePlate = getArguments().getString("license_plate");
        }

        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        cancelButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        saveButton.setOnClickListener(v -> saveService());

        return view;
    }

    private void saveService() {
        String serviceType = serviceTypeEditText.getText().toString();
        String serviceDate = serviceDateEditText.getText().toString();
        String mileage = mileageEditText.getText().toString();
        String cost = costEditText.getText().toString();
        String notes = notesEditText.getText().toString();

        if (serviceType.isEmpty() || serviceDate.isEmpty() || mileage.isEmpty() || cost.isEmpty()) {
            Toast.makeText(getContext(), "Molimo ispunite sva polja", Toast.LENGTH_SHORT).show();
            return;
        }

        Service service = new Service(serviceType, serviceDate, mileage, cost, notes, licensePlate);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("vehicles")
                .document(licensePlate)
                .collection("services")
                .add(service)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Servis uspješno dodan", Toast.LENGTH_SHORT).show();
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Dodavanje servisa nije uspjelo", Toast.LENGTH_SHORT).show();
                });
    }
}
