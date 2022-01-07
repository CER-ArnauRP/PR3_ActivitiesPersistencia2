package com.example.pr3_activitiespersistencia;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ResumFacturaFragment extends Fragment {

    private ComandaViewModel mComandaViewModel;

    private String mTaula = "", mTimeStamp = "";
    private List<String> mLlistaNomsPizzes = new ArrayList<>();
    private float mPreuPerTaula;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private TextView mResumFactura;
    private String mTextResumFactura;


    public ResumFacturaFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_resum_factura, container, false);

        mResumFactura = v.findViewById(R.id.TV_ResumFactura);

        mComandaViewModel = new ViewModelProvider(getActivity()).get(ComandaViewModel.class);
        mComandaViewModel.getComandaLiveData().observe(getViewLifecycleOwner(), comanda -> {

            mTaula = comanda.getTaula();
            mTimeStamp = comanda.getTimeStamp();
            mLlistaNomsPizzes = comanda.getLlistaNomsPizzes();

            CrearResumFactura();
        });

        return v;
    }

    private void CrearResumFactura() {

        // 1) Buscar les pizzes per nom a la base de dades.
        // 2) Obtenir el preu de cada pizza.
        // 3) Omplir el contingut en el TextView.
        // 4) Guardar la comanda a la base de dades.
        //Log.d("----", mTaula + " - " + mLlistaNomsPizzes.get(0));

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Pizzes");

        mTextResumFactura = "";

        mTextResumFactura = mTaula + "\n\n";
        mPreuPerTaula = 0;


        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren()) {

                    Pizza pizzaActual = ds.getValue(Pizza.class);

                    for (int i = 0; i < mLlistaNomsPizzes.size(); i++) {

                        if (pizzaActual.getNom().equals(mLlistaNomsPizzes.get(i))) {

                            mPreuPerTaula += Float.parseFloat(pizzaActual.getPreu());

                            mTextResumFactura = mTextResumFactura +
                                    pizzaActual.getNom() +
                                    " - " +
                                    pizzaActual.getPreu() +
                                    "\n";
                        }
                    }
                }

                mTextResumFactura = mTextResumFactura + "\nTotal: " + mPreuPerTaula + "\n";
                mResumFactura.setText(mTextResumFactura);

                //Log.d("----", "Dins del bucle:" + mTextResumFactura);
                GuardarComanda();
                mPreuPerTaula = 0;
                mLlistaNomsPizzes.clear();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

/*
        for (int i = 0; i < mLlistaNomsPizzes.size(); i++) {

            Query query = mReference.orderByChild("nom").equalTo(mLlistaNomsPizzes.get(i));

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot ds: snapshot.getChildren()) {

                        mPreuPerTaula += Float.parseFloat(ds.child("preu").getValue().toString());

                        mTextResumFactura = mTextResumFactura +
                                ds.child("nom").getValue().toString() +
                                " - " +
                                ds.child("preu").getValue().toString() +
                                "\n";
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            if (i == mLlistaNomsPizzes.size() - 1) {

                mTextResumFactura = mTextResumFactura + "\nTotal: " + mPreuPerTaula + "\n";
                mResumFactura.setText(mTextResumFactura);
                Log.d("----", "Dins del bucle:" + mTextResumFactura);
                GuardarComanda();
                mPreuPerTaula = 0;
                mLlistaNomsPizzes.clear();
            }
        }

        Log.d("----", "Fora del bucle2: " + mResumFactura.getText());
*/
    }

    private void GuardarComanda() {

        Comanda comanda = new Comanda(mTaula, mTimeStamp, mLlistaNomsPizzes);

        DatabaseReference reference = mDatabase.getReference();
        reference.child("Comandes").child(reference.push().getKey()).setValue(comanda);
    }
}