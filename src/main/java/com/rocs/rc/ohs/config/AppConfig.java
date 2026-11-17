package com.rocs.rc.ohs.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Loads configuration from application.properties.
 */
public final class AppConfig {

    /** Name of the properties file to load. */
    private static final String FILE_NAME = "application.properties";

    /** Loaded properties values. */
    private static final Properties VALUES = new Properties();

    /** Matches references to other properties, such as ${db.oracle.port}. */
    private static final Pattern PROPERTY_REFERENCE = Pattern.compile("\\$\\{([^}]+)}");

    static {
        try (InputStream stream = AppConfig.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (stream != null) {
                VALUES.load(stream);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + FILE_NAME + " from classpath.", e);
        }

        Path source = Paths.get(FILE_NAME);
        if (Files.isReadable(source)) {
            try (InputStream stream = Files.newInputStream(source)) {
                VALUES.load(stream);
            } catch (IOException e) {
                throw new IllegalStateException("Could not read " + FILE_NAME + ".", e);
            }
        }

        if (VALUES.isEmpty()) {
            throw new IllegalStateException(FILE_NAME + " not found in classpath or working directory.");
        }
    }

    private AppConfig() {
    }

    /**
     * Returns the resolved value for a key, or null if not found.
     *
     * @param key The configuration key to look up.
     * @return The property value, or null.
     */
    public static String getProperty(String key) {
        String value = findProperty(key);
        if (value == null) {
            return null;
        }
        return resolveProperty(key, value, new HashSet<String>());
    }

    private static String findProperty(String key) {
        String override = System.getProperty(key);
        return override != null ? override : VALUES.getProperty(key);
    }

    private static String resolveProperty(String key, String value, Set<String> resolving) {
        if (!resolving.add(key)) {
            throw new IllegalStateException("Circular property reference involving " + key + ".");
        }

        try {
            Matcher matcher = PROPERTY_REFERENCE.matcher(value);
            StringBuffer resolved = new StringBuffer();
            while (matcher.find()) {
                String referencedKey = matcher.group(1);
                String referencedValue = findProperty(referencedKey);
                if (referencedValue == null) {
                    throw new IllegalStateException("Property " + key + " references missing property "
                            + referencedKey + ".");
                }
                String replacement = resolveProperty(referencedKey, referencedValue, resolving);
                matcher.appendReplacement(resolved, Matcher.quoteReplacement(replacement));
            }
            matcher.appendTail(resolved);
            return resolved.toString();
        } finally {
            resolving.remove(key);
        }
    }

}
