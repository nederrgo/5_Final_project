package com.example.streetviewmap;

import static com.example.streetviewmap.FireBaseUtil.FireBaseHandlerCreator;

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

public class MarkersAdpters extends RecyclerView.Adapter<MarkersAdpters.ViewHolder> {
    private List<RecyclerViewMarkerData> markersData;
    private boolean isPurchased;
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewMarkerData markerData=markersData.get(position);
        isPurchased=markerData.isPurchased;
        ImageView markerPicture= holder.markerPicture;
        Drawable markerDrawable=ContextCompat.getDrawable(markerPicture.getContext(),markerData.getIdOfMarkerDrawble());
        Bitmap bitmapOfMarker=((BitmapDrawable) markerDrawable).getBitmap();
        markerPicture.setImageBitmap(Bitmap.createScaledBitmap( bitmapOfMarker,76,98,false));
        Button buyButton=holder.buyButton;
        Log.i("banana", "isItPurchesed: "+isPurchased);
        buyButton.setTag(markerData);
            if(markerData.isPurchased){
                buyButton.setText("use");
            }else{
                buyButton.setText("buy for "+CalculateSystem.storePointCost(markerData.position));
            }

    }

    @Override
    public int getItemCount() {
        return markersData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView markerPicture;
        Button buyButton;
        public ViewHolder(View itemView) {
            super(itemView);
            markerPicture =  (ImageView) itemView.findViewById(R.id.markerStyleImage);
            buyButton = (Button) itemView.findViewById(R.id.message_button);
            buyButton.setOnClickListener(view -> {
                RecyclerViewMarkerData markerData=(RecyclerViewMarkerData)buyButton.getTag();
                if(markerData.isPurchased){
                    Drawable markerDrawble= markerPicture.getDrawable();
                    RoundSystem.createMarkerBitMap(markerDrawble,76,98,buyButton.getContext());
                }else{
                    FireBaseUtil firebaseUser=FireBaseHandlerCreator();
                    firebaseUser.buyMarkerAndEditPage(markerData.position,buyButton.getContext());
                    Log.i("banana", "ViewHolder: "+markerData.isPurchased);
                }
            });
        }

    }

}
