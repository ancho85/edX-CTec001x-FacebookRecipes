package edu.galileo.android.facebookrecipes.support;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

/**
 * Created by carlos.gomez on 07/07/2016.
 */
@Implements(RecyclerView.Adapter.class)
public class ShadowRecyclerViewAdapter { //sin heredar nada
    @RealObject
    private RecyclerView.Adapter realObject;

    private RecyclerView recyclerView; //no habrá constructor para asignar al recyclerView
    private SparseArray<RecyclerView.ViewHolder> holders = new SparseArray<>(); //manejar la interacción con los viewHolders

    @Implementation //se define con esto una sombra sobre el real
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        //método para adjuntar el RecyclerView cuando el adaptador se vincula al recyclerView
        this.recyclerView = recyclerView;
    }

    @Implementation
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        realObject.onBindViewHolder(holder, position);
        holders.put(position, holder);
    }

    public int getItemCount() {
        return realObject.getItemCount();
    }

    public boolean performItemClick(int position) {
        //robolectric ya maneja el click sobre la vista, en este caso se maneja el click sobre una posición del adaptador
        View holderView = holders.get(position).itemView;
        return holderView.performClick();
    }

    public boolean performItemClickOverViewInHolder(int position, int viewId) {
        //manejar clicks para cada item de un holder (imagen, favorito, borrado, facebook share, facebook send)
        boolean valueToReturn = false;
        View holderView = holders.get(position).itemView;
        View viewToClick = holderView.findViewById(viewId);
        if (viewToClick != null) {
            valueToReturn = viewToClick.performClick(); //subelemento
        }
        return valueToReturn;
    }

    //creador de viewholder
    public void itemVisible(int position) {
        RecyclerView.ViewHolder holder =
                realObject.createViewHolder(recyclerView,
                        realObject.getItemViewType(position));
        onBindViewHolder(holder, position);
    }

    public View getViewForHolderPosition(int position) {
        // verificar que la vista tiene los parámetros adecuados.
        return holders.get(position).itemView;
    }

}
