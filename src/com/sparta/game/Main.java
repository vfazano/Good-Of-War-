package com.sparta.game;

import com.sparta.game.core.GameLoop;
import com.sparta.game.core.GamePanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * ============================================================================
 * PONTO DE ENTRADA PRINCIPAL — GOOD OF WAR
 * ============================================================================
 * Projeto Acadêmico SENAI
 * Disciplina: Programação Orientada a Objetos / Jogos Digitais
 *
 * Configura a janela principal do Swing e inicia a Thread do Game Loop.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame window = new JFrame("Good of War — SENAI POO 2D");
                GamePanel gamePanel = new GamePanel();

                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setResizable(false);
                window.add(gamePanel);
                window.pack();
                window.setLocationRelativeTo(null); // Centraliza na tela
                window.setVisible(true);

                // Inicia o Game Loop de 60 FPS
                GameLoop gameLoop = new GameLoop(gamePanel);
                gameLoop.start();
            }
        });
    }
}

