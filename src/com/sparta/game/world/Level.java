package com.sparta.game.world;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * PILAR DA POO: ENCAPSULAMENTO & ABSTRAÇÃO
 * ============================================================================
 * Representa o cenário do Templo de Esparta (Gates of Ares).
 * Gerencia a lista de plataformas, tochas e a renderização do cenário mitológico.
 */
public class Level {

    private final int width;
    private final int height;
    private final List<Platform> platforms;
    private final List<Torch> torches;

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
        this.platforms = new ArrayList<Platform>();
        this.torches = new ArrayList<Torch>();

        buildSpartanArena();
    }

    private void buildSpartanArena() {
        // Chão principal contínuo
        platforms.add(new Platform(0, 520, width, 180, true));

        // Pilares e plataformas elevadas (estilo arquitetura espartana)
        platforms.add(new Platform(260, 410, 220, 24, false));
        platforms.add(new Platform(600, 330, 260, 24, false));
        platforms.add(new Platform(1020, 420, 240, 24, false));
        platforms.add(new Platform(1380, 320, 280, 24, false));
        platforms.add(new Platform(1780, 410, 220, 24, false));

        // Brazeiros / Tochas distribuídas
        torches.add(new Torch(120, 520));
        torches.add(new Torch(370, 410));
        torches.add(new Torch(730, 330));
        torches.add(new Torch(1140, 420));
        torches.add(new Torch(1520, 320));
        torches.add(new Torch(1890, 410));
        torches.add(new Torch(2200, 520));
    }

    public void update() {
        for (Torch t : torches) {
            t.update();
        }
    }

    public void render(Graphics2D g, int cameraX) {
        // 1. Céu tempestuoso do Monte Olimpo (Gradiente vermelho e roxo sombrio de guerra)
        GradientPaint skyGrad = new GradientPaint(
            0, 0, new Color(40, 15, 25),
            0, height, new Color(110, 30, 30)
        );
        g.setPaint(skyGrad);
        g.fillRect(cameraX, 0, 1280, height);

        // 2. Parallax de montanhas espartanas ao fundo
        g.setColor(new Color(25, 10, 15, 180));
        for (int i = 0; i < width + 500; i += 300) {
            int[] mx = {i - (int)(cameraX * 0.2), i + 180 - (int)(cameraX * 0.2), i + 360 - (int)(cameraX * 0.2)};
            int[] my = {520, 180, 520};
            g.fillPolygon(mx, my, 3);
        }

        // 3. Colunatas gregas monumentais
        for (int x = 80; x < width; x += 320) {
            drawGreekColumn(g, x);
        }

        // 4. Friso de templo superior ao fundo
        g.setColor(new Color(45, 38, 35));
        g.fillRect(0, 60, width, 30);
        g.setColor(new Color(160, 140, 110));
        g.fillRect(0, 90, width, 8);

        // 5. Renderizar plataformas
        for (Platform p : platforms) {
            p.render(g);
        }

        // 6. Renderizar tochas acesas
        for (Torch t : torches) {
            t.render(g);
        }
    }

    private void drawGreekColumn(Graphics2D g, int x) {
        // Base da coluna
        g.setColor(new Color(65, 58, 52));
        g.fillRect(x - 24, 490, 48, 30);
        g.setColor(new Color(90, 80, 72));
        g.fillRect(x - 20, 480, 40, 10);

        // Corpo da coluna canelada
        g.setColor(new Color(80, 70, 65));
        g.fillRect(x - 16, 90, 32, 390);

        // Caneluras verticais
        g.setColor(new Color(55, 48, 44));
        g.drawLine(x - 10, 90, x - 10, 480);
        g.drawLine(x - 3, 90, x - 3, 480);
        g.drawLine(x + 4, 90, x + 4, 480);
        g.drawLine(x + 11, 90, x + 11, 480);

        // Capitel dórico
        g.setColor(new Color(100, 90, 80));
        g.fillRect(x - 22, 80, 44, 12);
        g.setColor(new Color(130, 120, 105));
        g.fillRect(x - 26, 70, 52, 10);
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

