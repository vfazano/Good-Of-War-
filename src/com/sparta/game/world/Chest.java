package com.sparta.game.world;

import java.awt.Color;
import java.awt.Graphics2D;

public class Chest {

    public enum Reward {
        HEALTH,
        RAGE,
        BLADE_UPGRADE
    }

    private final int x;
    private final int y;
    private final Reward reward;
    private boolean opened;
    private double glowPhase;

    public Chest(int x, int y, Reward reward) {
        this.x = x;
        this.y = y;
        this.reward = reward;
    }

    public void update() {
        glowPhase += 0.08;
    }

    public void render(Graphics2D g) {
        if (opened) {
            g.setColor(new Color(53, 38, 29));
            g.fillRoundRect(x, y + 10, 38, 15, 4, 4);
            g.setColor(new Color(106, 79, 45));
            g.drawRoundRect(x, y + 10, 38, 15, 4, 4);
            return;
        }

        int glowAlpha = 24 + (int) ((Math.sin(glowPhase) + 1) * 24);
        g.setColor(new Color(255, 190, 64, glowAlpha));
        g.fillOval(x - 9, y - 10, 56, 48);

        g.setColor(new Color(91, 48, 28));
        g.fillRoundRect(x, y + 9, 38, 22, 4, 4);
        g.setColor(new Color(154, 96, 42));
        g.fillRoundRect(x, y + 3, 38, 13, 4, 4);
        g.setColor(new Color(225, 178, 76));
        g.drawRoundRect(x, y + 3, 38, 28, 4, 4);
        g.fillRect(x + 17, y + 11, 5, 9);
        g.setColor(new Color(255, 224, 142));
        g.fillOval(x + 18, y + 13, 3, 3);
    }

    public boolean isNear(int playerX, int playerY, int playerWidth, int playerHeight) {
        int playerCenterX = playerX + playerWidth / 2;
        int playerCenterY = playerY + playerHeight / 2;
        return !opened && Math.abs(playerCenterX - (x + 19)) < 62
                && Math.abs(playerCenterY - (y + 15)) < 55;
    }

    public Reward open() {
        if (opened) return null;
        opened = true;
        return reward;
    }

    public boolean isOpened() { return opened; }
    public Reward getReward() { return reward; }
}