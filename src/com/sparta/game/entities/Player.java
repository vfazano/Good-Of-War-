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
            for (int i = 0; i < 2; i++) {
                double px = x + Math.random() * width;
                double py = y + Math.random() * height;
                context.addParticle(new FireParticle(px, py, (Math.random() - 0.5) * 2, -Math.random() * 3, 25, 6f));
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
        attackTimer = 16;
        attackCooldown = 18;

        AttackType type = (nextCombo == 1) ? AttackType.LIGHT_SLASH_1 :
                          (nextCombo == 2) ? AttackType.LIGHT_SLASH_2 :
                                             AttackType.LIGHT_SLASH_3;

        int finalDamage = type.getBaseDamage();
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
        attackTimer = 26;
        attackCooldown = 32;

        int finalDamage = AttackType.HEAVY_CYCLONE.getBaseDamage();
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
        this.rageTimer = 480; // 8 segundos a 60 FPS
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

        // 1. Pernas / Grevas de Bronze
        g2.setColor(new Color(80, 50, 30)); // Couro e sandálias
        g2.fillRect(cx - 14, cy + 48, 10, 20);
        g2.fillRect(cx + 4, cy + 48, 10, 20);
        g2.setColor(new Color(180, 130, 40)); // Grevas de bronze
        g2.fillRect(cx - 13, cy + 54, 8, 12);
        g2.fillRect(cx + 5, cy + 54, 8, 12);

        // 2. Saia de Guerra Espartana (Pteruges)
        g2.setColor(new Color(150, 20, 20)); // Vermelho espartano
        g2.fillRect(cx - 16, cy + 38, 32, 14);
        g2.setColor(new Color(210, 160, 50)); // Cinto dourado
        g2.fillRect(cx - 17, cy + 36, 34, 4);

        // 3. Tronco Muscular (Pele pálida de Cinzas dos Mortos)
        g2.setColor(new Color(215, 215, 220));
        g2.fillRect(cx - 15, cy + 18, 30, 20);

        // 4. A Famosa Tatuagem Vermelha de Kratos (Ombro e Peito)
        g2.setColor(new Color(190, 25, 25));
        g2.setStroke(new BasicStroke(3));
        if (facingRight) {
            g2.drawLine(cx - 10, cy + 20, cx - 2, cy + 35);
            g2.drawLine(cx - 2, cy + 35, cx + 8, cy + 30);
        } else {
            g2.drawLine(cx + 10, cy + 20, cx + 2, cy + 35);
            g2.drawLine(cx + 2, cy + 35, cx - 8, cy + 30);
        }

        // 5. Cabeça de Kratos (Careca, Barba Espartana e Marca Vermelha)
        g2.setColor(new Color(215, 215, 220));
        g2.fillOval(cx - 11, cy + 2, 22, 18);

        // Barba espartana negra
        g2.setColor(new Color(25, 20, 20));
        g2.fillRect(cx - (dir > 0 ? 4 : 10), cy + 12, 14, 8);

        // Tatuagem no rosto
        g2.setColor(new Color(190, 25, 25));
        g2.fillRect(cx - (dir > 0 ? 8 : 4), cy + 4, 3, 10);

        // Olhos ferozes (brilham em vermelho na fúria)
        g2.setColor(inRageMode ? new Color(255, 50, 0) : Color.WHITE);
        g2.fillRect(cx + (dir > 0 ? 2 : -5), cy + 8, 3, 3);

        // 6. Braçadeiras com as Correntes do Caos fundidas na carne
        g2.setColor(new Color(190, 145, 45)); // Bronze
        g2.fillRect(cx - 19, cy + 22, 6, 16);
        g2.fillRect(cx + 13, cy + 22, 6, 16);
        g2.setColor(new Color(70, 70, 70)); // Elos de corrente
        g2.drawLine(cx - 19, cy + 26, cx - 13, cy + 28);
        g2.drawLine(cx + 13, cy + 26, cx + 19, cy + 28);

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
        g.setColor(new Color(170, 130, 40));
        g.drawLine(cx - 8, cy + 10, cx + 8, cy + 32);
        g.drawLine(cx + 8, cy + 10, cx - 8, cy + 32);
    }

    private void renderSpartanShield(Graphics2D g, int sx, int sy) {
        // Escudo redondo grego (Aspis)
        g.setColor(new Color(175, 125, 35));
        g.fillOval(sx - 18, sy, 36, 36);
        g.setColor(new Color(120, 80, 20));
        g.drawOval(sx - 18, sy, 36, 36);

        // Letra grega Lambda (Λ) gravada em vermelho
        g.setColor(new Color(180, 20, 20));
        g.setStroke(new BasicStroke(3));
        g.drawLine(sx - 8, sy + 26, sx, sy + 10);
        g.drawLine(sx, sy + 10, sx + 8, sy + 26);
    }

    private void renderBladesOfChaosAttack(Graphics2D g, int cx, int cy, int dir) {
        // Correntes flamejantes estendidas
        g.setColor(new Color(255, 150, 30));
        g.setStroke(new BasicStroke(3));

        int reachX = cx + dir * (attackState == 4 ? 75 : 65);
        int reachY = cy + 22;

        // Desenha curva de arco da corrente
        g.drawLine(cx + dir * 10, cy + 24, reachX, reachY);

        // Lâmina do Caos na ponta (Foice curva grega com lâmina dentada)
        g.setColor(new Color(220, 210, 190));
        int[] bx = {reachX, reachX + dir * 22, reachX + dir * 14};
        int[] by = {reachY - 10, reachY, reachY + 12};
        g.fillPolygon(bx, by, 3);

        // Borda dourada e gume incandescente
        g.setColor(new Color(255, 90, 10));
        g.drawPolygon(bx, by, 3);
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

