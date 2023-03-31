package com.sortscript.serfix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.serfix.databinding.ActivitySignInBinding;
import com.sortscript.serfix.databinding.ActivitySplashScreenBinding;

import java.util.ArrayList;

public class SplashScreenActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RequestingLocPermission();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Serfix").child("UserDetail");

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            reference.orderByChild("UID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ModelForFirebase model = dataSnapshot.getValue(ModelForFirebase.class);
                            if (model.getActive_user().equals("Yes")) {
                                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SplashScreenActivity.this, "Waiting For Your Account Activation", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(SplashScreenActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(SplashScreenActivity.this, SignIn.class));
                    finish();
                }
            }, 43);

        }


    }

    private void RequestingLocPermission() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
