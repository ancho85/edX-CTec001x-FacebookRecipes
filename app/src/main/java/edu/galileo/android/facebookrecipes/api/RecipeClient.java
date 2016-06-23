package edu.galileo.android.facebookrecipes.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeClient {
    private Retrofit retrofit;
    //en caso de hacer testing interno, la URL podría recibirse en el constructor y
    //parsear y/o procesar manualmente las respuestas. En este caso, se define 'hardcodeado'
    private final static String BASE_URL = "http://food2fork.com/api/";

    public RecipeClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public RecipeService getRecipeService() {
        return retrofit.create(RecipeService.class);
    }
}
