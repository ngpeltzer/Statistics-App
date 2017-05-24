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

import com.google.common.primitives.Doubles;
import com.opencsv.CSVReader;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TabHost mainTabHost;
    TextView arithmeticMediaTxt;
    TextView geometricaMediaTxt;
    TextView armonicMediaTxt;
    TextView cuadraticMediaTxt;
    TextView medianTxt;
    TextView modeTxt;
    TextView q1Txt;
    TextView q2Txt;
    TextView q3Txt;
    TextView d1Txt;
    TextView d2Txt;
    TextView d3Txt;
    TextView d4Txt;
    TextView d5Txt;
    TextView d6Txt;
    TextView d7Txt;
    TextView d8Txt;
    TextView d9Txt;
    TextView rangeTxt;
    TextView averageDeviationTxt;
    TextView varianceTxt;
    TextView standardDeviationTxt;
    TextView coefficientOfVariationTxt;
    TextView skewnessTxt;
    TextView kurtosisTxt;

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

        arithmeticMediaTxt = (TextView) findViewById(R.id.tab_calculate_arithmetic_media);
        geometricaMediaTxt = (TextView) findViewById(R.id.tab_calculate_geometric_media);
        armonicMediaTxt = (TextView) findViewById(R.id.tab_calculate_armonic_media);
        cuadraticMediaTxt = (TextView) findViewById(R.id.tab_calculate_cuadratic_media);
        medianTxt = (TextView) findViewById(R.id.tab_calculate_median);
        modeTxt = (TextView) findViewById(R.id.tab_calculate_mode);
        q1Txt = (TextView) findViewById(R.id.tab_calculate_q1);
        q2Txt = (TextView) findViewById(R.id.tab_calculate_q2);
        q3Txt = (TextView) findViewById(R.id.tab_calculate_q3);
        d1Txt = (TextView) findViewById(R.id.tab_calculate_decil1);
        d2Txt = (TextView) findViewById(R.id.tab_calculate_decil2);
        d3Txt = (TextView) findViewById(R.id.tab_calculate_decil3);
        d4Txt = (TextView) findViewById(R.id.tab_calculate_decil4);
        d5Txt = (TextView) findViewById(R.id.tab_calculate_decil5);
        d6Txt = (TextView) findViewById(R.id.tab_calculate_decil6);
        d7Txt = (TextView) findViewById(R.id.tab_calculate_decil7);
        d8Txt = (TextView) findViewById(R.id.tab_calculate_decil8);
        d9Txt = (TextView) findViewById(R.id.tab_calculate_decil9);
        rangeTxt = (TextView) findViewById(R.id.tab_calculate_range);
        averageDeviationTxt = (TextView) findViewById(R.id.tab_calculate_average_deviation);
        varianceTxt = (TextView) findViewById(R.id.tab_calculate_variance);
        standardDeviationTxt = (TextView) findViewById(R.id.tab_calculate_standard_deviation);
        coefficientOfVariationTxt = (TextView) findViewById(R.id.tab_calculate_coefficient_variation);
        skewnessTxt = (TextView) findViewById(R.id.tab_calculate_skewness);
        kurtosisTxt = (TextView) findViewById(R.id.tab_calculate_kurtosis);

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

            /*DescriptiveStatistics stats = new DescriptiveStatistics();
            stats.addValue(25.5);
            stats.addValue(25);
            stats.addValue(33);
            stats.addValue(44.48);
            stats.addValue(54.15155);
            stats.addValue(23.4);
            stats.addValue(45);

            calculateAndShow(stats);*/

            //List<Double> csvData = new ArrayList<Double>();
            FileInputStream fis;
            CSVReader csvReader;

            try {
                fis = (FileInputStream)getContentResolver().openInputStream(data.getData());
                csvReader = new CSVReader(new FileReader(fis.getFD()));
                String values[] = csvReader.readNext();
                List<Double> doubleValues = new ArrayList<Double>();
                for(int i=0;i<values.length;i++)
                {
                    doubleValues.add(Double.valueOf(values[i]));
                }
                DescriptiveStatistics stats2 = new DescriptiveStatistics(Doubles.toArray(doubleValues));
                calculateAndShow(stats2);
            } catch (FileNotFoundException ex) {
                Log.d("FileNotFoundExc", ex.getMessage());
            } catch (Exception ex) {
                Log.d("Exception", ex.getMessage());
            } finally {

            }
        } else {
            Toast.makeText(this, "Seleccione un archivo .CSV válido", Toast.LENGTH_SHORT).show();
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

        // Centralization Measures
        double arithmeticMedia = stats.getMean();
        double geometricaMedia = stats.getGeometricMean();
        double armonicMedia = 0;
        double cuadraticMedia = stats.getQuadraticMean();
        double median = stats.getPercentile(50);
        double mode = 0;

        // Position Measures
        double q1 = stats.getPercentile(25);
        double q2 = stats.getPercentile(50);
        double q3 = stats.getPercentile(75);
        double d1 = stats.getPercentile(10);
        double d2 = stats.getPercentile(20);
        double d3 = stats.getPercentile(30);
        double d4 = stats.getPercentile(40);
        double d5 = stats.getPercentile(50);
        double d6 = stats.getPercentile(60);
        double d7 = stats.getPercentile(70);
        double d8 = stats.getPercentile(80);
        double d9 = stats.getPercentile(90);

        // TODO Calculate centils - (k * stats.getN())/ 100 - Where k is the centil value searched

        // Dispersion Measures
        double range = stats.getMax() - stats.getMin();
        double averageDeviation = 0;
        double variance = stats.getVariance();
        double standardDeviation = stats.getStandardDeviation();
        double coefficientOfVariation = 0;

        // Measures of Form
        double skewness = stats.getSkewness();
        double kurtosis = stats.getKurtosis();

        arithmeticMediaTxt.setText(String.valueOf(arithmeticMedia));
        geometricaMediaTxt.setText(String.valueOf(geometricaMedia));
        armonicMediaTxt.setText(String.valueOf(armonicMedia));
        cuadraticMediaTxt.setText(String.valueOf(cuadraticMedia));
        medianTxt.setText(String.valueOf(median));
        modeTxt.setText(String.valueOf(mode));
        q1Txt.setText(String.valueOf(q1));
        q2Txt.setText(String.valueOf(q2));
        q3Txt.setText(String.valueOf(q3));
        d1Txt.setText(String.valueOf(d1));
        d2Txt.setText(String.valueOf(d2));
        d3Txt.setText(String.valueOf(d3));
        d4Txt.setText(String.valueOf(d4));
        d5Txt.setText(String.valueOf(d5));
        d6Txt.setText(String.valueOf(d6));
        d7Txt.setText(String.valueOf(d7));
        d8Txt.setText(String.valueOf(d8));
        d9Txt.setText(String.valueOf(d9));
        rangeTxt.setText(String.valueOf(range));
        averageDeviationTxt.setText(String.valueOf(averageDeviation));
        varianceTxt.setText(String.valueOf(variance));
        standardDeviationTxt.setText(String.valueOf(standardDeviation));
        coefficientOfVariationTxt.setText(String.valueOf(coefficientOfVariation));
        skewnessTxt.setText(String.valueOf(skewness));
        kurtosisTxt.setText(String.valueOf(kurtosis));
    }
}
