package edu.galileo.android.facebookrecipes.recipemain.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.galileo.android.facebookrecipes.api.RecipeClient;
import edu.galileo.android.facebookrecipes.api.RecipeService;
import edu.galileo.android.facebookrecipes.libs.base.EventBus;
import edu.galileo.android.facebookrecipes.recipemain.GetNextRecipeInteractor;
import edu.galileo.android.facebookrecipes.recipemain.GetNextRecipeInteractorImpl;
import edu.galileo.android.facebookrecipes.recipemain.RecipeMainPresenter;
import edu.galileo.android.facebookrecipes.recipemain.RecipeMainPresenterImpl;
import edu.galileo.android.facebookrecipes.recipemain.RecipeMainRepository;
import edu.galileo.android.facebookrecipes.recipemain.RecipeMainRepositoryImpl;
import edu.galileo.android.facebookrecipes.recipemain.SaveRecipeInteractor;
import edu.galileo.android.facebookrecipes.recipemain.SaveRecipeInteractorImpl;
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
    RecipeMainView providesRecipeMainView() {
        return this.view;
    }

    @Provides
    @Singleton
    RecipeMainPresenter providesRecipeMainPresenter(EventBus eventBus, RecipeMainView view, SaveRecipeInteractor saveInteractor, GetNextRecipeInteractor getNextInteractor) {
        return new RecipeMainPresenterImpl(eventBus, view, saveInteractor, getNextInteractor);
    }

    @Provides
    @Singleton
    SaveRecipeInteractor providesSaveRecipeInteractor(RecipeMainRepository repository) {
        return new SaveRecipeInteractorImpl(repository);
    }

    @Provides
    @Singleton
    GetNextRecipeInteractor providesGetNextRecipeInteractor(RecipeMainRepository repository) {
        return new GetNextRecipeInteractorImpl(repository);
    }

    @Provides
    @Singleton
    RecipeMainRepository providesRecipeMainRepository(EventBus eventBus, RecipeService service) {
        return new RecipeMainRepositoryImpl(eventBus, service);
    }

    //EventBus ya es proveído en las librerías
    //falta proveer el servicio que tiene que  ver con retrofit, esto podría estar también entre las librerías
    //pero como no lo estoy usuando en otro módulo que no sea en éste, se lo coloca aquí.
    @Provides
    @Singleton
    RecipeService providesRecipeService() {
        return new RecipeClient().getRecipeService();
    }

}
