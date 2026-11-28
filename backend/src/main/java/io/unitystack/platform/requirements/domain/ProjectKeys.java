package io.unitystack.platform.requirements.domain;

import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/** Short, human-friendly project keys such as "GPB" (used later as a prefix for change IDs). */
public final class ProjectKeys {
    public static final Pattern VALID = Pattern.compile("^[A-Z][A-Z0-9]{1,9}$");

    private ProjectKeys() {
    }

    static String base(String name) {
        String cleaned = name == null ? "" : name.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]+", " ").strip();
        String key;
        if (cleaned.isEmpty()) {
            key = "PRJ";
        } else {
            String[] words = cleaned.split(" ");
            if (words.length == 1) {
                key = words[0].substring(0, Math.min(4, words[0].length()));
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < words.length && sb.length() < 5; i++) sb.append(words[i].charAt(0));
                key = sb.toString();
            }
        }
        if (!Character.isLetter(key.charAt(0))) key = "P" + key;
        if (key.length() < 2) key = key + "PRJ".substring(0, 2);
        return key.substring(0, Math.min(key.length(), 8));
    }

    public static String generate(String name, Predicate<String> exists) {
        String base = base(name);
        if (!exists.test(base)) return base;
        for (int i = 2; i < 1000; i++) {
            String candidate = base + i;
            if (!exists.test(candidate)) return candidate;
        }
        throw new IllegalStateException("Could not allocate a project key for " + base);
    }
}
