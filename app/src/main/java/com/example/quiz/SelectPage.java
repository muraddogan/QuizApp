package com.example.quiz;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.quiz.Student.StudentActivity;
import com.example.quiz.Teacher.TeacherActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SelectPage extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;


    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_page);




            mAuth = FirebaseAuth.getInstance();
            userId = mAuth.getCurrentUser().getUid();

        getUserInfo();

    }


    private void getUserInfo(){

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("hesap");

        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String hesap=dataSnapshot.getValue(String.class);
                switch (hesap){
                    case "ogrn":
                        getStudent();
                        break;
                    case "ogrt":
                        getTeacher();
                        break;
                        default:
                            break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getStudent(){
        Intent intent = new Intent(SelectPage.this, StudentActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    private void getTeacher(){
        Intent intent = new Intent(SelectPage.this, TeacherActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}
