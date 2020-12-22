package com.koksalyazilim.namazvakitleri;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VakitIndirActivity extends AppCompatActivity {

     //Vakitleri indir
    //Vakitler indirilince AnaActivity 'e yolla, Artık herşey Hazır..


    SharedPreferences sharedPreferences;
    static SQLiteDatabase myDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vakit_indir);

        sharedPreferences = this.getSharedPreferences("com.koksalyazilim.namazvakitleri",Context.MODE_PRIVATE);

       fontAyarlama();
       vakitIndir();
    }



    private void fontAyarlama() {
        TextView vakitindrtext,herseyhazirtext;
        vakitindrtext = findViewById(R.id.vakitIndiriliyor_text);
        herseyhazirtext = findViewById(R.id.herseyHazit_text);
        //Fontu tanımladım
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/UPBOLTERS New-Regular.otf");
        vakitindrtext.setTypeface(typeface);
        herseyhazirtext.setTypeface(typeface);
    }


    private void vakitIndir() {
        //Vakitleri indirme ve veri tabanına kaydetme işlemi
        try {

            VakitDownload vakitDownload = new VakitDownload();

            String vakit = "https://ezanvakti.herokuapp.com/vakitler?ilce=";
            String kayitliIlceKodu =sharedPreferences.getString("kayitliilce","none");  //ilçe verisi aındı

            String vakitSecimi = vakit+kayitliIlceKodu;
            vakitDownload.execute(vakitSecimi);

            System.out.println("DURUM: ŞEHİR VE İLÇE BULUNDU VAKİT İNDİRİLECEK");


        }catch (Exception e){
            System.out.println("DURUM: ŞEHİR VE İLÇE BULUNAMADI VAKİT İNDİRELİMİYOR HATA KODU--> "+e);
            //Bişeyler yanlış gitti yolla
            Intent yanliGitti = new Intent(getApplicationContext(),YanlisGittiActivity.class);
            startActivity(yanliGitti);
        }
    }






    ////////////////////// ////////////////////// VAKİT VERİSİ İNDİRME ////////////////////////////////////////////

    private class VakitDownload extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) { //arkaplanda al

            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {

                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data > 0){

                    char character  = (char) data;
                    result+= character;

                    data = inputStreamReader.read();

                }

                return result;

            }catch (Exception e){
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) { //alma ilmei bitince ne olsun
            super.onPostExecute(s);

            System.out.println("alınan data: "+ s); //data kontrol (url doğru mu)

            //VERİ TABANI KAYDI BURADA OLACAK 9 VERİ KAYDOLACAK (Veri sayısı arttı artık ayın şekli falan daha var)

            String sehir = sharedPreferences.getString("kayitlisehir","none");  //ilçe verisi aındı
            String ilce = sharedPreferences.getString("kayitliilce","none");  //ilçe verisi aındı




            try {

                JSONArray jsonArray = new JSONArray(s);
                // System.out.println("TAHA "+jsonArray); //ANA 1. PARCA


                //VAKİT ADI VE KODUNU ALAN KODALAR

                int a=-1;
                while (jsonArray.length()>10){ a++;

                    String asda = jsonArray.getString(a); //VAKİT SIRASI İLE ALINMASI İÇİN BUBU ARTIRARAK DİĞER veriler GELİR


                    JSONObject jsonObject = new JSONObject(asda);

                    String tarih = jsonObject.getString("MiladiTarihKisa");        //TARİH ALDIK (KAYIT KAYIT EDİLECEK)
                    String gunes = jsonObject.getString("Gunes");                 //GÜNEŞ ALDIK (KAYIT EDİLECEK)
                    String gunesBatis = jsonObject.getString("GunesBatis");      //GÜNEŞ BATIŞ NAMZI ALDIK (KAYIT KAYIT EDİLECEK)
                    String gunesDogus = jsonObject.getString("GunesDogus");     //GÜNEŞ DOĞUŞ ALDIK (KAYIT KAYIT EDİLECEK)
                    String imsak = jsonObject.getString("Imsak");              //İMSAK ALDIK (KAYIT EDİLECEK)
                    String ogle = jsonObject.getString("Ogle");              //ÖĞLE NAMZI ALDIK (KAYIT KAYIT EDİLECEK)
                    String ikindi = jsonObject.getString("Ikindi");         //İKİNDİ NAMZI ALDIK (KAYIT KAYIT EDİLECEK)
                    String aksam = jsonObject.getString("Aksam");          //AKSAM NAMZI ALDIK (KAYIT KAYIT EDİLECEK)
                    String yatsi = jsonObject.getString("Yatsi");         //YATSI NAMZI ALDIK (KAYIT KAYIT EDİLECEK)
                    String ayinSekli = jsonObject.getString("AyinSekliURL");       //AYIN ŞEKLİNİ ALDIK (KAYIT KAYIT EDİLECEK)
                    String hicriTarih = jsonObject.getString("HicriTarihUzun");       //AYIN ŞEKLİNİ ALDIK (KAYIT KAYIT EDİLECEK)
                    String kibleSaati = jsonObject.getString("KibleSaati");       //AYIN ŞEKLİNİ ALDIK (KAYIT KAYIT EDİLECEK)

                    System.out.println("AKSAM NAMAZI:"+sehir+" "+ilce+" "+tarih+" "+gunes+" "+gunesBatis+" "+gunesDogus+" "+imsak+" "+ogle+" "+ikindi+" "+aksam+" "+yatsi+" "+ayinSekli+" "+hicriTarih+" "+kibleSaati); //LOGA BASTIK



                    //GELEN VERİLERİ VERİTABANINA KAYDETME KISMI            !!!!ÖNEMLİ BURASI


                    try {
                        myDatabase = getApplicationContext().openOrCreateDatabase("Vakitlerrr",MODE_PRIVATE,null);
                        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS vakitverisi (il VARCHAR, ilce VARCHAR, tarih VARCHAR, gunes VARCHAR, gunesBatis VARCHAR, gunesDogus VARCHAR, imsak VARCHAR, ogle VARCHAR, ikindi VARCHAR, aksam VARCHAR, yatsi VARCHAR, ayinSekli VARCHAR, hicriTarih VARCHAR, kibleSaati VARCHAR)");

                        String sqlString = "INSERT INTO vakitverisi (il, ilce, tarih, gunes, gunesBatis, gunesDogus, imsak, ogle, ikindi, aksam, yatsi, ayinSekli, hicriTarih, kibleSaati) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        SQLiteStatement statement = myDatabase.compileStatement(sqlString);

                        statement.bindString(1,sehir);
                        statement.bindString(2,ilce);
                        statement.bindString(3,tarih);
                        statement.bindString(4,gunes);
                        statement.bindString(5,gunesBatis);
                        statement.bindString(6,gunesDogus);
                        statement.bindString(7,imsak);
                        statement.bindString(8,ogle);
                        statement.bindString(9,ikindi);
                        statement.bindString(10,aksam);
                        statement.bindString(11,yatsi);
                        statement.bindString(12,ayinSekli);
                        statement.bindString(13,hicriTarih);
                        statement.bindString(14,kibleSaati);

                        statement.execute();

                        System.out.println("DURUM: VERİ TABANINA VAKİT KAYDI BAŞARILI"+a);
                        Toast.makeText(VakitIndirActivity.this, "Vakitler başarıyla cihazınıza kaydedildi", Toast.LENGTH_SHORT).show();

                        sharedPreferences.edit().putString("info","old").apply();

                        //kayıt başarılı oldu artık kullanıcıyı vakitler görmesi için yolla
                        Intent anaActivity = new Intent(getApplicationContext(),AnaActivity.class);
                        startActivity(anaActivity);

                    }catch (Exception e){

                        System.out.println("DURUM: VERİ TABANINA VAKİT KAYDI BAŞARISIZ HATA KODU--> "+e);
                        sharedPreferences.edit().putString("info","new").apply();

                        Toast.makeText(VakitIndirActivity.this, "ÜZGÜNÜM!\nVakitler cihazınıza indirilemedi", Toast.LENGTH_SHORT).show();

                        Intent yanliGitti = new Intent(getApplicationContext(),YanlisGittiActivity.class);
                        startActivity(yanliGitti);
                    }



                }


            }catch (Exception e){
                //System.out.println("DURUM: SUNUCUDAN VERİ ALINAMADI VAKİTLERE ULAŞILAMIYOR HATA KODU--> "+e);
                //Intent yanliGitti = new Intent(getApplicationContext(),YanlisGittiActivity.class);
                //startActivity(yanliGitti);
            }

        }
    }


}
