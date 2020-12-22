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

public class IlceSecActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ListView ilceSecListview;
    List<String> ilcelist;
    List<String> ilceKodulist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilce_sec);

        TextView ilceSecText,devamEtButtonText;

        ilceSecText = findViewById(R.id.ilceSecText);
        devamEtButtonText = findViewById(R.id.devamEtButton);

        //Fontu tanımladım
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/UPBOLTERS New-Regular.otf");
        ilceSecText.setTypeface(typeface);
        devamEtButtonText.setTypeface(typeface);


        ilceSecListview = findViewById(R.id.ilceListView); //sehir adını basmak için listView oluşturduk
        ilcelist = new ArrayList<>();         //ilçe adını koymak için array oluşturduk
        ilceKodulist = new ArrayList<>();      //ilçe kodunu koymak için array oluşturduk

        sharedPreferences = this.getSharedPreferences("com.koksalyazilim.namazvakitleri",Context.MODE_PRIVATE);

        ilceSecListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), "İlçe "+ilcelist.get(position)+" olarak seçildi", Toast.LENGTH_SHORT).show();
                sharedPreferences.edit().putString("kayitliilce",ilceKodulist.get(position)).apply(); //list view den seçilen veri buraya gelecek

            }
        });

        ilceVerisiCek();

    }


    //Kullanıcı ilçe seçecek
    //İlçe seçildi kullanıcıyı VakitIndirActivity 'e yolla

    public void vakitSec(View view){
        //Vakit indirme ekranına yolla
        Intent vakitleriIndir = new Intent(getApplicationContext(), VakitIndirActivity.class);
        startActivity(vakitleriIndir);
    }




    private void ilceVerisiCek() {

        try {

            String kayitSehirKodu = sharedPreferences.getString("kayitlisehir","none"); //şehiri çektim gerekli yere yazdık


            IlceDownload ilceDownload = new IlceDownload();

            String ilce = "https://ezanvakti.herokuapp.com/ilceler?sehir="+kayitSehirKodu;
            ilceDownload.execute(ilce);


        }catch (Exception e){

            //Çekme işlemi sıkıtı olursa (internet yoktur veya sunucusan istek sınırına takılmışsa) yanlış gittiye yolla
            System.out.println("DURUM: URL İLETİLEMEDİ");

            Intent yanliGitti = new Intent(getApplicationContext(),YanlisGittiActivity.class);
            startActivity(yanliGitti);
        }

    }




    ////////////////////// ////////////////////// İLÇE VERİSİ İNDİRME ////////////////////////////////////////////

    private class IlceDownload extends AsyncTask<String, Void, String> {

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

            try {

                JSONArray jsonArray = new JSONArray(s);
                //System.out.println("TAHA "+jsonArray); //ANA 1. PARCA


                //İLÇE ADI VE KODUNU ALAN KODALAR



                for (int i=0; i<jsonArray.length(); i++){
                    String asda = jsonArray.getString(i); //İLÇENİN SIRASI İLE ALINMASI İÇİN BUBU ARTIRARAK DİĞER ŞEHİRLER GELİR


                    JSONObject jsonObject = new JSONObject(asda);
                    String ilceAdi = jsonObject.getString("IlceAdi"); //İLÇE ADINI ALDIK (KAYIT KAYIT EDİLECEK)
                    String ilceId = jsonObject.getString("IlceID");   //İLÇE KODUNU ALDIK (KAYIT EDİLECEK)
                    System.out.println("ILCE ADI:  "+ilceAdi+" ILCE KODU:  "+ilceId); //LOGA BASTIK
                    //GELEN İLCE ADLARI LİST VİEW E BASILCAK KULLANICI HANGİ İLÇEYİ SEÇERSE SEŞİLEN İLÇE TEK VERİ OLARAK KAYDEDİLECEK


                    ilcelist.add(ilceAdi);
                    ilceKodulist.add(ilceId);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, ilcelist);
                    ilceSecListview.setAdapter(arrayAdapter);
                }


            }catch (Exception e){
                System.out.println("DURUM: İLÇE LİSTESİ ALINAMADI HATA KODU--> "+e);
            }

        }
    }
}
