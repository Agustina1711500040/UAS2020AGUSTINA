package ac.id.atmaluhur.uts_amub_ti7jm_1711500040_agustina;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class RegisterTwoActivity extends AppCompatActivity {
    ImageButton bt_regis2, btn_add_photo;
    ImageView pic_photo;
    EditText hobi, alamat;
    EditText username, password, email;

    Uri photo_location;
    Integer photo_max = 1;
    DatabaseReference reference;
    StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        getUsernameLocal();
        pic_photo = findViewById(R.id.pic_photo);
        btn_add_photo = findViewById(R.id.btn_add_photo);
        bt_regis2 = findViewById(R.id.bt_regis2);
        hobi = findViewById(R.id.ed_hobi);
        alamat = findViewById(R.id.ed_alamat);

        //akan mengarahkan ke function findphoto()
        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                findPhoto();
            }
        });

        bt_regis2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference= FirebaseDatabase.getInstance().getReference().child("Users/Agustina");
                storage = FirebaseStorage.getInstance().getReference().child("PhotoUsers/");
                //validasi untuk file photo
                if (photo_location != null){
                    StorageReference storageReferencel =
                            storage.child(System.currentTimeMillis()+ "," +
                                    getFileExtension(photo_location));
                    storageReferencel.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String uri_photo = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                            reference.getRef().child("uri_photo_profile").setValue(uri_photo);
                            reference.getRef().child("hobi").setValue(hobi.getText().toString());
                            reference.getRef().child("alamat").setValue(alamat.getText().toString());
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Intent gotosuccess = new Intent(RegisterTwoActivity.this, MainActivity.class);
                            startActivity(gotosuccess);
                        }
                    });


                }
            }
        });
    }

    String getFileExtension (Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic,photo_max);
    }

    private void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

    //kita membutuhkan library picaso untuk menampilkan gambar menambahkan di grade app
    //membuat activity baru dengan cara Generate, (klik kanan, generate)


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(pic_photo);
        }
    }
}
