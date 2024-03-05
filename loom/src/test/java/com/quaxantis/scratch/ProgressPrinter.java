package com.quaxantis.scratch;

public class ProgressPrinter {

    final String pattern;
    String backspaces = null;

    public ProgressPrinter(String pattern) {
        this(pattern, true);
    }

    public ProgressPrinter(String pattern, boolean replaceLines) {
        this.pattern = pattern;
        backspaces = (replaceLines) ? null : "";
    }

    public void finish() {
        backspaces = null;
        System.out.println();
    }

    public <T> T update(T arg) {
        update(new Object[]{arg});
        return arg;
    }

    public void update(Object... args) {
        if (backspaces != null) {
            System.out.print(backspaces);
        }
        String newValue = pattern.formatted(args);
        System.out.print(newValue);
        if (backspaces != null && backspaces.isEmpty()) {
            System.out.println();
        } else {
            backspaces = "\b".repeat(newValue.length());
        }
    }

    public static String numberPattern(long maxNumber) {
        return "%" + Math.max(1, (int) Math.ceil(Math.log10(maxNumber))) + "s";
    }
}
