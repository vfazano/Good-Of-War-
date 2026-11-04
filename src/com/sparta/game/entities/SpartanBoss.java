package com.sparta.game.entities;

import com.sparta.game.combat.AttackType;
import com.sparta.game.combat.DamageInfo;
import com.sparta.game.combat.Hitbox;
import com.sparta.game.core.GameContext;
import com.sparta.game.effects.FireParticle;
import com.sparta.game.effects.FloatingText;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * ============================================================================
 * PILARES DA POO: HERANÇA, POLIMORFISMO & ENCAPSULAMENTO
 * ============================================================================
 * Chefe de Fase: O General de Esparta (Arauto de Ares).
 * Herda de Enemy e implementa tres fases de combate,
 * ataque de espada colossal e ondas de choque telúricas no solo.
 */
public class SpartanBoss extends Enemy {

    private int phase;
    private int phaseTransitionTimer;
    private int groundSlamCooldown;

    public SpartanBoss(double x, double y) {
        super(x, y, 64, 96, 350, 480, 85, 2.2);
        this.phase = 1;
        this.groundSlamCooldown = 180;
    }

    @Override
    public int getExperienceReward() {
        return 200;
    }

    /**
     * ========================================================================
     * PILAR DA POO: POLIMORFISMO (@Override)
     * ========================================================================
     * IA do Chefe de Fase com transição de fases baseada no percentual de vida.
     */
    @Override
    public void updateAI(Player target, GameContext context) {
        double dist = Math.abs((x + width / 2.0) - (target.getX() + target.getWidth() / 2.0));
        facingRight = (target.getX() > x);

        if (phaseTransitionTimer <= 0) {
            if (phase == 1 && health <= maxHealth * 0.70) {
                enterPhase(2, context);
            } else if (phase == 2 && health <= maxHealth * 0.35) {
                enterPhase(3, context);
            }
        }

        if (phaseTransitionTimer > 0) {
            phaseTransitionTimer--;
            if (phase >= 2) {
                context.addParticle(new FireParticle(x + Math.random() * width, y + Math.random() * height,
                        (Math.random() - 0.5) * 3, -Math.random() * 4, 24, 8f));
            }
            return;
        }

        if (phase >= 2) {
            context.addParticle(new FireParticle(x + Math.random() * width, y + Math.random() * height,
                    (Math.random() - 0.5) * (phase == 3 ? 3 : 2), -Math.random() * 4, 22, phase == 3 ? 8f : 6f));
        }

        if (groundSlamCooldown > 0) groundSlamCooldown--;

        // Ataque Especial: Impacto Telúrico de Choque (Fase 2 ou aleatório)
        if (phase >= 2 && groundSlamCooldown <= 0 && dist < (phase == 3 ? 380 : 260)) {
            executeGroundSlam(context);
            return;
        }

        // Ataque Corpo a Corpo: Corte da Espada Imperial
        if (dist <= attackRange) {
            if (attackCooldown <= 0 && attackTimer <= 0) {
                executeImperialSlash(context);
            }
        } else if (dist <= detectionRange && attackTimer <= 0) {
            vx = facingRight ? moveSpeed : -moveSpeed;
        }
    }

    private void enterPhase(int nextPhase, GameContext context) {
        phase = nextPhase;
        phaseTransitionTimer = 72;
        moveSpeed = phase == 2 ? 2.9 : 3.7;
        groundSlamCooldown = phase == 2 ? 150 : 95;
        context.getCamera().triggerShake(phase == 3 ? 13.0 : 10.0, 32);
        context.getSoundManager().playRageSound();
        String title = phase == 2 ? "FASE II: GENERAL FURIOSO" : "FASE III: IRA DE ARES";
        context.addParticle(new FloatingText(x + width / 2.0, y - 30, title,
                phase == 3 ? new Color(255, 90, 20) : new Color(255, 30, 20), 26));
    }

    private void executeImperialSlash(GameContext context) {
        attacking = true;
        attackTimer = phase == 3 ? 20 : 26;
        attackCooldown = phase == 3 ? 42 : phase == 2 ? 60 : 85;

        int hitWidth = 95;
        int hitX = facingRight ? (int) (x + width) : (int) (x - hitWidth);
        int damage = (int) (AttackType.BOSS_SLASH.getBaseDamage() * (phase == 3 ? 1.45 : phase == 2 ? 1.2 : 1.0));
        DamageInfo info = new DamageInfo(damage, AttackType.BOSS_SLASH,
                (facingRight ? 1 : -1) * 7.0, -3.5, false, false);

        Hitbox hitbox = new Hitbox(hitX, (int) (y + 15), hitWidth, 55, info, 14);
        context.addHitbox(hitbox);

        context.getSoundManager().playSlashSound();
        context.getCamera().triggerShake(4.5, 10);
    }

    private void executeGroundSlam(GameContext context) {
        attacking = true;
        attackTimer = 35;
        groundSlamCooldown = phase == 3 ? 150 : 210;

        // Onda de choque horizontal no solo em ambas as direções
        int damage = (int) (AttackType.BOSS_SHOCKWAVE.getBaseDamage() * (phase == 3 ? 1.4 : 1.0));
        DamageInfo shockwave = new DamageInfo(damage, AttackType.BOSS_SHOCKWAVE,
                0, -8.0, true, false);

        int shockWidth = phase == 3 ? 360 : 260;
        Hitbox hitbox = new Hitbox((int) (x + width / 2.0 - shockWidth / 2.0), (int) (y + height - 30),
                shockWidth, 35, shockwave, 18);
        context.addHitbox(hitbox);

        context.getSoundManager().playHeavyImpactSound();
        context.getCamera().triggerShake(9.0, 22);

        // Partículas da onda de choque no solo
        int particleCount = phase == 3 ? 34 : 22;
        for (int i = 0; i < particleCount; i++) {
            double angle = (Math.PI / particleCount) * i;
            double pvx = (Math.random() - 0.5) * 8;
            context.addParticle(new FireParticle(x + width / 2.0, y + height - 10, pvx, -Math.random() * 4, 30, 8f));
        }
    }

    @Override
    public void render(Graphics2D g) {
        if (!alive) return;

        Graphics2D g2 = (Graphics2D) g.create();
        int cx = (int) (x + width / 2);
        int cy = (int) y;
        int dir = facingRight ? 1 : -1;

        // Aura vermelha na Fase 2
        if (phase >= 2) {
            int auraPadding = phase == 3 ? 52 : 38;
            g2.setColor(phase == 3 ? new Color(255, 35, 5, 120) : new Color(255, 30, 10, 90));
            g2.fillOval(cx - auraPadding, cy - 10, auraPadding * 2, height + 20);
        }

        // 1. Pernas com Grevas Ornamentadas de Ouro
        g2.setColor(new Color(50, 40, 30));
        g2.fillRect(cx - 18, cy + 66, 14, 30);
        g2.fillRect(cx + 4, cy + 66, 14, 30);
        g2.setColor(new Color(210, 165, 45)); // Ouro
        g2.fillRect(cx - 16, cy + 72, 10, 18);
        g2.fillRect(cx + 6, cy + 72, 10, 18);

        // 2. Manto Real Escarlate (Capa espartana do General)
        g2.setColor(new Color(170, 15, 15));
        int[] capeX = {cx - dir * 18, cx - dir * 30, cx - dir * 26};
        int[] capeY = {cy + 24, cy + 85, cy + 88};
        g2.fillPolygon(capeX, capeY, 3);

        // 3. Couraça Muscular Dourada (Peitoral de Guerra)
        g2.setColor(new Color(210, 165, 45));
        g2.fillRect(cx - 22, cy + 24, 44, 42);
        g2.setColor(new Color(150, 20, 20)); // Cinto espartano
        g2.fillRect(cx - 24, cy + 54, 48, 12);

        // 4. Elmo Real Espartano de Crista Alta Transversal
        g2.setColor(new Color(220, 175, 50));
        g2.fillOval(cx - 15, cy + 2, 30, 24);
        g2.fillRect(cx - 14, cy + 12, 28, 14);

        // Crista transversal monumental vermelha e preta
        g2.setColor(new Color(190, 20, 20));
        g2.fillRect(cx - 18, cy - 12, 36, 14);
        g2.setColor(Color.BLACK);
        g2.fillRect(cx - 12, cy - 12, 24, 4);

        // Olhos brilhando ferozes
        g2.setColor(phase == 3 ? new Color(255, 255, 90) : new Color(255, 50, 50));
        g2.fillRect(cx + (dir > 0 ? 3 : -8), cy + 14, 5, 4);

        // 5. Espada Imperial Espartana (Xiphos Gigante)
        int swordX = cx + (attacking ? (dir * 45) : (dir * 22));
        int swordY = cy + (attacking ? 30 : 15);

        g2.setColor(new Color(180, 140, 40)); // Guarda dourada
        g2.fillRect(swordX - 4, swordY, 8, 8);

        // Lâmina longa de bronze e aço
        g2.setColor(new Color(230, 230, 240));
        g2.fillRect(swordX - 2, swordY - 45, 6, 45);
        g2.setColor(new Color(210, 165, 45));
        g2.drawLine(swordX + 1, swordY - 45, swordX + 1, swordY);

        g2.dispose();
    }

    public boolean isEnraged() {
        return phase >= 2;
    }

    public int getPhase() {
        return phase;
    }
}

