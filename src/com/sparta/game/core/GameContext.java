package com.sparta.game.core;

import com.sparta.game.combat.Hitbox;
import com.sparta.game.effects.Particle;
import com.sparta.game.world.Level;

import java.util.List;

/**
 * ============================================================================
 * PADRÃO DE PROJETO: CONTEXT OBJECT & ENCAPSULAMENTO
 * ============================================================================
 * Fornece acesso controlado aos subsistemas do jogo para as entidades
 * durante o ciclo de atualização (update), desacoplando-as da janela direta.
 */
public class GameContext {

    private final Level level;
    private final Camera camera;
    private final SoundManager soundManager;
    private final InputManager inputManager;
    private final List<Hitbox> hitboxes;
    private final List<Particle> particles;

    public GameContext(Level level, Camera camera, SoundManager soundManager,
                       InputManager inputManager, List<Hitbox> hitboxes, List<Particle> particles) {
        this.level = level;
        this.camera = camera;
        this.soundManager = soundManager;
        this.inputManager = inputManager;
        this.hitboxes = hitboxes;
        this.particles = particles;
    }

    public Level getLevel() {
        return level;
    }

    public Camera getCamera() {
        return camera;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public void addHitbox(Hitbox hitbox) {
        hitboxes.add(hitbox);
    }

    public void addParticle(Particle particle) {
        particles.add(particle);
    }
}

