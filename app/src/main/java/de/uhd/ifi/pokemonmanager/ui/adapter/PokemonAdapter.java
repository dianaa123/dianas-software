package de.uhd.ifi.pokemonmanager.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.Locale;

import de.uhd.ifi.pokemonmanager.R;
import de.uhd.ifi.pokemonmanager.data.Competition;
import de.uhd.ifi.pokemonmanager.data.Pokemon;
import de.uhd.ifi.pokemonmanager.data.Swap;
import de.uhd.ifi.pokemonmanager.storage.SerialStorage;
import de.uhd.ifi.pokemonmanager.ui.DetailActivity;
import de.uhd.ifi.pokemonmanager.ui.MainActivity;

/**
 * Enables to show a list of {@link Pokemon}s in the UI in a so called {@link
 * androidx.recyclerview.widget.RecyclerView}.
 */
public class PokemonAdapter extends Adapter<PokemonHolder> {

    private LayoutInflater inflater;
    private List<Pokemon> pokemons;

    public PokemonAdapter(Context context, List<Pokemon> pokemons) {
        this.inflater = LayoutInflater.from(context);
        this.pokemons = pokemons;
    }

    @NonNull
    @Override
    public PokemonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.listitem_pokemon, parent, false);

        // OnClickListener through implementation of interface
        itemView.setOnClickListener(view -> {
            ViewHolder holder = (ViewHolder) view.getTag();
            int clickPosition = holder.getBindingAdapterPosition();
            Pokemon pokemon = pokemons.get(clickPosition);
            Intent detailIntent = new Intent(view.getContext(), DetailActivity.class);
            detailIntent.putExtra(MainActivity.DETAIL_POKEMON, (Parcelable) pokemon);
            view.getContext().startActivity(detailIntent);
        });
        return new PokemonHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonHolder holder, int position) {
        holder.setPokemon(pokemons.get(position));
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    public void refresh() {
        this.pokemons = SerialStorage.getInstance().getAllPokemons();
        notifyDataSetChanged();
    }
}

/**
 * Responsible to show a single {@link Pokemon} in the UI and for the on deletion click event
 * listener.
 */
class PokemonHolder extends ViewHolder {

    private TextView pokemonName;
    private TextView pokemonType;
    private TextView pokemonId;
    private TextView trainerText;
    private TextView pokemonSwaps;
    private TextView pokemonCompetitions;
    private ImageView deletePokemonButton;
    private PokemonAdapter adapter;

    PokemonHolder(@NonNull View itemView, PokemonAdapter adapter) {
        super(itemView);
        pokemonName = itemView.findViewById(R.id.pokemonName);
        pokemonType = itemView.findViewById(R.id.pokemonType);
        pokemonId = itemView.findViewById(R.id.pokemonId);
        trainerText = itemView.findViewById(R.id.trainerText);
        pokemonSwaps = itemView.findViewById(R.id.pokemonSwaps);
        pokemonCompetitions = itemView.findViewById(R.id.pokemonCompetitions);
        deletePokemonButton = itemView.findViewById(R.id.deleteButton);
        this.adapter = adapter;
        itemView.setTag(this);
    }

    void setPokemon(Pokemon pokemon) {
        if (pokemon == null) {
            return;
        }
        this.pokemonName.setText(pokemon.getName());
        this.pokemonType.setText(pokemon.getType().toString());
        this.pokemonId.setText(String.format(Locale.getDefault(), "# %d", pokemon.getId()));
        this.trainerText.setText(pokemon.getTrainer().toString());
        this.pokemonSwaps.setText(String.format(Locale.getDefault(), "Swaps: %d", pokemon.getSwaps().size()));
        this.pokemonCompetitions.setText(String.format(Locale.getDefault(), "Competitions: %d", pokemon.getCompetitions().size()));

        // OnClickListener through implementation of interface (anonymous class).
        // This could be shortened using a lambda expression or method reference.
        this.deletePokemonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(view.getContext())
                        .setMessage(R.string.delete_pokemon_message)
                        .setTitle(R.string.delete_pokemon_title)
                        .setPositiveButton(R.string.delete_confirm, (dialog, id) -> {
                            //delete Swaps or Competitions if both participants deleted
                            for (Swap swap : pokemon.getAndCleanSwaps()) {
                                if (swap.getOtherPokemon(pokemon) == null) {
                                    SerialStorage.getInstance().remove(swap);
                                }
                            }
                            for (Competition competition : pokemon.getAndCleanCompetitions()) {
                                if (competition.getOtherPokemon(pokemon) == null) {
                                    SerialStorage.getInstance().remove(competition);
                                }
                            }
                            SerialStorage.getInstance().remove(pokemon);
                            dialog.dismiss();
                            adapter.refresh();
                        })
                        .setNegativeButton(R.string.delete_cancel, (dialog, id) -> dialog.dismiss())
                        .create()
                        .show();
            }
        });
    }
}
