package com.statiticsapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.statiticsapp.Utils.CSVHelper;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TabHost mainTabHost;
    TextView mediaAritmeticaTxt;
    TextView mediaGeometricaTxt;
    TextView mediaArmonicaTxt;
    TextView mediaCuadraticaTxt;
    TextView medianaTxt;
    TextView modaTxt;
    TextView q1Txt;
    TextView q2Txt;
    TextView q3Txt;
    TextView rangoTxt;
    TextView desviacionMediaTxt;
    TextView varianzaTxt;
    TextView desviacionEstandarTxt;
    TextView asimetriaTxt;
    TextView curtosisTxt;

    private static String CALCULAR = "tab1";
    private static String GRAFICA = "tab2";
    private static String TEORIA = "tab3";
    private static int CALCULAR_TAB = 0;
    private static int GRAFICA_TAB = 1;
    private static int TEORIA_TAB = 2;

    private static int OPEN_CSV_FILE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaAritmeticaTxt = (TextView) findViewById(R.id.tab_calcular_med_aritmetica);
        mediaGeometricaTxt = (TextView) findViewById(R.id.tab_calcular_med_geometrica);
        mediaArmonicaTxt = (TextView) findViewById(R.id.tab_calcular_med_armonica);
        mediaCuadraticaTxt = (TextView) findViewById(R.id.tab_calcular_med_cuadratica);
        medianaTxt = (TextView) findViewById(R.id.tab_calcular_mediana);
        modaTxt = (TextView) findViewById(R.id.tab_calcular_moda);
        q1Txt = (TextView) findViewById(R.id.tab_calcular_q1);
        q2Txt = (TextView) findViewById(R.id.tab_calcular_q2);
        q3Txt = (TextView) findViewById(R.id.tab_calcular_q3);
        rangoTxt = (TextView) findViewById(R.id.tab_calcular_rango);
        desviacionMediaTxt = (TextView) findViewById(R.id.tab_calcular_desv_media);
        varianzaTxt = (TextView) findViewById(R.id.tab_calcular_varianza);
        desviacionEstandarTxt = (TextView) findViewById(R.id.tab_calcular_desv_estandar);
        asimetriaTxt = (TextView) findViewById(R.id.tab_calcular_asimetria);
        curtosisTxt = (TextView) findViewById(R.id.tab_calcular_curtosis);

        // Main Tab Host
        mainTabHost = (TabHost) findViewById(R.id.activity_main_tab_host);
        mainTabHost.setup();

        // Tab Calcular
        TabHost.TabSpec spec = mainTabHost.newTabSpec(CALCULAR);
        spec.setContent(R.id.tab1);
        spec.setIndicator("Calcular");
        mainTabHost.addTab(spec);

        // Tab Gráfica
        spec = mainTabHost.newTabSpec(GRAFICA);
        spec.setContent(R.id.tab2);
        spec.setIndicator("Gráfica");
        mainTabHost.addTab(spec);

        // Tab Teoría
        spec = mainTabHost.newTabSpec(TEORIA);
        spec.setContent(R.id.tab3);
        spec.setIndicator("Teoría");
        mainTabHost.addTab(spec);

        mainTabHost.setCurrentTab(CALCULAR_TAB);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_load_csv) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, OPEN_CSV_FILE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == OPEN_CSV_FILE) {

            DescriptiveStatistics stats = new DescriptiveStatistics();
            stats.addValue(142.5);
            stats.addValue(25);
            stats.addValue(33);
            stats.addValue(87.48);
            stats.addValue(54.15155);
            stats.addValue(983.4);
            stats.addValue(5);

            calculateAndShow(stats);

            // Chequear si la extensión del archivo es .csv

            /*Uri uri = data.getData();
            String filePath = data.getData().getPath();
            if(!filePath.equals("")) {
                File csvFile = new File(filePath);
                FileInputStream fis;
                CSVHelper csvHelper;

                try {
                    fis = new FileInputStream(csvFile);
                    csvHelper = new CSVHelper(fis);
                    //csvData = csvHelper.read();
                } catch (FileNotFoundException ex) {
                    Log.d("FileNotFoundExc", ex.getMessage());
                } catch (Exception ex) {
                    Log.d("Exception", ex.getMessage());
                } finally {

                }
            } else {
                Toast.makeText(this, "Seleccione un archivo .CSV válido", Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    String getRealPathFromUri(Uri uri) {
        String displayName = "";
        String uriString = uri.toString();

        if (uriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }

        return displayName;
    }

    void calculateAndShow(DescriptiveStatistics stats) {

        // Medidas de Centralización
        double mediaAritmetica = stats.getMean();
        double mediaGeometrica = stats.getGeometricMean();
        double mediaArmonica = 0;
        double mediaCuadratica = stats.getQuadraticMean();
        double mediana = stats.getPercentile(50);
        double moda = 0;

        // Medidas de Posición
        double q1 = stats.getPercentile(25);
        double q2 = stats.getPercentile(50);
        double q3 = stats.getPercentile(75);

        // Medidas de Dispersión
        double rango = stats.getMax() - stats.getMin();
        double desviacionMedia = 0;
        double varianza = stats.getVariance();
        double desviacionEstandar = stats.getStandardDeviation();

        // Medidas de Forma
        double asimetria = stats.getSkewness();
        double curtosis = stats.getKurtosis();

        mediaAritmeticaTxt.setText(String.valueOf(mediaAritmetica));
        mediaGeometricaTxt.setText(String.valueOf(mediaGeometrica));
        mediaArmonicaTxt.setText(String.valueOf(mediaArmonica));
        mediaCuadraticaTxt.setText(String.valueOf(mediaCuadratica));
        medianaTxt.setText(String.valueOf(mediana));
        modaTxt.setText(String.valueOf(moda));
        q1Txt.setText(String.valueOf(q1));
        q2Txt.setText(String.valueOf(q2));
        q3Txt.setText(String.valueOf(q3));
        rangoTxt.setText(String.valueOf(rango));
        desviacionMediaTxt.setText(String.valueOf(desviacionMedia));
        varianzaTxt.setText(String.valueOf(varianza));
        desviacionEstandarTxt.setText(String.valueOf(desviacionEstandar));
        asimetriaTxt.setText(String.valueOf(asimetria));
        curtosisTxt.setText(String.valueOf(curtosis));
    }
}
