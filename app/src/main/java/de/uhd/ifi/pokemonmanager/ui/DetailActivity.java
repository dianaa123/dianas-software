package de.uhd.ifi.pokemonmanager.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import de.uhd.ifi.pokemonmanager.R;
import de.uhd.ifi.pokemonmanager.data.Pokemon;

public class DetailActivity extends AppCompatActivity {

    private Pokemon pokemon;
    //TODO: declare views here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        bindViews();
        getPokemonFromIntent();
        setupSwapList();
        setupCompetitionList();
        initPokemonDetailView();
    }

    private void bindViews() {
        //TODO: bind views (findViewById)
    }

    private void getPokemonFromIntent() {
        //TODO
    }

    private void setupSwapList() {
        //TODO:
        // Hint: MainActivity.setupList
    }

    private void setupCompetitionList() {
        //TODO
    }

    private void initPokemonDetailView() {
        //TODO
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                result = super.onOptionsItemSelected(item);
        }

        return result;
    }

}
