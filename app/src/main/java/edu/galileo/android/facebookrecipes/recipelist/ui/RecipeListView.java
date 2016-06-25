package edu.galileo.android.facebookrecipes.recipelist.ui;

import java.util.List;

import edu.galileo.android.facebookrecipes.entities.Recipe;

public interface RecipeListView {
    void setRecipes(List<Recipe> data);
    void recipeUpdated();
    void recipeDeleted(Recipe recipe);
}
