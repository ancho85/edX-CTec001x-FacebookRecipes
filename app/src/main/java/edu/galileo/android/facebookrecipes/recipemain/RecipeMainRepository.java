package edu.galileo.android.facebookrecipes.recipemain;

import edu.galileo.android.facebookrecipes.entities.Recipe;

/**
 * Created by carlos.gomez on 24/06/2016.
 */
public interface RecipeMainRepository {
    //constantes relacionadas con el API
    public final static int COUNT = 1;
    public final static String RECENT_SORT = "r";
    public final static int RECIPE_RANGE = 100000;

    void getNextRecipe();
    void saveRecipe(Recipe recipe);
    void setRecipePage(int recipePage);
}
