package com.sparta.game.entities;

import com.sparta.game.combat.AttackType;
import com.sparta.game.combat.DamageInfo;
import com.sparta.game.combat.Hitbox;
import com.sparta.game.core.GameContext;
import com.sparta.game.effects.FloatingText;
import com.sparta.game.effects.SparkParticle;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * ============================================================================
 * PILARES DA POO: HERANÇA & POLIMORFISMO
 * ============================================================================
 * Bruto Espartano / Guarda Pretoriano Pesado.
 * Herda de Enemy e SOBRESCREVE o método takeDamage para aplicar sua defesa
 * de armadura pesada (reduz 35% do dano) e resistência a knockback (Hyper-Armor).
 */
public class SpartanBrute extends Enemy {

    private boolean chargingSmash;
    private int smashChargeTimer;

    public SpartanBrute(double x, double y) {
        super(x, y, 52, 78, 120, 300, 70, 1.6);
        this.chargingSmash = false;
        this.smashChargeTimer = 0;
    }

    @Override
    public int getExperienceReward() {
        return 65;
    }

    /**
     * ========================================================================
     * PILAR DA POO: POLIMORFISMO (@Override)
     * ========================================================================
     * O Bruto absorve dano de forma diferente: sua blindagem espartana
     * absorve 35% do impacto e anula recuos leves.
     */
    @Override
    public void takeDamage(DamageInfo info) {
        if (!alive || invulnerableFrames > 0) return;

        // Redução de armadura pesada
        int reducedDamage = (int) (info.getAmount() * 0.65);
        if (reducedDamage < 1) reducedDamage = 1;

        // Aplica o dano encapsulado
        setHealth(this.health - reducedDamage);

        // Hyper-armor: se o golpe não for forte, o recuo é mínimo
        double effectiveKbX = info.canBreakBlock() ? info.getKnockbackX() * 0.5 : info.getKnockbackX() * 0.2;
        this.vx += effectiveKbX;
        this.hitStunFrames = info.canBreakBlock() ? 10 : 4;
        this.invulnerableFrames = 14;
    }

    @Override
    public void updateAI(Player target, GameContext context) {
        double dist = Math.abs((x + width / 2.0) - (target.getX() + target.getWidth() / 2.0));
        facingRight = (target.getX() > x);

        if (chargingSmash) {
            smashChargeTimer++;
            // Faíscas no martelo enquanto carrega o golpe
            int hammerX = facingRight ? (int) (x + width + 10) : (int) (x - 10);
            context.addParticle(new SparkParticle(hammerX, y + 20, (Math.random() - 0.5) * 4, -Math.random() * 3, 15));

            if (smashChargeTimer >= 35) {
                executeHammerSmash(context);
                chargingSmash = false;
                smashChargeTimer = 0;
            }
            return;
        }

        // Se estiver perto, inicia a carga do martelo
        if (dist <= attackRange) {
            if (attackCooldown <= 0 && attackTimer <= 0) {
                chargingSmash = true;
                smashChargeTimer = 0;
                attackCooldown = 110;
            }
        } else if (dist <= detectionRange && attackTimer <= 0) {
            vx = facingRight ? moveSpeed : -moveSpeed;
        }
    }

    private void executeHammerSmash(GameContext context) {
        attacking = true;
        attackTimer = 25;

        int hitWidth = 85;
        int hitX = facingRight ? (int) (x + width) : (int) (x - hitWidth);
        DamageInfo info = new DamageInfo(AttackType.BRUTE_SMASH.getBaseDamage(), AttackType.BRUTE_SMASH,
                (facingRight ? 1 : -1) * 8.0, -5.0, true, false);

        Hitbox hitbox = new Hitbox(hitX, (int) (y + height - 35), hitWidth, 40, info, 14);
        context.addHitbox(hitbox);

        context.getSoundManager().playHeavyImpactSound();
        context.getCamera().triggerShake(6.0, 12);
    }

    @Override
    public void render(Graphics2D g) {
        if (!alive) return;

        Graphics2D g2 = (Graphics2D) g.create();
        int cx = (int) (x + width / 2);
        int cy = (int) y;
        int dir = facingRight ? 1 : -1;

        // 1. Pernas pesadas e grevas reforçadas
        g2.setColor(new Color(50, 45, 45));
        g2.fillRect(cx - 15, cy + 54, 12, 24);
        g2.fillRect(cx + 3, cy + 54, 12, 24);

        // 2. Armadura de Placas de Ferro Escuro
        g2.setColor(new Color(65, 60, 62));
        g2.fillRect(cx - 18, cy + 22, 36, 34);

        // Ombreiras com espigões
        g2.setColor(new Color(140, 110, 50)); // Bronze antigo
        g2.fillRect(cx - 24, cy + 18, 12, 14);
        g2.fillRect(cx + 12, cy + 18, 12, 14);

        // 3. Elmo Pesado com Chifres de Touro / Minotauro
        g2.setColor(new Color(45, 40, 42));
        g2.fillRect(cx - 13, cy + 4, 26, 20);

        // Chifres
        g2.setColor(new Color(200, 190, 170));
        g2.drawLine(cx - 13, cy + 10, cx - 22, cy - 2);
        g2.drawLine(cx + 13, cy + 10, cx + 22, cy - 2);

        // Olhos brilhando em vermelho
        g2.setColor(new Color(255, 30, 30));
        g2.fillRect(cx + (dir > 0 ? 2 : -6), cy + 12, 4, 3);

        // 4. Martelo de Guerra Gigante
        int hammerX = cx + (chargingSmash ? (dir * 28) : (dir * 18));
        int hammerY = cy + (chargingSmash ? -8 : 28);

        g2.setColor(new Color(110, 80, 50)); // Cabo longo
        g2.fillRect(hammerX, hammerY - 14, 6, 52);

        // Cabeça de ferro do martelo
        g2.setColor(new Color(90, 85, 90));
        g2.fillRect(hammerX - 10, hammerY - 16, 26, 18);
        g2.setColor(new Color(160, 140, 50));
        g2.drawRect(hammerX - 10, hammerY - 16, 26, 18);

        // 5. Barra de vida
        renderMiniHealthBar(g2, cx, cy);

        g2.dispose();
    }

    private void renderMiniHealthBar(Graphics2D g, int cx, int cy) {
        int barW = 46;
        int barH = 5;
        int bx = cx - barW / 2;
        int by = cy - 14;

        g.setColor(Color.BLACK);
        g.fillRect(bx - 1, by - 1, barW + 2, barH + 2);
        g.setColor(new Color(50, 50, 50));
        g.fillRect(bx, by, barW, barH);

        double pct = (double) health / maxHealth;
        g.setColor(new Color(240, 140, 20));
        g.fillRect(bx, by, (int) (barW * pct), barH);
    }
}

