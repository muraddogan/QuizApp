package com.example.quiz.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.quiz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Map;

public class TeacherResultActivity extends AppCompatActivity {

    private String[] test={"1","2","3","4","5"};
    private String[] konu={"Doğa ve İnsan","Dünya’nın Şekli ve Hareketleri","Coğrafi Konum","a","b","c","d","e","f","g"};
    private static String userId,ogrenciId;
    private static int toplamYanlis,toplamDogru;
    private String[] ogrYanlis={"","","","","","","","","",""},ogrDogru={"","","","","","","","","",""};
    private FirebaseAuth mAuth;
    private DatabaseReference mBilgiDb;
    TextView tvSonuc1,tvSonuc2,tvSonuc3,tvSonuc4,tvSonuc5;
    GraphView graph11,graph12,graph21,graph22,graph31,graph32,graph41,graph42,graph51,graph52;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_result);

        tvSonuc1 = findViewById(R.id.twSonuc1);
        tvSonuc2 = findViewById(R.id.twSonuc2);
        tvSonuc3 = findViewById(R.id.twSonuc3);
        tvSonuc4 = findViewById(R.id.twSonuc4);
        tvSonuc5 = findViewById(R.id.twSonuc5);

        graph11=(GraphView)findViewById(R.id.graph1_1);
        graph12=(GraphView)findViewById(R.id.graph1_2);
        graph21=(GraphView)findViewById(R.id.graph2_1);
        graph22=(GraphView)findViewById(R.id.graph2_2);
        graph31=(GraphView)findViewById(R.id.graph3_1);
        graph32=(GraphView)findViewById(R.id.graph3_2);
        graph41=(GraphView)findViewById(R.id.graph4_1);
        graph42=(GraphView)findViewById(R.id.graph4_2);
        graph51=(GraphView)findViewById(R.id.graph5_1);
        graph52=(GraphView)findViewById(R.id.graph5_2);

        for(int a=0;a<5;a++){
            for(int b=0;b<10;b++){
                sonucOgren(a,b);
            }
        }
    }

    private void sonucOgren(final int i,final int j){

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mBilgiDb = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
        mBilgiDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                if(dataSnapshot.exists()) {
                }
                if (map.get("ogrenci") != null) {
                    ogrenciId = map.get("ogrenci").toString();

                    DatabaseReference mResultDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(ogrenciId).child("Durum").child(test[i]).child(konu[j]);

                    mResultDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            if (dataSnapshot.exists()) {
                                if (map.get("Yanlis") != null) {
                                    ogrYanlis[j] = map.get("Yanlis").toString();
                                }
                                if (map.get("Dogru") != null) {
                                    ogrDogru[j] = map.get("Dogru").toString();
                                }
                                if (i == 0) {
                                    toplamYanlis=toplamYanlis+Integer.parseInt(ogrYanlis[j]);
                                    toplamDogru=toplamDogru+Integer.parseInt(ogrDogru[j]);
                                    tvSonuc1.setText("Toplam Yanlış:" + toplamYanlis + "\n"+"Toplam Doğru:"+toplamDogru);
                                    if(j==9) {
                                        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[]{
                                                new DataPoint(0, Integer.parseInt(ogrYanlis[0])),
                                                new DataPoint(1, Integer.parseInt(ogrYanlis[1])),
                                                new DataPoint(2, Integer.parseInt(ogrYanlis[2])),
                                                new DataPoint(3, Integer.parseInt(ogrYanlis[3])),
                                                new DataPoint(4, Integer.parseInt(ogrYanlis[4])),
                                                new DataPoint(5, Integer.parseInt(ogrYanlis[5])),
                                                new DataPoint(6, Integer.parseInt(ogrYanlis[6])),
                                                new DataPoint(7, Integer.parseInt(ogrYanlis[7])),
                                                new DataPoint(8, Integer.parseInt(ogrYanlis[8])),
                                                new DataPoint(9, Integer.parseInt(ogrYanlis[9])),
                                        });
                                        graph11.addSeries(series1);
                                        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[]{
                                                new DataPoint(0, Integer.parseInt(ogrDogru[0])),
                                                new DataPoint(1, Integer.parseInt(ogrDogru[1])),
                                                new DataPoint(2, Integer.parseInt(ogrDogru[2])),
                                                new DataPoint(3, Integer.parseInt(ogrDogru[3])),
                                                new DataPoint(4, Integer.parseInt(ogrDogru[4])),
                                                new DataPoint(5, Integer.parseInt(ogrDogru[5])),
                                                new DataPoint(6, Integer.parseInt(ogrDogru[6])),
                                                new DataPoint(7, Integer.parseInt(ogrDogru[7])),
                                                new DataPoint(8, Integer.parseInt(ogrDogru[8])),
                                                new DataPoint(9, Integer.parseInt(ogrDogru[9])),
                                        });
                                        graph12.addSeries(series2);
                                    }
                                }

                                if (i == 1) {
                                    toplamYanlis=toplamYanlis+Integer.parseInt(ogrYanlis[j]);
                                    toplamDogru=toplamDogru+Integer.parseInt(ogrDogru[j]);
                                    tvSonuc2.setText("Toplam Yanlış:" + toplamYanlis + "\n"+"Toplam Doğru:"+toplamDogru);
                                    if(j==9) {
                                        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[]{
                                                new DataPoint(0, Integer.parseInt(ogrYanlis[0])),
                                                new DataPoint(1, Integer.parseInt(ogrYanlis[1])),
                                                new DataPoint(2, Integer.parseInt(ogrYanlis[2])),
                                                new DataPoint(3, Integer.parseInt(ogrYanlis[3])),
                                                new DataPoint(4, Integer.parseInt(ogrYanlis[4])),
                                                new DataPoint(5, Integer.parseInt(ogrYanlis[5])),
                                                new DataPoint(6, Integer.parseInt(ogrYanlis[6])),
                                                new DataPoint(7, Integer.parseInt(ogrYanlis[7])),
                                                new DataPoint(8, Integer.parseInt(ogrYanlis[8])),
                                                new DataPoint(9, Integer.parseInt(ogrYanlis[9])),
                                        });
                                        graph21.addSeries(series1);
                                        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[]{
                                                new DataPoint(0, Integer.parseInt(ogrDogru[0])),
                                                new DataPoint(1, Integer.parseInt(ogrDogru[1])),
                                                new DataPoint(2, Integer.parseInt(ogrDogru[2])),
                                                new DataPoint(3, Integer.parseInt(ogrDogru[3])),
                                                new DataPoint(4, Integer.parseInt(ogrDogru[4])),
                                                new DataPoint(5, Integer.parseInt(ogrDogru[5])),
                                                new DataPoint(6, Integer.parseInt(ogrDogru[6])),
                                                new DataPoint(7, Integer.parseInt(ogrDogru[7])),
                                                new DataPoint(8, Integer.parseInt(ogrDogru[8])),
                                                new DataPoint(9, Integer.parseInt(ogrDogru[9])),
                                        });
                                        graph22.addSeries(series2);
                                    }
                                }
                                if (i == 2) {
                                    toplamYanlis=toplamYanlis+Integer.parseInt(ogrYanlis[j]);
                                    toplamDogru=toplamDogru+Integer.parseInt(ogrDogru[j]);
                                    tvSonuc3.setText("Toplam Yanlış:" + toplamYanlis + "\n"+"Toplam Doğru:"+toplamDogru);
                                    if(j==9) {
                                        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[]{
                                                new DataPoint(0, Integer.parseInt(ogrYanlis[0])),
                                                new DataPoint(1, Integer.parseInt(ogrYanlis[1])),
                                                new DataPoint(2, Integer.parseInt(ogrYanlis[2])),
                                                new DataPoint(3, Integer.parseInt(ogrYanlis[3])),
                                                new DataPoint(4, Integer.parseInt(ogrYanlis[4])),
                                                new DataPoint(5, Integer.parseInt(ogrYanlis[5])),
                                                new DataPoint(6, Integer.parseInt(ogrYanlis[6])),
                                                new DataPoint(7, Integer.parseInt(ogrYanlis[7])),
                                                new DataPoint(8, Integer.parseInt(ogrYanlis[8])),
                                                new DataPoint(9, Integer.parseInt(ogrYanlis[9])),
                                        });
                                        graph31.addSeries(series1);
                                        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[]{
                                                new DataPoint(0, Integer.parseInt(ogrDogru[0])),
                                                new DataPoint(1, Integer.parseInt(ogrDogru[1])),
                                                new DataPoint(2, Integer.parseInt(ogrDogru[2])),
                                                new DataPoint(3, Integer.parseInt(ogrDogru[3])),
                                                new DataPoint(4, Integer.parseInt(ogrDogru[4])),
                                                new DataPoint(5, Integer.parseInt(ogrDogru[5])),
                                                new DataPoint(6, Integer.parseInt(ogrDogru[6])),
                                                new DataPoint(7, Integer.parseInt(ogrDogru[7])),
                                                new DataPoint(8, Integer.parseInt(ogrDogru[8])),
                                                new DataPoint(9, Integer.parseInt(ogrDogru[9])),
                                        });
                                        graph32.addSeries(series2);
                                    }
                                }
                                if (i == 3) {
                                    toplamYanlis=toplamYanlis+Integer.parseInt(ogrYanlis[j]);
                                    toplamDogru=toplamDogru+Integer.parseInt(ogrDogru[j]);
                                    tvSonuc4.setText("Toplam Yanlış:" + toplamYanlis + "\n"+"Toplam Doğru:"+toplamDogru);
                                    if(j==9) {
                                        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[]{
                                                new DataPoint(0, Integer.parseInt(ogrYanlis[0])),
                                                new DataPoint(1, Integer.parseInt(ogrYanlis[1])),
                                                new DataPoint(2, Integer.parseInt(ogrYanlis[2])),
                                                new DataPoint(3, Integer.parseInt(ogrYanlis[3])),
                                                new DataPoint(4, Integer.parseInt(ogrYanlis[4])),
                                                new DataPoint(5, Integer.parseInt(ogrYanlis[5])),
                                                new DataPoint(6, Integer.parseInt(ogrYanlis[6])),
                                                new DataPoint(7, Integer.parseInt(ogrYanlis[7])),
                                                new DataPoint(8, Integer.parseInt(ogrYanlis[8])),
                                                new DataPoint(9, Integer.parseInt(ogrYanlis[9])),
                                        });
                                        graph41.addSeries(series1);
                                        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[]{
                                                new DataPoint(0, Integer.parseInt(ogrDogru[0])),
                                                new DataPoint(1, Integer.parseInt(ogrDogru[1])),
                                                new DataPoint(2, Integer.parseInt(ogrDogru[2])),
                                                new DataPoint(3, Integer.parseInt(ogrDogru[3])),
                                                new DataPoint(4, Integer.parseInt(ogrDogru[4])),
                                                new DataPoint(5, Integer.parseInt(ogrDogru[5])),
                                                new DataPoint(6, Integer.parseInt(ogrDogru[6])),
                                                new DataPoint(7, Integer.parseInt(ogrDogru[7])),
                                                new DataPoint(8, Integer.parseInt(ogrDogru[8])),
                                                new DataPoint(9, Integer.parseInt(ogrDogru[9])),
                                        });
                                        graph42.addSeries(series2);
                                    }
                                }
                                if (i == 4) {
                                    toplamYanlis=toplamYanlis+Integer.parseInt(ogrYanlis[j]);
                                    toplamDogru=toplamDogru+Integer.parseInt(ogrDogru[j]);
                                    tvSonuc5.setText("Toplam Yanlış:" + toplamYanlis + "\n"+"Toplam Doğru:"+toplamDogru);
                                    if(j==9) {
                                        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[]{
                                                new DataPoint(0, Integer.parseInt(ogrYanlis[0])),
                                                new DataPoint(1, Integer.parseInt(ogrYanlis[1])),
                                                new DataPoint(2, Integer.parseInt(ogrYanlis[2])),
                                                new DataPoint(3, Integer.parseInt(ogrYanlis[3])),
                                                new DataPoint(4, Integer.parseInt(ogrYanlis[4])),
                                                new DataPoint(5, Integer.parseInt(ogrYanlis[5])),
                                                new DataPoint(6, Integer.parseInt(ogrYanlis[6])),
                                                new DataPoint(7, Integer.parseInt(ogrYanlis[7])),
                                                new DataPoint(8, Integer.parseInt(ogrYanlis[8])),
                                                new DataPoint(9, Integer.parseInt(ogrYanlis[9])),
                                        });
                                        graph51.addSeries(series1);
                                        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[]{
                                                new DataPoint(0, Integer.parseInt(ogrDogru[0])),
                                                new DataPoint(1, Integer.parseInt(ogrDogru[1])),
                                                new DataPoint(2, Integer.parseInt(ogrDogru[2])),
                                                new DataPoint(3, Integer.parseInt(ogrDogru[3])),
                                                new DataPoint(4, Integer.parseInt(ogrDogru[4])),
                                                new DataPoint(5, Integer.parseInt(ogrDogru[5])),
                                                new DataPoint(6, Integer.parseInt(ogrDogru[6])),
                                                new DataPoint(7, Integer.parseInt(ogrDogru[7])),
                                                new DataPoint(8, Integer.parseInt(ogrDogru[8])),
                                                new DataPoint(9, Integer.parseInt(ogrDogru[9])),
                                        });
                                        graph52.addSeries(series2);
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
