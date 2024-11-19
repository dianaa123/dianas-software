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
import de.uhd.ifi.pokemonmanager.data.Competition;
import de.uhd.ifi.pokemonmanager.data.Pokemon;
import de.uhd.ifi.pokemonmanager.data.Trainer;

/**
 * Enables to show a list of {@link Competition}s in the UI in a so called {@link
 * androidx.recyclerview.widget.RecyclerView}.
 */
public class CompetitionAdapter extends RecyclerView.Adapter<CompetitionHolder> {
    private LayoutInflater inflater;
    private List<Competition> competitions;
    private Pokemon pokemon;

    public CompetitionAdapter(final Context context, final List<Competition> competitions, final Pokemon pokemon) {
        this.inflater = LayoutInflater.from(context);
        this.competitions = competitions;
        this.pokemon = pokemon;
    }

    @NonNull
    @Override
    public CompetitionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CompetitionHolder(inflater.inflate(R.layout.listitem_competition, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CompetitionHolder holder, int position) {
        holder.setCompetition(competitions.get(position), pokemon);
    }

    @Override
    public int getItemCount() {
        return competitions.size();
    }
}

/**
 * Responsible to show a single {@link Competition} in the UI.
 */
class CompetitionHolder extends RecyclerView.ViewHolder {

    private final TextView competitionDateText;
    private final TextView competitionOpponent;
    private final TextView competitionResult;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);

    CompetitionHolder(@NonNull View itemView) {
        super(itemView);
        competitionDateText = itemView.findViewById(R.id.competitionDateText);
        competitionOpponent = itemView.findViewById(R.id.competitionOpponent);
        competitionResult = itemView.findViewById(R.id.competitionResult);

        itemView.setTag(this);
    }

    void setCompetition(Competition competition, Pokemon pokemon) {
        if(competition == null) {
            Log.println(Log.WARN, "CompetitionAdapter", String.format(Locale.getDefault(), "Empty Competition provided%n"));
            return;
        } else {
            //Log.println(Log.INFO, "Current Pokemon", String.format(Locale.getDefault(), pokemon.toString()));
        }
        this.competitionDateText.setText(formatter.format(competition.getDate()));

        Pokemon opponentPokemon = null;
        Trainer opponentTrainer = null;
        String opponentTeam;
        if (pokemon.equals(competition.getSourcePokemon())) {
            opponentPokemon = competition.getTargetPokemon();
            opponentTrainer = competition.getTargetTrainer();
        } else if (pokemon.equals(competition.getTargetPokemon())) {
            opponentPokemon = competition.getSourcePokemon();
            opponentTrainer = competition.getSourceTrainer();
        }
        if (opponentPokemon != null && opponentTrainer != null) {
            opponentTeam = opponentPokemon.getName() + " & " + opponentTrainer.toString();
        } else if (opponentPokemon != null) {
            opponentTeam = opponentPokemon.getName();
        } else if (opponentTrainer != null) {
            opponentTeam = "deleted Pokemon" + " & " + opponentTrainer.toString();
        } else {
            opponentTeam = "deleted Pokemon";
        }
        this.competitionOpponent.setText(opponentTeam);

        if (pokemon.equals(competition.getWinner())) {
            this.competitionResult.setText(R.string.competition_won);
        } else if (pokemon.equals(competition.getLoser())) {
            this.competitionResult.setText(R.string.competition_lost);
        } else {
            this.competitionResult.setText(R.string.competition_draw);
        }
    }
}