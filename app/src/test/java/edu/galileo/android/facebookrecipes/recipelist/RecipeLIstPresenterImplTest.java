package edu.galileo.android.facebookrecipes.recipelist;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.libs.base.EventBus;
import edu.galileo.android.facebookrecipes.recipelist.ui.RecipeListView;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Created by carlos.gomez on 06/07/2016.
 */
public class RecipeListPresenterImplTest extends BaseTest{
    @Mock
    private EventBus eventBus;
    @Mock
    private RecipeListView view;
    @Mock
    private RecipeListInteractor listInteractor;
    @Mock
    private StoredRecipesInteractor storedRecipesInteractor;

    private RecipeListPresenterImpl presenter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        presenter = new RecipeListPresenterImpl(eventBus, view, listInteractor, storedRecipesInteractor);
    }

    @Test
    public void testToggleFavorite_True() throws Exception {
        Recipe recipe = new Recipe();
        boolean favorite = true;
        recipe.setFavorite(favorite);

        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        presenter.toggleFavorite(recipe);
        verify(storedRecipesInteractor).executeUpdate(recipeArgumentCaptor.capture());

        assertEquals(!favorite, recipeArgumentCaptor.getValue().getFavorite());

    }

    @Test
    public void testToggleFavorite_False() throws Exception {
        Recipe recipe = new Recipe();
        boolean favorite = false;
        recipe.setFavorite(favorite);

        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        presenter.toggleFavorite(recipe);
        verify(storedRecipesInteractor).executeUpdate(recipeArgumentCaptor.capture());

        assertEquals(!favorite, recipeArgumentCaptor.getValue().getFavorite());

    }
}
