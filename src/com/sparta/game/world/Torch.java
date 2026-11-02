package com.sparta.game.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

/**
 * Tocha/Brazeiro do templo de Esparta com efeito de luz dinâmica.
 */
public class Torch {

    private final int x;
    private final int y;
    private double flickerTimer;

    public Torch(int x, int y) {
        this.x = x;
        this.y = y;
        this.flickerTimer = Math.random() * 100;
    }

    public void update() {
        flickerTimer += 0.15;
    }

    public void render(Graphics2D g) {
        // Pedestal de bronze
        g.setColor(new Color(120, 80, 40));
        g.fillRect(x - 6, y, 12, 24);
        g.setColor(new Color(170, 120, 60));
        g.fillRect(x - 10, y - 4, 20, 6);

        // Chamas animadas
        double wobble = Math.sin(flickerTimer) * 3;
        int flameHeight = (int) (14 + Math.cos(flickerTimer * 1.5) * 4);

        // Brilho difuso da luz da tocha
        int lightRadius = (int) (70 + Math.sin(flickerTimer * 2) * 8);
        Point2D center = new Point2D.Float(x, y - 6);
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {new Color(255, 140, 20, 60), new Color(0, 0, 0, 0)};
        RadialGradientPaint p = new RadialGradientPaint(center, lightRadius, dist, colors);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(p);
        g2.fillOval(x - lightRadius, (y - 6) - lightRadius, lightRadius * 2, lightRadius * 2);
        g2.dispose();

        // Chamas internas
        g.setColor(new Color(255, 60, 10));
        int[] px1 = {x - 8, x + 8, (int) (x + wobble)};
        int[] py1 = {y - 4, y - 4, y - flameHeight};
        g.fillPolygon(px1, py1, 3);

        g.setColor(new Color(255, 200, 30));
        int[] px2 = {x - 4, x + 4, (int) (x + wobble * 0.5)};
        int[] py2 = {y - 4, y - 4, y - (int) (flameHeight * 0.7)};
        g.fillPolygon(px2, py2, 3);
    }

    public int getX() { return x; }
    public int getY() { return y; }
}

