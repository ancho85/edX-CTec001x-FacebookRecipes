package edu.galileo.android.facebookrecipes;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

/**
 * Created by carlos.gomez on 06/07/2016.
 */
public abstract class BaseTest {
    @Before
    public void setUp() throws Exception {
        //inicialización de los mocks de mockito
        MockitoAnnotations.initMocks(this);
        //inicialización de robolectric
        if (RuntimeEnvironment.application != null){
            //creación de objecto que extiende comportamiento de objeto real
            //en este caso, se requerira probar funciones de facebook y debe tener acceso a internet
            ShadowApplication shadowApp = Shadows.shadowOf(RuntimeEnvironment.application);
            //robolectric requiere modificación del archivo de gradle para incluir AndroidHttpClient
            shadowApp.grantPermissions("android.permission.INTERNET");
        }
    }
}
