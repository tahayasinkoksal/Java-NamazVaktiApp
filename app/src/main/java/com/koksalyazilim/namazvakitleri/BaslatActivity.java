package com.koksalyazilim.namazvakitleri;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BaslatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baslat);

        TextView bilgiBaslik,bilgiMetni,butonBasla;

        bilgiBaslik = findViewById(R.id.bilgiBasliktextView);
        bilgiMetni = findViewById(R.id.bilgiMetnitextView);
        butonBasla = findViewById(R.id.buttonBasla);

        //Fontu tanımladım
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/UPBOLTERS New-Regular.otf");
        bilgiBaslik.setTypeface(typeface);
        bilgiMetni.setTypeface(typeface);
        butonBasla.setTypeface(typeface);
    }
    //Kullanıcıyı karşıla il defa uygulamayı açtığı için geldi buraya
    //İl defa geldiği için IlSecActivity 'e ekranına yolla


    public void sehirSec(View view){
        //Şehir Seçim Ekranına Yolla
        Intent sehirSecimi = new Intent(getApplicationContext(),SehirSecActivity.class);
        startActivity(sehirSecimi);
    }
}
