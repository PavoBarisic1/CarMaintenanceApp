package ba.sum.fsre.carmaintenanceapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Set up Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new VehiclesFragment())
                .commit();

        // Handle navigation item selection
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_vehicles) {
                selectedFragment = new VehiclesFragment();
            }
            else if (item.getItemId() == R.id.nav_notes) {
                selectedFragment = new NotesFragment();
            } else if (item.getItemId() == R.id.nav_reminders) {
                selectedFragment = new RemindersFragment();
            } else if (item.getItemId() == R.id.nav_more) {
                selectedFragment = new MoreFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .addToBackStack(null)  // Optional: Add to back stack for back navigation
                        .commit();
            }
            return true;
        });
    }
}
