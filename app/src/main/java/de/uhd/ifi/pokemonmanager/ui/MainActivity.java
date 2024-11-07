package de.uhd.ifi.pokemonmanager.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import de.uhd.ifi.pokemonmanager.R;
import de.uhd.ifi.pokemonmanager.data.Pokemon;
import de.uhd.ifi.pokemonmanager.data.Trainer;
import de.uhd.ifi.pokemonmanager.data.Type;
import de.uhd.ifi.pokemonmanager.storage.SerialStorage;
import de.uhd.ifi.pokemonmanager.ui.adapter.PokemonAdapter;
import de.uhd.ifi.pokemonmanager.ui.util.RecyclerViewUtil;

/**
 * Main Android activity for this app. Needs to be run to start the app.
 */
public class MainActivity extends AppCompatActivity {
    private static final SerialStorage STORAGE = SerialStorage.getInstance();

    // UI widget to show a list of pokemons
    private RecyclerView pokemonList;
    private PokemonAdapter pokemonAdapter;

    private FloatingActionButton addPokemonButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pokemonList = findViewById(R.id.pokemonList);
        addPokemonButton = findViewById(R.id.addPokemonButton);

        STORAGE.loadAll(this);
        setupList();
        initUI();
    }

    private void setupList() {
        createSampleDataIfNecessary();
        List<Pokemon> pokemons = STORAGE.getAllPokemons();
        pokemonAdapter = new PokemonAdapter(this, pokemons);
        RecyclerView.LayoutManager manager = RecyclerViewUtil.createLayoutManager(this);

        //pokemonAdapter.setOnItemClick(pokemon -> Toast.makeText( this, pokemon.toString(), Toast.LENGTH_SHORT).show());
        pokemonList.setLayoutManager(manager);
        pokemonList.setAdapter(pokemonAdapter);
    }

    private void initUI() {
        // OnClickListener through implementation of interface
        addPokemonButton.setOnClickListener(view -> {
            //TODO: implement create Pokemon.
            // The pokemon needs to be added to the storage.
            // Don't forget to refresh pokemonAdapter.

            //BONUS: if no Trainers are in Storage the App kinda crashes - how do we prevent that
        });
    }

    /**
     * Creates three sample {@link Pokemon} if none are in storage.
     */
    private void createSampleDataIfNecessary() {
        if (STORAGE.getAllPokemons().isEmpty()) {
            STORAGE.clear(this);

            Trainer t1 = new Trainer("Alisa", "Traurig");
            Trainer t2 = new Trainer("Petra", "Lustig");
            Pokemon p1 = new Pokemon("Shiggy", Type.WATER);
            Pokemon p2 = new Pokemon("Rettan", Type.POISON);
            Pokemon p3 = new Pokemon("Glurak", Type.FIRE);

            t1.addPokemon(p1);
            t1.addPokemon(p2);
            t2.addPokemon(p3);

            STORAGE.save(p1);
            STORAGE.save(p2);
            STORAGE.save(p3);
            STORAGE.save(t1);
            STORAGE.save(t2);
            STORAGE.saveAll(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        STORAGE.loadAll(this);
        createSampleDataIfNecessary();
        pokemonAdapter.refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        STORAGE.saveAll(this);
    }
}
