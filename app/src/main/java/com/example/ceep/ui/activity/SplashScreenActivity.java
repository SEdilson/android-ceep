package com.example.ceep.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.ceep.R;

import static com.example.ceep.ui.activity.SplashScreenActivityConstantes.PREFERENCE_CHECA_SE_APP_JA_FOI_ACESSADA;
import static com.example.ceep.ui.activity.SplashScreenActivityConstantes.SPLASH_SCREEN_PREFERENCES;
import static com.example.ceep.ui.activity.SplashScreenActivityConstantes.TEMPO_CASO_JA_TENHA_ACESSADO;
import static com.example.ceep.ui.activity.SplashScreenActivityConstantes.TEMPO_CASO_NAO_TENHA_ACESSADO;

public class SplashScreenActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        configuraSharedPreferences();
        setaTempoDeCarregamentoDaSplashScreen();
    }

    private void setaTempoDeCarregamentoDaSplashScreen() {
        if(preferences.contains(PREFERENCE_CHECA_SE_APP_JA_FOI_ACESSADA)) {
            configuraHandlerSlashScreen(TEMPO_CASO_JA_TENHA_ACESSADO);
        } else {
            configuraHandlerSlashScreen(TEMPO_CASO_NAO_TENHA_ACESSADO);
        }
    }

    private void configuraHandlerSlashScreen(int time) {
        Handler handleSplashScreen = new Handler();
        handleSplashScreen.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostraNotas();
            }
        }, time);
    }

    private void configuraSharedPreferences() {
        preferences = getSharedPreferences(SPLASH_SCREEN_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREFERENCE_CHECA_SE_APP_JA_FOI_ACESSADA, true);
        editor.apply();
    }

    private void mostraNotas() {
        Intent intent = new Intent(SplashScreenActivity.this,
                ListaNotasActivity.class);
        startActivity(intent);
        finish();
    }
}
