package com.sparta.game.effects;

import java.awt.Graphics2D;

/**
 * ============================================================================
 * PILAR DA POO: ABSTRAÇÃO & HERANÇA
 * ============================================================================
 * Classe base abstrata para todos os efeitos de partículas do jogo.
 * Define coordenadas, velocidade, tempo de vida e o contrato de renderização.
 */
public abstract class Particle {

    protected double x;
    protected double y;
    protected double vx;
    protected double vy;
    protected int life;
    protected int maxLife;
    protected boolean alive;

    public Particle(double x, double y, double vx, double vy, int maxLife) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.maxLife = maxLife;
        this.life = maxLife;
        this.alive = true;
    }

    public void update() {
        x += vx;
        y += vy;
        life--;
        if (life <= 0) {
            alive = false;
        }
    }

    public abstract void render(Graphics2D g);

    public boolean isAlive() {
        return alive;
    }

    protected float getAlpha() {
        return Math.max(0.0f, Math.min(1.0f, (float) life / (float) maxLife));
    }
}

