package com.sparta.game.effects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * ============================================================================
 * PILAR DA POO: HERANÇA & POLIMORFISMO
 * ============================================================================
 * Partícula de impacto visceral. Herda de Particle e sofre ação da gravidade.
 */
public class BloodParticle extends Particle {

    private final int size;

    public BloodParticle(double x, double y, double vx, double vy, int maxLife, int size) {
        super(x, y, vx, vy, maxLife);
        this.size = size;
    }

    @Override
    public void update() {
        super.update();
        vy += 0.25; // Gravidade afeta o respingo de sangue
        vx *= 0.96;
    }

    @Override
    public void render(Graphics2D g) {
        float alpha = getAlpha();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        g2.setColor(new Color(160, 10, 10));
        g2.fillOval((int) x, (int) y, size, size);

        g2.dispose();
    }
}

