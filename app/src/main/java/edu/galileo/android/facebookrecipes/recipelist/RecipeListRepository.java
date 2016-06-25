package edu.galileo.android.facebookrecipes.recipelist;

import edu.galileo.android.facebookrecipes.entities.Recipe;

public interface RecipeListRepository {
    void getSavedRecipes();
    void updateRecipe(Recipe recipe);
    void removeRecipe(Recipe recipe);
}
