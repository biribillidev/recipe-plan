# Recipe Plan — Java Advanced

Planejador de receitas com IA: o usuário digita o que quer cozinhar e o app mostra a receita completa
(nome, descrição, ingredientes e modo de preparo). Feito com Spring Boot, Vaadin e Spring AI, a partir do
repositório base do professor: <https://github.com/joaocarloslima/recipe-plan>

## Integrantes

| Nome | RM |
|------|----|
| _preencher_ | _preencher_ |

## O que foi implementado (tarefas do enunciado)

| # | Tarefa | Onde |
|---|--------|------|
| 1 | Clonar o repositório | Código-base do professor (telas `HomeView` e `RecipeView` e modelos `Recipe` e `Ingredient` mantidos) |
| 2 | Configurar o acesso à API da IA | `src/main/resources/application.properties` |
| 3 | Service que pede a receita à IA e devolve `Recipe` | `src/main/java/br/com/fiap/recipeplan/service/RecipeService.java` |
| 4 | `MessageInput` chama o service e mostra o resultado no `responsePanel` | `src/main/java/br/com/fiap/recipeplan/views/HomeView.java` |

- **`RecipeService`**: usa o `ChatClient` criado a partir do `ChatClient.Builder`, com uma *system message* de chef
  (resposta em português) e converte a resposta do modelo direto para `Recipe` com `.entity(Recipe.class)`.
  Pedidos que não são sobre culinária recebem uma resposta educada, sem inventar receita.
- **`HomeView`**: o `addSubmitListener` do `MessageInput` chama `recipeService.generateRecipe(...)` e passa o
  resultado para `showRecipe`. Se a IA falhar, aparece uma notificação de erro.

## Como executar

Pré-requisitos: JDK 17 ou superior (o Gradle baixa o 17 sozinho se faltar) e uma chave de API.

### 1. Chave da IA (nunca vai para o repositório)

O projeto usa por padrão o **Groq** (gratuito, API compatível com a da OpenAI), modelo `openai/gpt-oss-120b`.
Crie uma chave em <https://console.groq.com> e informe de **uma** destas formas:

- **Arquivo local (mais simples):** copie `local.properties.example` para `local.properties` e preencha
  `OPENAI_API_KEY`. Esse arquivo está no `.gitignore`.
- **Variável de ambiente:** `OPENAI_API_KEY=sua-chave`
  - PowerShell: `$env:OPENAI_API_KEY = "sua-chave"`
  - IntelliJ: *Run → Edit Configurations → Environment variables*

Para usar a OpenAI, defina também `OPENAI_BASE_URL=https://api.openai.com` e `OPENAI_MODEL=gpt-4o-mini`.

### 2. Rodar

```bash
./gradlew bootRun
```

No Windows (PowerShell): `.\gradlew bootRun`. A primeira execução demora alguns minutos (o Vaadin prepara o
front-end). O navegador abre em <http://localhost:8080>.

### 3. Testar

1. Digite `ovo frito` no campo e clique em **Send** (ou Enter).
   **Esperado:** em alguns segundos aparecem o nome da receita, a descrição, a lista de ingredientes
   (por exemplo `2 unidade de ovo`) e as instruções.
2. Digite `quem ganhou a copa de 2002`.
   **Esperado:** a receita "Não consegui criar essa receita", dizendo que o app só cria receitas.
3. Sem chave configurada, o app abre normalmente e, ao enviar, mostra a notificação de erro
   (o motivo aparece no console).

### Testes automatizados

```bash
./gradlew test
```

`RecipeServiceTest` confirma que o service devolve o `Recipe` produzido pelo modelo e
`RecipeplanApplicationTests` confirma que o contexto Spring sobe.
