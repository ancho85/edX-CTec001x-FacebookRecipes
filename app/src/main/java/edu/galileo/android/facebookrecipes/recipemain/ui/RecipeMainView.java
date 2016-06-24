package edu.galileo.android.facebookrecipes.recipemain.ui;

import edu.galileo.android.facebookrecipes.entities.Recipe;

/**
 * Created by carlos.gomez on 24/06/2016.
 */
public interface RecipeMainView {
    void showProgress();
    void hideProgress();
    void showUIElements();
    void hideUIElements();
    void saveAnimation();
    void dismissAnimation();

    void onRecipeSaved();

    void setRecipe(Recipe recipe);
    void onGetRecipeError(String error);
}
