package com.sparta.game.effects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * ============================================================================
 * PILAR DA POO: HERANÇA & POLIMORFISMO
 * ============================================================================
 * Partícula flamejante que herda de Particle. Representa as brasas e o fogo
 * primordial das Lâminas do Caos e da Fúria Espartana de Kratos.
 */
public class FireParticle extends Particle {

    private final float size;
    private final Color coreColor;

    public FireParticle(double x, double y, double vx, double vy, int maxLife, float size) {
        super(x, y, vx, vy, maxLife);
        this.size = size;
        this.coreColor = (Math.random() > 0.5) ? new Color(255, 180, 40) : new Color(255, 60, 20);
    }

    @Override
    public void update() {
        super.update();
        vy -= 0.08; // Chamas sobem naturalmente
        vx *= 0.94; // Resistência do ar
    }

    @Override
    public void render(Graphics2D g) {
        float alpha = getAlpha();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        int drawSize = Math.max(1, (int) (size * (0.4f + 0.6f * alpha)));
        g2.setColor(coreColor);
        g2.fillOval((int) x - drawSize / 2, (int) y - drawSize / 2, drawSize, drawSize);

        // Brilho exterior alaranjado
        g2.setColor(new Color(255, 120, 0, (int) (alpha * 120)));
        g2.fillOval((int) x - drawSize, (int) y - drawSize, drawSize * 2, drawSize * 2);

        g2.dispose();
    }
}

