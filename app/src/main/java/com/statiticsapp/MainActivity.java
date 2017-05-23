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
    TextView sumTxt;
    TextView mediaTxt;
    TextView rangeTxt;
    TextView maxTxt;
    TextView minTxt;
    TextView stdDeviationTxt;
    TextView topLimitTxt;
    TextView bottomLimitTxt;
    TextView studentBilateralTxt;
    TextView confidentIntervalTxt;
    TextView sampleSizeTxt;

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


        sumTxt = (TextView) findViewById(R.id.tab_calcular_sum);
        mediaTxt = (TextView) findViewById(R.id.tab_calcular_media);
        rangeTxt = (TextView) findViewById(R.id.tab_calcular_range);
        maxTxt = (TextView) findViewById(R.id.tab_calcular_max);
        minTxt = (TextView) findViewById(R.id.tab_calcular_min);
        stdDeviationTxt = (TextView) findViewById(R.id.tab_calcular_std_deviation);
        topLimitTxt = (TextView) findViewById(R.id.tab_calcular_top_limit);
        bottomLimitTxt = (TextView) findViewById(R.id.tab_calcular_bottom_limit);
        studentBilateralTxt = (TextView) findViewById(R.id.tab_calcular_student_bilateral);
        confidentIntervalTxt = (TextView) findViewById(R.id.tab_calcular_confident_interval);
        sampleSizeTxt = (TextView) findViewById(R.id.tab_calcular_sample_size);

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

            double sum = stats.getSum();
            double media = stats.getMean();
            double range = stats.getMax() - stats.getMin();
            double max = stats.getMax();
            double min = stats.getMin();
            double stdDeviation = stats.getStandardDeviation();
            double topLimit = 0;
            double bottomLimit = 0;
            double studentBilateral = 0;
            double confidentInterval = 0;
            long sampleSize = stats.getN();

            setTexts(sum, media, range, max, min, stdDeviation, topLimit, bottomLimit, studentBilateral, confidentInterval, sampleSize);

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

    void setTexts(double sum, double media, double range, double max, double min, double stdDeviation, double topLimit, double bottomLimit, double studentBilateral, double confidentInterval, long sampleSize) {
        sumTxt.setText(String.format(Locale.getDefault(), "%.2f", sum));
        mediaTxt.setText(String.format(Locale.getDefault(), "%.2f", media));
        rangeTxt.setText(String.format(Locale.getDefault(), "%.2f", range));
        maxTxt.setText(String.format(Locale.getDefault(), "%.2f", max));
        minTxt.setText(String.format(Locale.getDefault(), "%.2f", min));
        stdDeviationTxt.setText(String.format(Locale.getDefault(), "%.2f", stdDeviation));
        topLimitTxt.setText(String.format(Locale.getDefault(), "%.2f", topLimit));
        bottomLimitTxt.setText(String.format(Locale.getDefault(), "%.2f", bottomLimit));
        studentBilateralTxt.setText(String.format(Locale.getDefault(), "%.2f", studentBilateral));
        confidentIntervalTxt.setText(String.format(Locale.getDefault(), "%.2f", confidentInterval));
        sampleSizeTxt.setText(String.valueOf(sampleSize));
    }
}
