package com.statiticsapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TabHost mainTabHost;

    private static String CALCULAR = "tab1";
    private static String GRAFICA = "tab2";
    private static String TEORIA = "tab3";
    private static int CALCULAR_TAB = 0;
    private static int GRAFICA_TAB = 1;
    private static int TEORIA_TAB = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

}
