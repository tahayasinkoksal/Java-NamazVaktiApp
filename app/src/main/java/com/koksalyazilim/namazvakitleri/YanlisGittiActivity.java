package com.koksalyazilim.namazvakitleri;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class YanlisGittiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yanlis_gitti);

        TextView yanlisGitti,tekrarBasla,internetText,sunucuText;

        yanlisGitti = findViewById(R.id.yanlisGittitextView);
        tekrarBasla = findViewById(R.id.tekrarBaslabutton);
        internetText = findViewById(R.id.internettextView);
        sunucuText = findViewById(R.id.sunucutextView);

        //Fontu tanımladım
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/UPBOLTERS New-Regular.otf");
        yanlisGitti.setTypeface(typeface);
        tekrarBasla.setTypeface(typeface);
        internetText.setTypeface(typeface);
        sunucuText.setTypeface(typeface);
    }
    //Kullanıcı Buraya Geldiyse Bişeyler Ters Gitti Demek Baştan Başlat Adamı
    //Ya İnternet Yok   YADA   Sunucudan cevap gelmiyo demek.

    public void basaDon (View view){
        //Baştan başlat

        Intent baslatActivity = new Intent(getApplicationContext(), BaslatActivity.class);
        startActivity(baslatActivity);
    }
}