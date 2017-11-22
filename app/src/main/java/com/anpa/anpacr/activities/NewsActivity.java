package com.anpa.anpacr.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.anpa.anpacr.R;
import com.anpa.anpacr.app42.AsyncApp42ServiceApi;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.common.Util;
import com.anpa.anpacr.domain.FreqAnswer;
import com.anpa.anpacr.domain.News;
import com.anpa.anpacr.domain.Sponsor;
import com.anpa.anpacr.fragments.FreqAnswerFragment;
import com.anpa.anpacr.fragments.LastNewsFragment;
import com.anpa.anpacr.fragments.SponsorFragment;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.storage.Query;
import com.shephertz.app42.paas.sdk.android.storage.QueryBuilder;
import com.shephertz.app42.paas.sdk.android.storage.Storage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class NewsActivity extends AnpaAppFraqmentActivity implements
        FreqAnswerFragment.OnLoadListListenerFreqAnswerNews,
        SponsorFragment.OnLoadSponsorListListener,
        LastNewsFragment.OnLoadListListener,
        AsyncApp42ServiceApi.App42StorageServiceListener {

    private List<News> newsList;
    private List<FreqAnswer> freqAnswerList;
    private List<Sponsor> sponsorList;

    //App42:
    private AsyncApp42ServiceApi asyncService;
    private String docId = "";
    private ProgressDialog progressDialog;

    public static final String TAG_NEWS = "noticias";
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        //App42:
        asyncService = AsyncApp42ServiceApi.instance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_NEWS);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        // Set the text for each tab.
        tabLayout.addTab(tabLayout.newTab().setText(Constants.TITLE_LAST_NEWS));
        tabLayout.addTab(tabLayout.newTab().setText(Constants.TITLE_FREQ_ANSWER));
        tabLayout.addTab(tabLayout.newTab().setText(Constants.TITLE_SPONSOR));
        // Set the tabs to fill the entire layout.
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);

        newsList = new ArrayList<>();
        freqAnswerList = new ArrayList<>();
        sponsorList = new ArrayList<>();

        //Se carga la lista de noticias
        try {
            /* App42 */
            progressDialog = ProgressDialog.show(NewsActivity.this,
                    Constants.ESPERA, Constants.ESPERA_NOTICIAS);

            //Ejecurar fiiltros de noticias habilitadas
            Query queryNoticia1 = QueryBuilder.build(Constants.HABILITADO_NOTICIA, 1, QueryBuilder.Operator.EQUALS);

            //Ejecurar filtros de preguntas frecuentes estado = 0 y habilitados
            Query queryPreg1 = QueryBuilder.build(Constants.HABILITADO_PREGUNTA, 1, QueryBuilder.Operator.EQUALS);
            Query queryPreg2 = QueryBuilder.build(Constants.TIPO_PREGUNTA, 1, QueryBuilder.Operator.EQUALS);
            Query queryPreg3 = QueryBuilder.compoundOperator(queryPreg1, QueryBuilder.Operator.AND, queryPreg2);

            //Ejecurar fiiltros de patrocinios habilitados
            Query queryPatro1 = QueryBuilder.build(Constants.HABILITADO_PATROCINIO, 1, QueryBuilder.Operator.EQUALS);

            asyncService.findDocByColletionQuery(Constants.App42DBName, Constants.TABLE_NOTICIA, queryNoticia1, 1, this);
            asyncService.findDocByColletionQuery(Constants.App42DBName, Constants.TABLE_PREGUNTA_FREC, queryPreg3, 2, this);
            asyncService.findDocByColletionQuery(Constants.App42DBName, Constants.TABLE_PATROCINIO, queryPatro1, 3, this);

        } catch (Exception e) {
            progressDialog.dismiss();
            showMessage(Constants.MSJ_ERROR_NOTICIA);
            e.printStackTrace();
        } finally {
            //progressDialog.dismiss();
        }
    }

    private void setAdapter(){
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * Muestra un mensaje TOAST.
     */
    private void showMessage(String message) {
        Toast.makeText(NewsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /*
     * Implementacion del Interface que envia la lista al fragment.
     */
    @Override
    public List<News> loadList() {
        return newsList;
    }

    @Override
    public List<FreqAnswer> loadFreqAnswerList() {
        return freqAnswerList;
    }

    @Override
    public List<Sponsor> loadSponsorList() {
        return sponsorList;
    }

    @Override
    public void onDocumentInserted(Storage response) {

    }

    @Override
    public void onUpdateDocSuccess(Storage response) {

    }

    @Override
    public void onFindDocSuccess(Storage response, int type) {
        switch (type) {
            case 1://Noticias
                new AsyncLoadListTask().execute(response);
                break;
            case 2://Preguntas
                new AsyncLoadFreqAnswerListTask().execute(response);
                break;
            case 3://Patrocinio
                new AsyncLoadSponsorListTask().execute(response);
                break;
            default:
                break;
        }

    }

    @Override
    public void onInsertionFailed(App42Exception ex) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onFindDocFailed(App42Exception ex) {
        progressDialog.dismiss();
    }

    @Override
    public void onUpdateDocFailed(App42Exception ex) {
        // TODO Auto-generated method stub

    }

    private class AsyncLoadListTask extends AsyncTask<Storage, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Boolean doInBackground(Storage... storage) {

            ArrayList<Storage.JSONDocument> jsonDocList = storage[0].getJsonDocList();
            String sIdNews = "", sTitle = "", dCreationDate = "", sContent = "", sPhotoURL = "";
            Integer iHabilitado = 0;
            Date dateInicio = new Date();

            for (int i = 0; i < jsonDocList.size(); i++) {
                sIdNews = jsonDocList.get(i).getDocId();
                dCreationDate = jsonDocList.get(i).getCreatedAt();

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
                    sTitle = jsonObject.getString(Constants.TITULO_NOTICIA);
                    sContent = jsonObject.getString(Constants.CONTENIDO_NOTICIA);
                    iHabilitado = jsonObject.getInt(Constants.HABILITADO_NOTICIA);
                    sPhotoURL = jsonObject.getString(Constants.IMAGEN_NOTICIA);
                    byte[] photo = getBitmap(sPhotoURL);

                    if (iHabilitado == 1) {
                        News news = new News(sIdNews, Util.decode64AsText(sTitle), Util.decode64AsText(sContent), dCreationDate, photo, dateInicio, iHabilitado);
                        newsList.add(news);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if (!result)
                showMessage("No hay noticias registradas por el momento");
            setAdapter();
        }
    }


    //Obtiene la imagen desde una URL
    public static byte[] getBitmap(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Bitmap d = BitmapFactory.decodeStream(is);
            is.close();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            d.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        } catch (Exception e) {
            return null;
        }
    }

    /* Metodo para decodificar el json de preguntas */
    private class AsyncLoadFreqAnswerListTask extends AsyncTask<Storage, Integer, Boolean> {
        ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Boolean doInBackground(Storage... storage) {

            ArrayList<Storage.JSONDocument> jsonDocList = storage[0].getJsonDocList();

            String sIdPreg = "", sPregunta = "", sRespuesta = "", dCreationDate = "";
            Integer iOrden = 0, itipo = 0, iHabilitado = 0;
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");

            for (int i = 0; i < jsonDocList.size(); i++) {
                sIdPreg = jsonDocList.get(i).getDocId();
                dCreationDate = jsonDocList.get(i).getCreatedAt();

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
                    sPregunta = jsonObject.getString(Constants.DESC_PREGUNTA);
                    sRespuesta = jsonObject.getString(Constants.RESPESTA_PREGUNTA);
                    iOrden = jsonObject.getInt(Constants.ORDEN_PREGUNTA);
                    itipo = jsonObject.getInt(Constants.TIPO_PREGUNTA);
                    iHabilitado = jsonObject.getInt(Constants.HABILITADO_PREGUNTA);

                    if (iHabilitado == 1) {
                        FreqAnswer newPreg = new FreqAnswer(sIdPreg, Util.decode64AsText(sPregunta), Util.decode64AsText(sRespuesta), iOrden, itipo, dCreationDate, iHabilitado);
                        freqAnswerList.add(newPreg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return false;
                }
                Collections.sort(freqAnswerList, new Comparator<FreqAnswer>() {
                    @Override
                    public int compare(FreqAnswer freqAnswer1, FreqAnswer freqAnswer2) {
                        //comparision for primitive int uses compareTo of the wrapper Integer
                        return (new Integer(((FreqAnswer) freqAnswer1).get_iorden()))
                                .compareTo(((FreqAnswer) freqAnswer2).get_iorden());
                    }
                });
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            setAdapter();
        }
    }

    private class AsyncLoadSponsorListTask extends AsyncTask<Storage, Integer, Boolean> {
        ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Boolean doInBackground(Storage... storage) {

            ArrayList<Storage.JSONDocument> jsonDocList = storage[0].getJsonDocList();
            String sIdPatrocinios = "", sNombre = "", sDescripcion = "", sURL = "", dCreationDate = "", sPhotoURL = "";
            Integer iOrden = 0, iHabilitado = 0;


            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");

            for (int i = 0; i < jsonDocList.size(); i++) {
                sIdPatrocinios = jsonDocList.get(i).getDocId();
                dCreationDate = jsonDocList.get(i).getCreatedAt();

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
                    sNombre = jsonObject.getString(Constants.NOMBRE_PATROCINIO);
                    iOrden = jsonObject.getInt(Constants.ORDEN_PATROCINIO);
                    iHabilitado = jsonObject.getInt(Constants.HABILITADO_PATROCINIO);
                    sPhotoURL = jsonObject.getString(Constants.IMAGEN_PATROCINIO);
                    byte[] photo = getBitmap(sPhotoURL);

                    if (iHabilitado == 1) {
                        Sponsor newSpon = new Sponsor(sIdPatrocinios, Util.decode64AsText(sNombre), Util.decode64AsText(sDescripcion), Util.decode64AsText(sURL), iOrden, photo, dCreationDate, iHabilitado);
                        sponsorList.add(newSpon);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return false;
                }
                Collections.sort(sponsorList, new Comparator<Sponsor>() {
                    @Override
                    public int compare(Sponsor sponsor1, Sponsor sponsor2) {
                        //comparision for primitive int uses compareTo of the wrapper Integer
                        return (new Integer(((Sponsor) sponsor1).get_iorden()))
                                .compareTo(((Sponsor) sponsor2).get_iorden());
                    }
                });
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            setAdapter();
        }
    }

    /**
     * Control de los tabs 2017
     */
    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new LastNewsFragment();
                case 1:
                    return new FreqAnswerFragment();
                case 2:
                    return new SponsorFragment();
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
