package com.sparta.game.ui;

import com.sparta.game.core.GameState;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * ============================================================================
 * TELAS DE ESTADO: MENU INICIAL, GAME OVER, VITÓRIA & PAUSA
 * ============================================================================
 */
public class MenuOverlay {

    private final Font titleFont;
    private final Font subtitleFont;
    private final Font buttonFont;
    private final Font detailFont;
    private double animTimer;

    public MenuOverlay() {
        this.titleFont = new Font("Georgia", Font.BOLD, 42);
        this.subtitleFont = new Font("Georgia", Font.BOLD, 22);
        this.buttonFont = new Font("Arial", Font.BOLD, 18);
        this.detailFont = new Font("Arial", Font.PLAIN, 13);
        this.animTimer = 0;
    }

    public void update() {
        animTimer += 0.05;
    }

    public void render(Graphics2D g, GameState state, int screenWidth, int screenHeight, int maxCombo, int stageNumber) {
        switch (state) {
            case MENU:
                renderStartMenu(g, screenWidth, screenHeight);
                break;
            case PAUSED:
                renderPauseMenu(g, screenWidth, screenHeight);
                break;
            case LEVEL_UP:
                renderLevelUp(g, screenWidth, screenHeight);
                break;
            case STAGE_CLEAR:
                renderStageClear(g, screenWidth, screenHeight, stageNumber);
                break;
            case GAME_OVER:
                renderGameOver(g, screenWidth, screenHeight);
                break;
            case VICTORY:
                renderVictory(g, screenWidth, screenHeight, maxCombo);
                break;
            default:
                break;
        }
    }

    private void renderStartMenu(Graphics2D g, int width, int height) {
        // Escurecimento de fundo
        g.setColor(new Color(15, 10, 12, 235));
        g.fillRect(0, 0, width, height);

        // Caixa ornamental de apresentação
        int boxW = 760;
        int boxH = 460;
        int bx = (width - boxW) / 2;
        int by = (height - boxH) / 2;

        g.setColor(new Color(30, 20, 22));
        g.fillRect(bx, by, boxW, boxH);
        g.setColor(new Color(190, 140, 45));
        g.setStroke(new BasicStroke(3));
        g.drawRect(bx, by, boxW, boxH);

        // Título Principal
        g.setFont(titleFont);
        g.setColor(new Color(240, 50, 30));
        String title = "GOOD OF WAR";
        int tw = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (width - tw) / 2, by + 65);

        // Subtítulo
        g.setFont(subtitleFont);
        g.setColor(new Color(220, 190, 120));
        String sub = "— Projeto Acadêmico SENAI • Programação Orientada a Objetos —";
        int sw = g.getFontMetrics().stringWidth(sub);
        g.drawString(sub, (width - sw) / 2, by + 105);

        // Pilares POO em destaque
        g.setFont(detailFont);
        g.setColor(new Color(180, 180, 190));
        String oop = "Pilares Implementados: [Abstração] • [Encapsulamento] • [Herança] • [Polimorfismo]";
        int ow = g.getFontMetrics().stringWidth(oop);
        g.drawString(oop, (width - ow) / 2, by + 140);

        // Painel de Controles
        int panelY = by + 170;
        g.setColor(new Color(45, 35, 38));
        g.fillRect(bx + 40, panelY, boxW - 80, 204);
        g.setColor(new Color(160, 120, 40));
        g.drawRect(bx + 40, panelY, boxW - 80, 204);

        g.setFont(new Font("Georgia", Font.BOLD, 15));
        g.setColor(new Color(255, 215, 100));
        g.drawString("COMANDOS DE BATALHA DE KRATOS:", bx + 60, panelY + 30);

        g.setFont(detailFont);
        g.setColor(Color.WHITE);
        g.drawString("• A / D ou Setas: Movimentar Kratos pelo Templo de Esparta", bx + 60, panelY + 58);
        g.drawString("• W / Espaço: Salto Atlético com Gravidade", bx + 60, panelY + 82);
        g.drawString("• J ou Z: Ataque Leve com as Lâminas do Caos (Combo de 3 Golpes)", bx + 60, panelY + 106);
        g.drawString("• K ou X: Cyclone of Chaos (Ataque Pesado em Área 360°)", bx + 60, panelY + 130);
        g.drawString("• L ou C: Esquiva | U ou Shift: Bloqueio | I: Fúria | E: Abrir baú", bx + 60, panelY + 154);
        g.drawString("• Derrote inimigos para ganhar XP | 1 / 2 / 3: escolher melhoria ao subir de nível", bx + 60, panelY + 180);

        // Botão pulsar para iniciar
        float pulse = (float) ((Math.sin(animTimer * 2) + 1.0) / 2.0);
        Color startCol = new Color(255, (int) (180 + pulse * 60), 40);
        g.setFont(buttonFont);
        g.setColor(startCol);
        String startText = "▶ PRESSIONE [ENTER] OU [ESPAÇO] PARA COMEÇAR ◀";
        int stw = g.getFontMetrics().stringWidth(startText);
        g.drawString(startText, (width - stw) / 2, by + boxH - 35);
    }

    private void renderLevelUp(Graphics2D g, int width, int height) {
        g.setColor(new Color(12, 9, 10, 235));
        g.fillRect(0, 0, width, height);
        g.setFont(titleFont);
        g.setColor(new Color(255, 211, 92));
        String title = "NOVO NIVEL";
        g.drawString(title, (width - g.getFontMetrics().stringWidth(title)) / 2, 150);

        g.setFont(detailFont);
        g.setColor(new Color(225, 215, 198));
        String prompt = "Escolha uma melhoria permanente";
        g.drawString(prompt, (width - g.getFontMetrics().stringWidth(prompt)) / 2, 185);

        String[] upgrades = {"[1] VITALIDADE", "+20 vida maxima e recupera 30 HP",
                             "[2] LAMINAS", "+3 de dano em todos os golpes",
                             "[3] FURIA", "+2 segundos no modo Furia"};
        int cardWidth = 250;
        int cardHeight = 116;
        int gap = 18;
        int startX = (width - (cardWidth * 3 + gap * 2)) / 2;
        for (int i = 0; i < 3; i++) {
            int x = startX + i * (cardWidth + gap);
            g.setColor(new Color(39, 29, 24));
            g.fillRect(x, 220, cardWidth, cardHeight);
            g.setColor(new Color(190, 140, 50));
            g.drawRect(x, 220, cardWidth, cardHeight);
            g.setFont(subtitleFont);
            g.setColor(new Color(255, 211, 92));
            String label = upgrades[i * 2];
            g.drawString(label, x + (cardWidth - g.getFontMetrics().stringWidth(label)) / 2, 260);
            g.setFont(detailFont);
            g.setColor(Color.WHITE);
            String detail = upgrades[i * 2 + 1];
            g.drawString(detail, x + (cardWidth - g.getFontMetrics().stringWidth(detail)) / 2, 294);
        }
    }

    private void renderStageClear(Graphics2D g, int width, int height, int stageNumber) {
        g.setColor(new Color(12, 9, 10, 235));
        g.fillRect(0, 0, width, height);
        g.setFont(titleFont);
        g.setColor(new Color(255, 211, 92));
        String title = "AREA CONQUISTADA";
        g.drawString(title, (width - g.getFontMetrics().stringWidth(title)) / 2, height / 2 - 60);
        g.setFont(subtitleFont);
        g.setColor(Color.WHITE);
        String next = stageNumber < 3 ? "Pressione [ENTER] para seguir para a proxima arena" : "A batalha final espera por voce";
        g.drawString(next, (width - g.getFontMetrics().stringWidth(next)) / 2, height / 2 + 8);
    }

    private void renderGameOver(Graphics2D g, int width, int height) {
        g.setColor(new Color(40, 5, 5, 235));
        g.fillRect(0, 0, width, height);

        g.setFont(titleFont);
        g.setColor(new Color(255, 30, 20));
        String title = "VOCÊ CAIU EM BATALHA";
        int tw = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (width - tw) / 2, height / 2 - 40);

        g.setFont(subtitleFont);
        g.setColor(new Color(220, 200, 200));
        String sub = "O submundo de Hades reclama sua alma...";
        int sw = g.getFontMetrics().stringWidth(sub);
        g.drawString(sub, (width - sw) / 2, height / 2 + 10);

        g.setFont(buttonFont);
        g.setColor(new Color(255, 220, 50));
        String restart = "Pressione [R] para Renascer e Lutar Novamente";
        int rw = g.getFontMetrics().stringWidth(restart);
        g.drawString(restart, (width - rw) / 2, height / 2 + 75);
    }

    private void renderVictory(Graphics2D g, int width, int height, int maxCombo) {
        g.setColor(new Color(10, 25, 15, 235));
        g.fillRect(0, 0, width, height);

        g.setFont(titleFont);
        g.setColor(new Color(255, 215, 50));
        String title = "VITÓRIA! ESPARTA PREVALECE!";
        int tw = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (width - tw) / 2, height / 2 - 60);

        g.setFont(subtitleFont);
        g.setColor(Color.WHITE);
        String sub = "O Arauto de Ares e suas legiões foram aniquilados!";
        int sw = g.getFontMetrics().stringWidth(sub);
        g.drawString(sub, (width - sw) / 2, height / 2 - 10);

        g.setFont(buttonFont);
        g.setColor(new Color(255, 140, 30));
        String combo = "Maior Combo Realizado: " + maxCombo + " Golpes Consecutivos!";
        int cw = g.getFontMetrics().stringWidth(combo);
        g.drawString(combo, (width - cw) / 2, height / 2 + 35);

        g.setFont(buttonFont);
        g.setColor(new Color(100, 255, 120));
        String restart = "Pressione [R] para Reiniciar o Desafio";
        int rw = g.getFontMetrics().stringWidth(restart);
        g.drawString(restart, (width - rw) / 2, height / 2 + 90);
    }

    private void renderPauseMenu(Graphics2D g, int width, int height) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, width, height);

        g.setFont(titleFont);
        g.setColor(Color.WHITE);
        String title = "JOGO PAUSADO";
        int tw = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (width - tw) / 2, height / 2 - 20);

        g.setFont(buttonFont);
        g.setColor(new Color(255, 215, 60));
        String resume = "Pressione [P] para Continuar";
        int rw = g.getFontMetrics().stringWidth(resume);
        g.drawString(resume, (width - rw) / 2, height / 2 + 35);
    }
}

