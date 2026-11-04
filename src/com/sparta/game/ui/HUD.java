package com.sparta.game.ui;

import com.sparta.game.combat.ComboTracker;
import com.sparta.game.entities.Player;
import com.sparta.game.entities.SpartanBoss;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * ============================================================================
 * INTERFACE DE USUÁRIO (HUD) — TEMA GOD OF WAR
 * ============================================================================
 * Renderiza os mostradores de vida espartana (verde e vermelho), medidor de
 * Fúria Espartana, contador de combos e a barra monumental do Chefe de Fase.
 */
public class HUD {

    private final Font greekTitleFont;
    private final Font textFont;
    private final Font comboFont;

    public HUD() {
        this.greekTitleFont = new Font("Georgia", Font.BOLD, 18);
        this.textFont = new Font("Arial", Font.BOLD, 12);
        this.comboFont = new Font("Impact", Font.ITALIC, 26);
    }

    public void render(Graphics2D g, Player player, SpartanBoss boss, int stageNumber,
                       boolean chestNearby, int screenWidth, int screenHeight) {
        // 1. Barra de Vida de Kratos
        renderPlayerHealthBar(g, player);

        // 2. Medidor de Fúria Espartana (Spartan Rage)
        renderRageMeter(g, player);

        renderProgress(g, player);
        renderStageLabel(g, stageNumber, screenWidth);

        // 3. Mostrador de Combos estilizado
        renderComboDisplay(g, player.getComboTracker());

        // 4. Barra monumental de Chefe (se o chefe estiver vivo)
        if (boss != null && boss.isAlive()) {
            renderBossHealthBar(g, boss, screenWidth);
        }

        // 5. Guia sutil de controles no rodapé
        renderControlsHelp(g, screenWidth, screenHeight);
        if (chestNearby) {
            g.setFont(textFont);
            g.setColor(new Color(255, 220, 120));
            String prompt = "[E] ABRIR BAU";
            g.drawString(prompt, (screenWidth - g.getFontMetrics().stringWidth(prompt)) / 2, screenHeight - 38);
        }
    }

    private void renderProgress(Graphics2D g, Player player) {
        int x = 24;
        int y = 64;
        int barW = 180;
        g.setColor(new Color(30, 20, 20, 210));
        g.fillRect(x, y, barW, 7);
        g.setColor(new Color(85, 140, 205));
        double progress = (double) player.getExperience() / player.getExperienceToNextLevel();
        g.fillRect(x, y, (int) (barW * progress), 7);
        g.setFont(textFont);
        g.setColor(Color.WHITE);
        g.drawString("NIVEL " + player.getLevel() + "   XP " + player.getExperience()
                + " / " + player.getExperienceToNextLevel(), x, y + 20);
    }

    private void renderStageLabel(Graphics2D g, int stageNumber, int screenWidth) {
        String[] names = {"", "TEMPLO DE ESPARTA", "ARENA DE ARES", "PORTOES DE ARES"};
        String label = stageNumber >= 1 && stageNumber < names.length ? names[stageNumber] : "ARENA";
        g.setFont(greekTitleFont);
        g.setColor(new Color(235, 205, 143));
        int textWidth = g.getFontMetrics().stringWidth(label);
        g.drawString(label, screenWidth - textWidth - 24, 24);
    }

    private void renderPlayerHealthBar(Graphics2D g, Player player) {
        int x = 24;
        int y = 20;
        int barW = 220;
        int barH = 16;

        // Borda decorada em bronze
        g.setColor(new Color(25, 20, 15, 220));
        g.fillRect(x - 4, y - 4, barW + 8, barH + 8);
        g.setColor(new Color(170, 130, 45));
        g.setStroke(new BasicStroke(2));
        g.drawRect(x - 4, y - 4, barW + 8, barH + 8);

        // Fundo vermelho escuro (dano)
        g.setColor(new Color(90, 15, 15));
        g.fillRect(x, y, barW, barH);

        // Vida atual em verde clássico de God of War
        double hpPct = (double) player.getHealth() / player.getMaxHealth();
        g.setColor(new Color(40, 200, 60));
        g.fillRect(x, y, (int) (barW * hpPct), barH);

        // Texto com numeração da vida
        g.setFont(textFont);
        g.setColor(Color.WHITE);
        g.drawString("HP: " + player.getHealth() + " / " + player.getMaxHealth(), x + 8, y + 13);
    }

    private void renderRageMeter(Graphics2D g, Player player) {
        int x = 24;
        int y = 46;
        int barW = 180;
        int barH = 10;

        // Fundo do medidor
        g.setColor(new Color(30, 20, 20, 200));
        g.fillRect(x - 2, y - 2, barW + 4, barH + 4);
        g.setColor(new Color(140, 80, 30));
        g.drawRect(x - 2, y - 2, barW + 4, barH + 4);

        // Preenchimento de Fúria
        double ragePct = player.isInRageMode()
            ? (double) player.getRageTimer() / player.getRageDuration()
            : (double) player.getRage() / player.getMaxRage();
        Color rageColor = player.isInRageMode() ? new Color(255, 30, 10) : new Color(255, 140, 0);
        g.setColor(rageColor);
        g.fillRect(x, y, (int) (barW * ragePct), barH);

        // Indicador de fúria cheia
        if (player.getRage() >= player.getMaxRage() && !player.isInRageMode()) {
            g.setFont(textFont);
            g.setColor(new Color(255, 220, 40));
            g.drawString("★ FÚRIA PRONTA! [E] / [I] ★", x + barW + 12, y + 10);
        } else if (player.isInRageMode()) {
            g.setFont(textFont);
            g.setColor(new Color(255, 50, 20));
            g.drawString("FURIA ATIVA: " + ((player.getRageTimer() + 59) / 60) + "s", x + barW + 12, y + 10);
        }
    }

    private void renderComboDisplay(Graphics2D g, ComboTracker tracker) {
        int count = tracker.getComboCount();
        if (count < 2) return;

        int x = 32;
        int y = 105;

        g.setFont(comboFont);
        g.setColor(tracker.getStyleColor());
        g.drawString(count + " GOLPES", x, y);

        String styleRank = tracker.getStyleRank();
        if (!styleRank.isEmpty()) {
            g.setFont(new Font("Impact", Font.PLAIN, 18));
            g.setColor(Color.WHITE);
            g.drawString(styleRank, x, y + 20);
        }

        // Barra de decaimento do temporizador do combo
        int meterW = 120;
        g.setColor(new Color(60, 60, 60));
        g.fillRect(x, y + 26, meterW, 4);
        g.setColor(tracker.getStyleColor());
        g.fillRect(x, y + 26, (int) (meterW * tracker.getTimerPercentage()), 4);
    }

    private void renderBossHealthBar(Graphics2D g, SpartanBoss boss, int screenWidth) {
        int barW = 440;
        int barH = 18;
        int x = (screenWidth - barW) / 2;
        int y = 30;

        // Moldura grega
        g.setColor(new Color(20, 15, 15, 230));
        g.fillRect(x - 6, y - 20, barW + 12, barH + 28);
        g.setColor(new Color(210, 160, 40));
        g.setStroke(new BasicStroke(2));
        g.drawRect(x - 6, y - 20, barW + 12, barH + 28);

        // Título do Chefe
        g.setFont(greekTitleFont);
        g.setColor(new Color(255, 215, 100));
        String title = boss.getPhase() == 3 ? "ARAUTO DE ARES — IRA DE ARES"
            : boss.getPhase() == 2 ? "ARAUTO DE ARES — GENERAL FURIOSO"
            : "ARAUTO DE ARES — GENERAL DE ESPARTA";
        int strW = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (screenWidth - strW) / 2, y - 4);

        // Barra de Vida
        g.setColor(new Color(70, 10, 10));
        g.fillRect(x, y, barW, barH);

        double pct = (double) boss.getHealth() / boss.getMaxHealth();
        g.setColor(boss.isEnraged() ? new Color(255, 40, 20) : new Color(200, 30, 30));
        g.fillRect(x, y, (int) (barW * pct), barH);

        g.setFont(textFont);
        g.setColor(Color.WHITE);
        String hpText = boss.getHealth() + " / " + boss.getMaxHealth();
        int hpW = g.getFontMetrics().stringWidth(hpText);
        g.drawString(hpText, (screenWidth - hpW) / 2, y + 14);
    }

    private void renderControlsHelp(Graphics2D g, int screenWidth, int screenHeight) {
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.setColor(new Color(230, 230, 230, 170));
        String help = "[A/D] Mover | [W] Pular | [J] Golpe | [K] Ciclone | [L] Esquiva | [U] Bloquear | [I] Furia | [E] Bau | [P] Pausa";
        int textW = g.getFontMetrics().stringWidth(help);
        g.drawString(help, screenWidth - textW - 16, screenHeight - 14);
    }
}

