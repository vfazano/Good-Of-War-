package com.sparta.game.entities;

import com.sparta.game.combat.DamageInfo;
import java.awt.Rectangle;

/**
 * ============================================================================
 * PILAR DA POO: ABSTRAÇÃO
 * ============================================================================
 * Contrato puro que define as operações de combate que qualquer ser vivo
 * (seja Kratos, um legionário ou o chefe) deve disponibilizar para interações.
 */
public interface Combatant {

    /**
     * Aplica dano, calcula defesas e processa knockback.
     */
    void takeDamage(DamageInfo info);

    /**
     * Verifica se o combatente ainda possui pontos de vida ativos.
     */
    boolean isAlive();

    /**
     * Retorna a caixa de colisão física de dano (hurtbox).
     */
    Rectangle getHitbox();
}

