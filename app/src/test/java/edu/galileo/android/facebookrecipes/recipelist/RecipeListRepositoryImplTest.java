package edu.galileo.android.facebookrecipes.recipelist;

import com.raizlabs.android.dbflow.sql.language.Select;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.FacebookRecipesApp;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.entities.Recipe_Table;
import edu.galileo.android.facebookrecipes.libs.base.EventBus;
import edu.galileo.android.facebookrecipes.recipelist.events.RecipeListEvent;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.mockito.Mockito.verify;

/**
 * Created by carlos.gomez on 06/07/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RecipeListRepositoryImplTest extends BaseTest {
    @Mock
    private EventBus eventBus;

    private FacebookRecipesApp app; //acceso a la base de datos, no puede ser un mock porque la base está encerrada
    private RecipeListRepositoryImpl repository; //sujeto de pruebas
    private ArgumentCaptor<RecipeListEvent> recipeListEventArgumentCaptor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        repository = new RecipeListRepositoryImpl(eventBus);
        app = (FacebookRecipesApp) RuntimeEnvironment.application;
        recipeListEventArgumentCaptor = ArgumentCaptor.forClass(RecipeListEvent.class);
        app.onCreate(); //creación de app y de base de datos
    }


    @After
    public void tearDown() throws Exception {
        app.onTerminate(); // para que no quede un leak de la base de datos
    }

    @Test
    public void testGetSavedRecipes_eventPosted() throws Exception {
        int recipesToStore = 5;
        Recipe currentRecipe;
        List<Recipe> testRecipeList = new ArrayList<>();
        for (int i = 0; i < recipesToStore; i++) {
            currentRecipe = new Recipe();
            currentRecipe.setRecipeId("id " + i);
            currentRecipe.save();
            testRecipeList.add(currentRecipe);
        }

        // robolectric y flow manager no funcionan bien juntos, problema de cache
        // se utiliza un select de forma directa y se guarda los resultados en una variable
        List<Recipe> recipesFromDB = new Select()
                                            .from(Recipe.class)
                                            .queryList();

        repository.getSavedRecipes();
        verify(eventBus).post(recipeListEventArgumentCaptor.capture());
        RecipeListEvent event = recipeListEventArgumentCaptor.getValue();

        assertEquals(RecipeListEvent.READ_EVENT, event.getType());
        assertEquals(recipesFromDB, event.getRecipeList()); // la consulta previa y el listado del evento deberían de ser iguales

        // house keeping para que no quede en la base de datos
        for (Recipe recipe: testRecipeList){
            recipe.delete();
        }
    }

    @Test
    public void testUpdateRecipe() throws Exception {
        String newRecipeId = "id1";
        String titleBefore = "title before update";
        String titleAfter = "title after update";
        Recipe recipe = new Recipe();
        recipe.setRecipeId(newRecipeId);
        recipe.setTitle(titleBefore);
        recipe.save();
        recipe.setTitle(titleAfter);

        repository.updateRecipe(recipe);

        Recipe recipeFromDB = new Select()
                .from(Recipe.class)
                .where(Recipe_Table.recipeId.is(newRecipeId)) // Recipe_Table es una clase autogenerada por DBFlow que contiene todos los atributos del POJO
                .querySingle();

        assertEquals(titleAfter, recipeFromDB.getTitle());
        verify(eventBus).post(recipeListEventArgumentCaptor.capture());

        RecipeListEvent event = recipeListEventArgumentCaptor.getValue();
        assertEquals(RecipeListEvent.UPDATE_EVENT, event.getType());

        recipe.delete();
    }

    @Test
    public void testRemoveRecipe() throws Exception {
        int recipes_in_delete_event = 1;
        String newRecipeId = "id1";
        Recipe recipe = new Recipe();
        recipe.setRecipeId(newRecipeId);
        recipe.save();

        repository.removeRecipe(recipe);

        assertFalse(recipe.exists());
        verify(eventBus).post(recipeListEventArgumentCaptor.capture());

        RecipeListEvent event = recipeListEventArgumentCaptor.getValue();
        assertEquals(RecipeListEvent.DELETE_EVENT, event.getType());
        assertEquals(recipes_in_delete_event, event.getRecipeList().size());
        assertEquals(recipe, event.getRecipeList().get(0));

        recipe.delete();
    }
}
