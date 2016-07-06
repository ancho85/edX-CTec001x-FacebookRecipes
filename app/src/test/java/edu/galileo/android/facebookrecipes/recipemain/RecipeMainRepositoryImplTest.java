package edu.galileo.android.facebookrecipes.recipemain;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.api.RecipeService;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.libs.base.EventBus;
import edu.galileo.android.facebookrecipes.recipemain.events.RecipeMainEvent;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by carlos.gomez on 06/07/2016.
 */
public class RecipeMainRepositoryImplTest extends BaseTest {
    @Mock
    private EventBus eventBus;
    @Mock
    private RecipeService service;
    @Mock
    private Recipe recipe;
    private RecipeMainRepository repository;
    private ArgumentCaptor<RecipeMainEvent> recipeMainEventArgumentCaptor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        repository = new RecipeMainRepositoryImpl(eventBus, service);
        //captura de argumentos para la clase RecipeMainEvent
        recipeMainEventArgumentCaptor = ArgumentCaptor.forClass(RecipeMainEvent.class);
    }

    @Test
    public void testSaveRecipeCalled_eventPosted() throws Exception {
        when(recipe.exists()).thenReturn(true);
        repository.saveRecipe(recipe);

        verify(eventBus).post(recipeMainEventArgumentCaptor.capture());  //captura del argumento
        RecipeMainEvent event = recipeMainEventArgumentCaptor.getValue();  // obtención del argumento

        assertEquals(RecipeMainEvent.SAVE_EVENT, event.getType());  // prueba del argumento
        assertNull(event.getError());
        assertNull(event.getRecipe());
    }
}
