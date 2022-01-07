package com.example.pr3_activitiespersistencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaulesActivity extends AppCompatActivity {

    private EditText mElementsDemanats;
    private Button mTaula1, mTaula2;

    private ComandaViewModel mComandaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taules);

        InicialitzarComponents();

        InicialitzarListeners();
    }

    private void InicialitzarListeners() {

        mTaula1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Comanda comanda = CreaComanda("Taula 1");

                mComandaViewModel.setComandaLiveData(comanda);

                CarregarResumFacturaFragment();
            }
        });

        mTaula2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Comanda comanda = CreaComanda("Taula 2");

                mComandaViewModel.setComandaLiveData(comanda);

                CarregarResumFacturaFragment();
            }
        });
    }

    private Comanda CreaComanda(String taula) {

        // Guarda la comanda a la base de dades.
        // Camps comanda: String taula, String timeStamp, List<String> llistaIDPizzes


        String timeStamp = ObteTimeStamp();
        List<String> llistaPizzes = ObteLlistaPizzes();

        Comanda comanda = new Comanda(taula, timeStamp, llistaPizzes);

        return comanda;
    }

    private List<String> ObteLlistaPizzes() {

        List<String> llistaPizzes = new ArrayList<>();

        String[] arrayPizzes = mElementsDemanats.getText().toString().trim().split(",");

        for (int i = 0; i < arrayPizzes.length; i++) {
            llistaPizzes.add(arrayPizzes[i]);
        }

        return llistaPizzes;
    }

    private String ObteTimeStamp() {

        String timeStampEnMilisegons = String.valueOf(System.currentTimeMillis());

        Calendar calendari = Calendar.getInstance(Locale.ENGLISH);
        calendari.setTimeInMillis(Long.parseLong(timeStampEnMilisegons));

        // DateFormat del android.text.format
        String formatDateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendari).toString();

        return formatDateTime;
    }

    private void CarregarResumFacturaFragment() {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FL_ContingutFragment, new ResumFacturaFragment())
                .commit();
    }

    private void InicialitzarComponents() {

        mElementsDemanats = findViewById(R.id.ET_ElementsDemanats);
        mTaula1 = findViewById(R.id.BTN_Taula1);
        mTaula2 = findViewById(R.id.BTN_Taula2);

        mComandaViewModel = new ViewModelProvider(this).get(ComandaViewModel.class);
    }
}