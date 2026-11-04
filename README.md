# ⚔️ God of War: Sons of Sparta — Jogo 2D em Java Puro

> **Projeto Acadêmico SENAI** — Disciplina: Programação Orientada a Objetos (POO)

Um jogo de ação hack-and-slash 2D side-scroller inspirado na franquia God of War, desenvolvido inteiramente em **Java puro** (Swing + AWT + Java Sound API), sem bibliotecas ou engines externas.

---

## 📸 Visão Geral

- **Protagonista:** Kratos, o Fantasma de Esparta, com as Lâminas do Caos
- **Cenário:** Templo de Esparta / Portões de Ares
- **Inimigos:** Legionários Espartanos, Brutos Pesados e o General de Esparta (Boss)
- **Mecânicas:** Combos, Esquiva, Bloqueio com Escudo, Fúria Espartana (Spartan Rage)
- **Gráficos:** 100% procedurais via `Graphics2D` (sem sprites externos)
- **Áudio:** Sintetizado em tempo real via `javax.sound.sampled`

---

## 🏛️ Os 4 Pilares da POO Implementados

| Pilar | Onde está no código |
| :--- | :--- |
| **Abstração** | Classe abstrata `Entity`, classe abstrata `Enemy`, classe abstrata `Particle`, interface `Combatant` |
| **Encapsulamento** | Atributos `private`/`protected` com getters/setters validados em `Entity`, `Player`; Value Object imutável `DamageInfo` |
| **Herança** | `Player extends Entity`, `SpartanLegionnaire/Brute/Boss extends Enemy extends Entity`, `Fire/Blood/SparkParticle extends Particle` |
| **Polimorfismo** | `List<Enemy>` com `@Override` de `updateAI()`, `render()`, `takeDamage()` — cada inimigo se comporta diferente no mesmo loop |

> 📄 Documentação completa: [`docs/OOP_CONCEPTS.md`](docs/OOP_CONCEPTS.md)

---

## 📁 Estrutura do Projeto

```
Jogo Senai/
├── src/
│   └── com/sparta/game/
│       ├── Main.java                    # Ponto de entrada
│       ├── combat/
│       │   ├── AttackType.java          # Enum de tipos de ataque
│       │   ├── ComboTracker.java        # Contador de combo + rank de estilo
│       │   ├── DamageInfo.java          # Value Object imutável de dano
│       │   └── Hitbox.java              # Caixa de colisão de ataque
│       ├── core/
│       │   ├── Camera.java              # Câmera com lerp + screen shake
│       │   ├── GameContext.java         # Context Object para subsistemas
│       │   ├── GameLoop.java            # Loop principal a 60 FPS
│       │   ├── GamePanel.java           # Painel de jogo (controlador)
│       │   ├── GameState.java           # Enum de estados do jogo
│       │   ├── InputManager.java        # Gerenciador de teclado
│       │   └── SoundManager.java        # Sintetizador de áudio procedural
│       ├── effects/
│       │   ├── Particle.java            # Classe abstrata base de partículas
│       │   ├── BloodParticle.java       # Respingo de sangue
│       │   ├── FireParticle.java        # Chama das Lâminas do Caos
│       │   ├── FloatingText.java        # Texto flutuante de dano
│       │   └── SparkParticle.java       # Faísca de metal/bloqueio
│       ├── entities/
│       │   ├── Combatant.java           # Interface de combate
│       │   ├── Entity.java              # Superclasse abstrata
│       │   ├── Player.java              # Kratos (Fantasma de Esparta)
│       │   ├── Enemy.java               # Superclasse abstrata de inimigos
│       │   ├── SpartanLegionnaire.java  # Soldado ágil com lança
│       │   ├── SpartanBrute.java        # Guerreiro pesado com martelo
│       │   └── SpartanBoss.java         # General de Esparta (Chefe)
│       ├── oop/
│       │   └── OOPDocumentation.java    # Mapa de referência POO
│       ├── ui/
│       │   ├── HUD.java                 # Interface visual (barras de vida, combo)
│       │   └── MenuOverlay.java         # Telas de Menu, Pause, Game Over, Vitória
│       └── world/
│           ├── Level.java               # Cenário do Templo de Esparta
│           ├── Platform.java            # Plataforma de pedra
│           └── Torch.java               # Tocha/Brazeiro com luz dinâmica
├── docs/
│   ├── GITFLOW.md                       # Metodologia Gitflow
│   ├── COMMIT_PATTERN.md               # Padrão Conventional Commits
│   ├── ISSUES.md                        # Catálogo de Issues
│   ├── KANBAN_CARDS.md                  # Cards ágeis (User Stories)
│   └── OOP_CONCEPTS.md                  # Defesa técnica dos 4 Pilares POO
├── compile.bat                          # Script de compilação
├── run.bat                              # Script de execução
├── .gitignore                           # Ignora binários e configs de IDE
└── README.md                            # Este arquivo
```

---

## 🎮 Controles

| Tecla | Ação |
| :--- | :--- |
| `A` / `D` ou `←` / `→` | Movimentar Kratos |
| `W` / `Espaço` | Salto Atlético |
| `J` ou `Z` | Ataque Leve (Combo de 3 golpes com as Lâminas do Caos) |
| `K` ou `X` | Cyclone of Chaos (Ataque pesado em área 360°) |
| `L` ou `C` | Esquiva Espartana (Invulnerabilidade temporária) |
| `U` ou `Shift` | Bloqueio com Escudo (Aspis Espartano) |
| `E` ou `I` | Ativar Fúria Espartana (quando a barra estiver cheia) |
| `P` ou `Esc` | Pausar / Retomar o Jogo |
| `R` ou `Enter` | Reiniciar (nas telas de Game Over ou Vitória) |

---

## 🚀 Como Compilar e Executar

### Pré-requisito
- **JDK 8+** (Java Development Kit) instalado com `javac` no PATH.

### Opção 1: Scripts `.bat` (Windows)
```bash
# Compilar
compile.bat

# Executar
run.bat
```

### Opção 2: Linha de Comando Manual
```bash
# Compilar todos os arquivos Java
mkdir bin
javac -encoding UTF-8 -d bin src\com\sparta\game\combat\*.java src\com\sparta\game\core\*.java src\com\sparta\game\effects\*.java src\com\sparta\game\entities\*.java src\com\sparta\game\oop\*.java src\com\sparta\game\ui\*.java src\com\sparta\game\world\*.java src\com\sparta\game\Main.java

# Executar o jogo
java -cp bin com.sparta.game.Main
```

### Opção 3: IDE (IntelliJ / Eclipse / VS Code)
1. Importe a pasta `src` como raiz de código-fonte.
2. Defina a classe `com.sparta.game.Main` como ponto de entrada.
3. Execute normalmente com Run/Debug.

---

## 📋 Documentação Complementar

| Documento | Conteúdo |
| :--- | :--- |
| [`docs/OOP_CONCEPTS.md`](docs/OOP_CONCEPTS.md) | Diagrama UML e defesa técnica dos 4 pilares da POO |
| [`docs/GITFLOW.md`](docs/GITFLOW.md) | Fluxo de trabalho com branches e Pull Requests |
| [`docs/COMMIT_PATTERN.md`](docs/COMMIT_PATTERN.md) | Padrão Conventional Commits com exemplos reais |
| [`docs/ISSUES.md`](docs/ISSUES.md) | Catálogo de Issues e template de abertura |
| [`docs/KANBAN_CARDS.md`](docs/KANBAN_CARDS.md) | Cards ágeis com User Stories e critérios de aceite |

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 8+
- **Gráficos:** Java AWT `Graphics2D` (renderização procedural 100%)
- **Janela:** Java Swing `JFrame` + `JPanel`
- **Áudio:** `javax.sound.sampled` (sintetização procedural de ondas sonoras)
- **Controle de Versão:** Git com Gitflow
- **Metodologia Ágil:** Kanban + User Stories

---

## 👥 Equipe

Projeto desenvolvido como requisito de avaliação da disciplina de **Programação Orientada a Objetos** do curso do **SENAI**.

---

*"Nenhum homem ou deus pode me dizer o que fazer!" — Kratos*

