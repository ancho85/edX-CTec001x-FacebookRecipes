package edu.galileo.android.facebookrecipes.recipemain.di;

import javax.inject.Singleton;

import dagger.Component;
import edu.galileo.android.facebookrecipes.libs.base.ImageLoader;
import edu.galileo.android.facebookrecipes.libs.di.LibsModule;
import edu.galileo.android.facebookrecipes.recipemain.RecipeMainPresenter;

/**
 * Created by carlos.gomez on 25/06/2016.
 */

@Singleton
@Component(modules = {RecipeMainModule.class, LibsModule.class})
public interface RecipeMainComponent {
    //método 1 de inyección, con inject
    //void inject(RecipeMainActivity activity);/

    //método 2 de inyección, con get<Objeto_a_injectar> y son llamados en cascada
    //para que esto funcione hay que tener un provides que devuelve el objeto
    ImageLoader getImageLoader();
    RecipeMainPresenter getPresenter();
}
