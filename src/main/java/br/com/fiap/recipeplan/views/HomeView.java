package br.com.fiap.recipeplan.views;

import br.com.fiap.recipeplan.model.Recipe;
import br.com.fiap.recipeplan.service.RecipeService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route("")
public class HomeView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(HomeView.class);

    private final RecipeService recipeService;
    private final VerticalLayout responsePanel = new VerticalLayout();

    public HomeView(RecipeService recipeService) {
        this.recipeService = recipeService;

        var input = new MessageInput();
        input.setWidthFull();
        input.addSubmitListener(event -> {
            var request = event.getValue();
            if (request == null || request.isBlank()) {
                return;
            }
            try {
                showRecipe(recipeService.generateRecipe(request));
            } catch (Exception e) {
                log.error("Falha ao gerar a receita", e);
                var notification = Notification.show("Não foi possível gerar a receita. Tente novamente.");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        add(new H1("Planejador de Receitas"));
        add(new Paragraph("O que você gostaria de cozinhar hoje?"));
        add(input);
    }

    private void showRecipe(Recipe recipe) {
        responsePanel.removeAll();
        responsePanel.add(new RecipeView(recipe));
        add(responsePanel);
    }

}
