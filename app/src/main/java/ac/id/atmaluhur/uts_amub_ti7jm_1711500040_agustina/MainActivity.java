package ac.id.atmaluhur.uts_amub_ti7jm_1711500040_agustina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button login, bt_reg ;
    EditText et_name, et_pass;

    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name = findViewById(R.id.et_name);
        et_pass = findViewById(R.id.et_pass);


        login = findViewById(R.id.bt_login);
        bt_reg = findViewById(R.id.bt_reg);

        //berpindah ke activity lain ke activity registerone
        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotonextregister = new Intent(MainActivity.this, RegisterOneActivity.class);
                startActivity(gotonextregister);
            }
        });

        //menyimpan data kepada Localstorage (handphone)
        SharedPreferences sharedPreferences =getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(username_key,et_name.getText().toString()); //ngambil data imputan username disimpan ke variabel username_key
        editor.apply();

        //proses SIMPAN KE DATABASE FIREBASE
        reference = FirebaseDatabase.getInstance().getReference().child("User").child(et_name.getText().toString());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("username").setValue(et_name.getText().toString());
                dataSnapshot.getRef().child("password").setValue(et_pass.getText().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
