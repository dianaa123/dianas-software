package de.uhd.ifi.pokemonmanager.storage;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.uhd.ifi.pokemonmanager.data.Competition;
import de.uhd.ifi.pokemonmanager.data.Pokemon;
import de.uhd.ifi.pokemonmanager.data.Swap;
import de.uhd.ifi.pokemonmanager.data.Trainer;

import static java.util.stream.Collectors.toList;

/**
 * Manages Storage for PokemonManager.
 * <p>
 * Pokemon, Trainer, Swap and Competition data is saved to .ser files when {@link SerialStorage#saveAll(Context)} is called.
 * {@link Pokemon} and {@link Trainer} are stored in ArrayLists at the place corresponding to their id.
 * {@link Swap} and {@link Competition} do not have linear IDs and are stored in HashMaps instead.
 * <p>
 * For reading and writing files, {@link Serial} is used.
 */
public class SerialStorage {
    public static SerialStorage getInstance() {
        return SerialStorage.INSTANCE;
    }

    private static final SerialStorage INSTANCE = new SerialStorage();
    private static final String FOLDER_NAME = "pokemon_manager";

    // The files where data is saved
    private static final String MAX_POKEMON_ID_FILE = "max_pokemon_id.ser";
    private static final String MAX_TRAINER_ID_FILE = "max_trainer_id.ser";

    private static final String POKEMON_FILE = "pokemon_list.ser";
    private static final String TRAINER_FILE = "trainer_list.ser";
    private static final String SWAPS_FILE = "swaps.ser";
    private static final String COMPETITIONS_FILE = "competitions.ser";

    private final ArrayList<Pokemon> pokemons;
    private final ArrayList<Trainer> trainers;
    private final HashMap<String, Swap> swaps;
    private final HashMap<String, Competition> competitions;

    private SerialStorage() {
        pokemons = new ArrayList<>();
        trainers = new ArrayList<>();
        swaps = new HashMap<>();
        competitions = new HashMap<>();
    }

    /**
     * Gets all {@link Pokemon} from Storage.
     * <p>
     * Pokemon are removed by setting them to null, so we need .filter(Objects::nonNull) to exclude
     * empty entries of removed Pokemon.
     * <p>
     *
     * @return list of Pokemon, ordered by Id
     */
    public List<Pokemon> getAllPokemons() {
        return Collections.unmodifiableList(pokemons).stream().filter(Objects::nonNull).collect(toList());
    }

    /**
     * Gets all {@link Trainer}s from Storage.
     * <p>
     * Differs from getAllPokemon because Trainers can't be removed, so the no null objects need
     * to be filtered.
     * <p>
     *
     * @return List of Trainers, ordered by Id
     */
    public List<Trainer> getAllTrainers() {
        return Collections.unmodifiableList(trainers);
    }

    /**
     * Removes a {@link Pokemon} from storage.
     * <p>
     * Pokemon is removed by setting it to null.
     * <p>
     *
     * @param toRemove Pokemon that will be removed from storage.
     */
    public void remove(final Pokemon toRemove) {
        pokemons.set(toRemove.getId(), null);
    }

    /**
     * Saves attributes of a {@link Pokemon} in storage.
     *
     * @param toUpdate Pokemon that gets its attributes updated.
     */
    public void save(final Pokemon toUpdate) {
        updateLinearList(toUpdate.getId(), toUpdate, pokemons);
    }

    //Updates Pokemon or Trainer objects
    private <T> void updateLinearList(final int id, final T obj, final List<T> list) {
        while (list.size() <= id) {
            list.add(null);
        }
        list.set(id, obj);
    }

    /**
     * Saves attributes of a {@link Trainer} in storage.
     *
     * @param toUpdate Trainer that gets its attributes updated.
     */
    public void save(final Trainer toUpdate) {
        updateLinearList(toUpdate.getId(), toUpdate, trainers);
    }

    /**
     * Saves attributes of a {@link Swap} or {@link Competition} in storage.
     *
     * @param swap Swap or Competition that gets its attributes updated.
     */
    public void save(Swap swap) {
        if (swap instanceof Competition) {
            competitions.put(swap.getId(), (Competition) swap);
        } else {
            swaps.put(swap.getId(), swap);
        }
    }

    /**
     * Saves attributes of a {@link Competition}
     *
     * @param competition Competition that gets its attributes updated.
     */
    public void save(Competition competition) {
        if (competition.getLoser() != null) {
            swaps.put(competition.getId(), competition);
        }
        competitions.put(competition.getId(), competition);
    }

    /**
     * Removes a {@link Swap} or {@link Competition} from storage.
     * <p>
     *
     * @param toRemove Pokemon that will be removed from storage.
     */
    public void remove(Swap toRemove) {
        if (toRemove instanceof Competition) {
            competitions.remove(toRemove.getId());
        } else {
            swaps.remove(toRemove.getId());
        }
    }


    /**
     * Gets {@link Pokemon} from storage by id.
     *
     * @param id of Pokemon.
     * @return Pokemon with given id.
     */
    public Pokemon getPokemonById(final int id) {
        return getObjectByIdIn(id, pokemons);
    }

    //Used to get object at index id from list
    private static <T> T getObjectByIdIn(final int id, final List<T> objects) {
        T result;
        if (0 <= id && id < objects.size()) {
            result = objects.get(id);
        } else {
            result = null;
        }

        return result;
    }

    /**
     * Gets {@link Trainer} from storage by id.
     *
     * @param id of Trainer.
     * @return Trainer with given id.
     */
    public Trainer getTrainerById(final int id) {
        return getObjectByIdIn(id, trainers);
    }

    /**
     * Gets {@link Swap} from storage by id.
     *
     * @param id of Swap.
     * @return Swap with given id.
     */
    public Swap getSwapById(final String id) {
        return swaps.get(id);
    }

    /**
     * Gets {@link Competition} from storage by id.
     *
     * @param id of Competition.
     * @return Competition with given id.
     */
    public Competition getCompetitionById(final String id) {
        return competitions.get(id);
    }

    /**
     * Clears Storage, deletes all files.
     *
     * @param context from where method is called. Needed to get file directory.
     */
    public void clear(Context context) {
        File folder = new File(context.getFilesDir(), FOLDER_NAME);
        pokemons.clear();
        trainers.clear();
        swaps.clear();
        competitions.clear();

        Pokemon.setNextId(0);
        Trainer.setNextId(0);

        new File(folder, MAX_POKEMON_ID_FILE).delete();
        new File(folder, MAX_TRAINER_ID_FILE).delete();
        new File(folder, POKEMON_FILE).delete();
        new File(folder, TRAINER_FILE).delete();
        new File(folder, SWAPS_FILE).delete();
        new File(folder, COMPETITIONS_FILE).delete();
    }

    /**
     * Saves all data from storage to files.
     *
     * @param context from where method is called. Needed to get file directory.
     */
    public void saveAll(Context context) {
        File folder = new File(context.getFilesDir(), FOLDER_NAME);
        Serial.write(new File(folder, MAX_POKEMON_ID_FILE), pokemons.size());
        Serial.write(new File(folder, MAX_TRAINER_ID_FILE), trainers.size());

        Serial.write(new File(folder, POKEMON_FILE), pokemons);
        Serial.write(new File(folder, TRAINER_FILE), trainers);
        Serial.write(new File(folder, SWAPS_FILE), swaps);
        Serial.write(new File(folder, COMPETITIONS_FILE), competitions);
    }

    /**
     * Loads all data from files into storage.
     *
     * @param context from where method is called. Needed to get file directory.
     */
    public void loadAll(Context context) {
        File folder = new File(context.getFilesDir(), FOLDER_NAME);
        pokemons.clear();
        trainers.clear();
        swaps.clear();
        competitions.clear();

        Pokemon.setNextId(Serial.read(new File(folder, MAX_POKEMON_ID_FILE), 0));
        Trainer.setNextId(Serial.read(new File(folder, MAX_TRAINER_ID_FILE), 0));

        pokemons.addAll(Serial.read(new File(folder, POKEMON_FILE), new ArrayList<>()));
        trainers.addAll(Serial.read(new File(folder, TRAINER_FILE), new ArrayList<>()));
        swaps.putAll(Serial.read(new File(folder, SWAPS_FILE), new HashMap<>()));
        competitions.putAll(Serial.read(new File(folder, COMPETITIONS_FILE), new HashMap<>()));
    }
}
