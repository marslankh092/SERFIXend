package com.sortscript.serfix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.serfix.databinding.ActivityAdminPenalBinding;
import com.sortscript.serfix.databinding.ActivityViewProductsBinding;

import java.util.ArrayList;

public class ViewProductsActivity extends AppCompatActivity implements ViewProductsAdapter.ItemClickListener {
    ActivityViewProductsBinding binding;
    String UID = "";
    ArrayList<ProductModel> arraylist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseData();
        binding.recyclerview.setLayoutManager(new GridLayoutManager(ViewProductsActivity.this,2));



    }
    public void firebaseData() {

        ProgressDialog progressDialog = new ProgressDialog(ViewProductsActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Getting Data....");
        progressDialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Serfix").child("Items");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arraylist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductModel Model = snapshot.getValue(ProductModel.class);
                    arraylist.add(Model);

                }
                setAdapter();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }

    private void setAdapter() {
        binding.recyclerview.setAdapter(new ViewProductsAdapter(arraylist,ViewProductsActivity.this,this));
    }

    @Override
    public void delete(ProductModel model) {
        delete_firebaseData(model.getKey());
        Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show();
    }


    public void delete_firebaseData(String key) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Removing Item....");
        progressDialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Serfix").child("Items").child(key);
        ref.removeValue();
        setAdapter();
        progressDialog.dismiss();

    }
}