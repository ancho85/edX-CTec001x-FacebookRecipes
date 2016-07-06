package edu.galileo.android.facebookrecipes.recipemain;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.libs.base.ImageLoader;
import edu.galileo.android.facebookrecipes.recipemain.ui.RecipeMainActivity;
import edu.galileo.android.facebookrecipes.recipemain.ui.RecipeMainView;

/**
 * Created by carlos.gomez on 06/07/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RecipeMainActivityTest extends BaseTest {
    @Mock
    private RecipeMainPresenter presenter;
    @Mock
    private Recipe currentRecipe;
    @Mock
    private ImageLoader imageLoader;
    // private RecipeMainComponent component; // No hace falta porque es parte de la inyección de dependencias

    private RecipeMainActivity activity;
    private RecipeMainView view; // es la misma clase que la actividad, ya que la actividad la implementa, pero igual p/probar

    //Parte de robolectric, maneja elementos del ciclo de vida de la actividad
    private ActivityController<RecipeMainActivity> controller;  // se especifica la clase de la actividad

    @Override
    public void setUp() throws Exception {
        super.setUp();
        //se instancia la actividad sobrecargando los métodos que la inyección de dependencias inserta, se envía los mocks
        RecipeMainActivity recipeMainActivity = new RecipeMainActivity(){
            public ImageLoader getImageLoader() {
                return imageLoader;
            }

            public RecipeMainPresenter getPresenter() {
                return presenter;
            }
        };
        //roboelectric crea la actividad como shadow en función a todo lo que se envía como parámetro
        controller = ActivityController.of(Robolectric.getShadowsAdapter(), recipeMainActivity).create().visible(); //create y visible son métodos de robolectric
        activity = controller.get();
        view = (RecipeMainView) activity;
    }
}
