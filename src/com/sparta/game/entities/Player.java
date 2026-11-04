package com.sparta.game.entities;

import com.sparta.game.combat.AttackType;
import com.sparta.game.combat.ComboTracker;
import com.sparta.game.combat.DamageInfo;
import com.sparta.game.combat.Hitbox;
import com.sparta.game.core.GameContext;
import com.sparta.game.core.InputManager;
import com.sparta.game.effects.FireParticle;
import com.sparta.game.effects.FloatingText;
import com.sparta.game.effects.SparkParticle;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * ============================================================================
 * PILARES DA POO: HERANÇA, ENCAPSULAMENTO & POLIMORFISMO
 * ============================================================================
 * Representa o protagonista: Kratos, o Fantasma de Esparta (Ghost of Sparta).
 * Herda de Entity e implementa todas as mecânicas das Lâminas do Caos,
 * esquiva com rolamento, bloqueio com o Velo de Ouro / Escudo e a Fúria Espartana.
 */
public class Player extends Entity {

    // Encapsulamento de recursos
    private int rage;
    private final int maxRage;
    private boolean inRageMode;
    private int rageTimer;
    private int rageDurationBonus;
    private int experience;
    private int level;
    private int pendingLevelUps;
    private int bladeDamageBonus;

    private final ComboTracker comboTracker;

    // Estados de ação do jogador
    private boolean blocking;
    private boolean dodging;
    private int dodgeTimer;

    private int attackState; // 0 = Idle, 1 = Slash 1, 2 = Slash 2, 3 = Heavy Finisher, 4 = Cyclone
    private int attackTimer;
    private int attackCooldown;

    // Constantes de mobilidade
    private static final double WALK_SPEED = 4.8;
    private static final double RAGE_WALK_SPEED = 6.8;
    private static final double JUMP_FORCE = -12.5;

    public Player(double startX, double startY) {
        super(startX, startY, 44, 68, 100);
        this.rage = 0;
        this.maxRage = 100;
        this.inRageMode = false;
        this.rageTimer = 0;
        this.experience = 0;
        this.level = 1;
        this.pendingLevelUps = 0;
        this.bladeDamageBonus = 0;
        this.rageDurationBonus = 0;
        this.comboTracker = new ComboTracker();
        this.blocking = false;
        this.dodging = false;
    }

    /**
     * ========================================================================
     * PILAR DA POO: POLIMORFISMO (@Override)
     * ========================================================================
     * Sobrescreve update da superclasse Entity, gerenciando inputs, física,
     * combos de ataque e drenagem de fúria.
     */
    @Override
    public void update(GameContext context) {
        InputManager input = context.getInputManager();
        comboTracker.update();

        // 1. Atualizar Fúria Espartana (Spartan Rage)
        if (inRageMode) {
            rageTimer--;
            // Emite chamas intensas continuamente ao redor de Kratos
            for (int i = 0; i < 4; i++) {
                double px = x + Math.random() * width;
                double py = y + Math.random() * height;
                context.addParticle(new FireParticle(px, py, (Math.random() - 0.5) * 3, -Math.random() * 4, 28, 7f));
            }

            if (rageTimer <= 0) {
                inRageMode = false;
                rage = 0;
            }
        } else if (input.rage && rage >= maxRage) {
            activateSpartanRage(context);
        }

        // 2. Esquiva / Rolamento Espartano (Spartan Roll)
        if (dodging) {
            dodgeTimer--;
            invulnerableFrames = 2; // I-frames durante a esquiva
            vx = (facingRight ? 1 : -1) * 8.5;
            if (dodgeTimer <= 0) {
                dodging = false;
            }
        } else if (input.dodge && onGround && attackTimer <= 0) {
            dodging = true;
            dodgeTimer = 18;
            invulnerableFrames = 18;
            context.getSoundManager().playJumpSound();
        }

        // 3. Bloqueio / Postura de Escudo
        blocking = input.block && onGround && attackTimer <= 0 && !dodging;

        // 4. Movimentação Horizontal (se não estiver bloqueando ou atacando estático)
        if (!blocking && !dodging && attackState != 4) {
            double currentSpeed = inRageMode ? RAGE_WALK_SPEED : WALK_SPEED;
            if (input.left) {
                vx = -currentSpeed;
                facingRight = false;
            } else if (input.right) {
                vx = currentSpeed;
                facingRight = true;
            }
        }

        // 5. Pulo
        if (input.jump && onGround && !blocking && !dodging && attackTimer <= 0) {
            vy = JUMP_FORCE;
            onGround = false;
            context.getSoundManager().playJumpSound();
        }

        // 6. Ataques com as Lâminas do Caos (Blades of Chaos)
        handleCombatInputs(context, input);

        // 7. Atualização da física herdada de Entity
        applyPhysics(context.getLevel());
    }

    private void handleCombatInputs(GameContext context, InputManager input) {
        if (attackCooldown > 0) attackCooldown--;

        if (attackTimer > 0) {
            attackTimer--;
            // Geração contínua de partículas de fogo durante o golpe
            double bladeX = facingRight ? (x + width + 20) : (x - 20);
            double bladeY = y + 25;
            context.addParticle(new FireParticle(bladeX, bladeY, (Math.random() - 0.5) * 3, (Math.random() - 0.5) * 3, 20, 5f));

            if (attackTimer <= 0) {
                attackState = 0; // Volta para Idle
            }
        }

        if (dodging || blocking || attackCooldown > 0) return;

        // Ataque Leve com as Lâminas (Combo de 3 Golpes: J)
        if (input.lightAttack && attackTimer <= 4) {
            executeLightAttack(context);
        }
        // Ataque Pesado em Área (Cyclone of Chaos: K)
        else if (input.heavyAttack && attackTimer <= 0) {
            executeCycloneOfChaos(context);
        }
    }

    private void executeLightAttack(GameContext context) {
        int nextCombo = (attackState == 1) ? 2 : (attackState == 2) ? 3 : 1;
        attackState = nextCombo;
        attackTimer = inRageMode ? 12 : 16;
        attackCooldown = inRageMode ? 13 : 18;

        AttackType type = (nextCombo == 1) ? AttackType.LIGHT_SLASH_1 :
                          (nextCombo == 2) ? AttackType.LIGHT_SLASH_2 :
                                             AttackType.LIGHT_SLASH_3;

        int finalDamage = type.getBaseDamage() + bladeDamageBonus;
        if (inRageMode) finalDamage *= 2; // Fúria dobra o poder destrutivo

        double kbX = (facingRight ? 1 : -1) * type.getBaseKnockback();
        DamageInfo damageInfo = new DamageInfo(finalDamage, type, kbX, -3.0, nextCombo == 3, true);

        // Cria a hitbox de alcance das correntes
        int hitWidth = (nextCombo == 3) ? 90 : 75;
        int hitX = facingRight ? (int) (x + width) : (int) (x - hitWidth);
        int hitY = (int) (y + 10);
        Hitbox hitbox = new Hitbox(hitX, hitY, hitWidth, 50, damageInfo, 10);
        context.addHitbox(hitbox);

        context.getSoundManager().playSlashSound();
        if (nextCombo == 3) {
            context.getCamera().triggerShake(4.0, 10);
        }
    }

    private void executeCycloneOfChaos(GameContext context) {
        attackState = 4;
        attackTimer = inRageMode ? 20 : 26;
        attackCooldown = inRageMode ? 24 : 32;

        int finalDamage = AttackType.HEAVY_CYCLONE.getBaseDamage() + bladeDamageBonus;
        if (inRageMode) finalDamage *= 2;

        DamageInfo damageInfo = new DamageInfo(finalDamage, AttackType.HEAVY_CYCLONE, 0, -4.5, true, true);

        // Hitbox circular 360° ao redor de Kratos
        int radius = 100;
        Hitbox hitbox = new Hitbox((int) x - radius / 2, (int) y - 10, width + radius, height + 20, damageInfo, 16);
        context.addHitbox(hitbox);

        context.getSoundManager().playHeavyImpactSound();
        context.getCamera().triggerShake(6.5, 14);

        // Emite anel de partículas de fogo ao redor
        for (int i = 0; i < 18; i++) {
            double angle = (Math.PI * 2 / 18) * i;
            double fvx = Math.cos(angle) * 5;
            double fvy = Math.sin(angle) * 3;
            context.addParticle(new FireParticle(x + width / 2.0, y + height / 2.0, fvx, fvy, 30, 7f));
        }
    }

    public void activateSpartanRage(GameContext context) {
        this.inRageMode = true;
        this.rageTimer = 480 + rageDurationBonus;
        context.getSoundManager().playRageSound();
        context.getCamera().triggerShake(8.0, 20);
        context.addParticle(new FloatingText(x + width / 2.0, y - 25, "SPARTAN RAGE!", new Color(255, 50, 20), 20));
    }

    /**
     * ========================================================================
     * PILAR DA POO: POLIMORFISMO (@Override)
     * ========================================================================
     * Sobrescreve takeDamage com a mecânica de Bloqueio com o Escudo.
     */
    @Override
    public void takeDamage(DamageInfo info) {
        if (!alive || invulnerableFrames > 0 || dodging) return;

        // Se estiver bloqueando e o golpe não quebrar guarda
        if (blocking && !info.canBreakBlock()) {
            this.vx += (facingRight ? -3 : 3); // Pequeno recuo de guarda
            this.invulnerableFrames = 12;
            addRage(15); // Defender com sucesso gera fúria
            return;
        }

        super.takeDamage(info);
        comboTracker.reset(); // Tomar dano zera a sequência de combo
    }

    public void addRage(int amount) {
        this.rage = Math.min(maxRage, this.rage + amount);
    }

    public void restoreHealth(int amount) {
        health = Math.min(maxHealth, health + Math.max(0, amount));
        if (health > 0) alive = true;
    }

    public void upgradeBladesFromChest() {
        bladeDamageBonus += 2;
    }

    public void awardExperience(int amount) {
        experience += Math.max(0, amount);
        while (experience >= getExperienceToNextLevel()) {
            experience -= getExperienceToNextLevel();
            level++;
            pendingLevelUps++;
        }
    }

    public void applyLevelUpgrade(int choice) {
        if (pendingLevelUps <= 0) return;
        if (choice == 1) {
            maxHealth += 20;
            health = Math.min(maxHealth, health + 30);
        } else if (choice == 2) {
            bladeDamageBonus += 3;
        } else if (choice == 3) {
            rageDurationBonus += 120;
        } else {
            return;
        }
        pendingLevelUps--;
    }

    public int getExperienceToNextLevel() {
        return 100 + (level - 1) * 50;
    }

    public int getExperience() { return experience; }
    public int getLevel() { return level; }
    public int getPendingLevelUps() { return pendingLevelUps; }
    public int getRageTimer() { return rageTimer; }
    public int getRageDuration() { return 480 + rageDurationBonus; }
    public int getBladeDamageBonus() { return bladeDamageBonus; }

    /**
     * ========================================================================
     * PILAR DA POO: POLIMORFISMO (@Override)
     * ========================================================================
     * Renderização detalhada e procedural de Kratos (Fantasma de Esparta).
     */
    @Override
    public void render(Graphics2D g) {
        if (!alive) {
            renderKratosFallen(g);
            return;
        }

        // Efeito de piscar ao receber dano
        if (invulnerableFrames > 0 && (invulnerableFrames % 4 < 2)) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();

        // Aura de Fúria Espartana
        if (inRageMode) {
            g2.setColor(new Color(255, 40, 10, 80));
            g2.fillOval((int) x - 12, (int) y - 10, width + 24, height + 18);
        }

        // Direção do personagem
        int dir = facingRight ? 1 : -1;
        int cx = (int) (x + width / 2);
        int cy = (int) y;

        // 1. Pernas, sandalias e grevas de bronze
        g2.setColor(new Color(75, 43, 35));
        g2.fillRoundRect(cx - 13, cy + 47, 10, 18, 4, 4);
        g2.fillRoundRect(cx + 3, cy + 47, 10, 18, 4, 4);
        g2.setColor(new Color(31, 25, 24));
        g2.fillRect(cx - 15, cy + 63, 13, 5);
        g2.fillRect(cx + 2, cy + 63, 13, 5);
        g2.setColor(new Color(184, 129, 43));
        g2.fillPolygon(new int[]{cx - 13, cx - 4, cx - 4, cx - 12},
                       new int[]{cy + 52, cy + 52, cy + 63, cy + 63}, 4);
        g2.fillPolygon(new int[]{cx + 4, cx + 13, cx + 12, cx + 4},
                       new int[]{cy + 52, cy + 52, cy + 63, cy + 63}, 4);
        g2.setColor(new Color(235, 190, 91));
        g2.drawLine(cx - 11, cy + 54, cx - 6, cy + 54);
        g2.drawLine(cx + 6, cy + 54, cx + 11, cy + 54);

        // 2. Pteruges de couro com tiras individuais e cinto de bronze
        g2.setColor(new Color(91, 24, 28));
        g2.fillPolygon(new int[]{cx - 16, cx + 16, cx + 14, cx - 14},
                       new int[]{cy + 37, cy + 37, cy + 52, cy + 52}, 4);
        g2.setColor(new Color(132, 31, 34));
        for (int strip = -10; strip <= 10; strip += 7) {
            g2.fillPolygon(new int[]{cx + strip, cx + strip + 5, cx + strip + 4, cx + strip + 1},
                           new int[]{cy + 40, cy + 40, cy + 52, cy + 52}, 4);
        }
        g2.setColor(new Color(202, 151, 59));
        g2.fillRoundRect(cx - 17, cy + 35, 34, 5, 2, 2);
        g2.setColor(new Color(248, 207, 111));
        g2.fillRect(cx - 2, cy + 36, 5, 3);

        // 3. Tronco atletico, ombros e planos de sombra
        g2.setColor(new Color(177, 177, 184));
        g2.fillRoundRect(cx - 16, cy + 17, 32, 22, 9, 8);
        g2.setColor(new Color(218, 216, 213));
        g2.fillRoundRect(cx - 13, cy + 18, 26, 18, 7, 7);
        g2.setColor(new Color(153, 153, 162));
        g2.fillOval(cx - 12, cy + 20, 12, 8);
        g2.fillOval(cx + 1, cy + 20, 12, 8);
        g2.setColor(new Color(188, 187, 190));
        g2.drawLine(cx, cy + 22, cx, cy + 35);
        g2.drawLine(cx - 8, cy + 31, cx - 2, cy + 32);
        g2.drawLine(cx + 2, cy + 32, cx + 8, cy + 31);

        // 4. Tatuagem vermelha atravessando ombro e peito
        g2.setColor(new Color(174, 28, 35));
        g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        if (facingRight) {
            g2.drawLine(cx - 12, cy + 18, cx - 7, cy + 24);
            g2.drawLine(cx - 7, cy + 24, cx - 3, cy + 34);
            g2.drawLine(cx - 3, cy + 34, cx + 8, cy + 30);
        } else {
            g2.drawLine(cx + 12, cy + 18, cx + 7, cy + 24);
            g2.drawLine(cx + 7, cy + 24, cx + 3, cy + 34);
            g2.drawLine(cx + 3, cy + 34, cx - 8, cy + 30);
        }

        // 5. Cabeça, barba angular e marca vermelha facial
        g2.setColor(new Color(176, 176, 183));
        g2.fillOval(cx - 11, cy + 1, 22, 20);
        g2.setColor(new Color(221, 219, 216));
        g2.fillOval(cx - 9, cy + 2, 18, 16);
        g2.setColor(new Color(45, 37, 36));
        if (dir > 0) {
            g2.fillPolygon(new int[]{cx - 2, cx + 10, cx + 10, cx + 5, cx + 2},
                           new int[]{cy + 12, cy + 11, cy + 18, cy + 23, cy + 19}, 5);
        } else {
            g2.fillPolygon(new int[]{cx + 2, cx - 10, cx - 10, cx - 5, cx - 2},
                           new int[]{cy + 12, cy + 11, cy + 18, cy + 23, cy + 19}, 5);
        }
        g2.setColor(new Color(174, 28, 35));
        g2.fillPolygon(new int[]{cx - dir * 7, cx - dir * 4, cx - dir * 3, cx - dir * 6},
                       new int[]{cy + 3, cy + 5, cy + 17, cy + 19}, 4);
        g2.setColor(new Color(133, 22, 29));
        g2.drawLine(cx - dir * 6, cy + 7, cx - dir * 4, cy + 9);

        // Olhar e sobrancelha voltados para a direcao do movimento
        g2.setColor(new Color(49, 39, 38));
        g2.drawLine(cx + dir * 1, cy + 7, cx + dir * 7, cy + 6);
        g2.setColor(inRageMode ? new Color(255, 75, 20) : new Color(80, 42, 34));
        g2.fillRect(cx + dir * 3, cy + 9, 3, 2);

        // 6. Braceletes gravados e elos das correntes
        g2.setColor(new Color(130, 87, 34));
        g2.fillRoundRect(cx - 20, cy + 22, 7, 17, 3, 3);
        g2.fillRoundRect(cx + 13, cy + 22, 7, 17, 3, 3);
        g2.setColor(new Color(220, 171, 75));
        g2.drawLine(cx - 19, cy + 24, cx - 14, cy + 24);
        g2.drawLine(cx - 19, cy + 28, cx - 14, cy + 28);
        g2.drawLine(cx + 14, cy + 24, cx + 19, cy + 24);
        g2.drawLine(cx + 14, cy + 28, cx + 19, cy + 28);
        g2.setColor(new Color(67, 58, 51));
        g2.drawLine(cx - 19, cy + 33, cx - 14, cy + 35);
        g2.drawLine(cx + 14, cy + 35, cx + 19, cy + 33);

        // 7. Renderização das Ações Especiais (Ataques / Escudo)
        if (blocking) {
            renderSpartanShield(g2, cx + dir * 18, cy + 18);
        } else if (attackState > 0) {
            renderBladesOfChaosAttack(g2, cx, cy, dir);
        } else {
            // Lâminas guardadas nas costas
            renderBladesOnBack(g2, cx, cy, dir);
        }

        g2.dispose();
    }

    private void renderBladesOnBack(Graphics2D g, int cx, int cy, int dir) {
        g.setColor(new Color(74, 48, 34));
        g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(cx - 8, cy + 30, cx + 3, cy + 12);
        g.drawLine(cx + 8, cy + 30, cx - 3, cy + 12);
        g.setColor(new Color(190, 133, 49));
        g.setStroke(new BasicStroke(2));
        g.drawLine(cx - 8, cy + 29, cx + 3, cy + 11);
        g.drawLine(cx + 8, cy + 29, cx - 3, cy + 11);
        g.setColor(new Color(192, 194, 193));
        g.drawLine(cx + 3, cy + 11, cx + 11, cy + 4);
        g.drawLine(cx - 3, cy + 11, cx - 11, cy + 4);
        g.setColor(new Color(235, 225, 197));
        g.drawLine(cx + 5, cy + 9, cx + 10, cy + 5);
        g.drawLine(cx - 5, cy + 9, cx - 10, cy + 5);
    }

    private void renderSpartanShield(Graphics2D g, int sx, int sy) {
        // Escudo redondo grego (Aspis)
        g.setColor(new Color(175, 125, 35));
        g.fillOval(sx - 18, sy, 36, 36);
        g.setColor(new Color(103, 68, 28));
        g.drawOval(sx - 18, sy, 36, 36);
        g.setColor(new Color(225, 178, 78));
        g.drawOval(sx - 14, sy + 4, 28, 28);
        g.fillOval(sx - 4, sy + 14, 8, 8);

        // Letra grega Lambda (Λ) gravada em vermelho
        g.setColor(new Color(180, 20, 20));
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(sx - 8, sy + 26, sx, sy + 10);
        g.drawLine(sx, sy + 10, sx + 8, sy + 26);
        g.setColor(new Color(239, 191, 91));
        g.setStroke(new BasicStroke(1));
        g.drawLine(sx - 13, sy + 18, sx - 10, sy + 18);
        g.drawLine(sx + 10, sy + 18, sx + 13, sy + 18);
    }

    private void renderBladesOfChaosAttack(Graphics2D g, int cx, int cy, int dir) {
        // Corrente articulada e lamina curva em movimento
        g.setColor(new Color(56, 49, 43));
        g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int reachX = cx + dir * (attackState == 4 ? 75 : 65);
        int reachY = cy + 22;
        int handX = cx + dir * 15;
        int handY = cy + 27;
        g.drawLine(handX, handY, reachX, reachY);
        g.setColor(new Color(222, 151, 51));
        g.setStroke(new BasicStroke(2));
        for (int link = 1; link < 5; link++) {
            int linkX = handX + (reachX - handX) * link / 5;
            int linkY = handY + (reachY - handY) * link / 5;
            g.drawOval(linkX - 2, linkY - 2, 4, 4);
        }

        int[] bladeX = {reachX - dir * 3, reachX + dir * 7, reachX + dir * 21,
                        reachX + dir * 17, reachX + dir * 10, reachX + dir * 6};
        int[] bladeY = {reachY - 4, reachY - 12, reachY - 8,
                        reachY + 1, reachY + 4, reachY + 12};
        g.setColor(new Color(193, 195, 190));
        g.fillPolygon(bladeX, bladeY, bladeX.length);
        g.setColor(new Color(83, 75, 66));
        g.setStroke(new BasicStroke(1.5f));
        g.drawPolygon(bladeX, bladeY, bladeX.length);
        g.setColor(new Color(255, 100, 24));
        g.setStroke(new BasicStroke(2));
        g.drawLine(reachX + dir * 2, reachY - 7, reachX + dir * 15, reachY - 6);
        g.setColor(new Color(238, 170, 60));
        g.fillOval(handX - 4, handY - 4, 8, 8);
    }

    private void renderKratosFallen(Graphics2D g) {
        g.setColor(new Color(190, 190, 195));
        g.fillRect((int) x, (int) y + height - 20, height, 20);
        g.setColor(new Color(180, 20, 20));
        g.drawLine((int) x + 10, (int) y + height - 12, (int) x + 35, (int) y + height - 12);
    }

    // Getters e Setters com encapsulamento
    public int getRage() { return rage; }
    public int getMaxRage() { return maxRage; }
    public boolean isInRageMode() { return inRageMode; }
    public ComboTracker getComboTracker() { return comboTracker; }
    public boolean isBlocking() { return blocking; }
}

