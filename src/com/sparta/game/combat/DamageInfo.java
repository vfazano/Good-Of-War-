package com.sparta.game.combat;

/**
 * ============================================================================
 * PILAR DA POO: ENCAPSULAMENTO & IMUTABILIDADE
 * ============================================================================
 * DamageInfo é um Value Object imutável. Todos os atributos são 'final'
 * e 'private', garantindo que o pacote de dados do golpe não seja alterado
 * durante o tráfego no pipeline de colisão e cálculo de dano.
 */
public final class DamageInfo {

    private final int amount;
    private final AttackType attackType;
    private final double knockbackX;
    private final double knockbackY;
    private final boolean breakBlock;
    private final boolean isFromPlayer;

    public DamageInfo(int amount, AttackType attackType, double knockbackX, double knockbackY, boolean breakBlock, boolean isFromPlayer) {
        this.amount = Math.max(0, amount);
        this.attackType = attackType;
        this.knockbackX = knockbackX;
        this.knockbackY = knockbackY;
        this.breakBlock = breakBlock;
        this.isFromPlayer = isFromPlayer;
    }

    public int getAmount() {
        return amount;
    }

    public AttackType getAttackType() {
        return attackType;
    }

    public double getKnockbackX() {
        return knockbackX;
    }

    public double getKnockbackY() {
        return knockbackY;
    }

    public boolean canBreakBlock() {
        return breakBlock;
    }

    public boolean isFromPlayer() {
        return isFromPlayer;
    }
}

