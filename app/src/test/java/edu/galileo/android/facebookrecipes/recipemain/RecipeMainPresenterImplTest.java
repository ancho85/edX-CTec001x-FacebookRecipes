package edu.galileo.android.facebookrecipes.recipemain;

import org.junit.Test;
import org.mockito.Mock;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.libs.base.EventBus;
import edu.galileo.android.facebookrecipes.recipemain.ui.RecipeMainView;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.verify;

/**
 * Created by carlos.gomez on 06/07/2016.
 */
public class RecipeMainPresenterImplTest extends BaseTest {

    //mocks de cada uno de los atributos de la clase a ser testeada
    @Mock
    private EventBus eventBus;
    @Mock
    private RecipeMainView view;
    @Mock
    private SaveRecipeInteractor saveInteractor;
    @Mock
    private GetNextRecipeInteractor getNextInteractor;
    @Mock
    Recipe recipe = new Recipe();

    private RecipeMainPresenterImpl presenter; // este sera objecto de pruebas

    @Override
    public void setUp() throws Exception {
        super.setUp();
        presenter = new RecipeMainPresenterImpl(eventBus, view, saveInteractor, getNextInteractor);
    }

    @Test
    public void testOnCreate_subscribedToEventBus() throws Exception {
        presenter.onCreate();
        verify(eventBus).register(presenter); // verifique que register fue invocado con ese parámetro
    }

    @Test
    public void testOnDestroy_unsubscribedToEventBus() throws Exception {
        presenter.onDestroy();
        verify(eventBus).unregister(presenter); // verifique que unregister fue invocado con ese parámetro
        assertNull(presenter.getView()); // se verifica que la vista también sea nula
    }

    @Test
    public void testSaveRecipe_executeTestInteractor() throws Exception {
        presenter.saveRecipe(recipe);
        assertNotNull(presenter.getView());  // la vista no debe ser nula
        verify(view).saveAnimation(); // se hace animación
        verify(view).hideUIElements();  // se oculta elementos
        verify(view).showProgress(); // se muestra el progreso
        verify(saveInteractor).execute(recipe); // se ejecutó con parámetro recipe
    }

    @Test
    public void testDismissRecipe_executeGetNextRecipeInteractor() throws Exception {
        presenter.dismissRecipe();
        assertNotNull(presenter.getView());
        verify(view).dismissAnimation();
    }

    @Test
    public void testGetNextRecipe_executeGextNextRecipeInteractor() throws Exception {
        presenter.getNextRecipe();
        assertNotNull(presenter.getView());
        verify(view).hideUIElements();
        verify(view).showProgress();
        verify(getNextInteractor).execute();
    }
}