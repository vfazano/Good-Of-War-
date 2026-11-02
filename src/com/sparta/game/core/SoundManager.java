package com.sparta.game.core;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ============================================================================
 * SINTETIZADOR DE ÁUDIO PROCEDURAL NATIVO EM JAVA
 * ============================================================================
 * Gera e reproduz ondas de áudio em tempo de execução via javax.sound.sampled.
 * Permite efeitos sonoros instantâneos sem necessidade de carregar arquivos .wav externos.
 */
public class SoundManager {

    private static final float SAMPLE_RATE = 22050f;
    private final ExecutorService soundPool;
    private boolean soundEnabled = true;

    public SoundManager() {
        // Pool de threads desacoplado para não atrasar o GameLoop
        this.soundPool = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r, "SpartaAudioThread");
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * Toca o som do corte das Lâminas do Caos (ruído branco filtrado com sweep agudo).
     */
    public void playSlashSound() {
        if (!soundEnabled) return;
        soundPool.execute(() -> {
            int durationMs = 120;
            int numSamples = (int) (SAMPLE_RATE * (durationMs / 1000.0));
            byte[] buffer = new byte[numSamples];

            for (int i = 0; i < numSamples; i++) {
                double t = (double) i / SAMPLE_RATE;
                double freq = 700.0 - (500.0 * (i / (double) numSamples));
                double noise = (Math.random() * 2.0 - 1.0) * 0.4;
                double wave = Math.sin(2.0 * Math.PI * freq * t) * 0.6 + noise;
                double envelope = 1.0 - ((double) i / numSamples); // Fade out
                buffer[i] = (byte) (wave * envelope * 100.0);
            }
            playPcmBuffer(buffer);
        });
    }

    /**
     * Toca o som de impacto pesado / Cyclone of Chaos (grave com distorção).
     */
    public void playHeavyImpactSound() {
        if (!soundEnabled) return;
        soundPool.execute(() -> {
            int durationMs = 260;
            int numSamples = (int) (SAMPLE_RATE * (durationMs / 1000.0));
            byte[] buffer = new byte[numSamples];

            for (int i = 0; i < numSamples; i++) {
                double t = (double) i / SAMPLE_RATE;
                double freq = 140.0 - (90.0 * (i / (double) numSamples));
                double noise = (Math.random() * 2.0 - 1.0) * 0.5;
                double wave = Math.sin(2.0 * Math.PI * freq * t) * 0.7 + noise;
                double envelope = Math.pow(1.0 - ((double) i / numSamples), 1.5);
                buffer[i] = (byte) (wave * envelope * 115.0);
            }
            playPcmBuffer(buffer);
        });
    }

    /**
     * Toca o clang metálico do escudo / bloqueio espartano.
     */
    public void playBlockClangSound() {
        if (!soundEnabled) return;
        soundPool.execute(() -> {
            int durationMs = 180;
            int numSamples = (int) (SAMPLE_RATE * (durationMs / 1000.0));
            byte[] buffer = new byte[numSamples];

            for (int i = 0; i < numSamples; i++) {
                double t = (double) i / SAMPLE_RATE;
                // Harmônicos metálicos
                double wave1 = Math.sin(2.0 * Math.PI * 1200.0 * t);
                double wave2 = Math.sin(2.0 * Math.PI * 2400.0 * t) * 0.5;
                double wave = (wave1 + wave2) * 0.6;
                double envelope = Math.exp(-12.0 * (i / (double) numSamples));
                buffer[i] = (byte) (wave * envelope * 110.0);
            }
            playPcmBuffer(buffer);
        });
    }

    /**
     * Som do pulo atlético de Kratos.
     */
    public void playJumpSound() {
        if (!soundEnabled) return;
        soundPool.execute(() -> {
            int durationMs = 90;
            int numSamples = (int) (SAMPLE_RATE * (durationMs / 1000.0));
            byte[] buffer = new byte[numSamples];

            for (int i = 0; i < numSamples; i++) {
                double t = (double) i / SAMPLE_RATE;
                double freq = 200.0 + (300.0 * (i / (double) numSamples));
                double wave = Math.sin(2.0 * Math.PI * freq * t);
                double envelope = 1.0 - ((double) i / numSamples);
                buffer[i] = (byte) (wave * envelope * 80.0);
            }
            playPcmBuffer(buffer);
        });
    }

    /**
     * Rugido e pulso ao ativar Spartan Rage.
     */
    public void playRageSound() {
        if (!soundEnabled) return;
        soundPool.execute(() -> {
            int durationMs = 450;
            int numSamples = (int) (SAMPLE_RATE * (durationMs / 1000.0));
            byte[] buffer = new byte[numSamples];

            for (int i = 0; i < numSamples; i++) {
                double t = (double) i / SAMPLE_RATE;
                double freq = 80.0 + (Math.sin(t * 30.0) * 20.0);
                double noise = (Math.random() * 2.0 - 1.0) * 0.6;
                double wave = Math.sin(2.0 * Math.PI * freq * t) * 0.6 + noise;
                double envelope = (i < numSamples * 0.2)
                    ? (i / (numSamples * 0.2))
                    : (1.0 - (i - numSamples * 0.2) / (numSamples * 0.8));
                buffer[i] = (byte) (wave * envelope * 120.0);
            }
            playPcmBuffer(buffer);
        });
    }

    /**
     * Acerto de dano comum / impacto no inimigo.
     */
    public void playHitSound() {
        if (!soundEnabled) return;
        soundPool.execute(() -> {
            int durationMs = 80;
            int numSamples = (int) (SAMPLE_RATE * (durationMs / 1000.0));
            byte[] buffer = new byte[numSamples];

            for (int i = 0; i < numSamples; i++) {
                double noise = (Math.random() * 2.0 - 1.0);
                double envelope = 1.0 - ((double) i / numSamples);
                buffer[i] = (byte) (noise * envelope * 85.0);
            }
            playPcmBuffer(buffer);
        });
    }

    private void playPcmBuffer(byte[] buffer) {
        try {
            AudioFormat format = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
            Clip clip = AudioSystem.getClip();
            clip.open(format, buffer, 0, buffer.length);
            clip.start();
        } catch (LineUnavailableException | IllegalArgumentException ignored) {
            // Se o sistema de som do host estiver mudo/ocupado, falha silenciosamente
        }
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }
}

