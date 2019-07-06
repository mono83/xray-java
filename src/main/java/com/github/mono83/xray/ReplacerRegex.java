package com.github.mono83.xray;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regex replacer for testing purposes only.
 */
public class ReplacerRegex implements BiFunction<String, Args, String> {
    private final Pattern pattern;

    public ReplacerRegex() {
        this(":([0-9a-zA-Z\\\\-_]+)");
    }

    public ReplacerRegex(final String regexString) {
        pattern = Pattern.compile(regexString);
    }

    @Override
    public String apply(final String s, final Args args) {
        String src = s;
        if (args == null || args.size() == 0) {
            return src;
        }

        // Replacing
        Matcher m = pattern.matcher(src);
        HashMap<String, Integer> evidences = new HashMap<>();
        while (m.find()) {
            String full = m.group(0);
            String key = m.group(1);
            evidences.putIfAbsent(key, 0);
            int index = evidences.get(key);
            Optional<Arg> opt = args.get(key);
            String newValue = opt.get().getValue(index);
            src = src.replaceFirst(full, Matcher.quoteReplacement(newValue));
            evidences.put(key, index + 1);
        }

        return src;
    }
}
