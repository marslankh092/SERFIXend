package com.sortscript.serfix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sortscript.serfix.databinding.ActivityAdminPenalBinding;
import com.sortscript.serfix.databinding.ActivityCreateProductBinding;

import java.util.HashMap;

public class CreateProductActivity extends AppCompatActivity {
    ActivityCreateProductBinding binding;
    public static final int PICK_IMAGE = 1;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Uri uri = null;
    DatabaseReference reference;
    StorageReference storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storage = FirebaseStorage.getInstance().getReference();
        binding.productImage.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        binding.save.setOnClickListener(view -> {
            if (uri==null){
                Toast.makeText(this, "Enter Your Product Image", Toast.LENGTH_SHORT).show();
            }else if(binding.productName.getText().toString().equals("")){
                binding.productName.setError("Enter Product Name");
            }else if(binding.productDescription.getText().toString().equals("")){
                binding.productDescription.setError("Enter Description");
            }else if(binding.Price.getText().toString().equals("")){
                binding.Price.setError("Enter Price");
            }else {
                uploading(uri);
            }

        });





    }

    public void put_items_in_firebase(Uri urii) {
        reference = FirebaseDatabase.getInstance().getReference().child("Serfix").child("Items");
        String key = reference.push().getKey();
        HashMap<String, Object> map = new HashMap<>();
        map.put("ProductName", binding.productName.getText().toString().trim());
        map.put("ProductDescription", binding.productDescription.getText().toString().trim());
        map.put("Price", binding.Price.getText().toString().trim());
        map.put("ProductImage", urii.toString());
        map.put("Key", key);

        assert key != null;
        reference.child(key).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CreateProductActivity.this, "set", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateProductActivity.this,ViewProductsActivity.class));
                finish();
            }
        });



    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        uri = data.getData();
        if(uri!=null){
            if (requestCode == PICK_IMAGE) {
                binding.productImage.setImageURI(uri);
                binding.productText.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {


            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploading(Uri uri) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Creating Product....");
        progressDialog.show();

        StorageReference file = storage.child(System.currentTimeMillis() + "," + getfileextension(uri));
        file.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri urii) {

                        put_items_in_firebase(urii);
                        progressDialog.dismiss();

                    }
                });
            }

        });
    }

    private String getfileextension(Uri uri) {
        ContentResolver resolver = CreateProductActivity.this.getApplicationContext().getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(resolver.getType(uri));
    }
}

