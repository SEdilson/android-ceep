package com.example.ceep.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ceep.R;

public class FeedbackActivity extends AppCompatActivity {

    public static final String TITULO_ACTIVITY_APPBAR = "Feedback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        setTitle(TITULO_ACTIVITY_APPBAR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_feedback_envia, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.activity_feedback_menu_envia) {
            voltaPraListaDeNotas();
        }

        return super.onOptionsItemSelected(item);
    }

    private void voltaPraListaDeNotas() {
        Intent intent = new Intent(this, ListaNotasActivity.class);
        startActivity(intent);
        finish();
    }
}
