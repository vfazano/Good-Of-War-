package com.sparta.game.combat;

/**
 * Tipos de ataque disponíveis para o jogador e inimigos.
 * Utilizado para diferenciar o comportamento, partículas e efeitos de som.
 */
public enum AttackType {
    LIGHT_SLASH_1("Corte Rápido", 15, 4.0),
    LIGHT_SLASH_2("Corte Cruzado", 20, 5.0),
    LIGHT_SLASH_3("Finalizador Descendente", 35, 9.0),
    HEAVY_CYCLONE("Cyclone of Chaos", 45, 12.0),
    SPARTAN_RAGE_SLAM("Fúria de Ares", 70, 16.0),
    ENEMY_STRIKE("Golpe Inimigo", 12, 4.0),
    BRUTE_SMASH("Esmagamento Bruto", 28, 10.0),
    BOSS_SLASH("Corte Imperial", 25, 7.0),
    BOSS_SHOCKWAVE("Onda Telúrica", 35, 14.0);

    private final String displayName;
    private final int baseDamage;
    private final double baseKnockback;

    AttackType(String displayName, int baseDamage, double baseKnockback) {
        this.displayName = displayName;
        this.baseDamage = baseDamage;
        this.baseKnockback = baseKnockback;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public double getBaseKnockback() {
        return baseKnockback;
    }
}

