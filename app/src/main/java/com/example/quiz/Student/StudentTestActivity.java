package com.example.quiz.Student;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.quiz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StudentTestActivity extends AppCompatActivity {

    TextView tvTestSayi,tvKonuSayi,tvSoruSayi,tvSoru,tvCevapA,tvCevapB,tvCevapC,tvCevapD,tvCountDown;
    RadioGroup rgCevap;
    RadioButton rbCevap;
    Button cevap;
    ImageView imA,imB,imC,imD;
    private static int i,j=0,k=0;
    private String userId,imUrlA,imUrlB,imUrlC,imUrlD,ogrCevap;
    private static int[] tempYanlis={0,0,0,0,0,0,0,0,0,0},tempDogru={0,0,0,0,0,0,0,0,0,0};
    private String[] test={"1","2","3","4","5"};
    private String[] konu={"Doğa ve İnsan","Dünya’nın Şekli ve Hareketleri","Coğrafi Konum","Harita Bilgisi","İklim Bilgisi","Yerin Şekillenmesi","Doğanın Varlıkları","Beşeri Yapı","Nüfusun Gelişimi","Göç Nedenleri ve Sonuçları"};
    private String[] soru={"1","2","3","4","5","6","7","8","9"};
    private static String rbCev;
    private DatabaseReference mUserDatabase,mQuiz,mHocaDatabase,mDurumDatabase,dbSil,mKayitDb,mSoruSilDb,dbYeni;
    private FirebaseAuth mAuth;

    private static final long START_TIME_IN_MILLIS = 3600000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_test);


        Bundle extras = getIntent().getExtras();
        String hocaUserId = extras.getString("send_string");

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mKayitDb=FirebaseDatabase.getInstance().getReference().child("User").child(hocaUserId);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
        mHocaDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(hocaUserId).child("Soru");


        tvTestSayi = (TextView) findViewById(R.id.textViewTestSayi);
        tvKonuSayi = (TextView) findViewById(R.id.textViewKonuSayi);
        tvSoruSayi = (TextView) findViewById(R.id.textViewSoruSayi);
        tvSoru = (TextView) findViewById(R.id.textViewSoru);
        tvCevapA = (TextView) findViewById(R.id.textViewA);
        tvCevapB = (TextView) findViewById(R.id.textViewB);
        tvCevapC = (TextView) findViewById(R.id.textViewC);
        tvCevapD = (TextView) findViewById(R.id.textViewD);
        tvCountDown = findViewById(R.id.tvCountdown);

        imA=(ImageView)findViewById(R.id.cevapImageA);
        imB=(ImageView)findViewById(R.id.cevapImageB);
        imC=(ImageView)findViewById(R.id.cevapImageC);
        imD=(ImageView)findViewById(R.id.cevapImageD);

        rgCevap = (RadioGroup) findViewById(R.id.radioGroupCevap);

        cevap = (Button) findViewById(R.id.buttonCevap);

        startTimer();

        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                if(map.get("cozuldu")!=null) {
                   String str =  map.get("cozuldu").toString();
                      i = Integer.parseInt(str);

                      getQuizInfo(i,j,k);

                    testInfo(i,j,k);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mSoruSilDb=FirebaseDatabase.getInstance().getReference().getRoot().child("User").child(hocaUserId).child("Soru");

        cevap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = rgCevap.getCheckedRadioButtonId();
                rbCevap= (RadioButton) findViewById(selectedId);
                if(rbCevap==null){
                    Toast.makeText(StudentTestActivity.this, "Cevap boş bırakılamaz.", Toast.LENGTH_SHORT).show();
                    return;
                }
                rbCev=rbCevap.getText().toString();

                getButton();
            }
        });
    }

    private void getButton(){


        Map kayit = new HashMap<>();
        kayit.put("ogrenci",userId);
        mKayitDb.updateChildren(kayit);

        mDurumDatabase = mUserDatabase.child("Durum").child(test[i]).child(konu[j]);
        if(!rbCev.equals(ogrCevap)){
            tempYanlis[j]++;
            Map ogrDurum = new HashMap<>();
            ogrDurum.put("Yanlis",tempYanlis[j]);
            mDurumDatabase.updateChildren(ogrDurum);
        }
        else
        {
            tempDogru[j]++;
            Map ogrDurum = new HashMap<>();
            ogrDurum.put("Dogru",tempDogru[j]);
            mDurumDatabase.updateChildren(ogrDurum);
            dbSil = FirebaseDatabase.getInstance().getReference().getRoot().child("User").child(userId).child("Durum").child(test[i]).child(konu[j]).child("Yanlis").child(soru[k]);
            dbSil.setValue(null);
            dbYeni=mSoruSilDb.child(test[i+1]).child(konu[j]).child(soru[k]);
            dbYeni.setValue(null);
        }

        if(j==konu.length-1 && k==soru.length-1){
            Toast.makeText(StudentTestActivity.this,"Test Bitmiştir",Toast.LENGTH_SHORT).show();

            i++;
            Map cozuldu = new HashMap<>();
            cozuldu.put("cozuldu",i);
            mUserDatabase.updateChildren(cozuldu);


            Map saat = new HashMap<>();
            saat.put("birGunDolduMu",0);
            mUserDatabase.updateChildren(saat);

            Intent intentService = new Intent(getApplicationContext(), SaatServis.class);
            startService(intentService);//Servisi başlatır

            j=0;k=0;tempYanlis[j]=0;tempDogru[j]=0;
            Intent intent = new Intent(StudentTestActivity.this, StudentActivity.class);
            startActivity(intent);
            finish();
            return;

        }
        else if(k==soru.length-1 && j!=konu.length-1){j++;k=0;}
        else{k++;}

        testInfo(i,j,k);

        getQuizInfo(i,j,k);
    }

    private void getQuizInfo(int i,int j,int k)
    {
        mQuiz=mHocaDatabase.child(test[i]).child(konu[j]).child(soru[k]);

        mQuiz.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                if(dataSnapshot.exists()){
                    if(map.get("soru")!=null) {
                        String ogrSoru = map.get("soru").toString();
                        tvSoru.setText("soru:" +ogrSoru );
                    }
                    if(map.get("cevapA")!=null) {
                        String ogrCevapA = map.get("cevapA").toString();
                        tvCevapA.setText("cevap A:" + ogrCevapA);
                    }
                    if(map.get("cevapB")!=null) {
                        String ogrCevapB = map.get("cevapB").toString();
                        tvCevapB.setText("cevap B:" + ogrCevapB);
                    }
                    if(map.get("cevapC")!=null) {
                        String ogrCevapC = map.get("cevapC").toString();
                        tvCevapC.setText("cevap C:" + ogrCevapC);
                    }
                    if(map.get("cevapD")!=null) {
                        String ogrCevapD = map.get("cevapD").toString();
                        tvCevapD.setText("cevap D:" + ogrCevapD);
                    }
                    if(map.get("cevap")!=null) {
                        ogrCevap = map.get("cevap").toString();
                    }

                    Glide.clear(imA);
                    if(map.get("cevapImageA")!=null){
                        imUrlA = map.get("cevapImageA").toString();
                        switch(imUrlA) {
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.background).into(imA);
                                break;
                            default:
                                Glide.with(getApplication()).load(imUrlA).into(imA);
                                break;
                        }
                    }
                    Glide.clear(imB);
                    if(map.get("cevapImageB")!=null){
                        imUrlB = map.get("cevapImageB").toString();
                        switch(imUrlB) {
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.background).into(imB);
                                break;
                            default:
                                Glide.with(getApplication()).load(imUrlB).into(imB);
                                break;
                        }
                    }
                    Glide.clear(imC);
                    if(map.get("cevapImageC")!=null){
                        imUrlC = map.get("cevapImageC").toString();
                        switch(imUrlC) {
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.background).into(imC);
                                break;
                            default:
                                Glide.with(getApplication()).load(imUrlC).into(imC);
                                break;
                        }
                    }
                    Glide.clear(imD);
                    if(map.get("cevapImageD")!=null){
                        imUrlD = map.get("cevapImageD").toString();
                        switch(imUrlD) {
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.background).into(imD);
                                break;
                            default:
                                Glide.with(getApplication()).load(imUrlD).into(imD);
                                break;
                        }
                    }
                }
                else
                {
                    rbCev="E";
                    getButton();
                    //finish();
                    //Toast.makeText(StudentTestActivity.this,"Test yüklenemedi...",Toast.LENGTH_LONG).show();

                    /*
                    tvSoru.setText("");
                    tvCevapA.setText("");
                    tvCevapB.setText("");
                    tvCevapC.setText("");
                    tvCevapD.setText("");

                    Glide.clear(imA);
                    Glide.clear(imB);
                    Glide.clear(imC);
                    Glide.clear(imD);
                    Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(imA);
                    Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(imB);
                    Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(imC);
                    Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(imD);

                     */
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void testInfo(int i,int j,int k){
        tvTestSayi.setText("Test: "+test[i]);
        tvKonuSayi.setText("Konu: "+konu[j]);
        tvSoruSayi.setText("Soru: "+soru[k]);
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;

                Toast.makeText(StudentTestActivity.this,"Test Bitmiştir",Toast.LENGTH_SHORT).show();

                i++;
                Map cozuldu = new HashMap<>();
                cozuldu.put("cozuldu",i);
                mUserDatabase.updateChildren(cozuldu);

                Map saat = new HashMap<>();
                saat.put("birGunDolduMu",0);
                mUserDatabase.updateChildren(saat);

                Intent intentService = new Intent(getApplicationContext(), SaatServis.class);
                startService(intentService);//Servisi başlatır

                j=0;k=0;tempYanlis[j]=0;tempDogru[j]=0;
                Intent intent = new Intent(StudentTestActivity.this, StudentActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        }.start();

        mTimerRunning = true;

    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        tvCountDown.setText(timeLeftFormatted);
    }
}
