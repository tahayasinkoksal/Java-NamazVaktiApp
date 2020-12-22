package com.koksalyazilim.namazvakitleri;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SehirSecActivity extends AppCompatActivity {

    ListView listView;
    List<String> sehirKodulist;
    List<String> sehirlist;
    SharedPreferences sharedPreferences;

    //Kullanıcı şehir seçecek
    //Şehir seçildi kullanıcıyı IlceSecActivity 'e yolla


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sehir_sec);

        TextView sehirSecText,devamEtButtonText;

        sehirSecText = findViewById(R.id.sehirSecText);
        devamEtButtonText = findViewById(R.id.devamEtButton);

        //Fontu tanımladım
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/UPBOLTERS New-Regular.otf");
        sehirSecText.setTypeface(typeface);
        devamEtButtonText.setTypeface(typeface);

        listView = findViewById(R.id.sehirListView);  //Şehir listView i tanımladık
        sehirKodulist = new ArrayList<>();  //Şehir kodunu kayetmek için array oluşturduk
        sehirlist = new ArrayList<>();      //Şehir adını kayetmek için array oluşturduk

        sharedPreferences = this.getSharedPreferences("com.koksalyazilim.namazvakitleri",Context.MODE_PRIVATE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(SehirSecActivity.this, "Şehir "+sehirlist.get(position)+" olarak seçildi", Toast.LENGTH_SHORT).show();
                sharedPreferences.edit().putString("kayitlisehir",sehirKodulist.get(position)).apply(); //list view den seçilen şehrin kodunu kaydettik

            }
        });

        sehirVerisiCek();

    }



    public void ilceSec(View view){
        //ilçe seçim kısmına yolla
        Intent ilceSecAcvitity = new Intent(getApplicationContext(), IlceSecActivity.class);
        startActivity(ilceSecAcvitity);

    }






    private void sehirVerisiCek() {
        try {
            //Veriyi çekmeye çalış çekersen aşağıdaki fonksiyona yolla

            DownloadData downloadData = new DownloadData();
            String sehir = "https://ezanvakti.herokuapp.com/sehirler?ulke=2";
            downloadData.execute(sehir);


        }catch (Exception e){
            //Çekme işlemi sıkıtı olursa (internet yoktur veya sunucusan istek sınırına takılmışsa) yanlış gittiye yolla
            System.out.println("DURUM: URL İLETİLEMEDİ");
            Intent yanliGitti = new Intent(getApplicationContext(),YanlisGittiActivity.class);
            startActivity(yanliGitti);
        }
    }


    ////////////////////// ////////////////////// ŞEHİR VERİSİ İNDİRME ////////////////////////////////////////////
    private class DownloadData extends AsyncTask<String, Void, String> {

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

           // System.out.println("alınan data: "+ s); //data kontrol (url doğru mu)

            try {

                JSONArray jsonArray = new JSONArray(s);
                //System.out.println("TAHA "+jsonArray); //ANA 1. PARCA


                //ŞEHİR ADI VE KODUNU ALAN KODALAR





                for (int i=0; i<jsonArray.length(); i++){
                    String asda = jsonArray.getString(i); //ŞEHRİN SIRASI İLE ALINMASI İÇİN BUBU ARTIRARAK DİĞER ŞEHİRLER GELİR


                    JSONObject jsonObject = new JSONObject(asda);
                    String sehirAdi = jsonObject.getString("SehirAdi"); //ŞEHRİN ADINI ALDIK (KAYIT KAYIT EDİLECEK)
                    String sehirId = jsonObject.getString("SehirID");   //ŞEHRİN KODUNU ALDIK (KAYIT EDİLECEK)
                    System.out.println("SEHIR ADI:  "+sehirAdi+" SEHIR KODU:  "+sehirId); //LOGA BASTIK


                    //GELEN SEHİR ADI LİST VİEW E BASILCAK KULLANICI ŞEHİR SEÇİNCE TEK VERİ OLARAK KAYDEDİLECEK
                    sehirlist.add(sehirAdi);
                    sehirKodulist.add(sehirId);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, sehirlist);
                    listView.setAdapter(arrayAdapter);



                }




            }catch (Exception e){
                System.out.println("DURUM: ŞEHİR LİSTESİ ALINAMDI HATA KODU-> "+e);
                Intent yanliGitti = new Intent(getApplicationContext(),YanlisGittiActivity.class);
                startActivity(yanliGitti);
            }

        }
    }
}
