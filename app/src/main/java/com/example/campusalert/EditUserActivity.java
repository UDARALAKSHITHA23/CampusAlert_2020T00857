package com.example.campusalert;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditUserActivity extends AppCompatActivity {

    private EditText etUsername, etName, etEmail, etPassword;
    Button btnSave;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user); // Fixed: Use your app's R class

        etUsername = findViewById(R.id.etUsername); // Fixed
        etName = findViewById(R.id.etName); // Fixed
        etEmail = findViewById(R.id.etEmail); // Fixed
        etPassword = findViewById(R.id.etPassword); // Fixed
        btnSave = findViewById(R.id.btnSave); // Fixed

        // Rest of your code remains the same...
        String username = getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "No user data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etUsername.setText(username);
        reference = FirebaseDatabase.getInstance().getReference("users").child(username);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    etName.setText(snapshot.child("name").getValue(String.class));
                    etEmail.setText(snapshot.child("email").getValue(String.class));
                    etPassword.setText(snapshot.child("password").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditUserActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(v -> {
            reference.child("name").setValue(etName.getText().toString());
            reference.child("email").setValue(etEmail.getText().toString());
            reference.child("password").setValue(etPassword.getText().toString());
            Toast.makeText(EditUserActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}