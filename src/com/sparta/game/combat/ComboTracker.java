package com.sparta.game.combat;

import java.awt.Color;

/**
 * ============================================================================
 * PILAR DA POO: ENCAPSULAMENTO
 * ============================================================================
 * Gerencia a contagem de acertos consecutivos (combos), o temporizador de decaimento
 * e a classificação de estilo típica da franquia God of War.
 */
public class ComboTracker {

    private int comboCount;
    private int comboTimer;
    private static final int MAX_TIMER_FRAMES = 150; // ~2.5 segundos a 60 FPS
    private int maxComboAchieved;

    public ComboTracker() {
        this.comboCount = 0;
        this.comboTimer = 0;
        this.maxComboAchieved = 0;
    }

    public void registerHit() {
        this.comboCount++;
        this.comboTimer = MAX_TIMER_FRAMES;
        if (this.comboCount > this.maxComboAchieved) {
            this.maxComboAchieved = this.comboCount;
        }
    }

    public void update() {
        if (comboTimer > 0) {
            comboTimer--;
            if (comboTimer <= 0) {
                comboCount = 0;
            }
        }
    }

    public void reset() {
        this.comboCount = 0;
        this.comboTimer = 0;
    }

    public int getComboCount() {
        return comboCount;
    }

    public int getComboTimer() {
        return comboTimer;
    }

    public double getTimerPercentage() {
        return (double) comboTimer / MAX_TIMER_FRAMES;
    }

    public int getMaxComboAchieved() {
        return maxComboAchieved;
    }

    public String getStyleRank() {
        if (comboCount >= 30) return "SPARTAN GOD!";
        if (comboCount >= 20) return "GODLIKE!";
        if (comboCount >= 12) return "RELENTLESS!";
        if (comboCount >= 6) return "SAVAGE!";
        if (comboCount >= 3) return "BRUTAL!";
        return "";
    }

    public Color getStyleColor() {
        if (comboCount >= 30) return new Color(255, 60, 60);
        if (comboCount >= 20) return new Color(255, 140, 0);
        if (comboCount >= 12) return new Color(255, 215, 0);
        if (comboCount >= 6) return new Color(200, 100, 255);
        if (comboCount >= 3) return new Color(100, 200, 255);
        return Color.WHITE;
    }
}

