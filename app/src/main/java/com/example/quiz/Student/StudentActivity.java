package com.example.quiz.Student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.LoginActivity;
import com.example.quiz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class StudentActivity extends AppCompatActivity {

    Button btnTestBasla,btnHocaEkle,btnCikis,btnSonuc;
    EditText editTextHoca;
    TextView textViewHoca;
    private static String saat;
    private String userId,hocaId,id;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

        textViewHoca=(TextView)findViewById(R.id.textWiewHoca);

        editTextHoca=(EditText)findViewById(R.id.editTextHoca);

        btnTestBasla=(Button)findViewById(R.id.buttonTestBasla);
        btnHocaEkle=(Button)findViewById(R.id.buttonHocaEkle);
        btnSonuc=(Button)findViewById(R.id.buttonSonuc);
        btnCikis=(Button)findViewById(R.id.buttonCikis);

        btnTestBasla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gecisYap();
                textAta();
            }
        });

        btnHocaEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hocaId=editTextHoca.getText().toString();

                Map userInfo = new HashMap<>();
                userInfo.put("hoca",hocaId);

                mUserDatabase.updateChildren(userInfo);
                textAta();
            }
        });

        btnSonuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentActivity.this, StudentResultActivity.class);
                startActivity(intent);
                return;
            }
        });

        textAta();

        btnCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(StudentActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });


    }
    private void textAta(){
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                if(map.get("hoca")!=null){
                    id = map.get("hoca").toString();
                    textViewHoca.setText("hoca id:"+id);
                }
                if(map.get("birGunDolduMu")!=null){
                    saat=map.get("birGunDolduMu").toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void gecisYap(){

        if(saat=="0"){
            Toast.makeText(StudentActivity.this,"Bir gün dolmadı",Toast.LENGTH_SHORT).show();
        }
        else if(saat=="1")
        {
            Intent intentService = new Intent(getApplicationContext(), SaatServis.class);
            stopService(intentService);
            String s = id;
            Intent i = new Intent(getApplicationContext(), StudentTestActivity.class);
            i.putExtra("send_string",s);
            startActivity(i);
            return;
        }
    }
}
