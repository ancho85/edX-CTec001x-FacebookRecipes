package edu.galileo.android.facebookrecipes.recipelist;

import edu.galileo.android.facebookrecipes.entities.Recipe;

public interface StoredRecipesInteractor {
    void executeUpdate(Recipe recipe);
    void executeDelete(Recipe recipe);
}
