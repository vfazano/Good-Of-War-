package com.sparta.game.effects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * ============================================================================
 * PILAR DA POO: HERANÇA & POLIMORFISMO
 * ============================================================================
 * Partícula brilhante emitida ao colidir armas metálicas ou bloquear com o escudo.
 */
public class SparkParticle extends Particle {

    public SparkParticle(double x, double y, double vx, double vy, int maxLife) {
        super(x, y, vx, vy, maxLife);
    }

    @Override
    public void render(Graphics2D g) {
        float alpha = getAlpha();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        g2.setColor(Color.WHITE);
        g2.drawLine((int) x, (int) y, (int) (x - vx * 2), (int) (y - vy * 2));

        g2.setColor(new Color(255, 230, 100));
        g2.fillOval((int) x - 1, (int) y - 1, 3, 3);

        g2.dispose();
    }
}

