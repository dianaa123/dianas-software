package de.uhd.ifi.pokemonmanager.ui.adapter;

import android.content.Context;
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
import de.uhd.ifi.pokemonmanager.data.Competition;
import de.uhd.ifi.pokemonmanager.data.Pokemon;
import de.uhd.ifi.pokemonmanager.data.Swap;

//TODO:
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
    SwapHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setTag(this);
    }

    void setSwap(Swap swap, Pokemon pokemon) {
        //TODO
    }

}
