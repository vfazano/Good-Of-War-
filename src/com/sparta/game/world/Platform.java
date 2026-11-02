package com.sparta.game.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * ============================================================================
 * PILAR DA POO: ENCAPSULAMENTO
 * ============================================================================
 * Plataforma sólida de pedra / mármore espartano para colisão de entidades.
 */
public class Platform {

    private final Rectangle bounds;
    private final boolean isFloor;

    public Platform(int x, int y, int width, int height, boolean isFloor) {
        this.bounds = new Rectangle(x, y, width, height);
        this.isFloor = isFloor;
    }

    public void render(Graphics2D g) {
        // Bloco principal de pedra
        g.setColor(new Color(60, 55, 52));
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        // Borda superior decorada em mármore grego
        g.setColor(new Color(195, 185, 165));
        g.fillRect(bounds.x, bounds.y, bounds.width, 6);

        // Friso grego / relevo em bronze envelhecido
        g.setColor(new Color(110, 95, 75));
        for (int i = bounds.x; i < bounds.x + bounds.width - 20; i += 24) {
            g.drawRect(i, bounds.y + 8, 16, 8);
        }

        // Borda inferior com sombra
        g.setColor(new Color(30, 25, 25));
        g.fillRect(bounds.x, bounds.y + bounds.height - 4, bounds.width, 4);
    }

    public Rectangle getBounds() {
        return new Rectangle(bounds);
    }

    public boolean isFloor() {
        return isFloor;
    }
}

