package edu.galileo.android.facebookrecipes.recipemain;

import org.greenrobot.eventbus.Subscribe;

import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.libs.base.EventBus;
import edu.galileo.android.facebookrecipes.recipemain.events.RecipeMainEvent;
import edu.galileo.android.facebookrecipes.recipemain.ui.RecipeMainView;

/**
 * Created by carlos.gomez on 25/06/2016.
 */
public class RecipeMainPresenterImpl implements RecipeMainPresenter {
    private EventBus eventBus;
    private RecipeMainView view;
    private SaveRecipeInteractor saveInteractor;
    private GetNextRecipeInteractor getNextInteractor;

    public RecipeMainPresenterImpl(EventBus eventBus, RecipeMainView view, SaveRecipeInteractor saveInteractor, GetNextRecipeInteractor getNextInteractor) {
        this.eventBus = eventBus;
        this.view = view;
        this.saveInteractor = saveInteractor;
        this.getNextInteractor = getNextInteractor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public void dismissRecipe() {
        if (view != null) {
            view.dismissAnimation();
        }
        getNextRecipe();

    }

    @Override
    public void getNextRecipe() {
        if (view != null) {
            view.hideUIElements();
            view.showProgress();
        }
        getNextInteractor.execute();
    }

    @Override
    public void saveRecipe(Recipe recipe) {
        if (view != null) {
            view.saveAnimation();
            view.hideUIElements();
            view.showProgress();
        }
        saveInteractor.execute(recipe);
    }

    @Override
    @Subscribe
    public void onEventMainThread(RecipeMainEvent event) {
        if (view != null) {
            String error = event.getError();
            if (error != null) {
                view.hideProgress();
                view.onGetRecipeError(error);
            } else {
                if (event.getType() == RecipeMainEvent.NEXT_EVENT) {
                    view.setRecipe(event.getRecipe());
                } else if (event.getType() == RecipeMainEvent.SAVE_EVENT) {
                    view.onRecipeSaved();
                    getNextInteractor.execute();
                }

            }
        }
    }

    @Override
    public void imageReady() {
        if (view != null) {
            view.hideProgress();
            view.showUIElements();
        }
    }

    @Override
    public void imageError(String error) {
        if (view != null) {
            view.onGetRecipeError(error);
        }
    }

    @Override
    public RecipeMainView getView() {
        return this.view;
    }
}
