package ba.sum.fsre.carmaintenanceapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditReminderFragment extends Fragment {

    private EditText editTextReminderName;
    private EditText editTextReminderDate;
    private Button btnSave, btnCancel;
    private Reminder reminder;
    private FirebaseFirestore db;

    public EditReminderFragment(Reminder reminder) {
        this.reminder = reminder;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_reminder, container, false);

        editTextReminderName = view.findViewById(R.id.reminderName);
        editTextReminderDate = view.findViewById(R.id.reminderDate);
        btnSave = view.findViewById(R.id.save_button);
        btnCancel = view.findViewById(R.id.cancel_button);
        db = FirebaseFirestore.getInstance();
        ImageView backButton = view.findViewById(R.id.back_button);

        if (reminder != null) {
            editTextReminderName.setText(reminder.getreminderName());
            editTextReminderDate.setText(reminder.getreminderDate());
        }

        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        btnSave.setOnClickListener(v -> {
            if (validateInput()) {
                updateReminder();
            }
        });

        btnCancel.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(editTextReminderName.getText().toString())) {
            editTextReminderName.setError("Name is required");
            return false;
        }
        if (TextUtils.isEmpty(editTextReminderDate.getText().toString())) {
            editTextReminderDate.setError("Date is required");
            return false;
        }
        return true;
    }

    private void updateReminder() {
        String reminderName = editTextReminderName.getText().toString();
        String reminderDate = editTextReminderDate.getText().toString();

        reminder.setreminderName(reminderName);
        reminder.setreminderDate(reminderDate);

        db.collection("reminders")
                .document(reminder.getId())
                .set(reminder)
                .addOnSuccessListener(aVoid -> {
                    // Reminder uspješno ažuriran
                    getParentFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    // Rukovanje greškama
                });
    }
}
