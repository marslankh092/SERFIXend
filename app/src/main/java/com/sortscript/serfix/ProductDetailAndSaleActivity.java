package com.sortscript.serfix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.serfix.databinding.ActivityMainBinding;
import com.sortscript.serfix.databinding.ActivityProductDetailAndSaleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductDetailAndSaleActivity extends AppCompatActivity {
    ActivityProductDetailAndSaleBinding binding;
    String key = "";
    int quantity = 1, total_amount = 0;
    ArrayList<ProductModel> arrayList = new ArrayList<>();
    DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductDetailAndSaleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
//        Toast.makeText(this, "" + key, Toast.LENGTH_SHORT).show();
        get_product_from_firebase();
        binding.addMore.setOnClickListener(view -> {
            quantity++;
            binding.quantity.setText("" + quantity);
            total_amount = quantity * Integer.parseInt(arrayList.get(0).getPrice());
            binding.totalAmount.setText("" + total_amount);

        });
        binding.subtract.setOnClickListener(view -> {
            if (quantity > 1) {
                quantity--;
                binding.quantity.setText("" + quantity);
                total_amount = quantity * Integer.parseInt(arrayList.get(0).getPrice());
                binding.totalAmount.setText(""+ total_amount);
            } else {
                Toast.makeText(this, "One Item Should Be Added", Toast.LENGTH_SHORT).show();
            }

        });
        binding.placeOrder.setOnClickListener(view -> {
            binding.orderDetails.setVisibility(View.VISIBLE);
        });
        binding.orderDone.setOnClickListener(view -> {
            if(binding.customerName.getText().toString().equals("")){
                binding.customerName.setError("Please Provide Name");
            }else if(binding.customerPhone.getText().toString().equals("")){
                binding.customerPhone.setError("Please Provide Phone Number");
            }else if(binding.customerAddress.getText().toString().equals("")){
                binding.customerAddress.setError("Please Provide Address");
            }else{
                save_to_firebase();
                binding.orderDetails.setVisibility(View.GONE);
            }

        });
        binding.addToCart.setOnClickListener(view -> {
            save_cart_to_firebase();

        });
    }


    private void save_to_firebase() {


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Saving Information....");
        progressDialog.show();
        String UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        String Private = ref.push().getKey();
        ref = FirebaseDatabase.getInstance().getReference("Serfix").child("Orders").child(UID).child(Private);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref.child("Name").setValue(binding.customerName.getText().toString());
                ref.child("Phone").setValue(binding.customerPhone.getText().toString());
                ref.child("Address").setValue(binding.customerAddress.getText().toString());
                ref.child("Product").setValue(binding.name.getText().toString());
                ref.child("Description").setValue(binding.description.getText().toString());
                ref.child("Price").setValue(binding.price.getText().toString());
                ref.child("Quantity").setValue(binding.quantity.getText().toString());
                ref.child("Image").setValue(arrayList.get(0).getProductImage());
                ref.child("Private").setValue(Private);
                ref.child("TotalAmount").setValue(binding.totalAmount.getText().toString());
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void save_cart_to_firebase() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Saving Information....");
        progressDialog.show();
        String UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        String Private_cart = ref.push().getKey();
        ref = FirebaseDatabase.getInstance().getReference("Serfix").child("Carts").child(UID).child(Private_cart);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String Private = ref.push().getKey();
                ref.child("Product").setValue(binding.name.getText().toString());
                ref.child("Description").setValue(binding.description.getText().toString());
                ref.child("Price").setValue(binding.price.getText().toString());
                ref.child("Quantity").setValue(binding.quantity.getText().toString());
                ref.child("Image").setValue(arrayList.get(0).getProductImage());
                ref.child("Private").setValue(Private_cart);
                ref.child("TotalAmount").setValue(binding.totalAmount.getText().toString());
                Intent intent = new Intent(ProductDetailAndSaleActivity.this,CartActivity.class);
                progressDialog.dismiss();
                startActivity(intent);
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void get_product_from_firebase() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Serfix").child("Items");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductModel model = snapshot.getValue(ProductModel.class);
                    if (model.getKey().equals(key)) {
                        arrayList.add(model);
                    }

                }
                Picasso.get().load(arrayList.get(0).getProductImage()).into(binding.image);
                binding.name.setText(arrayList.get(0).getProductName());
                binding.description.setText(arrayList.get(0).getProductDescription());
                binding.price.setText(arrayList.get(0).getPrice());
                binding.totalAmount.setText(""+arrayList.get(0).getPrice());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }
}