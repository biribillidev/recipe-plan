package br.com.fiap.recipeplan;

import br.com.fiap.recipeplan.model.Ingredient;
import br.com.fiap.recipeplan.model.Recipe;
import br.com.fiap.recipeplan.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecipeServiceTest {

    @Test
    void returnsTheRecipeProducedByTheModel() {
        var builder = mock(ChatClient.Builder.class, RETURNS_DEEP_STUBS);
        var chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        when(builder.defaultSystem(org.mockito.ArgumentMatchers.anyString()).build()).thenReturn(chatClient);

        var expected = new Recipe();
        expected.setName("Ovo Frito Clássico");
        var egg = new Ingredient();
        egg.setName("Ovo");
        egg.setQuantity("1");
        egg.setUnit("unidade");
        expected.getIngredients().add(egg);
        when(chatClient.prompt().user("ovo frito").call().entity(Recipe.class)).thenReturn(expected);

        var recipe = new RecipeService(builder).generateRecipe("ovo frito");

        assertSame(expected, recipe);
        assertEquals("Ovo Frito Clássico", recipe.getName());
        assertEquals(1, recipe.getIngredients().size());
    }
}
