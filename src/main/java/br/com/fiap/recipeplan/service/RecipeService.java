package br.com.fiap.recipeplan.service;

import br.com.fiap.recipeplan.model.Recipe;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    private static final String SYSTEM_MESSAGE = """
            Você é um chef de cozinha experiente e prestativo.
            Crie uma receita completa a partir do pedido do usuário, sempre em português do Brasil.
            Preencha todos os campos da receita:
            - name: nome da receita;
            - description: descrição curta e apetitosa, em uma ou duas frases;
            - ingredients: lista de ingredientes, cada um com name, quantity e unit.
              Use quantidades simples como "1", "1/4" ou "200", e unidades como "unidade",
              "colher de chá", "colher de sopa", "xícara", "g", "ml" ou "pitada";
            - instructions: modo de preparo em um texto corrido, com os passos em ordem.
            Se o pedido não tiver relação com culinária, não invente uma receita: use o name
            "Não consegui criar essa receita", explique educadamente na description que você só
            cria receitas, deixe ingredients vazio e instructions em branco.
            """;

    private final ChatClient chatClient;

    public RecipeService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem(SYSTEM_MESSAGE)
                .build();
    }

    /**
     * Pede uma receita à IA. A resposta do modelo é convertida diretamente para {@link Recipe}.
     */
    public Recipe generateRecipe(String request) {
        return chatClient.prompt()
                .user(request)
                .call()
                .entity(Recipe.class);
    }
}
