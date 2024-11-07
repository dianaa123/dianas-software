package de.uhd.ifi.pokemonmanager.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;

/**
 * Contains methods for writing Serializable objects to files and reading them from files. Used in {@link SerialStorage}.
 */
public class Serial {

    private Serial() {
        // private because this is a static util class
    }

    public static void write(File destination, Serializable object) {
        requireNonNull(destination);
        requireNonNull(object);
        of(destination).map(File::getParentFile).ifPresent(File::mkdirs);
        try (ObjectOutputStream oos = createOutputStream(destination)) {
            oos.writeObject(object);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private static ObjectOutputStream createOutputStream(File destination) throws IOException {
        final FileOutputStream fos = new FileOutputStream(destination);
        return new ObjectOutputStream(fos);
    }

    public static <T> T read(File source, T defaultObj) {
        try {
            return read(source);
        } catch (RuntimeException e) {
            return defaultObj;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T read(File source) {
        try (ObjectInputStream ois = createInputStream(source)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectInputStream createInputStream(File source) throws IOException {
        final FileInputStream fis = new FileInputStream(source);
        return new ObjectInputStream(fis);
    }
}
