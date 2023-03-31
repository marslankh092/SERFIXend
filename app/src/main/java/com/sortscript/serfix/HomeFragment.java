package com.sortscript.serfix;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.serfix.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements MyAdapter.ItemClickListener {

    FragmentHomeBinding binding;
    ArrayList<ProductModel> arraylist = new ArrayList<>();

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firebaseData();
        binding.recyclerview.setLayoutManager(new GridLayoutManager(getActivity(),2));


        return view;
    }


    public void firebaseData() {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
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

        binding.recyclerview.setAdapter(new MyAdapter(arraylist,getActivity(),this));
    }

    @Override
    public void update(ProductModel model) {

        Intent intent = new Intent(getContext(),ProductDetailAndSaleActivity.class);
        intent.putExtra("key",model.getKey());
        startActivity(intent);

    }
}