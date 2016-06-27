package edu.galileo.android.facebookrecipes.recipelist.di;

import javax.inject.Singleton;

import dagger.Component;
import edu.galileo.android.facebookrecipes.libs.di.LibsModule;
import edu.galileo.android.facebookrecipes.recipelist.RecipeListPresenter;
import edu.galileo.android.facebookrecipes.recipelist.ui.adapters.RecipesAdapter;


@Singleton
@Component(modules = {RecipeListModule.class, LibsModule.class})
public interface RecipeListComponent {
    //método 2 de inyección, con get<Objeto_a_injectar> y son llamados en cascada
    //para que esto funcione hay que tener un provides que devuelve el objeto
    RecipesAdapter getAdapter();
    RecipeListPresenter getPresenter();
}
