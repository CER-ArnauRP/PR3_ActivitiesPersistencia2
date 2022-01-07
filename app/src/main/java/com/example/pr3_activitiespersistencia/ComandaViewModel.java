package com.example.pr3_activitiespersistencia;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ComandaViewModel extends ViewModel {

    private final MutableLiveData<Comanda> comandaLiveData = new MutableLiveData<>();

    public void setComandaLiveData(Comanda comanda) { comandaLiveData.setValue(comanda); }

    public LiveData<Comanda> getComandaLiveData() { return comandaLiveData; }
}
