package com.example.aplikacija;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.aplikacija.Adapter.Model.NewsModel;
import com.example.aplikacija.Adapter.NewsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private NewsAdapter newsAdapter;
    private RecyclerView recyclerView;
    private List<NewsModel> nModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Inicijuojamas recycler view ir nustatomas ant layout manager
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        nModel = new ArrayList<>();
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();

    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        return new duomenuPaimimas(this);

    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        jsonValdymas(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

    private static class duomenuPaimimas extends AsyncTaskLoader<String> {

        public duomenuPaimimas(Context context) {
            super(context);
        }

        @Override
        public String loadInBackground() {
            return getConnection();
        }

        @Override
        public void deliverResult(String data) {
            super.deliverResult(data);
        }
    }

    //Sukuriama prieiga prie interneto
    //Bei nuskaitomi duomenys
    private static String getConnection() {
        //Elementai priskiriami nuliui
        HttpURLConnection urlConnection = null;
        BufferedReader skaitytuvas = null;
        String jsonStr = null;
        String line;
        //Naudojamas try and catch kad isvengti crash
        try {
            //Sukuriamas sujungimas su nurodyta svetaine
            URL url = new URL("https://content.guardianapis.com/search?q=football&api-key=test");
            urlConnection = (HttpURLConnection) url.openConnection();
            //Naudojamas get nes bus gaunama informacija
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            //Konvertuoja input stream i String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            //Patikrina ar input stream nera tuscias
            if (inputStream == null) return null;
            //BufferedReader priskiriamas input stream
            skaitytuvas = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = skaitytuvas.readLine()) != null) buffer.append(line);
            //Patikrina ar gautas string nera tuscias
            if (buffer.length() == 0) return null;
            //String priskiriamas gautas rezultatas is stringBuffer
            jsonStr = buffer.toString();

        } catch (IOException e) {

            Log.e("MainActivity", "Error ", e);
            //Grazina nuli jei gauna klaida
            return null;

        } finally {
            //Patikrina ar url jungtis nera nulis
            if (urlConnection == null) urlConnection.disconnect();
            //Patikrina ar reader nera lygus nuliui
            //Jei salyga tenkino uzdaromas BufferedReader
            //Jei nepavyksta uzdaryti panaudojus try and catch aplikacija nenucrashins
            if (skaitytuvas != null) {

                try {

                    skaitytuvas.close();

                } catch (final IOException e) {

                    Log.e("MainActivity", "Error closing stream", e);

                }
            }
        }
        //Grazinamas json String
        return jsonStr;
    }

    //Json objektu pasiekimas
    private void jsonValdymas(String data) {
        //Naudojamas try and catch kad isvengti aplikacijos nuluzimo
        try {
            //Gaunamas json
            JSONObject jsonObject = new JSONObject(data);
            //Gaunamas json Response objektas
            JSONObject responseJSONObject = jsonObject.getJSONObject("response");
            //Gauti objektai sukeliami i array
            JSONArray newsJSONArray = responseJSONObject.getJSONArray("results");
            //Panaudojus cikla gaunami duomenys is array
            for (int i = 0; i < newsJSONArray.length(); i++) {

                JSONObject results = newsJSONArray.getJSONObject(i);
                //Gaunama is result objekto reikalingi string
                String sectionNameNews = results.getString("sectionName");
                String titleNews = results.getString("webTitle");
                String dataIsleidimo = results.getString("webPublicationDate");
                String webUrl = results.getString("webUrl");

                //Datos konvertavimas
                //Konvertavimas vyksta dviem etapais
                //1. Dekoduojama data gauta jsonFormatu. Gaunamas stai toks rezultatas (Mar 10, 2016 6:30:00 PM)
                //2. Dar karta formatuojama data kad gaut norima rezultata (2019-05-31 11:00:37)
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = null;
                //Naudojamas try and catch, kad isvengti app crash
                try {
                    date = myFormat.parse(dataIsleidimo);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //Gaunama suformatuota data
                String myDate = output.format(date);
                //Sukuriamas model elementas
                NewsModel newsModel = new NewsModel(sectionNameNews, titleNews, myDate, webUrl);
                //Pridedami elementai i sarasa
                nModel.add(newsModel);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Inicijuojamas adapteris
        newsAdapter = new NewsAdapter(nModel, MainActivity.this);
        //Sarasas nustatomas ant adapterio
        recyclerView.setAdapter(newsAdapter);
    }
}
