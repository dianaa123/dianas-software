package de.uhd.ifi.pokemonmanager.data;

import android.os.Parcel;
import android.util.Log;

import java.util.Date;
import java.util.Locale;

/**
 * Models a competition of two {@link Pokemon}s belonging to two different {@link Trainer}s.
 *
 * Run {@link #execute(Pokemon, Pokemon)} to perform the competition.
 */
public class Competition extends Swap {

    private static final String TAG = "Competition";
    private int winnerId;
    private int loserId;

    /**
     * Executes Competition.
     * <p>
     * sourcePokemon and targetPokemon need to have different Trainers.
     * The wining trainer will get the losing pokemon.
     * Pokemons will receive a combat score that is modified depending on its {@link Type}.
     *
     * @param sourcePokemon and
     * @param targetPokemon are the Pokemon that will compete.
     */
    @Override
    public void execute(Pokemon sourcePokemon, Pokemon targetPokemon) {
        if (sourcePokemon.getTrainer().equals(targetPokemon.getTrainer())) {
            Log.println(Log.WARN, TAG, String.format("No competition: Trainers '%s' == '%s' are identical!%n", sourcePokemon.getTrainer(), targetPokemon.getTrainer()));
            return;
        }
        Trainer sourceTrainer = sourcePokemon.getTrainer();
        Trainer targetTrainer = targetPokemon.getTrainer();

        this.date = new Date();
        this.id = "" + System.currentTimeMillis();
        this.sourcePokemonId = sourcePokemon.getId();
        this.targetPokemonId = targetPokemon.getId();
        this.sourceTrainerId = sourceTrainer.getId();
        this.targetTrainerId = targetTrainer.getId();
        double scoreSource = Math.random();
        double scoreTarget = Math.random();
        if (sourcePokemon.getType().isTypeBetterThan(targetPokemon.getType())) {
            scoreSource *= 2;
        } else if (targetPokemon.getType().isTypeBetterThan(sourcePokemon.getType())) {
            scoreTarget *= 2;
        }
        Log.println(Log.INFO, TAG, String.format(Locale.getDefault(), "Pokemon '%s' has score: %f%n", sourcePokemon, scoreSource));
        Log.println(Log.INFO, TAG, String.format(Locale.getDefault(), "Pokemon '%s' has score: %f%n", targetPokemon, scoreTarget));
        if (scoreSource > scoreTarget) {
            sourcePokemon.getTrainer().addPokemon(targetPokemon);
            setWinner(sourcePokemon);
            setLoser(targetPokemon);
            Log.println(Log.INFO, TAG, String.format("Pokemon '%s' wins!%n", sourcePokemon));
        } else if (scoreSource < scoreTarget) {
            targetPokemon.getTrainer().addPokemon(sourcePokemon);
            setWinner(targetPokemon);
            setLoser(sourcePokemon);
            Log.println(Log.INFO, TAG, String.format("Pokemon '%s' wins!%n", targetPokemon));
        } else {
            Log.println(Log.INFO, TAG, String.format("Pokemon '%s' and '%s' both have the same score, there is no winner or loser!", sourcePokemon, targetPokemon));
        }
        sourcePokemon.addCompetition(this);
        targetPokemon.addCompetition(this);
    }

    public Pokemon getWinner() {
        return STORAGE.getPokemonById(winnerId);
    }

    private void setWinner(Pokemon winner) {
        this.winnerId = winner.getId();
    }

    public Pokemon getLoser() {
        return STORAGE.getPokemonById(loserId);
    }

    private void setLoser(Pokemon loser) {
        this.loserId = loser.getId();
    }
}
