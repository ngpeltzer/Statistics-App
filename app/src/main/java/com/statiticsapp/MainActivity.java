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
import android.widget.ExpandableListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.common.primitives.Doubles;
import com.opencsv.CSVReader;
import com.statiticsapp.Adapters.CalculateExpandableListAdapter;

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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TabHost mainTabHost;
    /*TextView arithmeticMediaTxt;
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
    TextView kurtosisTxt;*/
    PDFView pdfView;
    ExpandableListView calculateExpandableListView;
    CalculateExpandableListAdapter cela;

    HashMap<String, List<String>> valuesHashMap;
    List<String> centralTendencyValues;
    List<String> positionValues;
    List<String> dispertionValues;
    List<String> formValues;

    private static String CALCULATE = "tab1";
    private static String GRAPHICS = "tab2";
    private static String THEORY = "tab3";
    private static String CENTRAL_TENDENCY_MEASURES = "Medidas de Tendencia Central";
    private static String POSITION_MEASURES = "Medidas de Posición";
    private static String DISPERTION_MEASURES = "Medidas de Dispersión";
    private static String FORM_MEASURES = "Medidas de Forma";

    private static int CALCULATE_TAB = 0;
    private static int GRAPHICS_TAB = 1;
    private static int THEORY_TAB = 2;

    private static int OPEN_CSV_FILE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        centralTendencyValues = new ArrayList<>();
        positionValues = new ArrayList<>();
        dispertionValues = new ArrayList<>();
        formValues = new ArrayList<>();

        pdfView = (PDFView) findViewById(R.id.tab_theory_pdf_view);

        /*arithmeticMediaTxt = (TextView) findViewById(R.id.tab_calculate_arithmetic_media);
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
        kurtosisTxt = (TextView) findViewById(R.id.tab_calculate_kurtosis);*/

        // Main Tab Host
        mainTabHost = (TabHost) findViewById(R.id.activity_main_tab_host);
        mainTabHost.setup();

        // Calculate Tab
        TabHost.TabSpec spec = mainTabHost.newTabSpec(CALCULATE);
        spec.setContent(R.id.tab1);
        spec.setIndicator("Cálculos");
        mainTabHost.addTab(spec);

        // Graphics Tab
        spec = mainTabHost.newTabSpec(GRAPHICS);
        spec.setContent(R.id.tab2);
        spec.setIndicator("Gráficos");
        mainTabHost.addTab(spec);

        // Theory Tab
        spec = mainTabHost.newTabSpec(THEORY);
        spec.setContent(R.id.tab3);
        spec.setIndicator("Teoría");
        mainTabHost.addTab(spec);

        mainTabHost.setCurrentTab(CALCULATE_TAB);

        // Set the ExpandableListView
        List<String> listTitles = new ArrayList<>();
        listTitles.add(CENTRAL_TENDENCY_MEASURES);
        listTitles.add(POSITION_MEASURES);
        listTitles.add(DISPERTION_MEASURES);
        listTitles.add(FORM_MEASURES);

        HashMap<String, List<String>> labelsHashMap = new HashMap<>();

        List<String> centralTendencyLabels = new ArrayList<>();
        centralTendencyLabels.add("Media Aritmética:");
        centralTendencyLabels.add("Media Geométrica:");
        centralTendencyLabels.add("Media Armónica:");
        centralTendencyLabels.add("Media Cuadrática:");
        centralTendencyLabels.add("Mediana:");
        centralTendencyLabels.add("Moda:");

        List<String> positionLabels = new ArrayList<>();
        positionLabels.add("Cuartil 1:");
        positionLabels.add("Cuartil 2:");
        positionLabels.add("Cuartil 3:");
        positionLabels.add("Decil 1:");
        positionLabels.add("Decil 2:");
        positionLabels.add("Decil 3:");
        positionLabels.add("Decil 4:");
        positionLabels.add("Decil 5:");
        positionLabels.add("Decil 6:");
        positionLabels.add("Decil 7:");
        positionLabels.add("Decil 8:");
        positionLabels.add("Decil 9:");

        List<String> dispertionLabels = new ArrayList<>();
        dispertionLabels.add("Rango:");
        dispertionLabels.add("Desviación Media:");
        dispertionLabels.add("Varianza:");
        dispertionLabels.add("Desviación Estándar:");
        dispertionLabels.add("Coeficiente de Variación:");

        List<String> formLabels = new ArrayList<>();
        formLabels.add("Asimetria:");
        formLabels.add("Curtosis:");

        for(int i = 0; i < listTitles.size(); i++) {
            switch (i) {
                case 0:
                    labelsHashMap.put(listTitles.get(i), centralTendencyLabels);
                break;
                case 1:
                    labelsHashMap.put(listTitles.get(i), positionLabels);
                    break;
                case 2:
                    labelsHashMap.put(listTitles.get(i), dispertionLabels);
                    break;
                case 3:
                    labelsHashMap.put(listTitles.get(i), formLabels);
                    break;
            }

        }

        valuesHashMap = new HashMap<>();
        valuesHashMap.put(CENTRAL_TENDENCY_MEASURES, centralTendencyValues);
        valuesHashMap.put(POSITION_MEASURES, positionValues);
        valuesHashMap.put(DISPERTION_MEASURES, dispertionValues);
        valuesHashMap.put(FORM_MEASURES, formValues);
        cela = new CalculateExpandableListAdapter(this, listTitles, labelsHashMap, valuesHashMap);

        calculateExpandableListView = (ExpandableListView) findViewById(R.id.tab_calculate_expandable_list_view);
        calculateExpandableListView.setAdapter(cela);

        pdfView.fromAsset("theory.pdf")
               .enableSwipe(true)
               .swipeHorizontal(true)
               .enableDoubletap(true)
               .scrollHandle(new DefaultScrollHandle(this))
               .load();
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

            FileInputStream fis;
            CSVReader csvReader;

            try {
                fis = (FileInputStream) getContentResolver().openInputStream(data.getData());
                csvReader = new CSVReader(new FileReader(fis.getFD()));
                String values[] = csvReader.readNext();
                List<Double> doubleValues = new ArrayList<Double>();

                for(int i = 0; i < values.length; i++)
                {
                    doubleValues.add(Double.valueOf(values[i]));
                }

                DescriptiveStatistics stats = new DescriptiveStatistics(Doubles.toArray(doubleValues));
                calculateAndShow(stats);
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

    void calculateAndShow(DescriptiveStatistics stats) {

        // Centralization Measures
        double arithmeticMedia = stats.getMean();
        double geometricaMedia = stats.getGeometricMean();
        double armonicMedia = 0;
        double cuadraticMedia = stats.getQuadraticMean();
        double median = stats.getPercentile(50);
        double mode = 0;

        centralTendencyValues.add(String.format(Locale.getDefault(), "%.2f", arithmeticMedia));
        centralTendencyValues.add(String.format(Locale.getDefault(), "%.2f", geometricaMedia));
        centralTendencyValues.add(String.format(Locale.getDefault(), "%.2f", armonicMedia));
        centralTendencyValues.add(String.format(Locale.getDefault(), "%.2f", cuadraticMedia));
        centralTendencyValues.add(String.format(Locale.getDefault(), "%.2f", median));
        centralTendencyValues.add(String.format(Locale.getDefault(), "%.2f", mode));

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

        positionValues.add(String.format(Locale.getDefault(), "%.2f", q1));
        positionValues.add(String.format(Locale.getDefault(), "%.2f", q2));
        positionValues.add(String.format(Locale.getDefault(), "%.2f", q3));
        positionValues.add(String.format(Locale.getDefault(), "%.2f", d1));
        positionValues.add(String.format(Locale.getDefault(), "%.2f", d2));
        positionValues.add(String.format(Locale.getDefault(), "%.2f", d3));
        positionValues.add(String.format(Locale.getDefault(), "%.2f", d4));
        positionValues.add(String.format(Locale.getDefault(), "%.2f", d5));
        positionValues.add(String.format(Locale.getDefault(), "%.2f", d6));
        positionValues.add(String.format(Locale.getDefault(), "%.2f", d7));
        positionValues.add(String.format(Locale.getDefault(), "%.2f", d8));
        positionValues.add(String.format(Locale.getDefault(), "%.2f", d9));

        // TODO Calculate centils - (k * stats.getN())/ 100 - Where k is the centil value searched

        // Dispersion Measures
        double range = stats.getMax() - stats.getMin();
        double averageDeviation = 0;
        double variance = stats.getVariance();
        double standardDeviation = stats.getStandardDeviation();
        double coefficientOfVariation = 0;

        dispertionValues.add(String.format(Locale.getDefault(), "%.2f", range));
        dispertionValues.add(String.format(Locale.getDefault(), "%.2f", averageDeviation));
        dispertionValues.add(String.format(Locale.getDefault(), "%.2f", variance));
        dispertionValues.add(String.format(Locale.getDefault(), "%.2f", standardDeviation));
        dispertionValues.add(String.format(Locale.getDefault(), "%.2f", coefficientOfVariation));

        // Measures of Form
        double skewness = stats.getSkewness();
        double kurtosis = stats.getKurtosis();

        formValues.add(String.format(Locale.getDefault(), "%.2f", skewness));
        formValues.add(String.format(Locale.getDefault(), "%.2f", kurtosis));

        valuesHashMap.put(CENTRAL_TENDENCY_MEASURES, centralTendencyValues);
        valuesHashMap.put(POSITION_MEASURES, positionValues);
        valuesHashMap.put(DISPERTION_MEASURES, dispertionValues);
        valuesHashMap.put(FORM_MEASURES, formValues);

        cela.setData(valuesHashMap);
        cela.notifyDataSetChanged();
    }
}
