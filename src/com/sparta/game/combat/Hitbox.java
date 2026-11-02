package com.sparta.game.combat;

import java.awt.Rectangle;

/**
 * ============================================================================
 * PILAR DA POO: ENCAPSULAMENTO
 * ============================================================================
 * Representa uma área de colisão de ataque ativa no espaço 2D, com tempo
 * de vida em frames e informações de dano acopladas.
 */
public class Hitbox {

    private final Rectangle bounds;
    private final DamageInfo damageInfo;
    private int lifetimeFrames;
    private boolean active;

    public Hitbox(int x, int y, int width, int height, DamageInfo damageInfo, int lifetimeFrames) {
        this.bounds = new Rectangle(x, y, width, height);
        this.damageInfo = damageInfo;
        this.lifetimeFrames = lifetimeFrames;
        this.active = true;
    }

    public void update() {
        if (lifetimeFrames > 0) {
            lifetimeFrames--;
            if (lifetimeFrames <= 0) {
                active = false;
            }
        }
    }

    public boolean intersects(Rectangle other) {
        return active && bounds.intersects(other);
    }

    public Rectangle getBounds() {
        return new Rectangle(bounds);
    }

    public DamageInfo getDamageInfo() {
        return damageInfo;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }
}

