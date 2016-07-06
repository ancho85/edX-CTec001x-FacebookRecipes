package edu.galileo.android.facebookrecipes.recipemain;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.api.RecipeSearchResponse;
import edu.galileo.android.facebookrecipes.api.RecipeService;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.libs.base.EventBus;
import edu.galileo.android.facebookrecipes.recipemain.events.RecipeMainEvent;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
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

    @Test
    public void testGetNextRecipeCalled_APIServiceSuccessCall_EventPosted() throws Exception {
        int recipePage = new Random().nextInt(RecipeMainRepository.RECIPE_RANGE);
        when(service.search(
                BuildConfig.FOOD_API_KEY,
                RecipeMainRepository.RECENT_SORT,
                RecipeMainRepository.COUNT,
                recipePage)
        ).thenReturn(buildCall(true, null));

        repository.setRecipePage(recipePage);
        repository.getNextRecipe();

        verify(service).search(
                BuildConfig.FOOD_API_KEY,
                RecipeMainRepository.RECENT_SORT,
                RecipeMainRepository.COUNT,
                recipePage);
        verify(eventBus).post(recipeMainEventArgumentCaptor.capture());
        RecipeMainEvent event = recipeMainEventArgumentCaptor.getValue();
        assertEquals(RecipeMainEvent.NEXT_EVENT, event.getType());
        assertNull(event.getError());
        assertNotNull(event.getRecipe());
        assertEquals(recipe, event.getRecipe());
    }

    @Test
    public void testGetNextRecipeCalled_APIServiceFailedCall_EventPosted() throws Exception {

    }

    //método helper
    private Call<RecipeSearchResponse> buildCall(final boolean success, final String errorMsg) {
        Call<RecipeSearchResponse> response = new Call<RecipeSearchResponse>() {
            @Override
            public Response<RecipeSearchResponse> execute() throws IOException {
                Response<RecipeSearchResponse> result = null;
                if (success){
                    RecipeSearchResponse recipeSearchResponse = new RecipeSearchResponse();
                    recipeSearchResponse.setCount(1);
                    recipeSearchResponse.setRecipes(Arrays.asList(recipe));
                    result = Response.success(recipeSearchResponse);
                }else {
                    result = Response.error(null, null);
                }
                return result;
            }

            @Override
            public void enqueue(Callback<RecipeSearchResponse> callback) {
                if (success){
                    try {
                        callback.onResponse(this, execute());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    callback.onFailure(this, new Throwable(errorMsg));
                }
            }

            @Override
            public boolean isExecuted() {
                return true;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<RecipeSearchResponse> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };
        return response;
    }
}
