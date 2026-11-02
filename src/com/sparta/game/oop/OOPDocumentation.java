package com.sparta.game.oop;

/**
 * ============================================================================
 * GUIA DE DEFESA TÉCNICA DOS 4 PILARES DA POO NO PROJETO (SENAI)
 * ============================================================================
 * Esta classe serve como mapa de navegação acadêmica para os professores
 * e avaliadores do SENAI, apontando exatamente onde cada pilar foi aplicado.
 */
public final class OOPDocumentation {

    private OOPDocumentation() {
        // Classe utilitária estática para documentação
    }

    /**
     * 1. ABSTRAÇÃO:
     * - Onde encontrar:
     *   * Interface Combatant: Contrato puro para qualquer entidade que luta e sofre dano.
     *   * Classe Abstrata Entity: Modela as características fundamentais (x, y, vx, vy, vida)
     *     e declara os métodos abstratos update(GameContext) e render(Graphics2D).
     *   * Classe Abstrata Enemy: Define a abstração de inteligência artificial através
     *     do método updateAI(Player, GameContext).
     *   * Classe Abstrata Particle: Abstrai a lógica de renderização de efeitos temporários.
     */
    public static final String ABSTRACAO = "Interfaces e Classes Abstratas isolando regras de negócio.";

    /**
     * 2. ENCAPSULAMENTO:
     * - Onde encontrar:
     *   * Atributos 'private' e 'protected' em Entity, Player, Enemy, GamePanel.
     *   * Getters e Setters com regras de negócio em Entity.java:
     *     setHealth(int) trava a vida entre 0 e maxHealth e dispara onDeath() automaticamente.
     *   * Value Object imutável DamageInfo: atributos 'final' e apenas leitura, impedindo
     *     adulteração dos valores de dano durante o tráfego do golpe.
     */
    public static final String ENCAPSULAMENTO = "Proteção de estado interno com validação de dados.";

    /**
     * 3. HERANÇA:
     * - Onde encontrar:
     *   * Player extends Entity (Kratos herda física, gravidade, colisão de chão e vida).
     *   * Enemy extends Entity (Superclasse para todos os adversários).
     *   * SpartanLegionnaire extends Enemy.
     *   * SpartanBrute extends Enemy.
     *   * SpartanBoss extends Enemy.
     *   * FireParticle, BloodParticle, SparkParticle, FloatingText extends Particle.
     */
    public static final String HERANCA = "Reuso de código e hierarquia consistente entre entidades.";

    /**
     * 4. POLIMORFISMO:
     * - Onde encontrar:
     *   * Coleção polimórfica List<Enemy> no GamePanel: o loop itera chamando enemy.updateEnemy()
     *     e enemy.render(), e cada classe derivada (Legionário, Bruto, Boss) executa sua
     *     própria IA e desenho em tempo de execução (dynamic dispatch).
     *   * Sobrescrita (@Override) de takeDamage() no SpartanBrute: reduz 35% do dano devido à
     *     sua armadura pesada e ignora recuo leve (hyper-armor), enquanto o Legionário sofre dano integral.
     *   * Sobrescrita (@Override) de takeDamage() no Player: bloqueia com escudo caso o botão
     *     de guarda esteja pressionado.
     */
    public static final String POLIMORFISMO = "Sobrescrita de métodos e coleções genéricas com comportamento polimórfico.";
}

