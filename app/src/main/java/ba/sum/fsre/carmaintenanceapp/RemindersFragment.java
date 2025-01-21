package ba.sum.fsre.carmaintenanceapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;



public class RemindersFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReminderAdapter adapter;
    private List<Reminder> remindersList;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminders, container, false);

        // Setting up the RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_reminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        remindersList = new ArrayList<>();
        adapter = new ReminderAdapter(remindersList);
        recyclerView.setAdapter(adapter);

        // Initializing Firestore instance
        db = FirebaseFirestore.getInstance();
        fetchReminders();

        // Setting up FloatingActionButton to navigate to AddReminderFragment
        FloatingActionButton addNotesButton = view.findViewById(R.id.nav_add_reminder);
        addNotesButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new AddReminderFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    private void fetchReminders() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reminders")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("RemindersFragment", "Documents fetched: " + queryDocumentSnapshots.size());
                    remindersList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Reminder reminder = document.toObject(Reminder.class);
                        reminder.setId(document.getId());
                        remindersList.add(reminder);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.w("RemindersFragment", "Error getting documents.", e);
                });
    }


}
