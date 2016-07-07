package edu.galileo.android.facebookrecipes.recipelist.ui.adapters;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.share.model.ShareContent;
import com.facebook.share.widget.SendButton;
import com.facebook.share.widget.ShareButton;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.internal.ShadowExtractor;

import java.util.List;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.R;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.libs.base.ImageLoader;
import edu.galileo.android.facebookrecipes.support.ShadowRecyclerViewAdapter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by carlos.gomez on 07/07/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, shadows = {ShadowRecyclerViewAdapter.class})
public class RecipesAdapterTest extends BaseTest {
    @Mock
    private List<Recipe> recipeList;
    @Mock
    private ImageLoader imageLoader;
    @Mock
    private OnItemClickListener onItemClickListener;
    @Mock
    private Recipe recipe;

    private RecipesAdapter adapter; //sujeto de pruebas
    private ShadowRecyclerViewAdapter shadowAdapter; // pruebas de clicks (render correcto)
    private String URL = "http://lastfm.es/user/ancho85";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        when(recipe.getSourceURL()).thenReturn(URL); // al probar el click del facebook, el recibo debe tener un URL válido

        adapter = new RecipesAdapter(recipeList, imageLoader, onItemClickListener); // adaptador real
        shadowAdapter = (ShadowRecyclerViewAdapter) ShadowExtractor.extract(adapter); // adaptador shadow, ejecutar click, contenido de viewHolders, etc.

        Activity activity = Robolectric.setupActivity(Activity.class); // se puede enviar cualquier actividad, es para construir el RecyclerView
        RecyclerView recyclerView = new RecyclerView(activity); // se pasa el parámetro creado
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        recyclerView.setAdapter(adapter);
    }

    @Test
    public void testSetRecipes_itemCountMatches() throws Exception {
        int itemCount = 5;
        when(recipeList.size()).thenReturn(itemCount);
        adapter.setRecipes(recipeList);
        assertEquals(itemCount, adapter.getItemCount());
    }

    @Test
    public void testRemoveRecipe_isRemovedFromAdapter() throws Exception {
        adapter.removeRecipe(recipe);
        verify(recipeList).remove(recipe);
    }

    @Test
    public void testOnItemClick_shouldCallListener() throws Exception {
        // PRUEBA UTILIZANDO EL SHADOW ADAPTER CREADO
        int positionToClick = 0;
        when(recipeList.get(positionToClick)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToClick); // que sea visible, se agrega el viewholder
        shadowAdapter.performItemClick(positionToClick);  // que se realize el click

        verify(onItemClickListener).onItemClick(recipe);  // verificar que el click se realizó y que se envia recipe como parámetro
    }

    @Test
    public void testViewHolder_shouldRenderTitle() throws Exception {
        int positionToShow = 0;
        String recipeTitle = "title";
        when(recipe.getTitle()).thenReturn(recipeTitle);
        when(recipeList.get(positionToShow)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToShow);

        View view = shadowAdapter.getViewForHolderPosition(positionToShow);
        TextView txtRecipeName = (TextView) view.findViewById(R.id.txtRecipeName);

        assertEquals(recipeTitle, txtRecipeName.getText().toString());
    }

    @Test
    public void testOnSubItemClick_favoriteShouldCallListener() throws Exception {
        int positionToClick = 0;
        boolean favorite = true;
        when(recipe.getFavorite()).thenReturn(favorite);
        when(recipeList.get(positionToClick)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performItemClickOverViewInHolder(positionToClick, R.id.imgFav);

        View view = shadowAdapter.getViewForHolderPosition(positionToClick);
        ImageButton imgFav = (ImageButton) view.findViewById(R.id.imgFav);

        assertEquals(favorite, imgFav.getTag());
        verify(onItemClickListener).onFavClick(recipe);
    }

    @Test
    public void testOnSubItemClick_deleteShouldCallListener() throws Exception {
        int positionToClick = 0;
        when(recipeList.get(positionToClick)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToClick);
        shadowAdapter.performItemClickOverViewInHolder(positionToClick, R.id.imgDelete);

        verify(onItemClickListener).onDeleteClick(recipe);
    }

    @Test
    public void testFacebookShareBind_shareContentSet() throws Exception {
        int positionToShow = 0;
        when(recipeList.get(positionToShow)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToShow);
        View view = shadowAdapter.getViewForHolderPosition(positionToShow);
        ShareButton fbShare = (ShareButton) view.findViewById(R.id.fbShare);

        ShareContent shareContent = fbShare.getShareContent();
        assertNotNull(shareContent);
        assertNotNull(shareContent.getContentUrl());
        assertEquals(URL, shareContent.getContentUrl().toString());
    }

    @Test
    public void testFacebookSendBind_shareContentSet() throws Exception {
        int positionToShow = 0;
        when(recipeList.get(positionToShow)).thenReturn(recipe);

        shadowAdapter.itemVisible(positionToShow);
        View view = shadowAdapter.getViewForHolderPosition(positionToShow);
        SendButton fbSend = (SendButton) view.findViewById(R.id.fbSend);

        ShareContent shareContent = fbSend.getShareContent();
        assertNotNull(shareContent);
        assertNotNull(shareContent.getContentUrl());
        assertEquals(URL, shareContent.getContentUrl().toString());
    }
}