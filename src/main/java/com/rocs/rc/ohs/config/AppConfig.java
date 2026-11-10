package com.rocs.rc.ohs.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Loads configuration from application.properties.
 */
public final class AppConfig {

    /** Name of the properties file to load. */
    private static final String FILE_NAME = "application.properties";

    /** Loaded properties values. */
    private static final Properties VALUES = new Properties();

    static {
        // 1. Try classpath first
        try (InputStream stream = AppConfig.class.getClassLoader()
                .getResourceAsStream(FILE_NAME)) {
            if (stream != null) {
                VALUES.load(stream);
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not read " + FILE_NAME + " from classpath.", e);
        }

        // 2. Fallback to working directory if not found in classpath
        if (VALUES.isEmpty()) {
            Path source = Paths.get(FILE_NAME);
            if (Files.isReadable(source)) {
                try (InputStream stream = Files.newInputStream(source)) {
                    VALUES.load(stream);
                } catch (IOException e) {
                    throw new IllegalStateException(
                            "Could not read " + FILE_NAME + ".", e);
                }
            }
        }

        if (VALUES.isEmpty()) {
            throw new IllegalStateException(FILE_NAME
                    + " not found in classpath or working directory.");
        }
    }

    private AppConfig() {
        // Prevent instantiation
    }

    /**
     * Returns the value for a key, or null if not found.
     *
     * @param key The property name.
     * @return The property value, or null.
     */
    public static String get(String key) {
        String override = System.getProperty(key);
        if (override != null) {
            return override;
        }
        return VALUES.getProperty(key);
    }

    /**
     * Returns the value for a key and throws an exception if missing.
     *
     * @param key The property name.
     * @return The configured value, trimmed.
     * @throws IllegalStateException If the key is missing or blank.
     */
    public static String getRequired(String key) {
        String value = get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(
                    "Missing required property \"" + key + "\"");
        }
        return value.trim();
    }
}
