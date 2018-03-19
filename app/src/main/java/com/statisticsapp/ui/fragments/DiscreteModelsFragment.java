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

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.GeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;

import java.util.Locale;

public class DiscreteModelsFragment extends Fragment {

    private static final int BINOMIAL_DISTRIBUTION = 0;
    private static final int GEOMETRIC_DISTRIBUTION = 1;
    private static final int POISSON_DISTRIBUTION = 2;
    private static final int HYPERGEOMETRIC_DISTRIBUTION = 3;

    private LinearLayout binomialLayout;
    private LinearLayout geometricLayout;
    private LinearLayout poissonLayout;
    private LinearLayout hypergeometricLayout;
    private LinearLayout resultLayout;
    private EditText binomialSampleSizeEt;
    private EditText binomialEventProbabilityEt;
    private EditText binomialValueEt;
    private EditText geometricSuccessProbabilityEt;
    private EditText geometricValueEt;
    private EditText poissonExpectedNumberOfOcurrencesEt;
    private EditText poissonValueEt;
    private EditText hypergeometricPopulationSizeEt;
    private EditText hypergeometricPositivesInPopulationEt;
    private EditText hypergeometricSampleSizeEt;
    private EditText hypergeometricValueEt;
    private TextView resultTxt;
    private CheckBox cumulativeCb;
    private ScrollView scrollView;

    private int selectedDistribution;

    public static Fragment newInstance() {
        return new DiscreteModelsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_discrete_models, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.add(getString(R.string.binomial));
        adapter.add(getString(R.string.geometric));
        adapter.add(getString(R.string.poisson));
        adapter.add(getString(R.string.hypergeometric));

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

        binomialLayout = root.findViewById(R.id.binomial_layout);
        binomialSampleSizeEt = root.findViewById(R.id.binomial_sample_size);
        binomialEventProbabilityEt = root.findViewById(R.id.binomial_event_probability);
        binomialValueEt = root.findViewById(R.id.binomial_value);

        geometricLayout = root.findViewById(R.id.geometric_layout);
        geometricSuccessProbabilityEt = root.findViewById(R.id.geometric_success_probability);
        geometricValueEt = root.findViewById(R.id.geometric_value);

        poissonLayout = root.findViewById(R.id.poisson_layout);
        poissonExpectedNumberOfOcurrencesEt = root.findViewById(R.id.poisson_expected_number_ocurrences);
        poissonValueEt = root.findViewById(R.id.poisson_value);

        hypergeometricLayout = root.findViewById(R.id.hypergemoetric_layout);
        hypergeometricPopulationSizeEt = root.findViewById(R.id.hypergemoetric_population_size);
        hypergeometricPositivesInPopulationEt = root.findViewById(R.id.hypergemoetric_positives_in_population);
        hypergeometricSampleSizeEt = root.findViewById(R.id.hypergemoetric_sample_size);
        hypergeometricValueEt = root.findViewById(R.id.hypergemoetric_value);

        resultLayout = root.findViewById(R.id.result_layout);
        resultTxt = root.findViewById(R.id.result);
        cumulativeCb = root.findViewById(R.id.cumulative_checkbox);
        scrollView = root.findViewById(R.id.discrete_scrollview);

        Button calculateButton = root.findViewById(R.id.calculate);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard(getActivity());

                if(!areInputsValid()) return;

                boolean accumulates = cumulativeCb.isChecked();

                switch (selectedDistribution) {
                    case BINOMIAL_DISTRIBUTION:
                        int binomialSampleSize = Integer.parseInt(binomialSampleSizeEt.getText().toString());
                        double eventProbability = Double.parseDouble(binomialEventProbabilityEt.getText().toString());
                        int binomialX = Integer.parseInt(binomialValueEt.getText().toString());

                        BinomialDistribution binomialDistribution = new BinomialDistribution(binomialSampleSize, eventProbability);
                        double binomialResult;
                        if(accumulates) binomialResult = binomialDistribution.cumulativeProbability(binomialX);
                        else binomialResult = binomialDistribution.probability(binomialX);

                        showResult(binomialResult);
                        break;
                    case GEOMETRIC_DISTRIBUTION:
                        double successProbability = Double.parseDouble(geometricSuccessProbabilityEt.getText().toString());
                        int geometricX = Integer.parseInt(geometricValueEt.getText().toString());

                        GeometricDistribution geometricDistribution = new GeometricDistribution(successProbability);
                        double geometricResult;
                        if(accumulates) geometricResult = geometricDistribution.cumulativeProbability(geometricX);
                        else geometricResult = geometricDistribution.probability(geometricX);

                        showResult(geometricResult);
                        break;
                    case POISSON_DISTRIBUTION:
                        double expectedNumberOfOcurrences = Double.parseDouble(poissonExpectedNumberOfOcurrencesEt.getText().toString());
                        int poissonX = Integer.parseInt(poissonValueEt.getText().toString());

                        PoissonDistribution poissonDistribution = new PoissonDistribution(expectedNumberOfOcurrences);
                        double poissonResult;
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
                        double hypergeometricResult;
                        if(accumulates) hypergeometricResult = hypergeometricDistribution.cumulativeProbability(hypergeometricX);
                        else hypergeometricResult = hypergeometricDistribution.probability(hypergeometricX);

                        showResult(hypergeometricResult);
                        break;
                }

                scrollDown();
            }
        });

        return root;
    }

    private void showSelectedDistribution() {
        Utils.hideAll(binomialLayout, geometricLayout, poissonLayout, hypergeometricLayout, resultLayout);

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
            case BINOMIAL_DISTRIBUTION:
                if (Utils.isInputEmpty(binomialSampleSizeEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.sample_size_error));
                }
                if (Utils.isInputEmpty(binomialEventProbabilityEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor para el evento de probabilidad");
                }
                else {
                    double eventProbability = Double.parseDouble(binomialEventProbabilityEt.getText().toString());
                    if(Utils.isProbabilityInvalid(eventProbability)) {
                        result = false;
                        Utils.showToast(getContext(), "El valor para el evento de probabilidad debe estar entre 0 y 1 inclusive");
                    }
                }
                if (Utils.isInputEmpty(binomialValueEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.x_value_error));
                }
                break;
            case GEOMETRIC_DISTRIBUTION:
                if (Utils.isInputEmpty(geometricSuccessProbabilityEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor para la probabilidad de éxito");
                }
                else {
                    double successProbability = Double.parseDouble(geometricSuccessProbabilityEt.getText().toString());
                    if(Utils.isProbabilityInvalid(successProbability)) {
                        result = false;
                        Utils.showToast(getContext(), "El valor para la probabilidad de éxito debe estar entre 0 y 1 inclusive");
                    }
                }
                if (Utils.isInputEmpty(geometricValueEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.x_value_error));
                }
                break;
            case POISSON_DISTRIBUTION:
                if (Utils.isInputEmpty(poissonExpectedNumberOfOcurrencesEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor para el nro. esperado de ocurrencias");
                }
                if (Utils.isInputEmpty(poissonValueEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.x_value_error));
                }
                break;
            case HYPERGEOMETRIC_DISTRIBUTION:
                if (Utils.isInputEmpty(hypergeometricPopulationSizeEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor de población");
                }
                if (Utils.isInputEmpty(hypergeometricPositivesInPopulationEt)) {
                    result = false;
                    Utils.showToast(getContext(), "Debe ingresar un valor para la cantidad de éxitos");
                }
                if (Utils.isInputEmpty(hypergeometricSampleSizeEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.sample_size_error));
                }
                else {
                    double sampleSize = Double.parseDouble(hypergeometricSampleSizeEt.getText().toString());
                    double populationSize = Double.parseDouble(hypergeometricPopulationSizeEt.getText().toString());
                    if(sampleSize > populationSize) {
                        result = false;
                        Utils.showToast(getContext(), "El tamaño de muestra no puede ser mayor que la población.");
                    }
                }
                if (Utils.isInputEmpty(hypergeometricValueEt)) {
                    result = false;
                    Utils.showToast(getContext(), getString(R.string.x_value_error));
                }
                break;
        }

        return result;
    }
}
