package ba.sum.fsre.carmaintenanceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private List<Reminder> remindersList;
    private FirebaseFirestore db;

    public ReminderAdapter(List<Reminder> remindersList) {
        this.remindersList = remindersList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = remindersList.get(position);
        holder.reminderName.setText(reminder.getreminderName());
        holder.reminderDate.setText(reminder.getreminderDate());

        holder.itemView.findViewById(R.id.btnEdit).setOnClickListener(v -> {
            FragmentTransaction transaction = ((MainActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new EditReminderFragment(reminder));
            transaction.addToBackStack(null);
            transaction.commit();
        });

        holder.itemView.findViewById(R.id.btnDelete).setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(v.getContext())
                    .setTitle("Delete Reminder")
                    .setMessage("Are you sure you want to delete this reminder?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        deleteReminder(reminder.getId(), position);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }


    @Override
    public int getItemCount() {
        return remindersList.size();
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        public TextView reminderName;
        public TextView reminderDate;

        public ReminderViewHolder(View itemView) {
            super(itemView);
            reminderName = itemView.findViewById(R.id.reminderName);
            reminderDate = itemView.findViewById(R.id.reminderDate);
        }
    }

    // Metoda za brisanje remindera iz Firestore baze
    private void deleteReminder(String reminderId, int position) {
        db.collection("reminders")
                .document(reminderId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    remindersList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, remindersList.size());
                })
                .addOnFailureListener(e -> {
                    // Rukovanje greškama
                });
    }
}
