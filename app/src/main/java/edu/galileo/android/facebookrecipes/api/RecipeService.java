package edu.galileo.android.facebookrecipes.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeService {
    //método del API
    @GET("search")
    //se hace una llamada sobre la respuesta obtenida
    //se define cada query sobre el api
    Call<RecipeSearchResponse> search(@Query("key") String key,
                                      @Query("sort") String sort,
                                      @Query("count") int count,
                                      @Query("page") int page);

}
