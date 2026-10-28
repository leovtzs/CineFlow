# 🎬 CineFlow

**CineFlow** é uma aplicação de streaming desenvolvida em Java, com interface gráfica construída utilizando JavaFX. O projeto simula uma plataforma de entretenimento digital, permitindo explorar conteúdos e organizar uma lista pessoal de filmes e séries.

O projeto foi desenvolvido como atividade acadêmica do curso de **Bacharelado em Sistemas de Informação do IFBA**, com foco na aplicação prática de conceitos de programação orientada a objetos e estruturas de dados.

## ✨ Funcionalidades

- 🔐 **Login e cadastro:** telas de acesso e criação de usuários.
- 🎥 **Catálogo de conteúdos:** visualização de filmes e séries.
- 🔎 **Pesquisa por título:** localização de conteúdos pelo nome.
- 🗂️ **Filtros por categoria:** separação entre filmes e séries.
- ❤️ **Lista de favoritos:** organização dos conteúdos favoritos.
- ⏳ **Assistir depois:** fila para organizar conteúdos que o usuário deseja assistir.
- ▶️ **Player:** acesso à interface de reprodução de conteúdo.
- 🖥️ **Interface gráfica:** experiência visual inspirada em plataformas modernas de streaming.

## 🛠️ Tecnologias utilizadas

- **Java 21:** linguagem de programação.
- **JavaFX:** desenvolvimento da interface gráfica.
- **Maven:** gerenciamento do projeto e das dependências.
- **Git e GitHub:** versionamento e hospedagem do código-fonte.

## 🧱 Estruturas de dados

Um dos objetivos do projeto é aplicar estruturas de dados na organização dos conteúdos e das funcionalidades da aplicação.

| Estrutura | Aplicação |
|---|---|
| Lista dinâmica | Armazenamento e gerenciamento de conteúdos e favoritos. |
| Fila | Organização dos conteúdos na lista de assistir depois. |
| Programação orientada a objetos | Modelagem de usuários, filmes, séries e demais componentes. |

As estruturas são organizadas em classes próprias, buscando separar responsabilidades e facilitar a manutenção do código.

## 📂 Organização do projeto

```text
CineFlow/
├── src/
│   └── main/
│       └── java/
│           └── br/edu/ifba/streaming/
│               ├── Main.java
│               ├── estrutura/
│               ├── model/
│               └── service/
├── pom.xml
└── README.md
```

*Observação: a estrutura acima representa a organização geral do projeto. Os diretórios e arquivos podem variar conforme a versão do código.*

## ⚙️ Como executar

### Pré-requisitos

- JDK 21 instalado.
- Maven instalado.
- Git instalado, caso deseje clonar o repositório.

### 1. Clone o repositório

```bash
git clone URL_DO_REPOSITORIO
```

### 2. Entre na pasta do projeto

```bash
cd CineFlow
```

### 3. Execute a aplicação

```bash
mvn clean javafx:run
```

O Maven irá preparar o projeto e iniciar a aplicação JavaFX.

## 🎯 Objetivos acadêmicos

O CineFlow foi desenvolvido para consolidar conhecimentos em:

- Programação orientada a objetos.
- Implementação e utilização de estruturas de dados.
- Desenvolvimento de interfaces gráficas com JavaFX.
- Organização de código em camadas e classes.
- Gerenciamento de dependências com Maven.
- Versionamento de código com Git.

## 👨‍💻 Desenvolvimento

Projeto acadêmico desenvolvido no curso de **Bacharelado em Sistemas de Informação do Instituto Federal da Bahia (IFBA)**.

---

**CineFlow**: seu próximo filme começa aqui. 🍿
