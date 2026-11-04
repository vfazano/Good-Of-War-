# 📋 Catálogo e Padrão de Issues — Good of War

Este documento apresenta o padrão de abertura de **Issues** (tarefas/problemas no GitHub/GitLab/Jira) e a listagem real das issues criadas para a entrega do projeto no SENAI.

---

## 1. Padrão de Templates de Issue

### Template: Feature (Nova Funcionalidade)
```markdown
---
name: Feature Request
about: Propor uma nova mecânica ou elemento para o jogo
title: '[FEAT] '
labels: 'enhancement, feature'
assignees: ''
---

### 🎯 Descrição
Descreva de forma clara e objetiva o que deve ser desenvolvido.

### 🎮 Comportamento Esperado
- [ ] O jogador deve ser capaz de...
- [ ] A tela deve exibir...
- [ ] O cálculo deve considerar...

### 🧩 Relação com POO
- **Classe afetada:** Ex: `Player.java`, `Combatant.java`
- **Pilar aplicado:** (Herança / Polimorfismo / Encapsulamento / Abstração)

### 📌 Critérios de Aceite
- [ ] Código compilando sem warnings críticos.
- [ ] Cobertura de teste manual com feedback visual e sonoro.
```

---

## 2. Backlog Oficial de Issues do Projeto

### 🏷️ EPIC 01 — Fundamentos da Engine & Arquitetura POO
- **Issue #01**: `[FEAT] Criação do Game Loop a 60 FPS com Double Buffering`
  - **Descrição:** Desenvolver o núcleo de execução do jogo (`GameLoop.java`) utilizando `Runnable` e `Thread`, garantindo 60 quadros estáveis por segundo e prevenindo flicker com `BufferStrategy` / `Graphics2D`.
  - **Pilar:** Abstração e Encapsulamento.
  - **Status:** Concluído.

- **Issue #02**: `[REFACTOR] Modelagem da Hierarquia de Entidades e Interfaces`
  - **Descrição:** Criar a classe abstrata `Entity` e a interface `Combatant` com métodos protegidos e atributos encapsulados.
  - **Pilar:** Abstração, Herança e Polimorfismo.
  - **Status:** Concluído.

---

### 🏷️ EPIC 02 — Protagonista: Kratos (Ghost of Sparta)
- **Issue #03**: `[FEAT] Sistema de Movimentação Física e Plataforma`
  - **Descrição:** Implementar aceleração horizontal, desaceleração com atrito, gravidade, salto e detecção de solo para Kratos (`Player.java`).
  - **Pilar:** Encapsulamento de vetores de velocidade e posição.
  - **Status:** Concluído.

- **Issue #04**: `[FEAT] Sistema de Combate: Lâminas do Caos e Combo Tracker`
  - **Descrição:** Implementar ataque leve (combo de 3 golpes), ataque pesado em área (Cyclone of Chaos), esquiva tática (Spartan Roll com invulnerabilidade) e bloqueio com escudo.
  - **Pilar:** Polimorfismo na emissão de `Hitbox` e `DamageInfo`.
  - **Status:** Concluído.

- **Issue #05**: `[FEAT] Sistema de Spartan Rage (Fúria Espartana)`
  - **Descrição:** Acúmulo de barra de fúria ao golpear inimigos. Ao pressionar `E`/`I`, ativa aumento de velocidade, dano dobrado e aura de fogo.
  - **Pilar:** Encapsulamento e controle de estado do jogador.
  - **Status:** Concluído.

---

### 🏷️ EPIC 03 — Inimigos e Inteligência Artificial (IA)
- **Issue #06**: `[FEAT] Implementação do Inimigo Básico: Legionário Espartano`
  - **Descrição:** Criar `SpartanLegionnaire` herdando de `Enemy`. IA com patrulha, perseguição ao jogador e ataque com espada curta.
  - **Pilar:** Herança e Polimorfismo (`updateAI()`, `render()`).
  - **Status:** Concluído.

- **Issue #07**: `[FEAT] Inimigo Pesado: Bruto Espartano com Martelo`
  - **Descrição:** Criar `SpartanBrute`, inimigo com armadura pesada, alta vida e ataque pesado esmagador de solo.
  - **Pilar:** Polimorfismo com sobrescrita de cálculo de dano e resistência.
  - **Status:** Concluído.

- **Issue #08**: `[FEAT] Chefe de Fase: General de Esparta / Arauto da Guerra`
  - **Descrição:** Implementar `SpartanBoss` com barra de vida no topo da tela, duas fases de combate, investida de escudo e golpe de choque telúrico.
  - **Pilar:** Herança, Polimorfismo e Máquina de Estados.
  - **Status:** Concluído.

---

### 🏷️ EPIC 04 — Efeitos Audiovisuais, HUD & Polimento
- **Issue #09**: `[FEAT] Sistema de Partículas e Feedback Visual`
  - **Descrição:** Criar partículas dinâmicas de fogo, faíscas de lâminas, sangue de impacto e números de dano flutuantes (`FloatingText`).
  - **Pilar:** Herança de `Particle` com renderização polimórfica.
  - **Status:** Concluído.

- **Issue #10**: `[FEAT] Sintetizador de Áudio Procedural sem Dependências Externas`
  - **Descrição:** Criar gerador de ondas de som via `javax.sound.sampled` para emitir som de cortes de espada, impactos pesados, bloqueio metálico e rugido de fúria.
  - **Status:** Concluído.

- **Issue #11**: `[FEAT] HUD Estilizado com Tema Mitológico Grego`
  - **Descrição:** Barra de vida espartana (verde/vermelha), orbe de fúria, contador de combos com títulos de estilo ("BRUTAL", "SAVAGE", "GODLIKE").
  - **Status:** Concluído.

