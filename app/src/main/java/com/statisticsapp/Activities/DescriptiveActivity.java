package com.statisticsapp.Activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.common.primitives.Doubles;
import com.statisticsapp.Adapters.CalculateExpandableListAdapter;
import com.statisticsapp.CustomViews.GenericDialog;
import com.statisticsapp.Interfaces.GenericDialogListener;
import com.statisticsapp.R;
import com.statisticsapp.Utils.Constants;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.Precision;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class DescriptiveActivity extends AppCompatActivity {

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

    private static final String CALCULATE = "tab1";
    private static final String GRAPHICS = "tab2";
    private static final String THEORY = "tab3";
    private static final String CENTRAL_TENDENCY_MEASURES = "Medidas de Tendencia Central";
    private static final String POSITION_MEASURES = "Medidas de Posición";
    private static final String DISPERTION_MEASURES = "Medidas de Dispersión";
    private static final String FORM_MEASURES = "Medidas de Forma";
    private static final int BIN_COUNT = 10;

    private static final int CALCULATE_TAB = 0;

    //private static int OPEN_CSV_FILE = 3;
    private static int OPEN_EXCEL_FILE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descriptive);

        centralTendencyValues = new ArrayList<>();
        positionValues = new ArrayList<>();
        dispertionValues = new ArrayList<>();
        formValues = new ArrayList<>();

        pdfView = findViewById(R.id.tab_theory_pdf_view);
        graphView = findViewById(R.id.tab_graphs_graph_view);
        stemTxt = findViewById(R.id.tab_graphs_stem);
        leafTxt = findViewById(R.id.tab_graphs_leaf);

        // Main Tab Host
        mainTabHost = findViewById(R.id.activity_main_tab_host);
        mainTabHost.setup();

        // Calculate Tab
        TabHost.TabSpec spec = mainTabHost.newTabSpec(CALCULATE);
        spec.setContent(R.id.tab1);
        spec.setIndicator(getResources().getString(R.string.tab_calculate));
        mainTabHost.addTab(spec);

        // Graphics Tab
        spec = mainTabHost.newTabSpec(GRAPHICS);
        spec.setContent(R.id.tab2);
        spec.setIndicator(getResources().getString(R.string.tab_graphics));
        mainTabHost.addTab(spec);

        // Theory Tab
        spec = mainTabHost.newTabSpec(THEORY);
        spec.setContent(R.id.tab3);
        spec.setIndicator(getResources().getString(R.string.tab_theory));
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

        calculateExpandableListView = findViewById(R.id.tab_calculate_expandable_list_view);
        calculateExpandableListView.setAdapter(cela);

        pdfView.fromAsset("descriptive.pdf")
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
            case R.id.menu_load_excel:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, OPEN_EXCEL_FILE);
            break;
            case R.id.menu_random_sample:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putSerializable(Constants.MESSAGE, getString(R.string.dialog_message));
                args.putSerializable(Constants.BUTTON_OK, getString(R.string.generate));
                args.putSerializable(Constants.BUTTON_CANCEL, getString(R.string.cancel));
                args.putSerializable(Constants.TITLE, getString(R.string.dialog_title));
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

        if(resultCode == RESULT_OK && requestCode == OPEN_EXCEL_FILE) {

            FileInputStream excelFile;

            try {
                excelFile = (FileInputStream) getContentResolver().openInputStream(data.getData());

                Workbook workbook = Workbook.getWorkbook(excelFile);
                Sheet sheet = workbook.getSheet(0);

                ArrayList<Double> doubleValues = new ArrayList<>();

                for (int i = 0; i < sheet.getRows(); i++) {
                    Cell cell = sheet.getCell(0, i);
                    String cellContent = cell.getContents();
                    if (cellContent.contains(",")) cellContent = cellContent.replace(',', '.');
                    doubleValues.add(Double.parseDouble(cellContent));
                }

                DescriptiveStatistics stats = new DescriptiveStatistics(Doubles.toArray(doubleValues));
                calculateAndShow(stats);
                showGraphics(stats);
            } catch (FileNotFoundException ex) {
                Log.d("FileNotFoundExc", ex.getMessage());
                Toast.makeText(DescriptiveActivity.this, "Archivo no encontrado", Toast.LENGTH_SHORT).show();
            } catch (BiffException ex) {
                Log.d("BiffException", ex.getMessage());
                Toast.makeText(DescriptiveActivity.this, "Seleccione un archivo .XLS válido", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException ex) {
                Log.d("NumberFormatExc", ex.getMessage());
                Toast.makeText(DescriptiveActivity.this, "Los números no tienen el formato correcto", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Log.d("Exception", ex.getMessage());
                Toast.makeText(DescriptiveActivity.this, "Debe ingresar un archivo solo con números y solo en la primer columna", Toast.LENGTH_SHORT).show();
            }

            /*FileInputStream fis;
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
                Toast.makeText(DescriptiveActivity.this, "Archivo no encontrado", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Log.d("Exception", ex.getMessage());
                Toast.makeText(DescriptiveActivity.this, "Debe ingresar un archivo solo con números separados por coma", Toast.LENGTH_SHORT).show();
            }*/
        } else {
            //Toast.makeText(this, "Seleccione un archivo .CSV válido", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Seleccione un archivo .XLS válido", Toast.LENGTH_SHORT).show();
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
            entriesLabels.add(formatOutput(bins[i]));
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
        StringBuilder stemsStringBuilder = new StringBuilder();
        StringBuilder leafsStringBuilder = new StringBuilder();
        stemsStringBuilder.append("Tallo\n\n");
        leafsStringBuilder.append("Hoja\n\n");

        while(mapIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) mapIterator.next();
            stemsStringBuilder.append(entry.getKey());
            stemsStringBuilder.append("\n");
            List<Integer> leafs = (List<Integer>) entry.getValue();
            Collections.sort(leafs);
            for(int i = 0; i < leafs.size(); i++) {
                leafsStringBuilder.append(leafs.get(i).toString());
                leafsStringBuilder.append(" ");
            }
            leafsStringBuilder.append("\n");
        }

        stemTxt.setText(stemsStringBuilder.toString());
        leafTxt.setText(leafsStringBuilder.toString());
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

        for (double i : data) {
            int actualStem = (int) i;
            double leaf = Precision.round(i, 1);
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
        /*for(int i = 0; i < data.length; i++) {
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
        }*/

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

    String formatOutput(double value) {
        return String.format(Locale.getDefault(), "%.2f", value);
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
        centralTendencyValues.add(formatOutput(arithmeticMedia));
        centralTendencyValues.add(formatOutput(geometricaMedia));
        centralTendencyValues.add(formatOutput(armonicMedia));
        centralTendencyValues.add(formatOutput(cuadraticMedia));
        centralTendencyValues.add(formatOutput(median));
        centralTendencyValues.add(formatOutput(mode));

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
        positionValues.add(formatOutput(q1));
        positionValues.add(formatOutput(q2));
        positionValues.add(formatOutput(q3));
        positionValues.add(formatOutput(d1));
        positionValues.add(formatOutput(d2));
        positionValues.add(formatOutput(d3));
        positionValues.add(formatOutput(d4));
        positionValues.add(formatOutput(d5));
        positionValues.add(formatOutput(d6));
        positionValues.add(formatOutput(d7));
        positionValues.add(formatOutput(d8));
        positionValues.add(formatOutput(d9));

        // TODO Calculate centils - (k * stats.getN())/ 100 - Where k is the centil value searched

        // Dispersion Measures
        double range = stats.getMax() - stats.getMin();
        double averageDeviation = 0;
        double variance = stats.getVariance();
        double standardDeviation = stats.getStandardDeviation();
        double coefficientOfVariation = 0;

        dispertionValues.clear();
        dispertionValues.add(formatOutput(range));
        dispertionValues.add(formatOutput(averageDeviation));
        dispertionValues.add(formatOutput(variance));
        dispertionValues.add(formatOutput(standardDeviation));
        dispertionValues.add(formatOutput(coefficientOfVariation));

        // Measures of Form
        double skewness = stats.getSkewness();
        double kurtosis = stats.getKurtosis();

        formValues.clear();
        formValues.add(formatOutput(skewness));
        formValues.add(formatOutput(kurtosis));

        valuesHashMap.put(CENTRAL_TENDENCY_MEASURES, centralTendencyValues);
        valuesHashMap.put(POSITION_MEASURES, positionValues);
        valuesHashMap.put(DISPERTION_MEASURES, dispertionValues);
        valuesHashMap.put(FORM_MEASURES, formValues);

        cela.setData(valuesHashMap);
        cela.notifyDataSetChanged();
    }
}
