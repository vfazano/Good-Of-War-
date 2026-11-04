package com.sparta.game.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * ============================================================================
 * PILAR DA POO: ENCAPSULAMENTO
 * ============================================================================
 * Gerencia o estado das teclas de forma encapsulada, fornecendo flags para
 * pressionamento contínuo e gatilhos de pulso único (justPressed).
 */
public class InputManager implements KeyListener {

    private final boolean[] keys = new boolean[256];
    private final boolean[] justPressed = new boolean[256];

    public boolean left;
    public boolean right;
    public boolean jump;
    public boolean lightAttack;
    public boolean heavyAttack;
    public boolean dodge;
    public boolean block;
    public boolean rage;
    public boolean interact;
    public boolean restart;
    public boolean pause;
    public boolean upgradeHealth;
    public boolean upgradeDamage;
    public boolean upgradeRage;

    public void update() {
        left = isDown(KeyEvent.VK_A) || isDown(KeyEvent.VK_LEFT);
        right = isDown(KeyEvent.VK_D) || isDown(KeyEvent.VK_RIGHT);
        jump = wasJustPressed(KeyEvent.VK_W) || wasJustPressed(KeyEvent.VK_UP) || wasJustPressed(KeyEvent.VK_SPACE);
        lightAttack = wasJustPressed(KeyEvent.VK_J) || wasJustPressed(KeyEvent.VK_Z);
        heavyAttack = wasJustPressed(KeyEvent.VK_K) || wasJustPressed(KeyEvent.VK_X);
        dodge = wasJustPressed(KeyEvent.VK_L) || wasJustPressed(KeyEvent.VK_C);
        block = isDown(KeyEvent.VK_U) || isDown(KeyEvent.VK_SHIFT);
        rage = wasJustPressed(KeyEvent.VK_I);
        interact = wasJustPressed(KeyEvent.VK_E);
        restart = wasJustPressed(KeyEvent.VK_R) || wasJustPressed(KeyEvent.VK_ENTER);
        pause = wasJustPressed(KeyEvent.VK_P) || wasJustPressed(KeyEvent.VK_ESCAPE);
        upgradeHealth = wasJustPressed(KeyEvent.VK_1);
        upgradeDamage = wasJustPressed(KeyEvent.VK_2);
        upgradeRage = wasJustPressed(KeyEvent.VK_3);

        // Limpa o buffer de pulso único
        for (int i = 0; i < justPressed.length; i++) {
            justPressed[i] = false;
        }
    }

    private boolean isDown(int keyCode) {
        if (keyCode >= 0 && keyCode < keys.length) {
            return keys[keyCode];
        }
        return false;
    }

    private boolean wasJustPressed(int keyCode) {
        if (keyCode >= 0 && keyCode < justPressed.length) {
            return justPressed[keyCode];
        }
        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code >= 0 && code < keys.length) {
            if (!keys[code]) {
                justPressed[code] = true;
            }
            keys[code] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code >= 0 && code < keys.length) {
            keys[code] = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Não utilizado para controle de gameplay em tempo real
    }
}

