package com.example.memorygame;

public enum Difficulty {
    EASY(12),
    MEDIUM(18),
    HARD(24);

    private final int pairs;

    Difficulty(int pairs) {
        this.pairs = pairs;
    }

    public int getPairs() {
        return pairs;
    }
}

