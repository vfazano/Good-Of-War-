package com.sparta.game.core;

/**
 * ============================================================================
 * NÚCLEO DO GAME LOOP A 60 FPS
 * ============================================================================
 * Implementa Runnable e gerencia o loop de execução temporal fixo a 60 FPS,
 * garantindo taxa de atualização uniforme independente do hardware.
 */
public class GameLoop implements Runnable {

    private static final int TARGET_FPS = 60;
    private static final long OPTIMAL_TIME = 1000000000 / TARGET_FPS; // nanos por frame (~16.66ms)

    private final GamePanel gamePanel;
    private boolean running;
    private Thread gameThread;

    public GameLoop(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.running = false;
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this, "SpartaMainGameLoop");
        gameThread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            if (gameThread != null) {
                gameThread.join(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        long lastLoopTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;

            // 1. Atualizar lógica de gameplay
            gamePanel.update();

            // 2. Renderizar novo quadro na tela
            gamePanel.repaint();

            // 3. Dormir pelo tempo restante do ciclo para manter 60 FPS
            long sleepTime = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    public boolean isRunning() {
        return running;
    }
}

