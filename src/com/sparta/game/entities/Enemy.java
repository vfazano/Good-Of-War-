package com.sparta.game.entities;

import com.sparta.game.combat.DamageInfo;
import com.sparta.game.core.GameContext;

import java.awt.Rectangle;

/**
 * ============================================================================
 * PILARES DA POO: ABSTRAÇÃO, HERANÇA & ENCAPSULAMENTO
 * ============================================================================
 * Classe abstrata base para todos os inimigos do jogo.
 * Herda toda a física e vida de Entity e define a estrutura da Inteligência
 * Artificial que cada monstro ou soldado espartano deve implementar.
 */
public abstract class Enemy extends Entity {

    protected int attackCooldown;
    protected int attackTimer;
    protected int detectionRange;
    protected int attackRange;
    protected double moveSpeed;
    protected boolean attacking;

    public Enemy(double x, double y, int width, int height, int maxHealth,
                 int detectionRange, int attackRange, double moveSpeed) {
        super(x, y, width, height, maxHealth);
        this.detectionRange = detectionRange;
        this.attackRange = attackRange;
        this.moveSpeed = moveSpeed;
        this.attackCooldown = 60;
        this.attackTimer = 0;
        this.attacking = false;
    }

    /**
     * ========================================================================
     * PILAR DA POO: ABSTRAÇÃO
     * ========================================================================
     * Método abstrato de IA. Cada inimigo (Legionário, Bruto, Boss) tem seu
     * próprio padrão tático de movimentação e ataque.
     */
    public abstract void updateAI(Player target, GameContext context);

    /**
     * Atualização comum de física, timers e chamada da IA polimórfica.
     */
    public void updateEnemy(Player target, GameContext context) {
        if (!alive) return;

        if (attackCooldown > 0) attackCooldown--;
        if (attackTimer > 0) {
            attackTimer--;
            if (attackTimer <= 0) {
                attacking = false;
            }
        }

        // Se estiver em stun por ter recebido golpe das lâminas, não age
        if (hitStunFrames <= 0) {
            updateAI(target, context);
        }

        // Aplica gravidade e colisão de chão
        applyPhysics(context.getLevel());
    }

    @Override
    public void update(GameContext context) {
        // Será chamado via polimorfismo passando o player através do GamePanel
    }

    @Override
    public void takeDamage(DamageInfo info) {
        super.takeDamage(info);
    }

    public boolean isAttacking() {
        return attacking;
    }
}

