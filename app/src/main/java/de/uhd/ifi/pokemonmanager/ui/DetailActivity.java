package de.uhd.ifi.pokemonmanager.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Locale;

import de.uhd.ifi.pokemonmanager.R;
import de.uhd.ifi.pokemonmanager.data.Pokemon;
import de.uhd.ifi.pokemonmanager.data.Swap;
import de.uhd.ifi.pokemonmanager.data.Competition;
import de.uhd.ifi.pokemonmanager.storage.SerialStorage;
import de.uhd.ifi.pokemonmanager.ui.adapter.CompetitionAdapter;
import de.uhd.ifi.pokemonmanager.ui.adapter.SwapAdapter;
import de.uhd.ifi.pokemonmanager.ui.util.RecyclerViewUtil;

public class DetailActivity extends AppCompatActivity {

    private Pokemon pokemon;
    //COMPLETED-TODO: declare views here
    private TextInputEditText pokemonName;
    //private TextInputEditText pokemonId;
    private TextInputEditText pokemonType;
    private TextInputEditText trainerName;
    //private TextView swapAllowed;
    private CheckBox swapAllowedCheckbox;
    private RecyclerView swapList;
    private SwapAdapter swapAdapter;
    private RecyclerView competitionList;
    private CompetitionAdapter competitionAdapter;

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
        //COMPLETED-TODO: bind views (findViewById)
        pokemonName = findViewById(R.id.nameText);
        pokemonType = findViewById(R.id.type);
        trainerName = findViewById(R.id.trainer);
        //swapAllowed = findViewById(R.id.swapAllowed);
        swapAllowedCheckbox = findViewById(R.id.swapAllowedCheckbox);
        swapList = findViewById(R.id.swapList);
        competitionList = findViewById(R.id.competitionList);
    }

    private void getPokemonFromIntent() {
        //COMPLETED-TODO: get Pokemon from intent
        pokemon = getIntent().getParcelableExtra(MainActivity.DETAIL_POKEMON);
    }

    private void setupSwapList() {
        //COMPLETED-TODO:
        // Hint: MainActivity.setupList
        List<Swap> swaps = pokemon.getSwaps();
        /*Log.println(Log.INFO, "DetailActivity", String.format(Locale.getDefault(), swaps.toString()));*/
        swapAdapter = new SwapAdapter(this, swaps, pokemon);
        RecyclerView.LayoutManager manager = RecyclerViewUtil.createLayoutManager(this);

        swapList.setLayoutManager(manager);
        swapList.setAdapter(swapAdapter);
    }

    private void setupCompetitionList() {
        //COMPLETED-TODO
        List<Competition> competitions = pokemon.getCompetitions();
        /*Log.println(Log.INFO, "DetailActivity", String.format(Locale.getDefault(), competitions.toString()));
        Log.println(Log.INFO, "DetailActivity", String.format(Locale.getDefault(), competitions.get(0).getWinner().toString()));*/
        competitionAdapter = new CompetitionAdapter(this, competitions, pokemon);
        RecyclerView.LayoutManager manager = RecyclerViewUtil.createLayoutManager(this);

        competitionList.setLayoutManager(manager);
        competitionList.setAdapter(competitionAdapter);
    }

    private void initPokemonDetailView() {
        //COMPLETED-TODO
        if (pokemon != null) {
            pokemonName.setText(pokemon.getName());
            pokemonType.setText(pokemon.getType().toString());
            //pokemonId.setText(String.format(Locale.getDefault(), "# %d", pokemon.getId()));
            trainerName.setText(pokemon.getTrainer().toString());
            //swapAllowed.setText((pokemon.isSwapAllowed()) ? "X": "");
            swapAllowedCheckbox.setChecked(pokemon.isSwapAllowed());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        /*switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                result = super.onOptionsItemSelected(item);
        }*/
        if(item.getItemId() == android.R.id.home) {
            finish();
        } else {
            result = super.onOptionsItemSelected(item);
        }

        return result;
    }

}
