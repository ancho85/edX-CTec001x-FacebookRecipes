package edu.galileo.android.facebookrecipes.recipelist;

import android.support.annotation.StyleRes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.List;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.R;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.recipelist.ui.RecipeListActivity;
import edu.galileo.android.facebookrecipes.recipelist.ui.RecipeListView;
import edu.galileo.android.facebookrecipes.recipelist.ui.adapters.RecipesAdapter;

import static org.mockito.Mockito.verify;

/**
 * Created by carlos.gomez on 07/07/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RecipeListActivityTest extends BaseTest {
    @Mock
    private Recipe recipe;
    @Mock
    private List<Recipe> recipeList;
    @Mock
    private RecipesAdapter adapter;
    @Mock
    private RecipeListPresenter presenter;

    private RecipeListView view;
    private ActivityController<RecipeListActivity> controller;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RecipeListActivity recipeListActivity = new RecipeListActivity(){

            @Override
            public void setTheme(@StyleRes int resid) {
                //se sobrecarga para que use el tema noactionbar
                //robolectric asigna el tema en base al manifest, pero como se instancia la actividad con un new entonces hay que sobrecargar
                super.setTheme(R.style.AppTheme_NoActionBar);
            }

            public RecipesAdapter getAdapter() {
                return adapter;
            }

            public RecipeListPresenter getPresenter() {
                return presenter;
            }
        };

        controller = ActivityController.of(Robolectric.getShadowsAdapter(), recipeListActivity).create().visible();
        view = (RecipeListView) controller.get();
    }

    //se ignoran pruebas para verificar que el presentador llame al onCreate y onDestroy... ver otro test

    @Test
    public void testSetRecipes_shouldSetInAdapter() throws Exception {
        view.setRecipes(recipeList);
        verify(adapter).setRecipes(recipeList);
    }

    @Test
    public void testRecipeUpdated_shouldUpdateAdapter() throws Exception {
        view.setRecipes(recipeList);
        verify(adapter).notifyDataSetChanged();
    }

    @Test
    public void testRecipeDeleted_shouldUpdateAdapter() throws Exception {
        view.recipeDeleted(recipe);
        verify(adapter).removeRecipe(recipe);
    }
}
