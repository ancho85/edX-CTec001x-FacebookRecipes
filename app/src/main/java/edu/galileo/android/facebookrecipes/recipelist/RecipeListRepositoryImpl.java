package edu.galileo.android.facebookrecipes.recipelist;

import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.Arrays;
import java.util.List;

import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.entities.Recipe_Table;
import edu.galileo.android.facebookrecipes.libs.base.EventBus;
import edu.galileo.android.facebookrecipes.recipelist.events.RecipeListEvent;

public class RecipeListRepositoryImpl implements RecipeListRepository {
    private EventBus eventBus;

    public RecipeListRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void getSavedRecipes() {
        FlowCursorList<Recipe> storedRecipes = new FlowCursorList<Recipe>(false, Recipe.class); // consulta a la base de datos
        post(RecipeListEvent.READ_EVENT, storedRecipes.getAll());
        storedRecipes.close();
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        recipe.update();
        post();
    }

    @Override
    public void removeRecipe(Recipe recipe) {
        recipe.delete();
        post(RecipeListEvent.DELETE_EVENT, Arrays.asList(recipe));
    }

    @Override
    public void getFavoritesRecipes() {
        //query con DBFlow
        //Recipe_Table es una clase autogenerada por DBFlow
        List<Recipe> recipes = new Select().from(Recipe.class).where(Recipe_Table.favorite.is(true)).queryList();
        post(RecipeListEvent.READ_EVENT, recipes);
    }

    private void post() {
        post(RecipeListEvent.UPDATE_EVENT, null);
    }

    private void post(int type, List<Recipe> recipeList) {
        RecipeListEvent event = new RecipeListEvent();
        event.setType(type);
        event.setRecipeList(recipeList);
        eventBus.post(event);
    }
}
