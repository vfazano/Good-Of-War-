# 📊 Quadro Kanban & Cards de Tarefas (Padrão Ágil / SENAI)

Este documento documenta o fluxo de trabalho ágil (Kanban / Scrum) aplicado ao desenvolvimento de **Good of War**, detalhando colunas, cards no formato de **User Stories** com critérios de aceitação BDD (Gherkin), priorização **MoSCoW** e estimativa em **Story Points** (Sequência de Fibonacci).

---

## 1. Visão Geral do Board Kanban

```
+----------------+----------------+----------------+-------------------+----------------+
|    BACKLOG     |     TO DO      |  IN PROGRESS   | CODE REVIEW / QA  |      DONE      |
+----------------+----------------+----------------+-------------------+----------------+
| • CARD-10      | • CARD-08      | • CARD-07      | • CARD-05         | • CARD-01 (Loop)|
|   (Menu Pause) |   (SpartanBoss)|   (Audio Syn.) |   (Spartan Rage)  | • CARD-02 (OOP)|
| • CARD-11      | • CARD-09      |                | • CARD-06         | • CARD-03 (Move)|
|   (Save State) |   (Cenário)    |                |   (Spartan Brute) | • CARD-04 (Comb)|
+----------------+----------------+----------------+-------------------+----------------+
```

---

## 2. Detalhamento dos Cards (User Stories)

### 🎴 CARD-01: Game Loop e Janela Gráfica 60 FPS
- **Prioridade:** Must Have (Crítico)
- **Story Points:** 5
- **Responsável:** Engenharia Core
- **História de Usuário:**
  > *Como* jogador,
  > *eu quero* uma taxa de quadros estável a 60 FPS com renderização suave sem flicker,
  > *para que* meus comandos e a ação na tela sejam fluidos e responsivos.
- **Critérios de Aceite (Gherkin):**
  - **Dado** que a aplicação é iniciada,
  - **Quando** o `GameLoop` for executado em sua `Thread`,
  - **Então** ele deve calcular o delta time e manter o loop cravado em 60 frames por segundo com Double Buffering.
- **Definição de Pronto (DoD):** Janela abre em 960x540 (ou proporcional), renderizando a tela de fundo sem travamentos.

---

### 🎴 CARD-02: Arquitetura POO Base (Abstração e Encapsulamento)
- **Prioridade:** Must Have (Crítico - Requisito SENAI)
- **Story Points:** 8
- **Responsável:** Arquiteto de Software
- **História de Usuário:**
  > *Como* desenvolvedor do time,
  > *eu quero* classes abstratas (`Entity`, `Enemy`) e interfaces (`Combatant`),
  > *para que* qualquer nova criatura ou objeto possa ser adicionado sem duplicar código de física ou colisão.
- **Critérios de Aceite:**
  - **Dado** a criação da classe abstrata `Entity`,
  - **Quando** atributos como vida, posição e velocidade forem manipulados,
  - **Então** eles devem ser acessados apenas através de métodos getters e setters encapsulados com validação.
  - **E** todas as entidades devem implementar `update(GameContext)` e `render(Graphics2D)`.
- **Definição de Pronto (DoD):** Diagrama e código validado com 0 violações de encapsulamento.

---

### 🎴 CARD-03: Movimentação Física e Pulo de Kratos
- **Prioridade:** Must Have
- **Story Points:** 5
- **Responsável:** Gameplay Dev
- **História de Usuário:**
  > *Como* jogador controlando Kratos,
  > *eu quero* andar para a esquerda/direita com aceleração e pular com gravidade,
  > *para que* eu navegue pelas ruínas de Esparta com precisão.
- **Critérios de Aceite:**
  - **Dado** Kratos no chão,
  - **Quando** eu pressionar `W` ou `Barra de Espaço`,
  - **Então** ele deve receber um impulso vertical negativo (`vy = -13`) e descer com aceleração de gravidade (`+0.65`).
  - **E** não permitir pulo duplo infinito a menos que toque no solo.

---

### 🎴 CARD-04: Combate com as Lâminas do Caos (Blades of Chaos)
- **Prioridade:** Must Have
- **Story Points:** 8
- **Responsável:** Gameplay Dev
- **História de Usuário:**
  > *Como* Kratos,
  > *eu quero* desferir sequências de ataques rápidos com correntes em chamas e ataques em área,
  > *para que* eu elimine as hordas inimigas com estilo hack-and-slash.
- **Critérios de Aceite:**
  - **Dado** o botão `J` pressionado repetidamente em intervalos adequados,
  - **Quando** Kratos estiver perto de inimigos,
  - **Então** deve encadear combo de até 3 golpes (Corte Horizontal, Corte Cruzado e Golpe Finalizador Descendente).
  - **E** pressionar `K` deve acionar o golpe pesado *Cyclone of Chaos* com dano em 360° e efeito de fogo.

---

### 🎴 CARD-05: Mecânica de Fúria Espartana (Spartan Rage)
- **Prioridade:** Should Have
- **Story Points:** 5
- **Responsável:** Gameplay Dev
- **História de Usuário:**
  > *Como* Kratos,
  > *eu quero* acumular uma barra de fúria ao acertar golpes nos inimigos e ativá-la quando cheia,
  > *para que* eu entre em modo berserk temporário com dano dobrado e invulnerabilidade parcial.
- **Critérios de Aceite:**
  - **Dado** a barra de fúria com 100 pontos,
  - **Quando** o jogador pressionar `E` ou `I`,
  - **Então** Kratos ganha aura flamejante vermelha intensa, velocidade aumentada em 40% e bônus de dano de 100% por 8 segundos.

---

### 🎴 CARD-06: Inimigo Legionário e Inimigo Bruto (Polimorfismo)
- **Prioridade:** Must Have
- **Story Points:** 8
- **Responsável:** IA Dev
- **História de Usuário:**
  > *Como* jogador,
  > *eu quero* enfrentar inimigos com diferentes tamanhos, ataques e comportamentos,
  > *para que* o combate seja desafiador e dinâmico.
- **Critérios de Aceite:**
  - **Dado** a lista genérica `List<Enemy> enemies`,
  - **Quando** a iteração de atualização for chamada,
  - **Então** o `SpartanLegionnaire` deve se mover rapidamente e dar estocadas com lança,
  - **E** o `SpartanBrute` deve andar lentamente, absorver mais dano e esmagar com martelo pesado.
- **Definição de Pronto (DoD):** Ambos herdam de `Enemy` e sobrepõem polimorficamente `updateAI()` e `render()`.

---

### 🎴 CARD-07: Chefe de Fase (Spartan Boss: General de Esparta)
- **Prioridade:** Should Have
- **Story Points:** 8
- **Responsável:** Gameplay / Boss Dev
- **História de Usuário:**
  > *Como* jogador no final do setor,
  > *eu quero* enfrentar um chefe com barra de vida exclusiva no topo e fases de combate,
  > *para que* haja um clímax recompensador na batalha.
- **Critérios de Aceite:**
  - **Dado** Kratos entrando na arena do chefe,
  - **Quando** o chefe perde 50% de vida,
  - **Então** ele entra na Fase 2 (Enrage), atacando mais rápido com ondas de choque no chão.

---

### 🎴 CARD-08: Efeitos Visuais, Partículas e Áudio Sintetizado
- **Prioridade:** Should Have
- **Story Points:** 5
- **Responsável:** Art & Audio Dev
- **História de Usuário:**
  > *Como* jogador,
  > *eu quero* ver faíscas, sangue, rastro de chamas e ouvir o som de cada golpe,
  > *para que* o impacto dos golpes seja satisfatório e visceral.
- **Critérios de Aceite:**
  - **Dado** qualquer ataque acertado,
  - **Quando** a hitbox do golpe interceptar a hurtbox do inimigo,
  - **Então** emitir partículas de impacto, números de dano flutuantes e som de corte gerado dinamicamente via Java Sound API.

