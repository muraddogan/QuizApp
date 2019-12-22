package com.example.quiz.Student;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SaatServis extends Service {

    Context context;
    Timer timer;
    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;
    public static int temp=0;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {//Servis startService(); metoduyla çağrıldığında çalışır
        context = getApplicationContext();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                temp++;
                if(temp==24*60){
                    ZmanDoldu();
                }
            }
        }, 0, 60000);
    }

    public void ZmanDoldu(){

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

        Map saatBilgi = new HashMap();
        saatBilgi.put("birGunDolduMu", 1);
        mUserDatabase.updateChildren(saatBilgi);

    }
    @Override
    public void onDestroy() {//Servis stopService(); metoduyla durdurulduğunda çalışır
        timer.cancel();
    }
}
