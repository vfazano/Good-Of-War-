package com.sparta.game.core;

import com.sparta.game.combat.Hitbox;
import com.sparta.game.effects.BloodParticle;
import com.sparta.game.effects.Particle;
import com.sparta.game.effects.SparkParticle;
import com.sparta.game.entities.Enemy;
import com.sparta.game.entities.Player;
import com.sparta.game.entities.SpartanBoss;
import com.sparta.game.entities.SpartanBrute;
import com.sparta.game.entities.SpartanLegionnaire;
import com.sparta.game.ui.HUD;
import com.sparta.game.ui.MenuOverlay;
import com.sparta.game.world.Level;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ============================================================================
 * PAINEL PRINCIPAL & CONTROLADOR DE GAMEPLAY
 * ============================================================================
 * Demonstra POLIMORFISMO ao gerenciar uma coleção heterogênea de inimigos
 * (List<Enemy>) e partículas (List<Particle>) processadas em loops unificados.
 */
public class GamePanel extends JPanel {

    public static final int SCREEN_WIDTH = 960;
    public static final int SCREEN_HEIGHT = 540;

    private GameState state;
    private final InputManager inputManager;
    private final SoundManager soundManager;
    private final Camera camera;
    private final HUD hud;
    private final MenuOverlay menuOverlay;

    private Level level;
    private Player player;
    private SpartanBoss boss;

    // Coleções polimórficas
    private final List<Enemy> enemies;
    private final List<Hitbox> hitboxes;
    private final List<Particle> particles;

    public GamePanel() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        setDoubleBuffered(true);

        this.inputManager = new InputManager();
        addKeyListener(inputManager);

        this.soundManager = new SoundManager();
        this.camera = new Camera(0, 0);
        this.hud = new HUD();
        this.menuOverlay = new MenuOverlay();

        this.enemies = new ArrayList<Enemy>();
        this.hitboxes = new ArrayList<Hitbox>();
        this.particles = new ArrayList<Particle>();

        this.state = GameState.MENU;

        resetGame();
    }

    public void resetGame() {
        this.level = new Level(2400, 700);
        this.player = new Player(180, 440);
        this.enemies.clear();
        this.hitboxes.clear();
        this.particles.clear();

        // População de Inimigos ao longo do templo de Esparta
        enemies.add(new SpartanLegionnaire(480, 440));
        enemies.add(new SpartanLegionnaire(760, 260));
        enemies.add(new SpartanBrute(1120, 430));
        enemies.add(new SpartanLegionnaire(1450, 250));
        enemies.add(new SpartanBrute(1680, 430));

        // Chefe de Fase no final da arena
        this.boss = new SpartanBoss(2050, 420);
        enemies.add(boss);
    }

    public void update() {
        inputManager.update();
        menuOverlay.update();

        // 1. Controle de telas / Estados de Jogo
        if (state == GameState.MENU) {
            if (inputManager.restart || inputManager.jump || inputManager.lightAttack) {
                state = GameState.PLAYING;
            }
            return;
        }

        if (state == GameState.GAME_OVER || state == GameState.VICTORY) {
            if (inputManager.restart) {
                resetGame();
                state = GameState.PLAYING;
            }
            return;
        }

        if (inputManager.pause) {
            state = (state == GameState.PLAYING) ? GameState.PAUSED : GameState.PLAYING;
            return;
        }

        if (state == GameState.PAUSED) {
            return;
        }

        // 2. Criação do contexto de execução
        GameContext context = new GameContext(level, camera, soundManager, inputManager, hitboxes, particles);

        // 3. Atualizar Level e Câmera com rastreio de Kratos
        level.update();
        player.update(context);
        camera.update(player.getX() + player.getWidth() / 2.0, player.getY(), SCREEN_WIDTH, level.getWidth());

        // 4. POLIMORFISMO: Atualizar lista de inimigos heterogêneos
        for (Enemy enemy : enemies) {
            enemy.updateEnemy(player, context);
        }

        // 5. Atualizar Hitboxes ativas
        Iterator<Hitbox> hitIt = hitboxes.iterator();
        while (hitIt.hasNext()) {
            Hitbox h = hitIt.next();
            h.update();
            if (!h.isActive()) {
                hitIt.remove();
            }
        }

        // 6. Processar Colisões de Combate (Hitbox x Hurtbox)
        processCombatCollisions();

        // 7. POLIMORFISMO: Atualizar lista genérica de partículas
        Iterator<Particle> partIt = particles.iterator();
        while (partIt.hasNext()) {
            Particle p = partIt.next();
            p.update();
            if (!p.isAlive()) {
                partIt.remove();
            }
        }

        // 8. Checagem de Condição de Derrota ou Vitória
        if (!player.isAlive()) {
            state = GameState.GAME_OVER;
        } else if (boss != null && !boss.isAlive()) {
            state = GameState.VICTORY;
        }
    }

    private void processCombatCollisions() {
        for (Hitbox h : hitboxes) {
            if (!h.isActive()) continue;

            // Se o golpe veio de Kratos, atinge os inimigos
            if (h.getDamageInfo().isFromPlayer()) {
                for (Enemy enemy : enemies) {
                    if (enemy.isAlive() && h.intersects(enemy.getHitbox())) {
                        enemy.takeDamage(h.getDamageInfo()); // Despacho Polimórfico!
                        player.getComboTracker().registerHit();
                        player.addRage(8);

                        // Partículas de sangue e faísca
                        for (int i = 0; i < 5; i++) {
                            particles.add(new BloodParticle(enemy.getX() + enemy.getWidth() / 2.0,
                                    enemy.getY() + enemy.getHeight() / 2.0,
                                    (Math.random() - 0.5) * 5, -Math.random() * 4, 30, 4));
                        }

                        soundManager.playHitSound();
                        h.deactivate(); // Desativa hitbox para não dar hit infinito no mesmo frame
                        break;
                    }
                }
            }
            // Se o golpe veio de um inimigo, atinge Kratos
            else {
                if (player.isAlive() && h.intersects(player.getHitbox())) {
                    if (player.isBlocking() && !h.getDamageInfo().canBreakBlock()) {
                        // Bloqueio bem-sucedido
                        soundManager.playBlockClangSound();
                        camera.triggerShake(3.0, 8);
                        for (int i = 0; i < 10; i++) {
                            particles.add(new SparkParticle(player.getX() + (player.isFacingRight() ? player.getWidth() : 0),
                                    player.getY() + 30, (Math.random() - 0.5) * 6, -Math.random() * 5, 20));
                        }
                    } else {
                        // Kratos recebe o golpe
                        player.takeDamage(h.getDamageInfo());
                        soundManager.playHitSound();
                    }
                    h.deactivate();
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Ativa antialiasing para gráficos suaves
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int camX = camera.getRenderX();
        int camY = camera.getRenderY();

        // Translação do mundo pela câmera
        g2.translate(-camX, -camY);

        // 1. Cenário do Templo de Esparta
        level.render(g2, camX);

        // 2. POLIMORFISMO: Renderizar todos os inimigos
        for (Enemy enemy : enemies) {
            enemy.render(g2);
        }

        // 3. Renderizar Kratos (Fantasma de Esparta)
        player.render(g2);

        // 4. POLIMORFISMO: Renderizar todas as partículas
        for (Particle p : particles) {
            p.render(g2);
        }

        // Desfaz translação da câmera para desenhar a HUD estática na tela
        g2.translate(camX, camY);

        // 5. Interface de Usuário (HUD)
        hud.render(g2, player, boss, SCREEN_WIDTH, SCREEN_HEIGHT);

        // 6. Overlays de Menu, Pause, Game Over e Vitória
        menuOverlay.render(g2, state, SCREEN_WIDTH, SCREEN_HEIGHT, player.getComboTracker().getMaxComboAchieved());

        g2.dispose();
    }
}

