package com.example.streetviewmap;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * The type Markers adpters.
 */
public class MarkersAdpters extends RecyclerView.Adapter<MarkersAdpters.ViewHolder> {
    private List<RecyclerViewMarkerData> markersData;
    private boolean isPurchased;

    /**
     * Instantiates a new Markers adpters.
     *
     * @param markersData the markers data
     */
    public  MarkersAdpters(List<RecyclerViewMarkerData> markersData){
        this.markersData = markersData;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View markerBuyView = inflater.inflate(R.layout.recycler_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(markerBuyView);
        return viewHolder;
    }

    /**
     *
     * @param holder the holder of the marker view
     * @param position position in the recycle view of the holder.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewMarkerData markerData=markersData.get(position);
        isPurchased=markerData.isPurchased;
        ImageView markerPicture= holder.markerPicture;
        Drawable markerDrawable=ContextCompat.getDrawable(markerPicture.getContext(),markerData.getIdOfMarkerDrawable());
        Bitmap bitmapOfMarker=((BitmapDrawable) markerDrawable).getBitmap();
        markerPicture.setImageBitmap(Bitmap.createScaledBitmap( bitmapOfMarker,76,98,false));
        Button buyButton=holder.buyButton;
        Log.i("banana", "isItPurchesed: "+isPurchased);
        //put in the buy button tage the marker data so i will be able to use the marker data in ViewHolder
        buyButton.setTag(markerData);
            if(markerData.isPurchased){
                buyButton.setText("use");
            }else{
                buyButton.setText("buy for "+CalculateSystem.storePointCost(markerData.position));
            }
    }
    /**
     * @return markerData size
     */
    @Override
    public int getItemCount() {
        return markersData.size();
    }

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Marker picture.
         */
        ImageView markerPicture;
        /**
         * The Buy button.
         */
        Button buyButton;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            findViewsByIds();
            //buy button set on click listener
            buyButton.setOnClickListener(view -> {
                RecyclerViewMarkerData markerData=(RecyclerViewMarkerData)buyButton.getTag();
                if(markerData.isPurchased){
                    Drawable markerDrawble= markerPicture.getDrawable();
                    RoundSystem.setMarkerBitMap(markerDrawble,76,98,buyButton.getContext());
                }else{
                    FireBaseUtil.buyMarkerAndRefreshPage(markerData.position,buyButton.getContext());
                    Log.i("banana", "ViewHolder: "+markerData.isPurchased);
                }
            });
        }

        /**
         * find views by ids
         */
         private void findViewsByIds(){
             markerPicture =  (ImageView) itemView.findViewById(R.id.markerStyleImage);
             buyButton = (Button) itemView.findViewById(R.id.message_button);
         }
    }

}
