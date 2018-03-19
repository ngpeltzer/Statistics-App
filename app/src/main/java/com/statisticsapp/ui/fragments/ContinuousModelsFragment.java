package com.statisticsapp.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.statisticsapp.R;
import com.statisticsapp.utils.Utils;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.GumbelDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.WeibullDistribution;

import java.util.Locale;

public class ContinuousModelsFragment extends Fragment {

    private static final int EXPONENTIAL_DISTRIBUTION = 0;
    private static final int GAMMA_DISTRIBUTION = 1;
    private static final int NORMAL_DISTRIBUTION = 2;
    private static final int GUMBEL_DISTRIBUTION = 3;
    private static final int WEIBULL_DISTRIBUTION = 4;
    private static final int STANDARD_NORMAL_DISTRIBUTION = 5;
    private static final int INVERSE_EXPONENTIAL_DISTRIBUTION = 6;
    private static final int INVERSE_GAMMA_DISTRIBUTION = 7;
    private static final int INVERSE_GUMBEL_DISTRIBUTION = 8;
    private static final int INVERSE_WEIBULL_DISTRIBUTION = 9;
    private static final int INVERSE_STANDARD_NORMAL_DISTRIBUTION = 10;

    private LinearLayout exponentialLayout;
    private LinearLayout gammaLayout;
    private LinearLayout normalLayout;
    private LinearLayout gumbelLayout;
    private LinearLayout weibullLayout;
    private LinearLayout standardNormalLayout;
    private LinearLayout inverseExponentialLayout;
    private LinearLayout inverseGammaLayout;
    private LinearLayout inverseGumbelLayout;
    private LinearLayout inverseWeibullLayout;
    private LinearLayout inverseStandardNormalLayout;
    private LinearLayout resultLayout;
    private EditText exponentialMeanEt;
    private EditText exponentialValueEt;
    private EditText gammaShapeEt;
    private EditText gammaScaleEt;
    private EditText gammaValueEt;
    private EditText normalMeanEt;
    private EditText normalStdDeviationEt;
    private EditText normalValueEt;
    private EditText gumbelMuEt;
    private EditText gumbelBetaEt;
    private EditText gumbelValueEt;
    private EditText weibullAlphaEt;
    private EditText weibullBetaEt;
    private EditText weibullValueEt;
    private EditText standardNormalValueEt;
    private EditText inverseExponentialMeanEt;
    private EditText inverseExponentialValueEt;
    private EditText inverseGammaShapeEt;
    private EditText inverseGammaScaleEt;
    private EditText inverseGammaValueEt;
    private EditText inverseGumbelMuEt;
    private EditText inverseGumbelBetaEt;
    private EditText inverseGumbelValueEt;
    private EditText inverseWeibullAlphaEt;
    private EditText inverseWeibullBetaEt;
    private EditText inverseWeibullValueEt;
    private EditText inverseStandardNormalValueEt;
    private TextView resultTxt;
    private CheckBox cumulativeCb;
    private ScrollView scrollView;

    private int selectedDistribution;

    public static Fragment newInstance() {
        return new ContinuousModelsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_continous_models, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.add(getString(R.string.exponential));
        adapter.add(getString(R.string.gamma));
        adapter.add(getString(R.string.normal));
        adapter.add(getString(R.string.gumbel));
        adapter.add(getString(R.string.weibull));
        adapter.add(getString(R.string.standard_normal));
        adapter.add(getString(R.string.inverse_exponential));
        adapter.add(getString(R.string.inverse_gamma));
        adapter.add(getString(R.string.inverse_gumbel));
        adapter.add(getString(R.string.inverse_weibull));
        adapter.add(getString(R.string.inverse_standard_normal));

        Spinner distributionsSpinner = root.findViewById(R.id.distributions_spinner);
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

        exponentialLayout = root.findViewById(R.id.exponential_layout);
        exponentialMeanEt = root.findViewById(R.id.exponential_mean);
        exponentialValueEt = root.findViewById(R.id.exponential_value);

        gammaLayout = root.findViewById(R.id.gamma_layout);
        gammaShapeEt = root.findViewById(R.id.gamma_shape);
        gammaScaleEt = root.findViewById(R.id.gamma_scale);
        gammaValueEt = root.findViewById(R.id.gamma_value);

        normalLayout = root.findViewById(R.id.normal_layout);
        normalMeanEt = root.findViewById(R.id.normal_mean);
        normalStdDeviationEt = root.findViewById(R.id.normal_standard_deviation);
        normalValueEt = root.findViewById(R.id.normal_value);

        gumbelLayout = root.findViewById(R.id.gumbel_layout);
        gumbelMuEt = root.findViewById(R.id.gumbel_mu);
        gumbelBetaEt = root.findViewById(R.id.gumbel_beta);
        gumbelValueEt = root.findViewById(R.id.gumbel_value);

        weibullLayout = root.findViewById(R.id.weibull_layout);
        weibullAlphaEt = root.findViewById(R.id.weibull_alpha);
        weibullBetaEt = root.findViewById(R.id.weibull_beta);
        weibullValueEt = root.findViewById(R.id.weibull_value);

        standardNormalLayout = root.findViewById(R.id.standard_normal_layout);
        standardNormalValueEt = root.findViewById(R.id.standard_normal_value);

        inverseExponentialLayout = root.findViewById(R.id.inverse_exponential_layout);
        inverseExponentialMeanEt = root.findViewById(R.id.inverse_exponential_mean);
        inverseExponentialValueEt = root.findViewById(R.id.inverse_exponential_value);

        inverseGammaLayout = root.findViewById(R.id.inverse_gamma_layout);
        inverseGammaShapeEt = root.findViewById(R.id.inverse_gamma_shape);
        inverseGammaScaleEt = root.findViewById(R.id.inverse_gamma_scale);
        inverseGammaValueEt = root.findViewById(R.id.inverse_gamma_value);

        inverseGumbelLayout = root.findViewById(R.id.inverse_gumbel_layout);
        inverseGumbelMuEt = root.findViewById(R.id.inverse_gumbel_mu);
        inverseGumbelBetaEt = root.findViewById(R.id.inverse_gumbel_beta);
        inverseGumbelValueEt = root.findViewById(R.id.inverse_gumbel_value);

        inverseWeibullLayout = root.findViewById(R.id.inverse_weibull_layout);
        inverseWeibullAlphaEt = root.findViewById(R.id.inverse_weibull_alpha);
        inverseWeibullBetaEt = root.findViewById(R.id.inverse_weibull_beta);
        inverseWeibullValueEt = root.findViewById(R.id.inverse_weibull_value);

        inverseStandardNormalLayout = root.findViewById(R.id.inverse_standard_normal_layout);
        inverseStandardNormalValueEt = root.findViewById(R.id.inverse_standard_normal_value);

        resultLayout = root.findViewById(R.id.result_layout);
        resultTxt = root.findViewById(R.id.result);
        cumulativeCb = root.findViewById(R.id.cumulative_checkbox);
        scrollView = root.findViewById(R.id.continuous_scrollview);

        Button calculateButton = root.findViewById(R.id.calculate);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard(getActivity());

                if(!areInputsValid()) return;

                boolean accumulates = cumulativeCb.isChecked();

                switch (selectedDistribution) {
                    case EXPONENTIAL_DISTRIBUTION:
                        double mean = Double.parseDouble(exponentialMeanEt.getText().toString());
                        double exponentialX = Double.parseDouble(exponentialValueEt.getText().toString());

                        ExponentialDistribution exponentialDistribution = new ExponentialDistribution(mean);
                        double exponentialResult;
                        if(accumulates) exponentialResult = exponentialDistribution.cumulativeProbability(exponentialX);
                        else exponentialResult = exponentialDistribution.probability(exponentialX);

                        showResult(exponentialResult);
                        break;
                    case GAMMA_DISTRIBUTION:
                        double shape = Double.parseDouble(gammaShapeEt.getText().toString());
                        double scale = Double.parseDouble(gammaScaleEt.getText().toString());
                        double gammaX = Double.parseDouble(gammaValueEt.getText().toString());

                        GammaDistribution gammaDistribution = new GammaDistribution(shape, scale);
                        double gammaResult;
                        if(accumulates) gammaResult = gammaDistribution.cumulativeProbability(gammaX);
                        else gammaResult = gammaDistribution.probability(gammaX);

                        showResult(gammaResult);
                        break;
                    case NORMAL_DISTRIBUTION:
                        double normalMean = Double.parseDouble(normalMeanEt.getText().toString());
                        double normalStdDeviation = Double.parseDouble(normalStdDeviationEt.getText().toString());
                        double normalX = Double.parseDouble(normalValueEt.getText().toString());

                        NormalDistribution normalDistribution = new NormalDistribution(normalMean, normalStdDeviation);
                        double normalResult;
                        if(accumulates) normalResult = normalDistribution.cumulativeProbability(normalX);
                        else normalResult = normalDistribution.probability(normalX);

                        showResult(normalResult);
                        break;
                    case GUMBEL_DISTRIBUTION:
                        double mu = Double.parseDouble(gumbelMuEt.getText().toString());
                        double gumbelBeta = Double.parseDouble(gumbelBetaEt.getText().toString());
                        double gumbelX = Double.parseDouble(gumbelValueEt.getText().toString());

                        GumbelDistribution gumbelDistribution = new GumbelDistribution(mu, gumbelBeta);
                        double gumbelResult;
                        if(accumulates) gumbelResult = gumbelDistribution.cumulativeProbability(gumbelX);
                        else gumbelResult = gumbelDistribution.probability(gumbelX);

                        showResult(gumbelResult);
                        break;
                    case WEIBULL_DISTRIBUTION:
                        double weibullAlpha = Double.parseDouble(weibullAlphaEt.getText().toString());
                        double weibullBeta = Double.parseDouble(weibullBetaEt.getText().toString());
                        double weibullX = Double.parseDouble(weibullValueEt.getText().toString());

                        WeibullDistribution weibullDistribution = new WeibullDistribution(weibullAlpha, weibullBeta);
                        double weibullResult;
                        if(accumulates) weibullResult = weibullDistribution.cumulativeProbability(weibullX);
                        else weibullResult = weibullDistribution.probability(weibullX);

                        showResult(weibullResult);
                        break;
                    case STANDARD_NORMAL_DISTRIBUTION:
                        int standardNormalMean = 0;
                        int standardNormalStdDeviation = 1;
                        double standardNormalZ = Double.parseDouble(standardNormalValueEt.getText().toString());

                        NormalDistribution standardNormalDistribution = new NormalDistribution(standardNormalMean, standardNormalStdDeviation);
                        double standardNormalResult;
                        if(accumulates) standardNormalResult = standardNormalDistribution.cumulativeProbability(standardNormalZ);
                        else standardNormalResult = standardNormalDistribution.probability(standardNormalZ);

                        showResult(standardNormalResult);
                        break;
                    case INVERSE_EXPONENTIAL_DISTRIBUTION:
                        double inverseExponentiaMean = Double.parseDouble(inverseExponentialMeanEt.getText().toString());
                        double inverseExponentialP = Double.parseDouble(inverseExponentialValueEt.getText().toString());

                        ExponentialDistribution inverseExponentialDistribution = new ExponentialDistribution(inverseExponentiaMean);
                        double inverseExponentialResult;
                        if(accumulates) inverseExponentialResult = inverseExponentialDistribution.inverseCumulativeProbability(inverseExponentialP);
                        else inverseExponentialResult = inverseExponentialDistribution.probability(inverseExponentialP);

                        showResult(inverseExponentialResult);
                        break;
                    case INVERSE_GAMMA_DISTRIBUTION:
                        double inverseShape = Double.parseDouble(gammaShapeEt.getText().toString());
                        double inverseScale = Double.parseDouble(gammaScaleEt.getText().toString());
                        double inverseGammaP = Double.parseDouble(gammaValueEt.getText().toString());

                        GammaDistribution inverseGammaDistribution = new GammaDistribution(inverseShape, inverseScale);
                        double inverseGammaResult;
                        if(accumulates) inverseGammaResult = inverseGammaDistribution.inverseCumulativeProbability(inverseGammaP);
                        else inverseGammaResult = inverseGammaDistribution.probability(inverseGammaP);

                        showResult(inverseGammaResult);
                        break;
                    case INVERSE_GUMBEL_DISTRIBUTION:
                        double inverseMu = Double.parseDouble(inverseGumbelMuEt.getText().toString());
                        double inverseGumbelBeta = Double.parseDouble(inverseGumbelBetaEt.getText().toString());
                        double inverseGumbelP = Double.parseDouble(inverseGumbelValueEt.getText().toString());

                        GumbelDistribution inverseGumbelDistribution = new GumbelDistribution(inverseMu, inverseGumbelBeta);
                        double inverseGumbelResult;
                        if(accumulates) inverseGumbelResult = inverseGumbelDistribution.inverseCumulativeProbability(inverseGumbelP);
                        else inverseGumbelResult = inverseGumbelDistribution.probability(inverseGumbelP);

                        showResult(inverseGumbelResult);
                        break;
                    case INVERSE_WEIBULL_DISTRIBUTION:
                        double inverseWeibullAlpha = Double.parseDouble(inverseWeibullAlphaEt.getText().toString());
                        double inverseWeibullBeta = Double.parseDouble(inverseWeibullBetaEt.getText().toString());
                        double inverseWeibullP = Double.parseDouble(inverseWeibullValueEt.getText().toString());

                        WeibullDistribution inverseWeibullDistribution = new WeibullDistribution(inverseWeibullAlpha, inverseWeibullBeta);
                        double inverseWeibullResult;
                        if(accumulates) inverseWeibullResult = inverseWeibullDistribution.inverseCumulativeProbability(inverseWeibullP);
                        else inverseWeibullResult = inverseWeibullDistribution.probability(inverseWeibullP);

                        showResult(inverseWeibullResult);
                        break;
                    case INVERSE_STANDARD_NORMAL_DISTRIBUTION:
                        double inverseStandardNormalP = Double.parseDouble(inverseStandardNormalValueEt.getText().toString());

                        NormalDistribution inverseStandardNormalDistribution = new NormalDistribution();
                        double inverseStandardNormalResult = inverseStandardNormalDistribution.inverseCumulativeProbability(inverseStandardNormalP);

                        showResult(inverseStandardNormalResult);
                        break;
                }

                scrollDown();
            }
        });

        return root;
    }

    private void showSelectedDistribution() {
        Utils.hideAll(exponentialLayout, gammaLayout, normalLayout,
                gumbelLayout, weibullLayout, standardNormalLayout,
                inverseExponentialLayout, inverseGammaLayout, inverseGumbelLayout,
                inverseWeibullLayout, inverseStandardNormalLayout, resultLayout);

        switch (selectedDistribution) {
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
            case STANDARD_NORMAL_DISTRIBUTION:
                standardNormalLayout.setVisibility(View.VISIBLE);
                break;
            case INVERSE_EXPONENTIAL_DISTRIBUTION:
                inverseExponentialLayout.setVisibility(View.VISIBLE);
                break;
            case INVERSE_GAMMA_DISTRIBUTION:
                inverseGammaLayout.setVisibility(View.VISIBLE);
                break;
            case INVERSE_GUMBEL_DISTRIBUTION:
                inverseGumbelLayout.setVisibility(View.VISIBLE);
                break;
            case INVERSE_WEIBULL_DISTRIBUTION:
                inverseWeibullLayout.setVisibility(View.VISIBLE);
                break;
            case INVERSE_STANDARD_NORMAL_DISTRIBUTION:
                inverseStandardNormalLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showResult(double result) {
        resultTxt.setText(String.format(Locale.getDefault(), "%.2f", result));
        resultLayout.setVisibility(View.VISIBLE);
    }

    private void scrollDown() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private boolean areInputsValid() {
        boolean result = true;

        switch (selectedDistribution) {
            case EXPONENTIAL_DISTRIBUTION:
                if(Utils.isInputEmpty(exponentialMeanEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor para la media.");
                }
                if(Utils.isInputEmpty(exponentialValueEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.x_value_error));
                }
                break;
            case GAMMA_DISTRIBUTION:
                if(Utils.isInputEmpty(gammaShapeEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor para la forma.");
                }
                if(Utils.isInputEmpty(gammaScaleEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor para la escala.");
                }
                if(Utils.isInputEmpty(gammaValueEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.x_value_error));
                }
                break;
            case NORMAL_DISTRIBUTION:
                if(Utils.isInputEmpty(normalMeanEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor para la media.");
                }
                if(Utils.isInputEmpty(normalStdDeviationEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar una desviación estándar.");
                }
                if(Utils.isInputEmpty(normalValueEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.x_value_error));
                }
                break;
            case GUMBEL_DISTRIBUTION:
                if(Utils.isInputEmpty(gumbelMuEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor para Mu.");
                }
                if(Utils.isInputEmpty(gumbelBetaEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.beta_empty_error));
                }
                else {
                    double beta = Double.parseDouble(gumbelBetaEt.getText().toString());
                    if(Utils.isNotPositive(beta)) {
                        result = false;
                        Utils.showToast(getContext(), getString(R.string.beta_invalid));
                    }
                }
                if(Utils.isInputEmpty(gumbelValueEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.x_value_error));
                }
                break;
            case WEIBULL_DISTRIBUTION:
                if(Utils.isInputEmpty(weibullAlphaEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor para Alpha.");
                }
                else {
                    double alpha = Double.parseDouble(weibullAlphaEt.getText().toString());
                    if(Utils.isNotPositive(alpha)) {
                        result = false;
                        Utils.showToast(getContext(), "Alpha debe ser positivo.");
                    }
                }
                if(Utils.isInputEmpty(weibullBetaEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.beta_empty_error));
                }
                else {
                    double beta = Double.parseDouble(weibullBetaEt.getText().toString());
                    if(Utils.isNotPositive(beta)) {
                        result = false;
                        Utils.showToast(getContext(), getString(R.string.beta_invalid));
                    }
                }
                if(Utils.isInputEmpty(weibullValueEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.x_value_error));
                }
                break;
            case STANDARD_NORMAL_DISTRIBUTION:
                if (Utils.isInputEmpty(standardNormalValueEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.x_value_error));
                }
                break;
            case INVERSE_EXPONENTIAL_DISTRIBUTION:
                if(Utils.isInputEmpty(inverseExponentialMeanEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor para la media.");
                }
                if(Utils.isInputEmpty(inverseExponentialValueEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.x_value_error));
                } else {
                    double probability = Double.parseDouble(inverseExponentialValueEt.getText().toString());
                    if(Utils.isProbabilityInvalid(probability)) {
                        result = false;
                        Utils.showToast(getContext(), getString(R.string.probability_error));
                    }
                }
                break;
            case INVERSE_GAMMA_DISTRIBUTION:
                if(Utils.isInputEmpty(inverseGammaShapeEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor para la forma.");
                }
                if(Utils.isInputEmpty(inverseGammaScaleEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor para la escala.");
                }
                if(Utils.isInputEmpty(inverseGammaValueEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.x_value_error));
                } else {
                    double probability = Double.parseDouble(inverseGammaValueEt.getText().toString());
                    if(Utils.isProbabilityInvalid(probability)) {
                        result = false;
                        Utils.showToast(getContext(), getString(R.string.probability_error));
                    }
                }
                break;
            case INVERSE_GUMBEL_DISTRIBUTION:
                if(Utils.isInputEmpty(inverseGumbelMuEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor para Mu.");
                }
                if(Utils.isInputEmpty(inverseGumbelBetaEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.beta_empty_error));
                }
                else {
                    double beta = Double.parseDouble(inverseGumbelBetaEt.getText().toString());
                    if(Utils.isNotPositive(beta)) {
                        result = false;
                        Utils.showToast(getContext(), getString(R.string.beta_invalid));
                    }
                }
                if(Utils.isInputEmpty(inverseGumbelValueEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.x_value_error));
                } else {
                    double probability = Double.parseDouble(inverseGumbelValueEt.getText().toString());
                    if(Utils.isProbabilityInvalid(probability)) {
                        result = false;
                        Utils.showToast(getContext(), getString(R.string.probability_error));
                    }
                }
                break;
            case INVERSE_WEIBULL_DISTRIBUTION:
                if(Utils.isInputEmpty(inverseWeibullAlphaEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor para Alpha.");
                }
                else {
                    double alpha = Double.parseDouble(weibullAlphaEt.getText().toString());
                    if(Utils.isNotPositive(alpha)) {
                        result = false;
                        Utils.showToast(getContext(), "Alpha debe ser positivo.");
                    }
                }
                if(Utils.isInputEmpty(inverseWeibullBetaEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.beta_empty_error));
                }
                else {
                    double beta = Double.parseDouble(inverseWeibullBetaEt.getText().toString());
                    if(Utils.isNotPositive(beta)) {
                        result = false;
                        Utils.showToast(getContext(), getString(R.string.beta_invalid));
                    }
                }
                if(Utils.isInputEmpty(inverseWeibullValueEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.x_value_error));
                } else {
                    double probability = Double.parseDouble(inverseWeibullValueEt.getText().toString());
                    if(Utils.isProbabilityInvalid(probability)) {
                        result = false;
                        Utils.showToast(getContext(), getString(R.string.probability_error));
                    }
                }
                break;
            case INVERSE_STANDARD_NORMAL_DISTRIBUTION:
                if(Utils.isInputEmpty(inverseStandardNormalValueEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor de probabilidad.");
                }
                else {
                    double probability = Double.parseDouble(inverseStandardNormalValueEt.getText().toString());
                    if(probability < 0 || probability > 1) {
                        result = false;
                        Utils.showToast(getContext(), getString(R.string.probability_error));
                    }
                }
                break;
        }

        return result;
    }
}
