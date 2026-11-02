package com.sparta.game.entities;

import com.sparta.game.combat.AttackType;
import com.sparta.game.combat.DamageInfo;
import com.sparta.game.combat.Hitbox;
import com.sparta.game.core.GameContext;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * ============================================================================
 * PILARES DA POO: HERANÇA & POLIMORFISMO
 * ============================================================================
 * Soldado Legionário de Esparta. Herda de Enemy e implementa
 * comportamento tático de perseguição ágil e estocada com lança.
 */
public class SpartanLegionnaire extends Enemy {

    private int spearThrustFrames;

    public SpartanLegionnaire(double x, double y) {
        super(x, y, 38, 64, 45, 340, 55, 2.8);
    }

    /**
     * ========================================================================
     * POLIMORFISMO: Implementação da IA específica do Legionário
     * ========================================================================
     */
    @Override
    public void updateAI(Player target, GameContext context) {
        double dist = Math.abs((x + width / 2.0) - (target.getX() + target.getWidth() / 2.0));
        facingRight = (target.getX() > x);

        // Se estiver dentro da área de ataque
        if (dist <= attackRange) {
            if (attackCooldown <= 0 && attackTimer <= 0) {
                executeSpearThrust(context);
            }
        }
        // Se estiver no raio de detecção, persegue Kratos
        else if (dist <= detectionRange && attackTimer <= 0) {
            vx = facingRight ? moveSpeed : -moveSpeed;
        }
    }

    private void executeSpearThrust(GameContext context) {
        attacking = true;
        attackTimer = 22;
        attackCooldown = 75;

        // Cria a hitbox de dano da lança espartana
        int hitWidth = 60;
        int hitX = facingRight ? (int) (x + width) : (int) (x - hitWidth);
        DamageInfo info = new DamageInfo(AttackType.ENEMY_STRIKE.getBaseDamage(), AttackType.ENEMY_STRIKE,
                (facingRight ? 1 : -1) * 4.0, -2.0, false, false);

        Hitbox hitbox = new Hitbox(hitX, (int) y + 20, hitWidth, 24, info, 12);
        context.addHitbox(hitbox);
        context.getSoundManager().playSlashSound();
    }

    /**
     * ========================================================================
     * POLIMORFISMO: Renderização do Legionário Espartano
     * ========================================================================
     */
    @Override
    public void render(Graphics2D g) {
        if (!alive) return;

        Graphics2D g2 = (Graphics2D) g.create();
        int cx = (int) (x + width / 2);
        int cy = (int) y;
        int dir = facingRight ? 1 : -1;

        // 1. Pernas
        g2.setColor(new Color(60, 40, 20));
        g2.fillRect(cx - 10, cy + 44, 8, 20);
        g2.fillRect(cx + 2, cy + 44, 8, 20);

        // 2. Túnica Vermelha e Armadura de Bronze
        g2.setColor(new Color(160, 20, 20));
        g2.fillRect(cx - 13, cy + 34, 26, 12);
        g2.setColor(new Color(185, 135, 45)); // Couraça de bronze
        g2.fillRect(cx - 12, cy + 18, 24, 18);

        // 3. Elmo Coríntio Espartano com Penacho Vermelho
        g2.setColor(new Color(185, 135, 45));
        g2.fillOval(cx - 10, cy + 2, 20, 18);
        g2.fillRect(cx - 10, cy + 10, 20, 10);

        // Abertura em "T" do elmo
        g2.setColor(Color.BLACK);
        g2.fillRect(cx + (dir > 0 ? 0 : -6), cy + 8, 6, 8);

        // Penacho de crina de cavalo vermelho no topo
        g2.setColor(new Color(200, 25, 25));
        g2.fillRect(cx - 4, cy - 6, 8, 8);

        // 4. Lança Espartana (Dory)
        g2.setColor(new Color(120, 80, 40)); // Haste de madeira
        int spearReach = attacking ? (dir * 45) : (dir * 15);
        g2.fillRect(cx + dir * 8, cy + 22, spearReach, 4);

        // Ponta de ferro da lança
        g2.setColor(new Color(200, 200, 210));
        g2.fillRect(cx + dir * 8 + spearReach, cy + 20, dir * 12, 8);

        // 5. Barra de vida individual sobre o inimigo
        renderMiniHealthBar(g2, cx, cy);

        g2.dispose();
    }

    private void renderMiniHealthBar(Graphics2D g, int cx, int cy) {
        int barW = 34;
        int barH = 4;
        int bx = cx - barW / 2;
        int by = cy - 12;

        g.setColor(Color.BLACK);
        g.fillRect(bx - 1, by - 1, barW + 2, barH + 2);
        g.setColor(new Color(60, 60, 60));
        g.fillRect(bx, by, barW, barH);

        double pct = (double) health / maxHealth;
        g.setColor(new Color(220, 40, 40));
        g.fillRect(bx, by, (int) (barW * pct), barH);
    }
}

