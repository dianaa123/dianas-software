package de.uhd.ifi.pokemonmanager.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.Locale;

import de.uhd.ifi.pokemonmanager.R;
import de.uhd.ifi.pokemonmanager.data.Pokemon;
import de.uhd.ifi.pokemonmanager.storage.SerialStorage;

/**
 * Enables to show a list of {@link Pokemon}s in the UI in a so called {@link
 * androidx.recyclerview.widget.RecyclerView}.
 */
public class PokemonAdapter extends Adapter<PokemonHolder> {

    private LayoutInflater inflater;
    private List<Pokemon> pokemons;
    private Consumer<Pokemon> onItemClick;

    public PokemonAdapter(Context context, List<Pokemon> pokemons) {
        this.inflater = LayoutInflater.from(context);
        this.pokemons = pokemons;
    }

    public void setOnItemClick(Consumer<Pokemon> onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public PokemonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.listitem_pokemon, parent, false);
        itemView.setOnClickListener(this::onItemClick);
        return new PokemonHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonHolder holder, int position) {
        holder.setPokemon(pokemons.get(position));
    }

    private void onItemClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        int pos = holder.getBindingAdapterPosition();
        Pokemon pokemon = pokemons.get(pos);
        if(onItemClick != null) {
            onItemClick.accept(pokemon);
        }
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
        // COMPLETED_TOD0: Add delete button here
        deletePokemonButton = itemView.findViewById(R.id.deletePokemonButton);
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
        this.pokemonCompetitions.setText(String.format(Locale.getDefault(), "Competitions: %d", 0));

        // COMPLETED_TOD0: Add OnClickListener for delete button here. Don't forget to confirm deletion via a dialog.
        this.deletePokemonButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    new AlertDialog.Builder(view.getContext())
                            .setMessage(R.string.delete_pokemon_message)
                            .setTitle(R.string.delete_pokemon_title)
                            .setPositiveButton(R.string.delete_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    SerialStorage.getInstance().remove(pokemon);
                                    dialog.dismiss();
                                    adapter.refresh();
                                }
                            })
                            .setNegativeButton(R.string.delete_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                }
            }
        );
    }
}
