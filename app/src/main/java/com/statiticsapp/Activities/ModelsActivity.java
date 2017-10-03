package com.statiticsapp.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.mikephil.charting.charts.BarChart;
import com.statiticsapp.R;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.GeometricDistribution;
import org.apache.commons.math3.distribution.GumbelDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.WeibullDistribution;
import org.apache.commons.math3.geometry.euclidean.threed.Line;

import java.util.Locale;

import static android.view.View.GONE;

public class ModelsActivity extends AppCompatActivity {

    TabHost mainTabHost;
    PDFView pdfView;

    LinearLayout binomialLayout;
    LinearLayout geometricLayout;
    LinearLayout poissonLayout;
    LinearLayout hypergeometricLayout;
    LinearLayout exponentialLayout;
    LinearLayout gammaLayout;
    LinearLayout normalLayout;
    LinearLayout gumbelLayout;
    LinearLayout weibullLayout;
    LinearLayout resultLayout;

    EditText binomialSampleSizeEt;
    EditText binomialEventProbabilityEt;
    EditText binomialValueEt;
    EditText geometricSuccessProbabilityEt;
    EditText geometricValueEt;
    EditText poissonExpectedNumberOfOcurrencesEt;
    EditText poissonValueEt;
    EditText hypergeometricPopulationSizeEt;
    EditText hypergeometricPositivesInPopulationEt;
    EditText hypergeometricSampleSizeEt;
    EditText hypergeometricValueEt;
    EditText exponentialMeanEt;
    EditText exponentialValueEt;
    EditText gammaShapeEt;
    EditText gammaScaleEt;
    EditText gammaValueEt;
    EditText normalMeanEt;
    EditText normalStdDeviationEt;
    EditText normalValueEt;
    EditText gumbelMuEt;
    EditText gumbelBetaEt;
    EditText gumbelValueEt;
    EditText weibullAlphaEt;
    EditText weibullBetaEt;
    EditText weibullValueEt;

    TextView resultTxt;
    Spinner distributionsSpinner;
    Button calculateButton;

    private static final String CALCULATE = "tab1";
    private static final String GRAPHICS = "tab2";
    private static final String THEORY = "tab3";

    private static final int CALCULATE_TAB = 0;

    private static final int BINOMIAL_DISTRIBUTION = 0;
    private static final int GEOMETRIC_DISTRIBUTION = 1;
    private static final int POISSON_DISTRIBUTION = 2;
    private static final int HYPERGEOMETRIC_DISTRIBUTION = 3;
    private static final int EXPONENTIAL_DISTRIBUTION = 4;
    private static final int GAMMA_DISTRIBUTION = 5;
    private static final int NORMAL_DISTRIBUTION = 6;
    private static final int GUMBEL_DISTRIBUTION = 7;
    private static final int WEIBULL_DISTRIBUTION = 8;

    private int selectedDistribution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_models);

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
        adapter.add("Exponencial");
        adapter.add("Gamma");
        adapter.add("Normal");
        adapter.add("Gumbel");
        adapter.add("Weibull");

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
                        double eventProbability = Double.parseDouble(binomialEventProbabilityEt.getText().toString());
                        int binomialX = Integer.parseInt(binomialValueEt.getText().toString());

                        BinomialDistribution binomialDistribution = new BinomialDistribution(binomialSampleSize, eventProbability);
                        double binomialResult = 0;
                        if(accumulates) binomialResult = binomialDistribution.cumulativeProbability(binomialX);
                        else binomialResult = binomialDistribution.probability(binomialX);

                        showResult(binomialResult);
                        break;
                    case GEOMETRIC_DISTRIBUTION:
                        double successProbability = Double.parseDouble(geometricSuccessProbabilityEt.getText().toString());
                        int geometricX = Integer.parseInt(geometricValueEt.getText().toString());

                        GeometricDistribution geometricDistribution = new GeometricDistribution(successProbability);
                        double geometricResult = 0;
                        if(accumulates) geometricResult = geometricDistribution.cumulativeProbability(geometricX);
                        else geometricResult = geometricDistribution.probability(geometricX);

                        showResult(geometricResult);
                        break;
                    case POISSON_DISTRIBUTION:
                        double expectedNumberOfOcurrences = Double.parseDouble(poissonExpectedNumberOfOcurrencesEt.getText().toString());
                        int poissonX = Integer.parseInt(poissonValueEt.getText().toString());

                        PoissonDistribution poissonDistribution = new PoissonDistribution(expectedNumberOfOcurrences);
                        double poissonResult = 0;
                        if(accumulates) poissonResult = poissonDistribution.cumulativeProbability(poissonX);
                        else poissonResult = poissonDistribution.probability(poissonX);

                        showResult(poissonResult);
                        break;
                    case HYPERGEOMETRIC_DISTRIBUTION:
                        int populationSize = Integer.parseInt(hypergeometricPopulationSizeEt.getText().toString());
                        int positivesInPopulation = Integer.parseInt(hypergeometricPositivesInPopulationEt.getText().toString());
                        int hypergeometricSampleSize = Integer.parseInt(hypergeometricSampleSizeEt.getText().toString());
                        int hypergeometricX = Integer.parseInt(hypergeometricValueEt.getText().toString());

                        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(populationSize, positivesInPopulation, hypergeometricSampleSize);
                        double hypergeometricResult = 0;
                        if(accumulates) hypergeometricResult = hypergeometricDistribution.cumulativeProbability(hypergeometricX);
                        else hypergeometricResult = hypergeometricDistribution.probability(hypergeometricX);

                        showResult(hypergeometricResult);
                        break;
                    case EXPONENTIAL_DISTRIBUTION:
                        double mean = Double.parseDouble(exponentialMeanEt.getText().toString());
                        double exponentialX = Double.parseDouble(exponentialValueEt.getText().toString());

                        ExponentialDistribution exponentialDistribution = new ExponentialDistribution(mean);
                        double exponentialResult = 0;
                        if(accumulates) exponentialResult = exponentialDistribution.cumulativeProbability(exponentialX);
                        else exponentialResult = exponentialDistribution.probability(exponentialX);

                        showResult(exponentialResult);
                        break;
                    case GAMMA_DISTRIBUTION:
                        double shape = Double.parseDouble(gammaShapeEt.getText().toString());
                        double scale = Double.parseDouble(gammaScaleEt.getText().toString());
                        double gammaX = Double.parseDouble(gammaValueEt.getText().toString());

                        GammaDistribution gammaDistribution = new GammaDistribution(shape, scale);
                        double gammaResult = 0;
                        if(accumulates) gammaResult = gammaDistribution.cumulativeProbability(gammaX);
                        else gammaResult = gammaDistribution.probability(gammaX);

                        showResult(gammaResult);
                        break;
                    case NORMAL_DISTRIBUTION:
                        double normalMean = Double.parseDouble(normalMeanEt.getText().toString());
                        double normalStdDeviation = Double.parseDouble(normalStdDeviationEt.getText().toString());
                        double normalX = Double.parseDouble(normalValueEt.getText().toString());

                        NormalDistribution normalDistribution = new NormalDistribution(normalMean, normalStdDeviation);
                        double normalResult = 0;
                        if(accumulates) normalResult = normalDistribution.cumulativeProbability(normalX);
                        else normalResult = normalDistribution.probability(normalX);

                        showResult(normalResult);
                        break;
                    case GUMBEL_DISTRIBUTION:
                        double mu = Double.parseDouble(gumbelMuEt.getText().toString());
                        double gumbelBeta = Double.parseDouble(gumbelBetaEt.getText().toString());
                        double gumbelX = Double.parseDouble(gumbelValueEt.getText().toString());

                        GumbelDistribution gumbelDistribution = new GumbelDistribution(mu, gumbelBeta);
                        double gumbelResult = 0;
                        if(accumulates) gumbelResult = gumbelDistribution.cumulativeProbability(gumbelX);
                        else gumbelResult = gumbelDistribution.probability(gumbelX);

                        showResult(gumbelResult);
                        break;
                    case WEIBULL_DISTRIBUTION:
                        double weibullAlpha = Double.parseDouble(weibullAlphaEt.getText().toString());
                        double weibullBeta = Double.parseDouble(weibullBetaEt.getText().toString());
                        double weibullX = Double.parseDouble(weibullValueEt.getText().toString());

                        WeibullDistribution weibullDistribution = new WeibullDistribution(weibullAlpha, weibullBeta);
                        double weibullResult = 0;
                        if(accumulates) weibullResult = weibullDistribution.cumulativeProbability(weibullX);
                        else weibullResult = weibullDistribution.probability(weibullX);

                        showResult(weibullResult);
                        break;
                }
            }
        });

        binomialLayout = (LinearLayout) findViewById(R.id.binomial_layout);
        binomialSampleSizeEt = (EditText) findViewById(R.id.binomial_sample_size);
        binomialEventProbabilityEt = (EditText) findViewById(R.id.binomial_event_probability);
        binomialValueEt = (EditText) findViewById(R.id.binomial_value);

        geometricLayout = (LinearLayout) findViewById(R.id.geometric_layout);
        geometricSuccessProbabilityEt = (EditText) findViewById(R.id.geometric_success_probability);
        geometricValueEt = (EditText) findViewById(R.id.geometric_value);

        poissonLayout = (LinearLayout) findViewById(R.id.poisson_layout);
        poissonExpectedNumberOfOcurrencesEt = (EditText) findViewById(R.id.poisson_expected_number_ocurrences);
        poissonValueEt = (EditText) findViewById(R.id.poisson_value);

        hypergeometricLayout = (LinearLayout) findViewById(R.id.hypergemoetric_layout);
        hypergeometricPopulationSizeEt = (EditText) findViewById(R.id.hypergemoetric_population_size);
        hypergeometricPositivesInPopulationEt = (EditText) findViewById(R.id.hypergemoetric_positives_in_population);
        hypergeometricSampleSizeEt = (EditText) findViewById(R.id.hypergemoetric_sample_size);
        hypergeometricValueEt = (EditText) findViewById(R.id.hypergemoetric_value);

        exponentialLayout = (LinearLayout) findViewById(R.id.exponential_layout);
        exponentialMeanEt = (EditText) findViewById(R.id.exponential_mean);
        exponentialValueEt = (EditText) findViewById(R.id.exponential_value);

        gammaLayout = (LinearLayout) findViewById(R.id.gamma_layout);
        gammaShapeEt = (EditText) findViewById(R.id.gamma_shape);
        gammaScaleEt = (EditText) findViewById(R.id.gamma_scale);
        gammaValueEt = (EditText) findViewById(R.id.gamma_value);

        normalLayout = (LinearLayout) findViewById(R.id.normal_layout);
        normalMeanEt = (EditText) findViewById(R.id.normal_mean);
        normalStdDeviationEt = (EditText) findViewById(R.id.normal_standard_deviation);
        normalValueEt = (EditText) findViewById(R.id.normal_value);

        gumbelLayout = (LinearLayout) findViewById(R.id.gumbel_layout);
        gumbelMuEt = (EditText) findViewById(R.id.gumbel_mu);
        gumbelBetaEt = (EditText) findViewById(R.id.gumbel_beta);
        gumbelValueEt = (EditText) findViewById(R.id.gumbel_value);

        weibullLayout = (LinearLayout) findViewById(R.id.weibull_layout);
        weibullAlphaEt = (EditText) findViewById(R.id.weibull_alpha);
        weibullBetaEt = (EditText) findViewById(R.id.weibull_beta);
        weibullValueEt = (EditText) findViewById(R.id.weibull_value);

        resultLayout = (LinearLayout) findViewById(R.id.result_layout);
        resultTxt = (TextView) findViewById(R.id.result);

        pdfView.fromAsset("models.pdf")
               .enableSwipe(true)
               .swipeHorizontal(true)
               .enableDoubletap(true)
               .scrollHandle(new DefaultScrollHandle(this))
               .load();
    }

    private void showResult(double result) {
        resultTxt.setText(String.format(Locale.getDefault(), "%.2f", result));
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
            case EXPONENTIAL_DISTRIBUTION:
                exponentialLayout.setVisibility(View.VISIBLE);
                break;
            case GAMMA_DISTRIBUTION:
                gammaLayout.setVisibility(View.VISIBLE);
                break;
            case NORMAL_DISTRIBUTION:
                normalLayout.setVisibility(View.VISIBLE);
                break;
            case GUMBEL_DISTRIBUTION:
                gumbelLayout.setVisibility(View.VISIBLE);
                break;
            case WEIBULL_DISTRIBUTION:
                weibullLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void hideAll() {
        binomialLayout.setVisibility(GONE);
        geometricLayout.setVisibility(GONE);
        poissonLayout.setVisibility(GONE);
        hypergeometricLayout.setVisibility(GONE);
        exponentialLayout.setVisibility(GONE);
        gammaLayout.setVisibility(GONE);
        normalLayout.setVisibility(GONE);
        gumbelLayout.setVisibility(GONE);
        weibullLayout.setVisibility(GONE);
        resultLayout.setVisibility(GONE);
    }

    private boolean isNotPositive(double value) {
        return value <= 0;
    }

    private boolean isInputEmpty(EditText editText) {
        return editText.getText().toString().length() == 0;
    }

    private boolean isProbabilityInvalid(double probability) {
        return probability < 0 || probability > 1;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean checkForInvalidInputs() {
        boolean result = true;

        switch (selectedDistribution) {
            case BINOMIAL_DISTRIBUTION:
                if (isInputEmpty(binomialSampleSizeEt)) {
                    result = false;
                    showToast(this.getResources().getString(R.string.sample_size_error));
                }
                if (isInputEmpty(binomialEventProbabilityEt)) {
                    result = false;
                    showToast("Debe ingresar un valor para el evento de probabilidad");
                }
                else {
                    double eventProbability = Double.parseDouble(binomialEventProbabilityEt.getText().toString());
                    if(isProbabilityInvalid(eventProbability)) {
                        result = false;
                        showToast("El valor para el evento de probabilidad debe estar entre 0 y 1 inclusive");
                    }
                }
                if (isInputEmpty(binomialValueEt)) {
                    result = false;
                    showToast(this.getResources().getString(R.string.x_value_error));
                }
                break;
            case GEOMETRIC_DISTRIBUTION:
                if (isInputEmpty(geometricSuccessProbabilityEt)) {
                    result = false;
                    showToast("Debe ingresar un valor para la probabilidad de éxito");
                }
                else {
                    double successProbability = Double.parseDouble(geometricSuccessProbabilityEt.getText().toString());
                    if(isProbabilityInvalid(successProbability)) {
                        result = false;
                        showToast("El valor para la probabilidad de éxito debe estar entre 0 y 1 inclusive");
                    }
                }
                if (isInputEmpty(geometricValueEt)) {
                    result = false;
                    showToast(this.getResources().getString(R.string.x_value_error));
                }
                break;
            case POISSON_DISTRIBUTION:
                if (isInputEmpty(poissonExpectedNumberOfOcurrencesEt)) {
                    result = false;
                    showToast("Debe ingresar un valor para el nro. esperado de ocurrencias");
                }
                if (isInputEmpty(poissonValueEt)) {
                    result = false;
                    showToast(this.getResources().getString(R.string.x_value_error));
                }
                break;
            case HYPERGEOMETRIC_DISTRIBUTION:
                if (isInputEmpty(hypergeometricPopulationSizeEt)) {
                    result = false;
                    showToast("Debe ingresar un valor de población");
                }
                if (isInputEmpty(hypergeometricPositivesInPopulationEt)) {
                    result = false;
                    showToast("Debe ingresar un valor para la cantidad de éxitos");
                }
                if (isInputEmpty(hypergeometricSampleSizeEt)) {
                    result = false;
                    showToast(this.getResources().getString(R.string.sample_size_error));
                }
                if (isInputEmpty(hypergeometricValueEt)) {
                    result = false;
                    showToast(this.getResources().getString(R.string.x_value_error));
                }
                break;
            case EXPONENTIAL_DISTRIBUTION:
                if(isInputEmpty(exponentialMeanEt)) {
                    result = false;
                    showToast("Debe ingresar un valor para la media.");
                }
                if(isInputEmpty(exponentialValueEt)) {
                    result = false;
                    showToast(this.getResources().getString(R.string.x_value_error));
                }
                break;
            case GAMMA_DISTRIBUTION:
                if(isInputEmpty(gammaShapeEt)) {
                    result = false;
                    showToast("Debe ingresar un valor para la forma.");
                }
                if(isInputEmpty(gammaScaleEt)) {
                    result = false;
                    showToast("Debe ingresar un valor para la escala.");
                }
                if(isInputEmpty(gammaValueEt)) {
                    result = false;
                    showToast(getResources().getString(R.string.x_value_error));
                }
                break;
            case NORMAL_DISTRIBUTION:
                if(isInputEmpty(normalMeanEt)) {
                    result = false;
                    showToast("Debe ingresar un valor para la media.");
                }
                if(isInputEmpty(normalStdDeviationEt)) {
                    result = false;
                    showToast("Debe ingresar una desviación estándar.");
                }
                if(isInputEmpty(normalValueEt)) {
                    result = false;
                    showToast(getResources().getString(R.string.x_value_error));
                }
                break;
            case GUMBEL_DISTRIBUTION:
                if(isInputEmpty(gumbelMuEt)) {
                    result = false;
                    showToast("Debe ingresar un valor para Mu.");
                }
                if(isInputEmpty(gumbelBetaEt)) {
                    result = false;
                    showToast(getResources().getString(R.string.beta_empty_error));
                }
                else {
                    double beta = Double.parseDouble(gumbelBetaEt.getText().toString());
                    if(isNotPositive(beta)) {
                        result = false;
                        showToast(getResources().getString(R.string.beta_invalid));
                    }
                }
                if(isInputEmpty(gumbelValueEt)) {
                    result = false;
                    showToast(getResources().getString(R.string.x_value_error));
                }
                break;
            case WEIBULL_DISTRIBUTION:
                if(isInputEmpty(weibullAlphaEt)) {
                    result = false;
                    showToast("Debe ingresar un valor para Alpha.");
                }
                else {
                    double alpha = Double.parseDouble(weibullAlphaEt.getText().toString());
                    if(isNotPositive(alpha)) {
                        result = false;
                        showToast("Alpha debe ser positivo.");
                    }
                }
                if(isInputEmpty(weibullBetaEt)) {
                    result = false;
                    showToast(getResources().getString(R.string.beta_empty_error));
                }
                else {
                    double beta = Double.parseDouble(weibullBetaEt.getText().toString());
                    if(isNotPositive(beta)) {
                        result = false;
                        showToast(getResources().getString(R.string.beta_invalid));
                    }
                }
                if(isInputEmpty(weibullValueEt)) {
                    result = false;
                    showToast(getResources().getString(R.string.x_value_error));
                }
                break;
        }

        return result;
    }
}
