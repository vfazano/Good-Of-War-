package com.sparta.game.entities;

import com.sparta.game.combat.DamageInfo;
import com.sparta.game.core.GameContext;
import com.sparta.game.effects.BloodParticle;
import com.sparta.game.effects.FloatingText;
import com.sparta.game.world.Level;
import com.sparta.game.world.Platform;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * ============================================================================
 * PILARES DA POO: ABSTRAÇÃO, ENCAPSULAMENTO & HERANÇA
 * ============================================================================
 * Superclasse abstrata fundamental. Modela as propriedades comuns de todos
 * os seres que habitam o mundo de God of War (Kratos e Inimigos).
 */
public abstract class Entity implements Combatant {

    // Encapsulamento: atributos protegidos ou privados
    protected double x;
    protected double y;
    protected double vx;
    protected double vy;
    protected int width;
    protected int height;

    protected int health;
    protected int maxHealth;
    protected boolean alive;

    protected boolean facingRight;
    protected boolean onGround;

    protected int invulnerableFrames;
    protected int hitStunFrames;

    // Constantes físicas globais
    protected static final double GRAVITY = 0.58;
    protected static final double MAX_FALL_SPEED = 12.0;

    public Entity(double x, double y, int width, int height, int maxHealth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.alive = true;
        this.facingRight = true;
        this.onGround = false;
        this.invulnerableFrames = 0;
        this.hitStunFrames = 0;
    }

    /**
     * Contrato Abstrato: Toda subclasse deve ditar como atualiza sua lógica.
     */
    public abstract void update(GameContext context);

    /**
     * Contrato Abstrato: Toda subclasse deve ditar como se renderiza na tela.
     */
    public abstract void render(Graphics2D g);

    /**
     * Física compartilhada de gravidade, limites e colisão com plataformas.
     */
    protected void applyPhysics(Level level) {
        // Gravidade
        if (!onGround) {
            vy += GRAVITY;
            if (vy > MAX_FALL_SPEED) {
                vy = MAX_FALL_SPEED;
            }
        }

        // Atualiza posição horizontal e atrito
        x += vx;
        vx *= 0.86;

        // Limite lateral da arena
        if (x < 10) {
            x = 10;
            vx = 0;
        } else if (x + width > level.getWidth() - 10) {
            x = level.getWidth() - width - 10;
            vx = 0;
        }

        // Atualiza posição vertical
        y += vy;
        onGround = false;

        // Detecção de colisão com plataformas
        Rectangle myBounds = getBounds();
        for (Platform p : level.getPlatforms()) {
            Rectangle pb = p.getBounds();
            if (myBounds.intersects(pb)) {
                // Checa se estava caindo em cima da plataforma
                if (vy > 0 && (y + height - vy) <= pb.y + 12) {
                    y = pb.y - height;
                    vy = 0;
                    onGround = true;
                    break;
                }
            }
        }

        // Redução de frames de invulnerabilidade e stun
        if (invulnerableFrames > 0) invulnerableFrames--;
        if (hitStunFrames > 0) hitStunFrames--;
    }

    /**
     * ========================================================================
     * PILAR DA POO: POLIMORFISMO
     * ========================================================================
     * Implementação padrão da interface Combatant, podendo ser sobrescrita
     * pelas subclasses (como o SpartanBrute com sua armadura).
     */
    @Override
    public void takeDamage(DamageInfo info) {
        if (!alive || invulnerableFrames > 0) return;

        // Aplica o dano respeitando o encapsulamento
        setHealth(this.health - info.getAmount());

        // Knockback físico
        this.vx += info.getKnockbackX();
        this.vy += info.getKnockbackY();
        this.hitStunFrames = 12;
        this.invulnerableFrames = 16;
    }

    /**
     * Spawna respingos de sangue e texto flutuante padrão ao ser atingido.
     */
    protected void spawnHitEffects(GameContext context, DamageInfo info) {
        // Respingo de sangue
        for (int i = 0; i < 7; i++) {
            double bvx = (Math.random() - 0.5) * 6;
            double bvy = -Math.random() * 5;
            context.addParticle(new BloodParticle(x + width / 2.0, y + height / 2.0, bvx, bvy, 35, 4));
        }

        // Dano flutuante
        Color textColor = info.isFromPlayer() ? new Color(255, 220, 60) : new Color(255, 60, 60);
        context.addParticle(new FloatingText(x + width / 2.0, y - 10, "-" + info.getAmount(), textColor, 15));
    }

    /**
     * Hook chamado quando os pontos de vida chegam a zero.
     */
    protected void onDeath() {
        // Subclasses podem estender esse comportamento
    }

    // ========================================================================
    // GETTERS E SETTERS ENCAPSULADOS COM VALIDAÇÃO DE ESTADO
    // ========================================================================

    public double getX() { return x; }
    public double getY() { return y; }
    public void setPosition(double x, double y) { this.x = x; this.y = y; }

    public double getVx() { return vx; }
    public double getVy() { return vy; }
    public void setVelocity(double vx, double vy) { this.vx = vx; this.vy = vy; }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public int getHealth() { return health; }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, this.maxHealth));
        if (this.health <= 0) {
            this.alive = false;
            onDeath();
        }
    }

    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    @Override
    public boolean isAlive() { return alive; }

    public boolean isFacingRight() { return facingRight; }
    public boolean isOnGround() { return onGround; }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    @Override
    public Rectangle getHitbox() {
        return getBounds();
    }
}

