package com.statiticsapp.Activitys;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.common.math.DoubleMath;
import com.google.common.primitives.Doubles;
import com.opencsv.CSVReader;
import com.statiticsapp.Adapters.CalculateExpandableListAdapter;
import com.statiticsapp.CustomViews.GenericDialog;
import com.statiticsapp.Interfaces.GenericDialogListener;
import com.statiticsapp.Model.Stem;
import com.statiticsapp.R;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.Precision;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    TabHost mainTabHost;
    PDFView pdfView;
    BarChart graphView;
    TextView stemTxt;
    TextView leafTxt;
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
    private static int BIN_COUNT = 10;

    private static int CALCULATE_TAB = 0;
    private static int GRAPHICS_TAB = 1;
    private static int THEORY_TAB = 2;

    private static int OPEN_CSV_FILE = 3;

    private Activity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ma = this;

        centralTendencyValues = new ArrayList<>();
        positionValues = new ArrayList<>();
        dispertionValues = new ArrayList<>();
        formValues = new ArrayList<>();

        pdfView = (PDFView) findViewById(R.id.tab_theory_pdf_view);
        graphView = (BarChart) findViewById(R.id.tab_graphs_graph_view);
        stemTxt = (TextView) findViewById(R.id.tab_graphs_stem);
        leafTxt = (TextView) findViewById(R.id.tab_graphs_leaf);

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

        // Set labels of the ExpandableListView
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

        labelsHashMap.put(listTitles.get(0), centralTendencyLabels);
        labelsHashMap.put(listTitles.get(1), positionLabels);
        labelsHashMap.put(listTitles.get(2), dispertionLabels);
        labelsHashMap.put(listTitles.get(3), formLabels);

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
        switch (id) {
            case R.id.menu_load_csv:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, OPEN_CSV_FILE);
            break;
            case R.id.menu_random_sample:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putSerializable("message", getString(R.string.dialog_message));
                args.putSerializable("buttonOk", getString(R.string.generate));
                args.putSerializable("buttonCancel", getString(R.string.cancel));
                args.putSerializable("title", getString(R.string.dialog_title));
                final GenericDialog dialog = new GenericDialog();
                dialog.setArguments(args);
                dialog.setCallback(new GenericDialogListener() {
                    @Override
                    public void onOkPressed(int sampleSize, double median, double stdDeviation) {
                        DescriptiveStatistics stats = generateRandomSample(median, stdDeviation, sampleSize);
                        calculateAndShow(stats);
                        showGraphics(stats);
                    }
                });
                dialog.show(ft, "RandomSample");
            break;
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
                showGraphics(stats);
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

    DescriptiveStatistics generateRandomSample(double mean, double standardDeviation, int sampleSize) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        RandomDataGenerator rdg = new RandomDataGenerator();

        for(int i = 0; i < sampleSize; i++) {
            double number = rdg.nextGaussian(mean, standardDeviation);
            stats.addValue(number);
        }

        return stats;
    }

    void showGraphics(DescriptiveStatistics stats) {
        // Data for Histogram and Steam And Leaf diagrams
        double[] data = stats.getValues();

        // Histogram diagram
        double max = stats.getMax();
        double min = stats.getMin();

        HashMap<String, double[]> histogramData = calculateHistogram(data, max, min, BIN_COUNT);
        double[] bins = histogramData.get("Bins");
        double[] values = histogramData.get("Values");

        List<BarEntry> entries = new ArrayList<>();
        final List<String> entriesLabels = new ArrayList<>();
        for(int i = 0; i < bins.length; i++) {
            entriesLabels.add(String.format("%.2f", bins[i]));
            entries.add(new BarEntry(i, (float)values[i]));
        }

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setBarBorderColor(R.color.colorAccent);
        dataSet.setBarBorderWidth(1f);
        dataSet.setDrawValues(false);
        BarData graphicData = new BarData(dataSet);
        graphicData.setBarWidth(1f);
        graphView.setData(graphicData);
        graphView.setDoubleTapToZoomEnabled(true);
        graphView.getDescription().setText("");
        graphView.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        graphView.getXAxis().setValueFormatter(new IndexAxisValueFormatter(entriesLabels));
        graphView.animateXY(2000, 2000);

        // Stem and leaf diagram
        SortedMap<Integer, List<Integer>> steamAndLeafData = calculateStemsAndLeafs(data);
        Iterator mapIterator = steamAndLeafData.entrySet().iterator();
        String stemsString = "Tallo\n\n";
        String leafsString = "Hoja\n\n";

        while(mapIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) mapIterator.next();
            stemsString += entry.getKey() + "\n";
            List<Integer> leafs = (List<Integer>) entry.getValue();
            Collections.sort(leafs);
            for(int i = 0; i < leafs.size(); i++) {
                leafsString += leafs.get(i).toString() + " ";
            }
            leafsString += "\n";
        }

        stemTxt.setText(stemsString);
        leafTxt.setText(leafsString);
    }

    double calculateBinSize(double max, double min, int numBins) {
        double range = max - min;
        double binSize = range/numBins;
        return binSize;
    }

    double[] calculateBins(int numBins, double binSize, double min) {
        double[] result = new double[numBins];
        result[0] = min;

        for(int i = 1; i < numBins; i++) {
            result[i] = binSize + result[i - 1];
        }
        return result;
    }

    SortedMap<Integer, List<Integer>> calculateStemsAndLeafs(double[] data) {
        SortedMap<Integer, List<Integer>> result = new TreeMap<>();

        for(int i = 0; i < data.length; i++) {
            int actualStem = (int)data[i];
            double leaf = Precision.round(data[i], 1);
            leaf = (leaf - actualStem) * 10;
            leaf = Math.abs(Precision.round(leaf, 0, BigDecimal.ROUND_HALF_DOWN));

            if(!result.containsKey(actualStem)) {
                List<Integer> leafs = new ArrayList<>();
                leafs.add((int)leaf);
                result.put(actualStem, leafs);
            }
            else {
                result.get(actualStem).add((int)leaf);
            }
        }

        return result;
    }

    HashMap<String, double[]> calculateHistogram(double[] data, double max, double min, int numBins) {
        HashMap<String, double[]> histogramData = new HashMap<>();
        double binSize = calculateBinSize(max, min, numBins);
        double[] bins = calculateBins(numBins, binSize, min);    // Calculate the bins to show in the histogram
        double[] values = new double[numBins];

        for(double number : data) {
            int bin = (int) ((number - min) / binSize); // Calculate the class where "number" goes

            if(bin >= 0 && bin < numBins) {             // If the class is not in the interval (0 - numBins),
                values[bin] += 1;                       // it means that the number was higher than max value or lower than min value
            }
        }

        histogramData.put("Bins", bins);
        histogramData.put("Values", values);
        return histogramData;
    }

    void calculateAndShow(DescriptiveStatistics stats) {

        // Centralization Measures
        double arithmeticMedia = stats.getMean();
        double geometricaMedia = stats.getGeometricMean();
        double armonicMedia = 0;
        double cuadraticMedia = stats.getQuadraticMean();
        double median = stats.getPercentile(50);
        double mode = 0;

        centralTendencyValues.clear();
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

        positionValues.clear();
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

        dispertionValues.clear();
        dispertionValues.add(String.format(Locale.getDefault(), "%.2f", range));
        dispertionValues.add(String.format(Locale.getDefault(), "%.2f", averageDeviation));
        dispertionValues.add(String.format(Locale.getDefault(), "%.2f", variance));
        dispertionValues.add(String.format(Locale.getDefault(), "%.2f", standardDeviation));
        dispertionValues.add(String.format(Locale.getDefault(), "%.2f", coefficientOfVariation));

        // Measures of Form
        double skewness = stats.getSkewness();
        double kurtosis = stats.getKurtosis();

        formValues.clear();
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
