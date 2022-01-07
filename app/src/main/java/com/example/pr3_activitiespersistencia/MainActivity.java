package com.example.pr3_activitiespersistencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mNom, mIngredients, mPreu;
    private Button mAdd, mUpd, mDel;
    private ListView mListViewPizzes;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private List<Pizza> mLlistaPizzes = new ArrayList<>();
    private ArrayAdapter<Pizza> mAdapterPizzes;

    private Pizza mPizzaSeleccionada;

    private Button mTaules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InicialitzarComponents();

        InicialitzarListeners();

        LlistarPizzes(); // Read.
    }

    private void LlistarPizzes() {

        mReference.child("Pizzes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mLlistaPizzes.clear();

                for (DataSnapshot ds: snapshot.getChildren()) {

                    Pizza pizza = ds.getValue(Pizza.class);
                    mLlistaPizzes.add(pizza);
                }

                mAdapterPizzes = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, mLlistaPizzes);
                mListViewPizzes.setAdapter(mAdapterPizzes);// Mostra el que retorni el toString().
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void InicialitzarListeners() {

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AfegirPizza(); // Create.
            }
        });

        mUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditarPizza(); // Update
            }
        });

        mDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EsborrarPizza();
            }
        });

        mTaules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, TaulesActivity.class));
            }
        });

        mListViewPizzes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mPizzaSeleccionada = (Pizza) adapterView.getItemAtPosition(i);

                mNom.setText(mPizzaSeleccionada.getNom());
                mIngredients.setText(mPizzaSeleccionada.getIngredients());
                mPreu.setText(mPizzaSeleccionada.getPreu());
            }
        });
    }

    private void EsborrarPizza() {

        mReference.child("Pizzes").child(mPizzaSeleccionada.getUid()).removeValue();

        ResetCamps();
    }

    private void EditarPizza() {

        String nom = mNom.getText().toString().trim();
        String ingredients = mIngredients.getText().toString().trim();
        String preu = mPreu.getText().toString().trim();

        Pizza pizza = new Pizza(nom, ingredients, preu, mPizzaSeleccionada.getUid());

        mReference.child("Pizzes").child(pizza.getUid()).setValue(pizza);

        ResetCamps();
    }

    private void AfegirPizza() {

        String nom = mNom.getText().toString().trim();
        String ingredients = mIngredients.getText().toString().trim();
        String preu = mPreu.getText().toString().trim();
        String uid = mReference.push().getKey();//UUID.randomUUID().toString();//

        Pizza pizza = new Pizza(nom, ingredients, preu, uid);

        mReference.child("Pizzes").child(uid).setValue(pizza);

        ResetCamps();
    }

    private void ResetCamps() {

        mNom.setText("");
        mIngredients.setText("");
        mPreu.setText("");
    }

    private void InicialitzarComponents() {

        mNom = findViewById(R.id.ET_Nom);
        mIngredients = findViewById(R.id.ET_Ingredients);
        mPreu = findViewById(R.id.ET_Preu);
        mAdd = findViewById(R.id.BTN_Add);
        mUpd = findViewById(R.id.BTN_Upd);
        mDel = findViewById(R.id.BTN_Del);
        mTaules = findViewById(R.id.BTN_Taules);
        mListViewPizzes = findViewById(R.id.LV_Carta);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }
}