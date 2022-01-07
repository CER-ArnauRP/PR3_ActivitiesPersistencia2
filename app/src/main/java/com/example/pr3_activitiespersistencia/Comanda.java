package com.example.pr3_activitiespersistencia;

import java.util.ArrayList;
import java.util.List;

public class Comanda { // View model i guardar-ho a Firebase.

    private String mTaula, mTimeStamp;
    private List<String> mLlistaNomsPizzes = new ArrayList<>();

    public Comanda() { }
    public Comanda(String taula, String timeStamp, List<String> llistaNomsPizzes) {
        mTaula = taula;
        mTimeStamp = timeStamp;
        mLlistaNomsPizzes = llistaNomsPizzes;
    }

    public String getTaula() { return mTaula; }
    public String getTimeStamp() { return mTimeStamp; }
    public List<String> getLlistaNomsPizzes() { return mLlistaNomsPizzes; }
}
