package com.example.quiz.Teacher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.quiz.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TeacherTestActivity extends AppCompatActivity {

    private String[] test={"1","2","3","4"};
    private String[] konu={"Doğa ve İnsan","Dünya’nın Şekli ve Hareketleri","Coğrafi Konum","Harita Bilgisi","İklim Bilgisi","Yerin Şekillenmesi","Doğanın Varlıkları","Beşeri Yapı","Nüfusun Gelişimi","Göç Nedenleri ve Sonuçları"};
    private String[] soru={"1","2","3","4","5","6","7","8","9"};
    private int size=test.length;
    private static int i,j=0,k=0;
    private Spinner spTest,spKonu;
    private Button btnEkle;
    private EditText editTextsoru,editTextcvpA,editTextcvpB,editTextcvpC,editTextcvpD;
    private RadioButton rButtonCevap;
    private RadioGroup rdGroupCevap;
    private TextView tvKonuSayi,tvSoruSayi;
    private ImageView imA,imB,imC,imD,imSoru;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase,mQuiz,mBilgiDb,ogrenciDb;

    private String userId,textTest,textKonu,textSoru,soruYukle,cevapYukleA,cevapYukleB,cevapYukleC,cevapYukleD,imUrlA,imUrlB,imUrlC,imUrlD,imUrlSoru;
    private static String ogrenciId;
    private Uri uriA,uriB,uriC,uriD,uriSoru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_test);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        btnEkle=(Button)findViewById(R.id.buttonEkle);

        rdGroupCevap=(RadioGroup)findViewById(R.id.radioGroupCevap);

        editTextsoru=(EditText)findViewById(R.id.editTextSoru);
        editTextcvpA=(EditText)findViewById(R.id.editTextA);
        editTextcvpB=(EditText)findViewById(R.id.editTextB);
        editTextcvpC=(EditText)findViewById(R.id.editTextC);
        editTextcvpD=(EditText)findViewById(R.id.editTextD);

        imA = (ImageView) findViewById(R.id.ImageA);
        imB = (ImageView) findViewById(R.id.ImageB);
        imC = (ImageView) findViewById(R.id.ImageC);
        imD = (ImageView) findViewById(R.id.ImageD);
        imSoru=(ImageView)findViewById(R.id.imageSoru);

        tvKonuSayi = (TextView) findViewById(R.id.textViewKonuSayi);
        tvSoruSayi = (TextView) findViewById(R.id.textViewSoruSayi);

        textKonu=konu[j];
        textSoru=soru[k];

        spTest=(Spinner)findViewById(R.id.spinnerTestSayi);
        spKonu=(Spinner)findViewById(R.id.spinnerKonuSayi);

        ArrayAdapter<String> adapterTest=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, test);;
        adapterTest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTest.setAdapter(adapterTest);

        ArrayAdapter<String> adapterKonu=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, konu);;
        adapterKonu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKonu.setAdapter(adapterKonu);

        spTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textTest=spTest.getSelectedItem().toString();
                i=Integer.parseInt(textTest);
                testInfo(i,j,k);
                getQuizInfo(i,j,k);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spKonu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textKonu=spKonu.getSelectedItem().toString();
                j=spKonu.getSelectedItemPosition();
                testInfo(i,j,k);
                getQuizInfo(i,j,k);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        testInfo(i,j,k);


        btnEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rdGroupCevap.getCheckedRadioButtonId();

                rButtonCevap= (RadioButton) findViewById(selectedId);
                if(rButtonCevap==null){
                    Toast.makeText(TeacherTestActivity.this, "Cevap boş bırakılamaz.", Toast.LENGTH_SHORT).show();
                    return;
                }
                setQuizInfo(i,j,k);

            }
        });

        imA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        imB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });
        imC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 3);
            }
        });
        imD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 4);
            }
        });
        imSoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 5);
            }
        });

    }


    private void setQuizInfo(int i,int j,int k){

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("Soru").child(test[i-1]).child(konu[j]).child(soru[k]);

        soruYukle = editTextsoru.getText().toString();
        cevapYukleA = editTextcvpA.getText().toString();
        cevapYukleB = editTextcvpB.getText().toString();
        cevapYukleC = editTextcvpC.getText().toString();
        cevapYukleD = editTextcvpD.getText().toString();

        Map userInfo = new HashMap<>();
        userInfo.put("soru",soruYukle);
        userInfo.put("cevapA",cevapYukleA);
        userInfo.put("cevapB",cevapYukleB);
        userInfo.put("cevapC",cevapYukleC);
        userInfo.put("cevapD",cevapYukleD);
        userInfo.put("cevap",rButtonCevap.getText().toString());
        userInfo.put("soruImage","default");
        userInfo.put("cevapImageA","default");
        userInfo.put("cevapImageB","default");
        userInfo.put("cevapImageC","default");
        userInfo.put("cevapImageD","default");

        mUserDatabase.updateChildren(userInfo);

        if(uriA!=null){
            setImageUrl(uriA,"cevapImageA");
        }
        if(uriB!=null){
            setImageUrl(uriB,"cevapImageB");
        }
        if(uriC!=null){
            setImageUrl(uriC,"cevapImageC");
        }
        if(uriD!=null){
            setImageUrl(uriD,"cevapImageD");
        }
        if(uriSoru!=null){
            setImageUrl(uriSoru,"soruImage");
        }
    }

    private void setImageUrl(Uri ur,final String cevapImageX){

            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("cevapImages").child(userId).child(cevapImageX);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), ur);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map userInfo = new HashMap();
                            userInfo.put(cevapImageX, uri.toString());
                            mUserDatabase.updateChildren(userInfo);

                            return;
                        }
                    });
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUriA = data.getData();
            uriA=imageUriA;imA.setImageURI(uriA);
        }
        if(requestCode == 2 && resultCode == Activity.RESULT_OK) {
            final Uri imageUriB = data.getData();
            uriB=imageUriB;imB.setImageURI(uriB);
        }
        if(requestCode == 3 && resultCode == Activity.RESULT_OK) {
            final Uri imageUriC = data.getData();
            uriC=imageUriC;imC.setImageURI(uriC);
        }
        if(requestCode == 4 && resultCode == Activity.RESULT_OK) {
            final Uri imageUriD = data.getData();
            uriD=imageUriD;imD.setImageURI(uriD);
        }
        if(requestCode == 5 && resultCode == Activity.RESULT_OK) {
            final Uri soruUri = data.getData();
            uriSoru=soruUri;imSoru.setImageURI(uriSoru);
        }
    }




    public void geri(View view) {
        if(j==0 && k==0){
            Toast.makeText(TeacherTestActivity.this,"Geri gitmez",Toast.LENGTH_SHORT).show();
        }
        else if(k==0 && j!=0){
            j--;
            k=konu.length-1;
        }
        else{
            k--;
        }

        testInfo(i,j,k);

        getQuizInfo(i,j,k);

        Toast.makeText(TeacherTestActivity.this,"j:"+j+" k:"+k,Toast.LENGTH_SHORT).show();
    }


    public void ileri(View view) {
        if(j==konu.length-1 && k==soru.length-1){
            Toast.makeText(TeacherTestActivity.this,"İleri gitmez",Toast.LENGTH_SHORT).show();
        }
        else if(k==soru.length-1 && j!=konu.length-1){
            j++;
            k=0;
        }
        else
        {
            k++;
        }

        testInfo(i,j,k);

        getQuizInfo(i,j,k);

        Toast.makeText(TeacherTestActivity.this,"j:"+j+" k:"+k,Toast.LENGTH_SHORT).show();
    }

    private void getQuizInfo(int i,int j,int k)
    {
        mQuiz = FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("Soru").child(test[i-1]).child(konu[j]).child(soru[k]);

        mQuiz.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                if(dataSnapshot.exists()) {
                    if (map.get("soru") != null) {
                        String ogrSoru = map.get("soru").toString();
                        editTextsoru.setText(ogrSoru);
                    }
                    if (map.get("cevapA") != null) {
                        String ogrCevapA = map.get("cevapA").toString();
                        editTextcvpA.setText(ogrCevapA);
                    }
                    if (map.get("cevapB") != null) {
                        String ogrCevapB = map.get("cevapB").toString();
                        editTextcvpB.setText(ogrCevapB);
                    }
                    if (map.get("cevapC") != null) {
                        String ogrCevapC = map.get("cevapC").toString();
                        editTextcvpC.setText(ogrCevapC);
                    }
                    if (map.get("cevapD") != null) {
                        String ogrCevapD = map.get("cevapD").toString();
                        editTextcvpD.setText(ogrCevapD);
                    }
                    Glide.clear(imA);
                    if(map.get("cevapImageA")!=null){
                        imUrlA = map.get("cevapImageA").toString();
                        switch(imUrlA) {
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.add).into(imA);
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
                                Glide.with(getApplication()).load(R.drawable.add).into(imB);
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
                                Glide.with(getApplication()).load(R.drawable.add).into(imC);
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
                                Glide.with(getApplication()).load(R.drawable.add).into(imD);
                                break;
                            default:
                                Glide.with(getApplication()).load(imUrlD).into(imD);
                                break;
                        }
                    }
                    Glide.clear(imSoru);
                    if(map.get("soruImage")!=null){
                         imUrlSoru= map.get("soruImage").toString();
                        switch(imUrlSoru) {
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.add).into(imSoru);
                                break;
                            default:
                                Glide.with(getApplication()).load(imUrlSoru).into(imSoru);
                                break;
                        }
                    }

                }

                else
                {
                    editTextsoru.setText("");
                    editTextcvpA.setText("");
                    editTextcvpB.setText("");
                    editTextcvpC.setText("");
                    editTextcvpD.setText("");
                    Glide.clear(imA);
                    Glide.clear(imB);
                    Glide.clear(imC);
                    Glide.clear(imD);
                    Glide.clear(imSoru);
                    Glide.with(getApplication()).load(R.drawable.add).into(imA);
                    Glide.with(getApplication()).load(R.drawable.add).into(imB);
                    Glide.with(getApplication()).load(R.drawable.add).into(imC);
                    Glide.with(getApplication()).load(R.drawable.add).into(imD);
                    Glide.with(getApplication()).load(R.drawable.add).into(imSoru);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void testInfo(int i,int j,int k){
        tvKonuSayi.setText("Konu:"+konu[j]);
        tvSoruSayi.setText("Soru: "+soru[k]);
    }

}
