package edu.galileo.android.facebookrecipes.recipemain;

import org.junit.Test;
import org.mockito.Mock;

import edu.galileo.android.facebookrecipes.BaseTest;

import static org.mockito.Mockito.verify;

/**
 * Created by carlos.gomez on 06/07/2016.
 */
public class GetNextRecipeInteractorImplTest extends BaseTest {
    @Mock
    private RecipeMainRepository repository;
    private GetNextRecipeInteractor interactor; // versión genérica en definición

    @Override
    public void setUp() throws Exception {
        super.setUp();
        interactor = new GetNextRecipeInteractorImpl(repository); //versión implementación en instanciación
    }

    @Test
    public void testExecute_callRepository() throws Exception {
        interactor.execute();
        verify(repository).getNextRecipe();
    }
}
