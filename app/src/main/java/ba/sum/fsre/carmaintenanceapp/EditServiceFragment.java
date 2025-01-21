package ba.sum.fsre.carmaintenanceapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

public class EditServiceFragment extends Fragment {

    private static final String ARG_SERVICE = "service";
    private Service service;

    private EditText etServiceType, etServiceDate, etMileage, etCost, etNotes;

    public static EditServiceFragment newInstance(Service service) {
        EditServiceFragment fragment = new EditServiceFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SERVICE, service);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_service, container, false);

        etServiceType = view.findViewById(R.id.edit_service_type);
        etServiceDate = view.findViewById(R.id.edit_service_date);
        etMileage = view.findViewById(R.id.edit_service_mileage);
        etCost = view.findViewById(R.id.edit_service_cost);
        etNotes = view.findViewById(R.id.edit_service_notes);

        ImageView backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        Button btnSave = view.findViewById(R.id.save_edit_button);
        Button btnCancel = view.findViewById(R.id.cancel_edit_button);

        btnCancel.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        if (getArguments() != null) {
            service = (Service) getArguments().getSerializable(ARG_SERVICE);
            if (service != null) {
                populateFields(service);
            }
        }

        btnSave.setOnClickListener(v -> saveService());

        return view;
    }

    private void populateFields(Service service) {
        etServiceType.setText(service.getServiceType());
        etServiceDate.setText(service.getServiceDate());
        etMileage.setText(service.getMileage());
        etCost.setText(service.getCost());
        etNotes.setText(service.getNotes());
    }

    private void saveService() {
        service.setServiceType(etServiceType.getText().toString());
        service.setServiceDate(etServiceDate.getText().toString());
        service.setMileage(etMileage.getText().toString());
        service.setCost(etCost.getText().toString());
        service.setNotes(etNotes.getText().toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("vehicles").document(service.getLicensePlate())
                .collection("services").document(service.getServiceDate())
                .update(
                        "serviceType", service.getServiceType(),
                        "serviceDate", service.getServiceDate(),
                        "mileage", service.getMileage(),
                        "cost", service.getCost(),
                        "notes", service.getNotes()
                )
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Servis ažuriran", Toast.LENGTH_SHORT).show();
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Greška pri ažuriranju", Toast.LENGTH_SHORT).show());

    }
}
