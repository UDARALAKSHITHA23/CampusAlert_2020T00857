package com.example.campusalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.*;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileName, profileEmail;
    private DatabaseReference reference;
    private String username;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }

        // Get username passed from previous activity
        username = getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "User session expired", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI components
        initializeViews();
        loadUserData();
    }

    private void initializeViews() {
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        TextView tvLevel = findViewById(R.id.tvLevel);
        TextView tvYear = findViewById(R.id.tvYear);
        TextView tvAge = findViewById(R.id.tvAge);
        TextView tvHome = findViewById(R.id.tvHome);
        LinearLayout devInfoSection = findViewById(R.id.devInfoSection);
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btndelete);

        // Set static sample values
        tvLevel.setText("Mid level");
        tvYear.setText("4th Year");
        tvAge.setText("22y");
        tvHome.setText("Kegalle");

        // Set click listeners
        devInfoSection.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, DevInfro.class));
        });

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditUserActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> deleteUser());

        // Initialize Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setupBottomNavigation(bottomNavigationView);
    }
    private void setupBottomNavigation(BottomNavigationView bottomNavigation) {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Already on profile, do nothing
                return true;
            } else {
                String category = "";
                String title = "";

                if (itemId == R.id.nav_Sports) {
                    category = "sports";
                    title = "Sports News";
                } else if (itemId == R.id.nav_academic) {
                    category = "academic";
                    title = "Academic News";
                } else if (itemId == R.id.nav_events) {
                    category = "events";
                    title = "Campus Events";
                }

                Intent intent = new Intent(ProfileActivity.this, ActivityNews.class);
                intent.putExtra("category", category);
                intent.putExtra("title", title);
                intent.putExtra("username", username);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
        });

        // Set profile as selected by default
        bottomNavigation.setSelectedItemId(R.id.nav_home);
    }



    private void deleteUser() {
        DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference("users").child(username);
        deleteRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ProfileActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                    redirectToLogin();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProfileActivity.this, "Delete failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void loadUserData() {
        reference = FirebaseDatabase.getInstance().getReference("users").child(username);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    profileName.setText(name != null ? name : "No name");
                    profileEmail.setText(email != null ? email : "No email");
                } else {
                    Toast.makeText(ProfileActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                    redirectToLogin();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset navigation selection when returning
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }
}