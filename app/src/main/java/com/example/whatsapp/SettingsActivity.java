package com.example.whatsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsapp.Models.Users;
import com.example.whatsapp.databinding.ActivitySettingsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    private static final int CAMPIC_ID =674;
    private static final int GALLERYPIC_ID=74;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storage=FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
         sharedPreferences=getSharedPreferences("save",MODE_PRIVATE);
         binding.lockswitch.setChecked(sharedPreferences.getBoolean("value",false));
        binding.lockswitch.setOnClickListener(view -> {
            if (binding.lockswitch.isChecked()) {
                SharedPreferences.Editor editor=getSharedPreferences("save",MODE_PRIVATE).edit();
                editor.putBoolean("value",true);
                editor.apply();
                binding.lockswitch.setChecked(true);
            }
            else {
                SharedPreferences.Editor editor=getSharedPreferences("save",MODE_PRIVATE).edit();
                editor.putBoolean("value",false);
                editor.apply();
                binding.lockswitch.setChecked(false);
            }
            ProcessPhoenix.triggerRebirth(getApplicationContext());
        });
        binding.backbutton.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        });
       binding.savebtn.setOnClickListener(v -> {
           String status=binding.etAbout.getText().toString();
           String username=binding.etUserName.getText().toString();
           HashMap<String,Object> hashMap=new HashMap<>();
           hashMap.put("username",username);
           hashMap.put("status",status);
           database.getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).updateChildren(hashMap);
           Toast.makeText(getApplicationContext(), "Profile updated", Toast.LENGTH_SHORT).show();
       });
        database.getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Users users=snapshot.getValue(Users.class);
                                Picasso.get().load(Objects.requireNonNull(users).getProfilepic())
                                        .placeholder(R.drawable.whatsapp)
                                        .into(binding.profileImage);

                                binding.etAbout.setText(users.getStatus());
                                binding.etUserName.setText(users.getUsername());
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
        binding.plus.setOnClickListener(v -> showBottomSheetDialog());
        binding.appabout.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AboutUsActivity.class)));
        binding.termsandpolicy.setOnClickListener(v -> {
            String url ="https://www.whatsapp.com/legal?eea=0&lg=en&lc=US";
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERYPIC_ID&&resultCode!=RESULT_CANCELED){
            if (Objects.requireNonNull(data).getData()!=null) {
                Uri sFile=data.getData();
                binding.profileImage.setImageURI(sFile);
                final StorageReference reference=storage.getReference().child("profile picture")
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                reference.putFile(sFile).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri -> {
                    database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                            .child("profilepic").setValue(uri.toString());
                    Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                }));
            }
        }
        if(requestCode==CAMPIC_ID&&resultCode!=RESULT_CANCELED){
            Bitmap photo = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).get("data");
            binding.profileImage.setImageBitmap(photo);
        }
    }
    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);
        LinearLayout cam= bottomSheetDialog.findViewById(R.id.cam);
        LinearLayout gallerypicker= bottomSheetDialog.findViewById(R.id.gallerypicker);
        Objects.requireNonNull(cam).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMPIC_ID);
        });
        Objects.requireNonNull(gallerypicker).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,GALLERYPIC_ID);
        });
        bottomSheetDialog.show();
    }
}