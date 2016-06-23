package edu.galileo.android.facebookrecipes.libs.base;

import android.widget.ImageView;

public interface ImageLoader {
    void load(ImageView imageView, String URL);

    void setOnFinishedImageLoadingListener(Object listener); //este listener se ejecuta al terminar la carga
}
