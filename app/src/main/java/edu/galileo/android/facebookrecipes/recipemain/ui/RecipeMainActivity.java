package edu.galileo.android.facebookrecipes.recipemain.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.galileo.android.facebookrecipes.R;
import edu.galileo.android.facebookrecipes.entities.Recipe;
import edu.galileo.android.facebookrecipes.recipemain.RecipeMainPresenter;

public class RecipeMainActivity extends AppCompatActivity implements RecipeMainView {

    @Bind(R.id.imgRecipe)
    ImageView imgRecipe;
    @Bind(R.id.imgDismiss)
    ImageButton imgDismiss;
    @Bind(R.id.imgKeep)
    ImageButton imgKeep;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.layoutContainer)
    RelativeLayout layoutContainer;

    private RecipeMainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_main);
        ButterKnife.bind(this);
        setUpInjection();
        presenter.onCreate();
        presenter.getNextRecipe();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void setUpInjection() {
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showUIElements() {
        imgKeep.setVisibility(View.VISIBLE);
        imgDismiss.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideUIElements() {
        imgKeep.setVisibility(View.GONE);
        imgDismiss.setVisibility(View.GONE);
    }

    @Override
    public void saveAnimation() {

    }

    @Override
    public void dismissAnimation() {

    }

    @Override
    public void onRecipeSaved() {

    }

    @Override
    public void setRecipe(Recipe recipe) {

    }

    @Override
    public void onGetRecipeError(String error) {

    }
}
