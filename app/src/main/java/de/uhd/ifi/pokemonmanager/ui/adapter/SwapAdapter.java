package de.uhd.ifi.pokemonmanager.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.uhd.ifi.pokemonmanager.R;
import de.uhd.ifi.pokemonmanager.data.Pokemon;
import de.uhd.ifi.pokemonmanager.data.Trainer;
import de.uhd.ifi.pokemonmanager.data.Swap;

//COMPLETED-TODO:
// Hint: check CompetitionAdapter

public class SwapAdapter extends RecyclerView.Adapter<SwapHolder> {
    private LayoutInflater inflater;
    private List<Swap> swaps;
    private Pokemon pokemon;

    public SwapAdapter(final Context context, final List<Swap> swaps, final Pokemon pokemon) {
        this.inflater = LayoutInflater.from(context);
        this.swaps = swaps;
        this.pokemon = pokemon;
    }

    @NonNull
    @Override
    public SwapHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SwapHolder(inflater.inflate(R.layout.listitem_swap, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SwapHolder holder, int position) {
        holder.setSwap(swaps.get(position), pokemon);
    }

    @Override
    public int getItemCount() {
        return swaps.size();
    }
}

class SwapHolder extends RecyclerView.ViewHolder {
    private final TextView swapDateText;
    private final TextView swapSourcePokemon;
    private final TextView swapSourceTrainer;
    private final TextView swapTargetPokemon;
    private final TextView swapTargetTrainer;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
    SwapHolder(@NonNull View itemView) {
        super(itemView);
        swapDateText = itemView.findViewById(R.id.swapDateText);
        swapSourcePokemon = itemView.findViewById(R.id.swapSourcePokemon);
        swapSourceTrainer = itemView.findViewById(R.id.swapSourceTrainer);
        swapTargetPokemon = itemView.findViewById(R.id.swapTargetPokemon);
        swapTargetTrainer = itemView.findViewById(R.id.swapTargetTrainer);

        itemView.setTag(this);
    }

    void setSwap(Swap swap, Pokemon pokemon) {
        //COMPLETED-TODO
        if(swap == null) {
            Log.println(Log.WARN, "SwapAdapter", String.format(Locale.getDefault(), "Empty Swap provided%n"));
            return;
        } else {
            //Log.println(Log.INFO, "Current Pokemon", String.format(Locale.getDefault(), pokemon.toString()));
        }
        this.swapDateText.setText(formatter.format(swap.getDate()));

        Pokemon leftPokemon = pokemon;
        Trainer leftTrainer;
        Pokemon rightPokemon = swap.getOtherPokemon(pokemon);
        Trainer rightTrainer;
        if (pokemon.equals(swap.getSourcePokemon())) {
            leftTrainer = swap.getSourceTrainer();
            rightTrainer = swap.getTargetTrainer();
        } else {
            leftTrainer = swap.getTargetTrainer();
            rightTrainer = swap.getSourceTrainer();
        }
        this.swapSourcePokemon.setText(leftPokemon.getName());
        this.swapSourceTrainer.setText((leftTrainer != null) ? leftTrainer.toString() : "deleted Trainer");
        this.swapTargetPokemon.setText((rightPokemon != null) ? rightPokemon.getName() : "deleted Pokemon");
        this.swapTargetTrainer.setText((rightTrainer != null) ? rightTrainer.toString() : "deleted Trainer");
    }

}
