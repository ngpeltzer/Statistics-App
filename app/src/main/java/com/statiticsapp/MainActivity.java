package com.statiticsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.statiticsapp.Utils.CSVHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    TabHost mainTabHost;
    Button getCsvButton;

    private static String CALCULAR = "tab1";
    private static String GRAFICA = "tab2";
    private static String TEORIA = "tab3";
    private static int CALCULAR_TAB = 0;
    private static int GRAFICA_TAB = 1;
    private static int TEORIA_TAB = 2;

    private static int OPEN_CSV_FILE = 3;

    String[] values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        values = new String[1];

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

        getCsvButton = (Button) mainTabHost.findViewById(R.id.tab_calcular_get_csv);
        getCsvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setType("text/csv");
                startActivityForResult(Intent.createChooser(intent, "Seleccionar archivo CSV"), OPEN_CSV_FILE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {
            File root = Environment.getExternalStorageDirectory();
            //InputStream csvFile = new FileInputStream();

            //CSVHelper csvHelper = new CSVHelper(csvFile);
            //InputStream inputStream =
        }
    }

}
