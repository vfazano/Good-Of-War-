# 📜 Padrão de Commits — Conventional Commits (SENAI)

Este projeto adota a convenção **Conventional Commits** (versão 1.0.0), garantindo um histórico semântico, legível, padronizado e auditável para toda a equipe e para a banca avaliadora do SENAI.

---

## 1. Estrutura da Mensagem de Commit

```
<tipo>(<escopo opcional>): <descrição no imperativo e minúscula>

[corpo opcional explicando o motivo da mudança e contexto]

[rodapé opcional com referências a issues ou breaking changes]
```

---

## 2. Tipos Permitidos (`type`)

| Tipo | Significado | Exemplo Aplicado ao Jogo |
| :--- | :--- | :--- |
| `feat` | Uma nova funcionalidade/feature | `feat(player): add dodge roll with invincibility frames` |
| `fix` | Correção de um bug | `fix(collision): prevent player from clipping through floor platforms` |
| `docs` | Alterações exclusivas na documentação | `docs(gitflow): add branching diagram and rules` |
| `style` | Formatação de código (espaçamento, ponto e vírgula, sem alterar lógica) | `style(enemy): standardize indentation and brace positions` |
| `refactor` | Refatoração que não corrige bug nem adiciona funcionalidade | `refactor(entities): extract abstract base class Entity for polymorphism` |
| `perf` | Melhoria de desempenho/performance | `perf(particles): reuse particle pool to reduce garbage collection load` |
| `test` | Adição ou correção de testes unitários/manuais | `test(combat): add tests for damage calculation and armor reduction` |
| `chore` | Tarefas de manutenção, scripts de build, configs de IDE | `chore(build): create compile and run batch scripts` |
| `ci` | Mudanças em pipelines de Integração Contínua | `ci(github-actions): configure automatic compilation check on push` |

---

## 3. Escopos Recomendados para o Projeto (`scope`)

- `player`: Mecânicas, física, sprites e controles de Kratos.
- `combat`: Lâminas do Caos, hitboxes, cálculo de dano, combos e Spartan Rage.
- `enemy`: IA e comportamento de inimigos (Legionários, Brutos, Harpias e Chefe).
- `engine`: Game loop, câmera, renderização (`Graphics2D`) e input manager.
- `audio`: Sintetizador de áudio procedimental em Java e efeitos sonoros.
- `ui`: HUD (barra de vida, fúria, contador de combos) e telas de menu/vitória/game over.
- `world`: Plataformas, cenário do templo de Esparta, tochas e colisões.
- `oop`: Implementação dos pilares de POO (Herança, Polimorfismo, etc.).

---

## 4. Regras e Boas Práticas

1. **Tempo verbal imperativo:** Escreva como se estivesse dando uma ordem.
   - ✅ `feat(combat): implement Spartan Rage activation`
   - ❌ `feat(combat): implemented Spartan Rage activation`
   - ❌ `feat(combat): implementing Spartan Rage activation`
2. **Sem ponto final no título:** Mantenha a primeira linha com no máximo 72 caracteres.
3. **Explique o "Porquê" no corpo:** O diff do Git mostra *o que* mudou; o corpo do commit deve explicar *o motivo*.
4. **Referencie a Issue no rodapé:** Exemplo: `Closes #14` ou `Ref: SPARTA-08`.

---

## 5. Exemplos Reais de Commits do Projeto

### Exemplo 1: Nova Funcionalidade
```text
feat(combat): add heavy attack Cyclone of Chaos with area knockback

Implement key 'K' action triggering a dual-blade 360-degree sweep.
Applies fire particles, camera shake and 35 points of physical damage
to all enemies within a 90px radius.

Closes #12
```

### Exemplo 2: Correção de Bug
```text
fix(boss): clamp health value to prevent negative life bar overflow

When receiving fatal damage from a combo finisher, the SpartanBoss health
was dropping below zero, causing a visual glitch in the UI health bar.
Now encapsulated within setHealth() method.

Fixes #27
```

### Exemplo 3: Refatoração para POO (Polimorfismo e Herança)
```text
refactor(entities): apply polymorphism on Enemy class hierarchy

Refactor SpartanLegionnaire, SpartanBrute, and SpartanBoss to override
the abstract updateAI() and render() methods inherited from Enemy.
Enables GamePanel to iterate over a generic List<Enemy> seamlessly.

Relates to #18
```

