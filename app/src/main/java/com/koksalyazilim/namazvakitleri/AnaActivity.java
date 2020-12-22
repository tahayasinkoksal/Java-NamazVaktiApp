package com.koksalyazilim.namazvakitleri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AnaActivity extends AppCompatActivity {

    //Kullanıcının karşısına ilk burası gelecek
    //İlk defa burası açılıyosa BaslatActivity 'e yolla

    SharedPreferences sharedPreferences;
    Date tarih = new Date();
    SimpleDateFormat bugun;

    ImageView ayLogoImage;
    TextView tarihBugunText,gunesinDogusuText,kibleSaatiText,gunesinBatisiText,hicriTarihText,  imsakText,gunesText,ogleText,ikindiText,aksamText,yatsiText,kalanSureText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana);
        bugun = new SimpleDateFormat("dd.MM.yyyy");
        //textView.setText("Bugünün Tarihi "+bugun.format(tarih));
        tanimlama();
        veriAl();


        //Kayıtlı verilere erişim sağladık
        sharedPreferences = this.getSharedPreferences("com.koksalyazilim.namazvakitleri",Context.MODE_PRIVATE);
        String yeniMi =sharedPreferences.getString("info","none");

        yeniKontrol();
    }

    private void tanimlama() {
        ayLogoImage = findViewById(R.id.ayLogo_imageView);
        tarihBugunText = findViewById(R.id.tarihBugun_textView);
        gunesinDogusuText = findViewById(R.id.gunesinDogusu_textView);
        kibleSaatiText = findViewById(R.id.kibleSaati_textView);
        gunesinBatisiText = findViewById(R.id.gunesinBatısı_textView);
        hicriTarihText = findViewById(R.id.tarihHicri_textView);
        imsakText = findViewById(R.id.imsaktextView);
        gunesText = findViewById(R.id.gunestextView);
        ogleText = findViewById(R.id.ogletextView);
        ikindiText = findViewById(R.id.ikinditextView);
        aksamText = findViewById(R.id.aksamtextView);
        yatsiText = findViewById(R.id.yatsitextView);
        kalanSureText = findViewById(R.id.kalanSureTextView);
    }


    //KULLANICIYI KONTROL ET YENİ Mİ DEĞİLMİ BAK Bİ
    private void yeniKontrol() {
        String yeniMi =sharedPreferences.getString("info","none");
        try {
            if(yeniMi.matches("new")|| yeniMi.matches("none")){
                //adam ilk defa gedli
                System.out.println("DURUM: Yeni Kullanıcı Geldi "+yeniMi);
                Intent yeniKullanici = new Intent(getApplicationContext(),BaslatActivity.class);
                startActivity(yeniKullanici);

            }else if(yeniMi.matches("old")){
                //adam eski kullanıcı
                System.out.println("DURUM: Eski Kullanıcı Geldi "+yeniMi);
            }else if(yeniMi.matches("none")){
                //Yeni mi geldi Eski kullanıcı mı bilinmiyo
                System.out.println("DURUM: Kullanıcı Yeni mi Eski mi Bilinmiyor");
            }else {
                //Yeni mi geldi Eski kullanıcı mı bilinmiyo
            }

        }catch (Exception e){
            System.out.println("DURUM: AnaActivity HATA NEDENİ-->"+e);
        }
    }






    @SuppressLint("SetTextI18n")
    private void veriAl() {

        try {
            //Databaseye eriş vakitleri çek
            VakitIndirActivity.myDatabase = getApplicationContext().openOrCreateDatabase("Vakitlerrr",MODE_PRIVATE,null);
            VakitIndirActivity.myDatabase.execSQL("CREATE TABLE IF NOT EXISTS vakitverisi (il VARCHAR, ilce VARCHAR, tarih VARCHAR, gunes VARCHAR, gunesBatis VARCHAR, gunesDogus VARCHAR, imsak VARCHAR, ogle VARCHAR, ikindi VARCHAR, aksam VARCHAR, yatsi VARCHAR, ayinSekli VARCHAR, hicriTarih VARCHAR, kibleSaati VARCHAR)");

            //Vakitleri çekerken bu günun tarihiyle aynı olanları çek dedik    strftime('%d.%m.%Y','now')
            Cursor cursor = VakitIndirActivity.myDatabase.rawQuery("select * from vakitverisi where tarih=strftime('%d.%m.%Y','now')",null);
            // System.out.println("HATA cursor "+cursor.getCount()); veri sayısını veriyor bize 0 ise veri yok demek

            if (cursor.getCount() != 0){
                System.out.println("DURUM: Kayıtlı Veri Sayısı: "+cursor.getCount());   //cursor.getCount() kaç tane veri var demek

                int ilIx = cursor.getColumnIndex("il");
                int ilceIx = cursor.getColumnIndex("ilce");
                int tarihIx = cursor.getColumnIndex("tarih");
                int guneIx = cursor.getColumnIndex("gunes");
                int gunesDogusIx = cursor.getColumnIndex("gunesDogus");
                int gunesBatisIx = cursor.getColumnIndex("gunesBatis");
                int imsakIx = cursor.getColumnIndex("imsak");
                int ogleIx = cursor.getColumnIndex("ogle");
                int ikindiIx = cursor.getColumnIndex("ikindi");
                int aksamIx = cursor.getColumnIndex("aksam");
                int yatakIx = cursor.getColumnIndex("yatsi");
                int ayinSekliIx = cursor.getColumnIndex("ayinSekli");
                int hicriTarihIx = cursor.getColumnIndex("hicriTarih");
                int kibleSaatiIx = cursor.getColumnIndex("kibleSaati");






                while (cursor.moveToNext()){

                    String il = cursor.getString(ilIx);
                    String ilce = cursor.getString(ilceIx);
                    String tarih = cursor.getString(tarihIx);
                    String gunes = cursor.getString(guneIx);
                    String gunesDogus = cursor.getString(gunesDogusIx);
                    String gunesBatis = cursor.getString(gunesBatisIx);
                    String imsak = cursor.getString(imsakIx);
                    String ogle = cursor.getString(ogleIx);
                    String ikindi = cursor.getString(ikindiIx);
                    String aksam = cursor.getString(aksamIx);
                    String yatak = cursor.getString(yatakIx);
                    String ayinSekli = cursor.getString(ayinSekliIx);
                    String hicriTarih = cursor.getString(hicriTarihIx);
                    String kibleSaati = cursor.getString(kibleSaatiIx);




                    System.out.println("ARTIK "+tarih+" "+ ayinSekli+"   "+il+" "+ilce+" "+tarih+" "+gunes+" "+gunesBatis);









                    //ImageView ayResim = findViewById(R.id.ay_imageView);
                    //Picasso.get().load(ayinSekli).into(ayResim);

                    System.out.println("İL: "+il+" İLÇE: "+ilce+"tykfjmgm");
                    //TextView veri = findViewById(R.id.sehirTextView);
                    //veri.setText("İL: "+il+" İLÇE: "+ilce);


                    //<-------TANİMLAMA BAŞLIYOR------->


                    Picasso.get().load(ayinSekli).into(ayLogoImage);
                    tarihBugunText.setText(tarih);
                    gunesinDogusuText.setText(gunesDogus+"\nGüneşin\nDoğuşu");
                    kibleSaatiText.setText(kibleSaati+"\nKıble\nSaati");
                    gunesinBatisiText.setText(gunesBatis+"\nGüneşin\nBatışı");
                    hicriTarihText.setText(hicriTarih);
                    imsakText.setText("İmsak: "+imsak);
                    gunesText.setText("Güneş: "+gunes);
                    ogleText.setText("Öğle: "+ogle);
                    ikindiText.setText("İkindi: "+ikindi);
                    aksamText.setText("Akşam: "+aksam);
                    yatsiText.setText("Yatsı: "+yatak);

                }


                cursor.close();
            }

            if (cursor.getCount() == 0){
                System.out.println("DURUM: Kayıtlı Veri Yok -- Veri Sayısı--> "+cursor.getCount());
                Toast.makeText(this, "Kayıtlı vakit bulunamadı\nSizi vakit indirme kısmına yönlendiriyorum", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), VakitIndirActivity.class);
                startActivity(intent);
            }







        }

        catch (Exception e){
            System.out.println("DURUM: VERİTABANI VAKİT ÇEKME BAŞARISIZ HATA KODU--> "+e);
            System.out.println(e);
            //Toast.makeText(this, "Cihazınızda kayıtlı vakitler çekilirken hata oluştu \nMuhtemelen 1 aylık vakitler bitti \nsizi vakit güncelleme kısmında yönlendiriyorum..", Toast.LENGTH_SHORT).show();

            //Intent yanliGitti = new Intent(getApplicationContext(),VakitIndirActivity.class);
            //startActivity(yanliGitti);
        }


    }

     public void linkGonder(View view){

         if (view.getId()==R.id.koksal_imageView || view.getId()==R.id.koksal_textView){
             Uri linkimiz=Uri.parse(getString(R.string.koksalyazilim_url)); //butona vermek istediğimiz linki buraya yazıyoruz.
             Intent intentimiz =new Intent(Intent.ACTION_VIEW,linkimiz);
             startActivity(intentimiz);
         }
         if (view.getId()==R.id.diger_imageView || view.getId()==R.id.diger_textView){
             Uri linkimiz=Uri.parse(getString(R.string.tum_uygulamalar)); //butona vermek istediğimiz linki buraya yazıyoruz.
             Intent intentimiz =new Intent(Intent.ACTION_VIEW,linkimiz);
             startActivity(intentimiz);
         }
         if (view.getId()==R.id.kuran_imageView || view.getId()==R.id.kuran_textView){
             Uri linkimiz=Uri.parse("https://play.google.com/store/apps/details?id=com.koksalyazilim.kuranoku&hl=tr"); //butona vermek istediğimiz linki buraya yazıyoruz.
             Intent intentimiz =new Intent(Intent.ACTION_VIEW,linkimiz);
             startActivity(intentimiz);
         }


    }


}
