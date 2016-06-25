package edu.galileo.android.facebookrecipes.recipemain.di;

import javax.inject.Singleton;

import dagger.Component;
import edu.galileo.android.facebookrecipes.libs.di.LibsModule;

/**
 * Created by carlos.gomez on 25/06/2016.
 */

@Singleton
@Component(modules = {RecipeMainModule.class, LibsModule.class})
public interface RecipeMainComponent {
}
