package edu.galileo.android.facebookrecipes.recipelist;

import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.StyleRes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.util.ActivityController;

import java.util.List;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.R;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.libs.base.ImageLoader;
import edu.galileo.android.facebookrecipes.login.ui.LoginActivity;
import edu.galileo.android.facebookrecipes.recipelist.ui.RecipeListActivity;
import edu.galileo.android.facebookrecipes.recipelist.ui.RecipeListView;
import edu.galileo.android.facebookrecipes.recipelist.ui.adapters.OnItemClickListener;
import edu.galileo.android.facebookrecipes.recipelist.ui.adapters.RecipesAdapter;
import edu.galileo.android.facebookrecipes.recipemain.ui.RecipeMainActivity;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

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
    private ImageLoader imageLoader;
    @Mock
    private RecipesAdapter adapter;
    @Mock
    private RecipeListPresenter presenter;

    private RecipeListView view;
    private RecipeListActivity activity;
    private OnItemClickListener onItemClickListener;
    private ShadowActivity shadowActivity;
    private ActivityController<RecipeListActivity> controller;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RecipeListActivity recipeListActivity = new RecipeListActivity() {

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
        activity = controller.get(); //sin type casting
        view = (RecipeListView) controller.get(); // type casting redundante, pero hay que definirlo
        onItemClickListener = (OnItemClickListener) activity; // type casting redundante, pero hay que definirlo

        shadowActivity = shadowOf(activity);
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

    @Test
    public void testLogoutMenuClicked_ShouldLaunchLoginActivity() throws Exception {
        shadowActivity.clickMenuItem(R.id.action_logout);  // simulación de click de menú logout
        Intent intentAfterLogout = shadowActivity.peekNextStartedActivity();  //obtener la actividad siguiente
        ComponentName loginComponent = new ComponentName(activity, LoginActivity.class);  //contexto y clase al ComponentName
        assertEquals(loginComponent, intentAfterLogout.getComponent());
    }

    @Test
    public void testMainMenuClicked_ShouldLaunchRecipeMainActivity() throws Exception {
        shadowActivity.clickMenuItem(R.id.action_main);  // simulación de click de menú lista
        Intent intentAfterLogout = shadowActivity.peekNextStartedActivity();  //obtener la actividad siguiente
        ComponentName loginComponent = new ComponentName(activity, RecipeMainActivity.class);  //contexto y clase al ComponentName
        assertEquals(loginComponent, intentAfterLogout.getComponent());
    }
}
