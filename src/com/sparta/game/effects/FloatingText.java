package com.sparta.game.effects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * ============================================================================
 * PILAR DA POO: HERANÇA & POLIMORFISMO
 * ============================================================================
 * Texto flutuante de combate (dano, bloqueio, status). Herda da classe Particle.
 */
public class FloatingText extends Particle {

    private final String text;
    private final Color color;
    private final Font font;

    public FloatingText(double x, double y, String text, Color color, int size) {
        super(x, y, (Math.random() - 0.5) * 1.5, -1.8, 55);
        this.text = text;
        this.color = color;
        this.font = new Font("Arial", Font.BOLD, size);
    }

    @Override
    public void update() {
        super.update();
        vy *= 0.94; // Desacelera a subida suavemente
    }

    @Override
    public void render(Graphics2D g) {
        float alpha = getAlpha();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setFont(font);

        // Sombra preta para alto contraste
        g2.setColor(new Color(0, 0, 0, (int) (alpha * 200)));
        g2.drawString(text, (int) x + 1, (int) y + 1);

        // Cor principal do texto
        g2.setColor(color);
        g2.drawString(text, (int) x, (int) y);

        g2.dispose();
    }
}

