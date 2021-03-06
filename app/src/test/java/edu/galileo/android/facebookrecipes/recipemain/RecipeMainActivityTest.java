package edu.galileo.android.facebookrecipes.recipemain;

import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.internal.ShadowExtractor;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.util.ActivityController;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.R;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.libs.base.ImageLoader;
import edu.galileo.android.facebookrecipes.login.ui.LoginActivity;
import edu.galileo.android.facebookrecipes.recipelist.ui.RecipeListActivity;
import edu.galileo.android.facebookrecipes.recipemain.ui.RecipeMainActivity;
import edu.galileo.android.facebookrecipes.recipemain.ui.RecipeMainView;
import edu.galileo.android.facebookrecipes.support.ShadowImageView;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by carlos.gomez on 06/07/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, shadows ={ShadowImageView.class}) //robolectric ya tiene ShadowImageView, pero utilizo el de mi paquete support
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
    private ShadowActivity shadowActivity; // extiende la funcionalidad de la actividad

    @Override
    public void setUp() throws Exception {
        super.setUp();
        //se instancia la actividad sobrecargando los métodos que la inyección de dependencias inserta, se envía los mocks
        RecipeMainActivity recipeMainActivity = new RecipeMainActivity() {
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
        shadowActivity = shadowOf(activity); //robolectric construye a partir de la actividad real
    }

    @Test
    public void testOnActivityCreated_getNextRecipe() throws Exception {
        //la actividad ya está creada en el setUp con el controller
        verify(presenter).onCreate();
        verify(presenter).getNextRecipe();
    }

    @Test
    public void testOnActivityDestroyed_destroyPresenter() throws Exception {
        controller.destroy(); //elemento del ciclo de vida de la actividad
        verify(presenter).onDestroy();
    }

    @Test
    public void testLogoutMenuClicked_ShouldLaunchLoginActivity() throws Exception {
        shadowActivity.clickMenuItem(R.id.action_logout);  // simulación de click de menú logout
        Intent intentAfterLogout = shadowActivity.peekNextStartedActivity();  //obtener la actividad siguiente
        ComponentName loginComponent = new ComponentName(activity, LoginActivity.class);  //contexto y clase al ComponentName
        assertEquals(loginComponent, intentAfterLogout.getComponent());
    }

    @Test
    public void testListMenuClicked_ShouldLaunchRecipeListActivity() throws Exception {
        shadowActivity.clickMenuItem(R.id.action_list);  // simulación de click de menú lista
        Intent intentAfterLogout = shadowActivity.peekNextStartedActivity();  //obtener la actividad siguiente
        ComponentName loginComponent = new ComponentName(activity, RecipeListActivity.class);  //contexto y clase al ComponentName
        assertEquals(loginComponent, intentAfterLogout.getComponent());
    }

    @Test
    public void testKeepButtonClicked_shouldSaveRecipe() throws Exception {
        activity.setRecipe(currentRecipe);
        ImageButton imgKeep = (ImageButton) activity.findViewById(R.id.imgKeep);
        assertNotNull(imgKeep);
        imgKeep.performClick();
        verify(presenter).saveRecipe(currentRecipe);
    }

    @Test
    public void testDismissButtonClicked_shouldDismissRecipe() throws Exception {
        ImageButton imgDismiss = (ImageButton) activity.findViewById(R.id.imgDismiss);
        assertNotNull(imgDismiss);
        imgDismiss.performClick();
        verify(presenter).dismissRecipe();
    }

    @Test
    public void testOnSwipeToKeep_shouldSaveRecipe() throws Exception {
        activity.setRecipe(currentRecipe);
        ImageView imgRecipe = (ImageView) activity.findViewById(R.id.imgRecipe);
        assertNotNull(imgRecipe);
        ShadowImageView shadowImage = (ShadowImageView) ShadowExtractor.extract(imgRecipe);
        shadowImage.performSwipe(200, 200, 500, 250, 50);
        verify(presenter).saveRecipe(currentRecipe);
    }

    @Test
    public void testOnSwipeToDismiss_shouldDiscardRecipe() throws Exception {
        ImageView imgRecipe = (ImageView) activity.findViewById(R.id.imgRecipe);
        assertNotNull(imgRecipe);
        ShadowImageView shadowImage = (ShadowImageView) ShadowExtractor.extract(imgRecipe);
        shadowImage.performSwipe(200, 200, -500, 250, 50);
        verify(presenter).dismissRecipe();
    }

    @Test
    public void testShowProgress_progressBarShouldBeVisible() throws Exception {
        view.showProgress();
        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.progressBar);
        assertNotNull(progressBar);
        assertEquals(View.VISIBLE, progressBar.getVisibility());
    }

    @Test
    public void testHideProgress_progressBarShouldBeGone() throws Exception {
        view.hideProgress();
        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.progressBar);
        assertNotNull(progressBar);
        assertEquals(View.GONE, progressBar.getVisibility());
    }

    @Test
    public void testShowUIElements_buttonsShouldBeVisible() throws Exception {
        view.showUIElements();
        ImageButton imgKeep = (ImageButton) activity.findViewById(R.id.imgKeep);
        ImageButton imgDismiss = (ImageButton) activity.findViewById(R.id.imgDismiss);
        assertNotNull(imgKeep);
        assertNotNull(imgDismiss);
        assertEquals(View.VISIBLE, imgKeep.getVisibility());
        assertEquals(View.VISIBLE, imgDismiss.getVisibility());
    }

    @Test
    public void testHideUIElements_buttonsShouldBeGone() throws Exception {
        view.hideUIElements();
        ImageButton imgKeep = (ImageButton) activity.findViewById(R.id.imgKeep);
        ImageButton imgDismiss = (ImageButton) activity.findViewById(R.id.imgDismiss);
        assertNotNull(imgKeep);
        assertNotNull(imgDismiss);
        assertEquals(View.GONE, imgKeep.getVisibility());
        assertEquals(View.GONE, imgDismiss.getVisibility());
    }

    // los snackbars en onRecipeSaved y onGetRecipeError (en la clase RecipeMainActivity)
    // no se los puede probar, entonces no se escriben las pruebas

    @Test
    public void testSetRecipe_imageLoaderShouldBeCalled() throws Exception {
        String url = "http://lastfm.es/user/ancho85";
        //stub
        when(currentRecipe.getImageURL()).thenReturn(url);

        view.setRecipe(currentRecipe);
        ImageView imgRecipe = (ImageView) activity.findViewById(R.id.imgRecipe);
        verify(imageLoader).load(imgRecipe, currentRecipe.getImageURL());
    }

    @Test
    public void testSaveAnimation_animationShouldBeStarted() throws Exception {
        view.saveAnimation();
        ImageView imgRecipe = (ImageView) activity.findViewById(R.id.imgRecipe);
        assertNotNull(imgRecipe);
        assertNotNull(imgRecipe.getAnimation());
        assertTrue(imgRecipe.getAnimation().hasStarted());
    }

    @Test
    public void testDismissAnimation_animationShouldBeStarted() throws Exception {
        view.dismissAnimation();
        ImageView imgRecipe = (ImageView) activity.findViewById(R.id.imgRecipe);
        assertNotNull(imgRecipe);
        assertNotNull(imgRecipe.getAnimation());
        assertTrue(imgRecipe.getAnimation().hasStarted());
    }
}
