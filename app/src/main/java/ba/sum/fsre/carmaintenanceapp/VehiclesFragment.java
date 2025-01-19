package ba.sum.fsre.carmaintenanceapp;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import ba.sum.fsre.carmaintenanceapp.R;
import ba.sum.fsre.carmaintenanceapp.Vehicle;
import ba.sum.fsre.carmaintenanceapp.VehicleAdapter;
import ba.sum.fsre.carmaintenanceapp.VehicleDetailsFragment;

public class VehiclesFragment extends Fragment {

    private VehicleAdapter vehicleAdapter;
    private List<Vehicle> vehicleList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicles, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        vehicleList = new ArrayList<>();
        vehicleAdapter = new VehicleAdapter(vehicleList);

        // Postavljanje adaptera na RecyclerView
        recyclerView.setAdapter(vehicleAdapter);
        FloatingActionButton addVehicleButton = view.findViewById(R.id.nav_add_vehicle);
        addVehicleButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new Vehicles_registrationFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Postavljanje listener-a za klik na stavku
        vehicleAdapter.setOnItemClickListener(licensePlate -> {
            VehicleDetailsFragment detailFragment = VehicleDetailsFragment.newInstance(licensePlate);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Dohvatanje podataka iz Firestore-a
        fetchVehicles();

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchVehicles() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("vehicles").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    vehicleList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Vehicle vehicle = document.toObject(Vehicle.class);
                        vehicleList.add(vehicle);
                    }
                    vehicleAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Rukovanje greškama
                });
    }
}