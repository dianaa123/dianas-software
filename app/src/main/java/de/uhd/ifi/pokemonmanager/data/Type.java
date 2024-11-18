package de.uhd.ifi.pokemonmanager.data;

/**
 * Models the type of the {@link Pokemon}.
 * <p>
 * The type hierarchy is:
 * WATER > FIRE, POISON > WATER and FIRE > POISON
 */
public enum Type {
    FIRE, WATER, POISON;

    /**
     * Compares a type to another one, according to WATER > FIRE, POISON > WATER and FIRE > POISON.
     *
     * @param otherType type of other pokemon, e.g. FIRE
     * @return true if this type > otherType, e.g. WATER > FIRE
     */
    public boolean isTypeBetterThan(Type otherType) {
        return this.ordinal() == (otherType.ordinal() + 1) % Type.values().length;
    }
}
