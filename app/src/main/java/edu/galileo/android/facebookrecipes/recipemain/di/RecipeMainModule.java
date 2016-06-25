package edu.galileo.android.facebookrecipes.recipemain.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.galileo.android.facebookrecipes.recipemain.ui.RecipeMainView;

/**
 * Created by carlos.gomez on 25/06/2016.
 */
@Module
public class RecipeMainModule {
    RecipeMainView view;

    public RecipeMainModule(RecipeMainView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    RecipeMainView providesRecipeMainView(){
        return this.view;
    }

}
