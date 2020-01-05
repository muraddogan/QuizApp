package com.example.quiz.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.LoginActivity;
import com.example.quiz.R;
import com.google.firebase.auth.FirebaseAuth;

public class TeacherActivity extends AppCompatActivity{

    TextView tw1;
    Button btnTestEkle,btnCikis,btnSonuc;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        mAuth = FirebaseAuth.getInstance();

        btnTestEkle=(Button)findViewById(R.id.buttonTestEkle);
        btnSonuc=(Button)findViewById(R.id.buttonSonuc);
        btnCikis=(Button)findViewById(R.id.buttonCikis);
        tw1=(TextView)findViewById(R.id.textWiewHoca);

        tw1.setText("Hoca Id: "+mAuth.getCurrentUser().getUid());
        btnTestEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherActivity.this, TeacherTestActivity.class);
                startActivity(intent);
                return;
            }
        });

        btnSonuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherActivity.this, TeacherResultActivity.class);
                startActivity(intent);
                return;
            }
        });

        btnCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(TeacherActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });
    }

}
