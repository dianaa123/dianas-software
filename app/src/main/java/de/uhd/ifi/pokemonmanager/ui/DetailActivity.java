package de.uhd.ifi.pokemonmanager.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.uhd.ifi.pokemonmanager.R;
import de.uhd.ifi.pokemonmanager.data.Pokemon;
import de.uhd.ifi.pokemonmanager.data.Competition;
import de.uhd.ifi.pokemonmanager.ui.adapter.CompetitionAdapter;
import de.uhd.ifi.pokemonmanager.ui.adapter.SwapAdapter;
import de.uhd.ifi.pokemonmanager.ui.util.RecyclerViewUtil;

public class DetailActivity extends AppCompatActivity {

    private Pokemon pokemon;
    //TODO: declare views here
    private RecyclerView competitionList;
    private CompetitionAdapter competitionAdapter;
    private RecyclerView swapList;
    private SwapAdapter swapAdapter;

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
        competitionList = findViewById(R.id.competitionList);
    }

    private void getPokemonFromIntent() {
        //TODO: get Pokemon from intent
        pokemon = getIntent().getParcelableExtra(MainActivity.DETAIL_POKEMON);
    }

    private void setupSwapList() {
        //TODO:
        // Hint: MainActivity.setupList
    }

    private void setupCompetitionList() {
        //TODO
        List<Competition> competitions = pokemon.getCompetitions();
        competitionAdapter = new CompetitionAdapter(this, competitions, pokemon);
        RecyclerView.LayoutManager manager = RecyclerViewUtil.createLayoutManager(this);

        competitionList.setLayoutManager(manager);
        competitionList.setAdapter(competitionAdapter);
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
