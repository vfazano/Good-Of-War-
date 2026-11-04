# Tecnologias Utilizadas no Good of War

## 1. Visao Geral

Good of War e um jogo de acao 2D desenvolvido em Java. A aplicacao usa as bibliotecas que acompanham o JDK para criar a janela, desenhar os elementos e receber controles, alem de uma dependencia externa pequena para reproduzir os arquivos MP3. A logica do jogo, as entidades, os cenarios e os efeitos visuais foram implementados no proprio projeto.

O projeto nao utiliza uma engine de jogos, como Unity, Godot ou libGDX, nem um framework de interface externo. Isso deixa visiveis no codigo responsabilidades como o loop principal, a fisica simples, a deteccao de colisao, a camera e a renderizacao.

## 2. Linguagem e Plataforma

### Java

Java e a linguagem usada em todo o codigo da aplicacao. As classes estao organizadas em pacotes sob `com.sparta.game`, separados por responsabilidade: nucleo, combate, entidades, efeitos, interface, mundo e documentacao de POO.

O codigo foi estruturado para Java 8 ou superior. A compilacao usa `javac`, com codificacao UTF-8, e os arquivos `.class` sao gerados na pasta `bin`.

### JDK e JVM

O JDK (Java Development Kit) fornece as ferramentas de desenvolvimento, principalmente `javac`, que compila os arquivos `.java`. A JVM (Java Virtual Machine) executa os arquivos `.class` gerados.

Os scripts `compile.bat` e `run.bat` automatizam as operacoes no Windows. O primeiro compila o projeto e adiciona o JAR do decoder MP3 ao classpath. O segundo inicia a classe principal usando os arquivos compilados e a mesma dependencia.

## 3. Janela e Interface: Java Swing

O Swing e a biblioteca de interface grafica incluida no Java. No projeto, ele e usado para:

- Criar a janela principal com `JFrame` em `Main.java`.
- Exibir o jogo em um painel `JPanel` por meio de `GamePanel.java`.
- Receber eventos de teclado associados ao painel.
- Solicitar a atualizacao visual usando `repaint()`.

A janela e criada dentro de `SwingUtilities.invokeLater`, que agenda a construcao da interface na Event Dispatch Thread (EDT), a thread usada pelo Swing para operacoes de interface. O painel tem tamanho de referencia de 960 por 540 pixels e usa desenho duplo para reduzir tremulacao durante a animacao.

## 4. Desenho 2D: Java AWT e Graphics2D

AWT e a biblioteca grafica de baixo nivel do Java; `Graphics2D` e a API de desenho usada pelo jogo. O metodo `paintComponent` de `GamePanel` coordena a composicao dos quadros. As classes de entidade e de mundo desenham seus proprios elementos usando formas, linhas, cores e gradientes.

Essa abordagem e chamada de renderizacao procedural: os personagens e cenarios sao construidos por instrucoes de desenho, sem depender de sprites externos para os elementos do jogo. Exemplos incluem:

- Kratos, sua tatuagem, armadura, escudo e Laminas do Caos em `Player.java`.
- Legionarios, brutos e chefe nas classes de `entities`.
- Plataformas, colunas, tochas e baus nas classes de `world`.
- Barras de vida, XP, furia e telas de estado nas classes de `ui`.
- Sangue, fogo, faiscas e textos flutuantes nas classes de `effects`.

O codigo ativa antialiasing para suavizar formas e texto. A camera aplica uma translacao ao contexto grafico para desenhar o mundo em coordenadas de jogo; depois essa translacao e desfeita para renderizar a HUD fixa na tela.

## 5. Ciclo de Jogo e Concorrencia

O loop principal e implementado pelo projeto em `GameLoop.java`, usando uma `Thread` dedicada. O alvo e 60 quadros por segundo, o que corresponde a aproximadamente 16,67 milissegundos por quadro. A cada ciclo, o loop:

1. Atualiza o estado do jogo por meio de `GamePanel.update()`.
2. Solicita um novo desenho com `GamePanel.repaint()`.
3. Dorme pelo tempo restante do ciclo, quando ha tempo disponivel.

O Swing executa efetivamente o desenho na EDT quando processa a solicitacao de `repaint()`. A logica de atualizacao roda na thread propria do loop. Essa separacao permite que a simulacao continue avancando sem executar todas as operacoes de jogo dentro da thread de interface.

O audio tambem roda em uma thread dedicada para a musica e em um pool de threads para efeitos sintetizados. Isso evita que a decodificacao ou abertura de um som seja feita diretamente dentro do ciclo de atualizacao.

## 6. Entrada de Teclado

A classe `InputManager` implementa `KeyListener` e traduz eventos do teclado em estados que o jogo consulta durante a atualizacao. Algumas acoes sao mantidas enquanto a tecla permanece pressionada, como movimento e bloqueio. Outras usam um pulso de uma atualizacao, como pulo, ataques, esquiva, interacao, pausa e escolha de melhoria.

Essa separacao evita que uma tecla de acao pontual seja interpretada como pressionada repetidamente em todos os quadros enquanto o jogador a mantem apertada.

## 7. Logica de Jogo e Colisoes

As regras do jogo foram implementadas em Java, sem motor fisico externo. `Entity` concentra estado e comportamento compartilhado, como posicao, velocidade, vida, gravidade e colisao com plataformas. `Player` e os inimigos especializam esses comportamentos.

O combate usa hitboxes retangulares: os ataques criam instancias de `Hitbox`, que sao atualizadas no loop e comparadas com as areas de colisao das entidades. Ao ocorrer uma colisao, o sistema aplica dano, recuo, invulnerabilidade temporaria, efeitos visuais e recompensas de XP quando um inimigo e derrotado.

A camera acompanha o jogador com interpolacao e pode receber tremores de tela para reforcar impactos. A progressao entre arenas e controlada pelo estado atual do jogo e pela quantidade de inimigos ainda vivos.

## 8. Organizacao Orientada a Objetos

A organizacao do codigo aplica os quatro conceitos de POO trabalhados no projeto:

- **Abstracao:** `Entity`, `Enemy` e `Particle` definem contratos e comportamento comum sem representar, por si so, uma entidade concreta de jogo.
- **Encapsulamento:** os estados de jogador, inimigo, audio e mundo sao mantidos em classes responsaveis por suas regras e expostos por metodos quando necessario.
- **Heranca:** `Player` e os inimigos derivam de `Entity`; tipos concretos de inimigo derivam de `Enemy`; efeitos concretos derivam de `Particle`.
- **Polimorfismo:** o loop pode percorrer colecoes de `Enemy` ou `Particle` e chamar `update`/`render` sem precisar conhecer todos os detalhes internos das subclasses.

As classes de combate tambem incluem tipos de ataque, hitboxes e informacoes de dano, mantendo parte das regras de combate separada das classes que desenham o mundo.

## 9. Audio

### MP3 com JLayer

Os arquivos `music/musicmenu.mp3` e `music/musicagameplay.mp3` sao decodificados pela biblioteca JLayer, localizada em `lib/jlayer-1.0.1.jar`. O Java Sound padrao nao garante a leitura de MP3 em todas as instalacoes, por isso o projeto inclui esse decoder no classpath.

`GamePanel` escolhe a faixa de acordo com o estado: a musica do menu toca em `MENU`; a faixa de gameplay toca nos demais estados, incluindo pausa, subida de nivel, transicao de arena e telas de resultado. Cada faixa e repetida em loop. A faixa atual nao e reiniciada em todos os quadros: `SoundManager` verifica se o caminho solicitado ja corresponde a faixa em reproducao e so troca de arquivo quando necessario.

### Efeitos sintetizados

Os efeitos de combate sao sintetizados pelo proprio `SoundManager` com `javax.sound.sampled`. O codigo cria buffers PCM para sons curtos como corte, impacto, bloqueio, pulo, dano e ativacao da Furia Espartana. Portanto, os efeitos nao dependem de arquivos de audio separados.

O impacto de um ataque do jogador contra um inimigo usa o efeito MP3 `music/espada.mp3`, reproduzido uma vez pelo JLayer em uma thread de audio. O som de Kratos recebendo dano continua sendo sintetizado.

## 10. Dependencias e Recursos Externos

| Recurso | Uso | Local |
| --- | --- | --- |
| JDK 8+ | Compilacao e execucao do jogo | Instalacao do Java |
| JLayer 1.0.1 | Decodificacao das musicas MP3 | `lib/jlayer-1.0.1.jar` |
| Musica do menu | Trilha em loop no menu inicial | `music/musicmenu.mp3` |
| Musica de gameplay | Trilha em loop durante a campanha e demais estados fora do menu | `music/musicagameplay.mp3` |
| Efeito de espada | Som unico quando o ataque do jogador acerta um inimigo | `music/espada.mp3` |

Nao ha dependencias de Maven ou Gradle configuradas. O classpath e montado pelos scripts `.bat` ou pelos comandos manuais documentados no README.

## 11. Compilar e Executar

No Windows, a forma recomendada e usar os scripts da raiz do projeto:

```bat
compile.bat
run.bat
```

A compilacao manual equivalente, executada na raiz do repositorio, e:

```bat
if not exist bin mkdir bin
javac -encoding UTF-8 -cp "lib\jlayer-1.0.1.jar" -d bin src\com\sparta\game\combat\*.java src\com\sparta\game\core\*.java src\com\sparta\game\effects\*.java src\com\sparta\game\entities\*.java src\com\sparta\game\oop\*.java src\com\sparta\game\ui\*.java src\com\sparta\game\world\*.java src\com\sparta\game\Main.java
java -cp "bin;lib\jlayer-1.0.1.jar" com.sparta.game.Main
```

Na configuracao de uma IDE, `src` deve ser marcado como raiz de codigo-fonte e `lib/jlayer-1.0.1.jar` adicionado ao classpath do projeto.

## 12. Limites Tecnicos Atuais

- O loop tenta manter 60 FPS com pausas por tempo, mas nao implementa um acumulador de tempo com atualizacao fixa para recuperar quadros atrasados.
- A fisica e a colisao sao simples e especificas para este jogo; nao usam um motor fisico geral.
- O progresso de XP, nivel e melhorias fica em memoria durante a execucao. Nao ha salvamento em arquivo entre partidas.
- A renderizacao procedural facilita modificar formas diretamente no codigo, mas nao oferece o fluxo de animacao e troca de sprites de uma engine ou ferramenta de arte.
- As musicas sao carregadas a partir de caminhos relativos a pasta raiz. Para que sejam encontradas, inicie o programa a partir da raiz do projeto ou ajuste o diretorio de trabalho da IDE.

## 13. Resumo da Arquitetura

```text
Main
  -> Swing JFrame / GamePanel
       -> GameLoop (atualizacao e solicitacao de desenho)
       -> InputManager (teclado)
       -> GameContext (dependencias da simulacao)
       -> Level / Entity / Enemy / Hitbox / Particle
       -> HUD / MenuOverlay (Graphics2D)
       -> SoundManager
            -> JLayer (MP3 de musica)
            -> Java Sound (efeitos PCM)
```

A arquitetura combina bibliotecas padrao do Java para janela, entrada, desenho e efeitos sonoros com codigo proprio para as regras de jogo e JLayer para os arquivos MP3.
