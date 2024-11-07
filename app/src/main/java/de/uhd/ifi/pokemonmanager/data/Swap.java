package de.uhd.ifi.pokemonmanager.data;

import android.util.Log;

import java.io.Serializable;
import java.util.Date;

import de.uhd.ifi.pokemonmanager.storage.SerialStorage;

/**
 * Swap class for handling Swap object, implements Parcelable and Serializable
 * <p>
 * Swap has id, date, target and source -Pokemon and -Trainer attributes. Call Swap.execute to swap.
 * <p>
 *
 * @see Pokemon for more information on Serializable and Parcelable
 */
public class Swap implements Serializable {

    protected static final SerialStorage STORAGE = SerialStorage.getInstance();

    private static final String TAG = "Swap";
    protected String id;
    protected Date date;
    protected int sourcePokemonId;
    protected int targetPokemonId;
    protected int sourceTrainerId;
    protected int targetTrainerId;


    public Swap() {

    }

    public void execute(Pokemon sourcePokemon, Pokemon targetPokemon) {
        if (!sourcePokemon.isSwapAllowed() || !targetPokemon.isSwapAllowed()) {
            Log.println(Log.WARN, TAG, String.format("No swap: Pokemons '%s' and '%s' are NOT both allowed to be swapped!%n", sourcePokemon.getName(), targetPokemon.getName()));
            return;
        }
        Trainer sourceTrainer = sourcePokemon.getTrainer();
        Trainer targetTrainer = targetPokemon.getTrainer();

        if (sourceTrainer == null) {
            Log.println(Log.WARN, TAG, String.format("No swap: source Trainer is null!%n"));
            return;
        }

        if (targetTrainer == null) {
            Log.println(Log.WARN, TAG, String.format("No swap: target Trainer is null!%n"));
            return;
        }

        if (sourceTrainer.equals(targetTrainer)) {
            Log.println(Log.WARN, TAG, String.format("No swap: Trainers '%s' == '%s' are identical!%n", sourceTrainer, targetTrainer));
            return;
        }

        this.sourcePokemonId = sourcePokemon.getId();
        this.targetPokemonId = targetPokemon.getId();
        this.sourceTrainerId = sourceTrainer.getId();
        this.targetTrainerId = targetTrainer.getId();
        this.date = new Date();
        this.id = "" + System.currentTimeMillis();
        targetTrainer.addPokemon(sourcePokemon);
        sourceTrainer.addPokemon(targetPokemon);
        sourcePokemon.addSwap(this);
        targetPokemon.addSwap(this);

    }

    public Date getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public Pokemon getSourcePokemon() {
        return STORAGE.getPokemonById(sourcePokemonId);
    }

    public Pokemon getTargetPokemon() {
        return STORAGE.getPokemonById(targetPokemonId);
    }

    public Trainer getSourceTrainer() {
        return STORAGE.getTrainerById(sourceTrainerId);
    }

    public Trainer getTargetTrainer() {
        return STORAGE.getTrainerById(targetTrainerId);
    }
}
