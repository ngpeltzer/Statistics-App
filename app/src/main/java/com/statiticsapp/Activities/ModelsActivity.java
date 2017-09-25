package com.statiticsapp.Activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.google.common.primitives.Floats;
import com.opencsv.CSVReader;
import com.statiticsapp.Adapters.CalculateExpandableListAdapter;
import com.statiticsapp.CustomViews.GenericDialog;
import com.statiticsapp.Interfaces.GenericDialogListener;
import com.statiticsapp.R;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.GeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.Precision;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

import static android.view.View.GONE;

public class ModelsActivity extends AppCompatActivity {

    TabHost mainTabHost;
    PDFView pdfView;
    BarChart graphView;

    LinearLayout binomialLayout;
    LinearLayout geometricLayout;
    LinearLayout poissonLayout;
    LinearLayout hypergeometricLayout;
    LinearLayout resultLayout;

    EditText binomialSampleSizeEt;
    EditText eventProbabilityEt;
    EditText binomialValueEt;
    EditText successProbabilityEt;
    EditText geometricValueEt;
    EditText expectedNumberOfOcurrencesEt;
    EditText poissonValueEt;
    EditText populationSizeEt;
    EditText positivesInPopulationEt;
    EditText hypergeometricSampleSizeEt;
    EditText hypergeometricValueEt;

    TextView resultTxt;
    Spinner distributionsSpinner;
    Button calculateButton;

    private static String CALCULATE = "tab1";
    private static String GRAPHICS = "tab2";
    private static String THEORY = "tab3";

    private static int CALCULATE_TAB = 0;
    private static int GRAPHICS_TAB = 1;
    private static int THEORY_TAB = 2;

    private static final int BINOMIAL_DISTRIBUTION = 0;
    private static final int GEOMETRIC_DISTRIBUTION = 1;
    private static final int POISSON_DISTRIBUTION = 2;
    private static final int HYPERGEOMETRIC_DISTRIBUTION = 3;

    private Activity ma;
    private int selectedDistribution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_models);
        ma = this;

        pdfView = (PDFView) findViewById(R.id.tab_theory_pdf_view);

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.add("Binomial");
        adapter.add("Geométrica");
        adapter.add("Poisson");
        adapter.add("Hypergeométrica");

        distributionsSpinner = (Spinner) findViewById(R.id.distributions_spinner);
        distributionsSpinner.setAdapter(adapter);
        distributionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedDistribution = position;
                showSelectedDistribution();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        calculateButton = (Button) findViewById(R.id.calculate);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if(!checkForInvalidInputs()) return;

                CheckBox cumulativeCb = (CheckBox) findViewById(R.id.cumulative_checkbox);
                boolean accumulates = cumulativeCb.isChecked();

                switch (selectedDistribution) {
                    case BINOMIAL_DISTRIBUTION:
                        int binomialSampleSize = Integer.parseInt(binomialSampleSizeEt.getText().toString());
                        double eventProbability = Double.parseDouble(eventProbabilityEt.getText().toString());
                        int binomialX = Integer.parseInt(binomialValueEt.getText().toString());

                        BinomialDistribution binomialDistribution = new BinomialDistribution(binomialSampleSize, eventProbability);
                        double binomialResult = 0;
                        if(accumulates) binomialResult = binomialDistribution.cumulativeProbability(binomialX);
                        else binomialResult = binomialDistribution.probability(binomialX);

                        showResult(String.format("%.2f", binomialResult));
                        break;
                    case GEOMETRIC_DISTRIBUTION:
                        double successProbability = Double.parseDouble(successProbabilityEt.getText().toString());
                        int geometricX = Integer.parseInt(geometricValueEt.getText().toString());

                        GeometricDistribution geometricDistribution = new GeometricDistribution(successProbability);
                        double geometricResult = 0;
                        if(accumulates) geometricResult = geometricDistribution.cumulativeProbability(geometricX);
                        else geometricResult = geometricDistribution.probability(geometricX);

                        showResult(String.format("%.2f", geometricResult));
                        break;
                    case POISSON_DISTRIBUTION:
                        double expectedNumberOfOcurrences = Double.parseDouble(expectedNumberOfOcurrencesEt.getText().toString());
                        int poissonX = Integer.parseInt(poissonValueEt.getText().toString());

                        PoissonDistribution poissonDistribution = new PoissonDistribution(expectedNumberOfOcurrences);
                        double poissonResult = 0;
                        if(accumulates) poissonResult = poissonDistribution.cumulativeProbability(poissonX);
                        else poissonResult = poissonDistribution.probability(poissonX);

                        showResult(String.format("%.2f", poissonResult));
                        break;
                    case HYPERGEOMETRIC_DISTRIBUTION:
                        int populationSize = Integer.parseInt(populationSizeEt.getText().toString());
                        int positivesInPopulation = Integer.parseInt(positivesInPopulationEt.getText().toString());
                        int hypergeometricSampleSize = Integer.parseInt(hypergeometricSampleSizeEt.getText().toString());
                        int hypergeometricX = Integer.parseInt(hypergeometricValueEt.getText().toString());

                        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, positivesInPopulation, hypergeometricSampleSize);
                        double hypergeometricResult = 0;
                        if(accumulates) hypergeometricResult = hypergeometricDistribution.cumulativeProbability(hypergeometricX);
                        else hypergeometricResult = hypergeometricDistribution.probability(hypergeometricX);

                        showResult(String.format("%.2f", hypergeometricResult));
                        break;
                }
            }
        });

        resultTxt = (TextView) findViewById(R.id.result);

        binomialLayout = (LinearLayout) findViewById(R.id.binomial_layout);
        geometricLayout = (LinearLayout) findViewById(R.id.geometric_layout);
        poissonLayout = (LinearLayout) findViewById(R.id.poisson_layout);
        hypergeometricLayout = (LinearLayout) findViewById(R.id.hypergemoetric_layout);
        resultLayout = (LinearLayout) findViewById(R.id.result_layout);

        binomialSampleSizeEt = (EditText) findViewById(R.id.binomial_sample_size);
        eventProbabilityEt = (EditText) findViewById(R.id.binomial_event_probability);
        binomialValueEt = (EditText) findViewById(R.id.binomial_value);

        successProbabilityEt = (EditText) findViewById(R.id.geometric_success_probability);
        geometricValueEt = (EditText) findViewById(R.id.geometric_value);

        expectedNumberOfOcurrencesEt = (EditText) findViewById(R.id.poisson_expected_number_ocurrences);
        poissonValueEt = (EditText) findViewById(R.id.poisson_value);

        populationSizeEt = (EditText) findViewById(R.id.hypergemoetric_population_size);
        positivesInPopulationEt = (EditText) findViewById(R.id.hypergemoetric_positives_in_population);
        hypergeometricSampleSizeEt = (EditText) findViewById(R.id.hypergemoetric_sample_size);
        hypergeometricValueEt = (EditText) findViewById(R.id.hypergemoetric_value);

        pdfView.fromAsset("models.pdf")
               .enableSwipe(true)
               .swipeHorizontal(true)
               .enableDoubletap(true)
               .scrollHandle(new DefaultScrollHandle(this))
               .load();
    }

    private void showResult(String result) {
        resultTxt.setText(result);
        resultLayout.setVisibility(View.VISIBLE);
    }

    private void showSelectedDistribution() {
        hideAll();

        switch (selectedDistribution) {
            case BINOMIAL_DISTRIBUTION:
                binomialLayout.setVisibility(View.VISIBLE);
                break;
            case GEOMETRIC_DISTRIBUTION:
                geometricLayout.setVisibility(View.VISIBLE);
                break;
            case POISSON_DISTRIBUTION:
                poissonLayout.setVisibility(View.VISIBLE);
                break;
            case HYPERGEOMETRIC_DISTRIBUTION:
                hypergeometricLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void hideAll() {
        binomialLayout.setVisibility(GONE);
        geometricLayout.setVisibility(GONE);
        poissonLayout.setVisibility(GONE);
        hypergeometricLayout.setVisibility(GONE);
        resultLayout.setVisibility(GONE);
    }

    private boolean checkForInvalidInputs() {
        boolean result = true;

        switch (selectedDistribution) {
            case BINOMIAL_DISTRIBUTION:
                if (binomialSampleSizeEt.getText().toString().length() == 0) {
                    result = false;
                    Toast.makeText(this, this.getResources().getString(R.string.sample_size_error), Toast.LENGTH_SHORT).show();
                }
                if (eventProbabilityEt.getText().toString().length() == 0) {
                    result = false;
                    Toast.makeText(this, "Debe ingresar un valor para el evento de probabilidad", Toast.LENGTH_SHORT).show();
                }
                else {
                    double eventProbability = Double.parseDouble(eventProbabilityEt.getText().toString());
                    if(eventProbability < 0 || eventProbability > 1) {
                        result = false;
                        Toast.makeText(this, "El valor para el evento de probabilidad debe estar entre 0 y 1 inclusive", Toast.LENGTH_SHORT).show();
                    }
                }
                if (binomialValueEt.getText().toString().length() == 0) {
                    result = false;
                    Toast.makeText(this, this.getResources().getString(R.string.x_value_error), Toast.LENGTH_SHORT).show();
                }
                break;
            case GEOMETRIC_DISTRIBUTION:
                if (successProbabilityEt.getText().toString().length() == 0) {
                    result = false;
                    Toast.makeText(this, "Debe ingresar un valor para la probabilidad de éxito", Toast.LENGTH_SHORT).show();
                }
                else {
                    double successProbability = Double.parseDouble(successProbabilityEt.getText().toString());
                    if(successProbability < 0 || successProbability > 1) {
                        result = false;
                        Toast.makeText(this, "El valor para la probabilidad de éxito debe estar entre 0 y 1 inclusive", Toast.LENGTH_SHORT).show();
                    }
                }
                if (geometricValueEt.getText().toString().length() == 0) {
                    result = false;
                    Toast.makeText(this, this.getResources().getString(R.string.x_value_error), Toast.LENGTH_SHORT).show();
                }
                break;
            case POISSON_DISTRIBUTION:
                if (expectedNumberOfOcurrencesEt.getText().toString().length() == 0) {
                    result = false;
                    Toast.makeText(this, "Debe ingresar un valor para el nro. esperado de ocurrencias", Toast.LENGTH_SHORT).show();
                }
                if (poissonValueEt.getText().toString().length() == 0) {
                    result = false;
                    Toast.makeText(this, this.getResources().getString(R.string.x_value_error), Toast.LENGTH_SHORT).show();
                }
                break;
            case HYPERGEOMETRIC_DISTRIBUTION:
                if (populationSizeEt.getText().toString().length() == 0) {
                    result = false;
                    Toast.makeText(this, "Debe ingresar un valor de población", Toast.LENGTH_SHORT).show();
                }
                if (positivesInPopulationEt.getText().toString().length() == 0) {
                    result = false;
                    Toast.makeText(this, "Debe ingresar un valor para la cantidad de éxitos", Toast.LENGTH_SHORT).show();
                }
                if (hypergeometricSampleSizeEt.getText().toString().length() == 0) {
                    result = false;
                    Toast.makeText(this, this.getResources().getString(R.string.sample_size_error), Toast.LENGTH_SHORT).show();
                }
                if (hypergeometricValueEt.getText().toString().length() == 0) {
                    result = false;
                    Toast.makeText(this, this.getResources().getString(R.string.x_value_error), Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return result;
    }
}
