package ba.sum.fsre.carmaintenanceapp;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class VehicleViewModel extends ViewModel {

    private final MutableLiveData<List<Vehicle>> vehicles = new MutableLiveData<>(new ArrayList<>());
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration listenerRegistration;

    public LiveData<List<Vehicle>> getVehicles() {
        return vehicles;
    }

    public void startListeningForVehicles() {
        listenerRegistration = db.collection("vehicles")
                .addSnapshotListener((QuerySnapshot value, FirebaseFirestoreException error) -> {
                    if (error != null) {
                        Log.w("Firebase", "Error getting documents.", error);
                        return;
                    }

                    List<Vehicle> updatedVehicles = new ArrayList<>();
                    for (DocumentSnapshot document : value) {
                        Vehicle vehicle = document.toObject(Vehicle.class);
                        updatedVehicles.add(vehicle);
                    }

                    Log.d("VehicleViewModel", "Updated vehicles list: " + updatedVehicles.size());
                    vehicles.setValue(updatedVehicles);
                });
    }

    public void stopListeningForVehicles() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    public void addVehicle(Vehicle vehicle) {
        List<Vehicle> currentVehicles = vehicles.getValue();
        if (currentVehicles != null) {
            currentVehicles.add(vehicle);
            vehicles.setValue(currentVehicles);
        }
    }
}
