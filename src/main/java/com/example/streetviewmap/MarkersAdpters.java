package com.example.streetviewmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
        ImageView markerPicture= holder.markerPicture;
        Drawable markerDrawable=ContextCompat.getDrawable(markerPicture.getContext(),markerData.getIdOfMarkerDrawble());
        Bitmap bitmapOfMarker=((BitmapDrawable) markerDrawable).getBitmap();
        markerPicture.setImageBitmap(Bitmap.createScaledBitmap( bitmapOfMarker,76,98,false));
        Button buyButton=holder.buyButton;
        buyButton.setText(markerData.getName());
    }

    @Override
    public int getItemCount() {
        return markersData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView markerPicture;
        public Button buyButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            markerPicture =  (ImageView) itemView.findViewById(R.id.markerStyleImage);
            buyButton = (Button) itemView.findViewById(R.id.message_button);
            buyButton.setOnClickListener(view -> {
                Drawable markerDrawble= markerPicture.getDrawable();
                RoundSystem.createMarkerBitMap(markerDrawble,76,98,buyButton.getContext());
            });
        }

    }

}
