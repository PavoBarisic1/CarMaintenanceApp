package ba.sum.fsre.carmaintenanceapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MoreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflater za XML fajl
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        // Pronalaženje FloatingActionButton-a
        FloatingActionButton logoutFab = view.findViewById(R.id.nav_logout);

        // Postavljanje click listenera
        logoutFab.setOnClickListener(v -> {
            // Pokretanje SignUpActivity
            Intent intent = new Intent(getActivity(), SignUpActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
