# 🔱 Fluxo de Trabalho Gitflow — God of War: Sons of Sparta

Este documento descreve a metodologia e a política de versionamento **Gitflow** adotada no desenvolvimento do projeto acadêmico do SENAI **God of War: Sons of Sparta**.

---

## 1. Visão Geral do Gitflow

O Gitflow é um modelo de ramificação (branching model) robusto e previsível, projetado para gerenciar lançamentos contínuos e o ciclo de vida do software. Ele define papéis específicos para diferentes branches e como elas devem interagir.

```
 [main]    ------●-----------------------------● (v1.0.0) ------● (v1.0.1 hotfix)
                  \                           /                 /
[hotfix]           \                         /         ●-------/ (hotfix/fix-audio)
                    \                       /         /
[release]            \             ●-------● (rc1)---/
                      \           /
[develop]  ---●--------●---------●-----------------●
               \      / \       /
[feature]       ●----●   ●-----● (feature/combat-system)
            (feature/player-movement)
```

---

## 2. Estrutura das Branches

### 2.1. Branches Principais (Longo Prazo / Infinitas)

| Branch | Propósito | Nível de Estabilidade | Permissão de Push Direto |
| :--- | :--- | :--- | :--- |
| `main` | Código pronto para produção (versões estáveis, tagueadas). | Máxima estabilidade. | **Bloqueado** (Somente via Pull Request de `release/*` ou `hotfix/*`). |
| `develop` | Branch de integração do desenvolvimento diário. Contém as últimas features prontas. | Alta (sempre compilável). | **Bloqueado** (Somente via Pull Request de `feature/*`). |

---

### 2.2. Branches de Suporte (Temporárias)

#### A. Feature Branches (`feature/*`)
- **Origem:** `develop`
- **Destino final:** `develop`
- **Convenção de nomenclatura:** `feature/<identificador-da-issue>-<nome-curto>`
  - Exemplo: `feature/SPARTA-12-blades-of-chaos-combos`
  - Exemplo: `feature/SPARTA-05-player-physics-movement`
- **Ciclo de vida:** Criada para desenvolver uma funcionalidade isolada; destruída após merge via Pull Request.

**Comandos Git:**
```bash
# 1. Atualizar develop local
git checkout develop
git pull origin develop

# 2. Criar e entrar na branch da feature
git checkout -b feature/SPARTA-12-blades-of-chaos-combos

# 3. Desenvolver e comitar
git add .
git commit -m "feat(combat): implement 3-hit combo sequence for Blades of Chaos"

# 4. Publicar a branch e abrir PR
git push -u origin feature/SPARTA-12-blades-of-chaos-combos
```

#### B. Release Branches (`release/*`)
- **Origem:** `develop`
- **Destino final:** `main` e `develop`
- **Convenção de nomenclatura:** `release/v<major>.<minor>.<patch>`
  - Exemplo: `release/v1.0.0`
- **Finalidade:** Preparação final para entrega (ajustes de documentação, numeração de versão, pequenos polimentos). Não recebe novas features.

**Comandos Git:**
```bash
# 1. Iniciar release
git checkout develop
git checkout -b release/v1.0.0

# 2. Ajustes finais (README, versão, metadados)
git commit -m "chore(release): bump version to 1.0.0 and finalize docs"

# 3. Merge na main com tag
git checkout main
git merge --no-ff release/v1.0.0
git tag -a v1.0.0 -m "Release v1.0.0 - Versao de Apresentacao SENAI"

# 4. Sincronizar de volta na develop
git checkout develop
git merge --no-ff release/v1.0.0

# 5. Excluir a branch temporária
git branch -d release/v1.0.0
```

#### C. Hotfix Branches (`hotfix/*`)
- **Origem:** `main`
- **Destino final:** `main` e `develop`
- **Convenção de nomenclatura:** `hotfix/<identificador>-<nome-do-bug>`
  - Exemplo: `hotfix/SPARTA-99-crash-on-boss-defeat`
- **Finalidade:** Correções críticas emergenciais detectadas na branch de produção (`main`).

**Comandos Git:**
```bash
# 1. Criar a partir de main
git checkout main
git checkout -b hotfix/SPARTA-99-crash-on-boss-defeat

# 2. Corrigir o bug
git commit -m "fix(boss): resolve NullPointerException on SpartanBoss death state"

# 3. Integrar na main com nova tag patch
git checkout main
git merge --no-ff hotfix/SPARTA-99-crash-on-boss-defeat
git tag -a v1.0.1 -m "Hotfix v1.0.1 - Fix boss death NPE"

# 4. Integrar a correção também na develop
git checkout develop
git merge --no-ff hotfix/SPARTA-99-crash-on-boss-defeat

# 5. Excluir branch de hotfix
git branch -d hotfix/SPARTA-99-crash-on-boss-defeat
```

---

## 3. Políticas de Pull Request (Code Review)

1. **Revisão Obrigatória:** Todo Pull Request deve ser aprovado por pelo menos 1 membro da equipe antes do merge.
2. **Build e Testes Verificados:** O código deve compilar 100% sem erros (`javac`) e rodar sem quebras de execução.
3. **Padrão de POO Respeitado:** Verificar se a nova classe implementa adequadamente Encapsulamento, Herança, Polimorfismo e Abstração.
4. **Sem commits de "WIP" ou "fix do fix":** Histórico limpo com mensagens no padrão Conventional Commits.

