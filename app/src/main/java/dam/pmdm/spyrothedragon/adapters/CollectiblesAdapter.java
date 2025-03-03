package dam.pmdm.spyrothedragon.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.VideoActivity;
import dam.pmdm.spyrothedragon.models.Collectible;

public class CollectiblesAdapter extends RecyclerView.Adapter<CollectiblesAdapter.CollectiblesViewHolder> {

    private List<Collectible> list;
    private int clickCount = 0; // Contador de clics en el cardView
    private static final int MAX_CLICKS = 4;
    private Context context;

    public CollectiblesAdapter(List<Collectible> collectibleList) {
        this.list = collectibleList;
    }

    @Override
    public CollectiblesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
        return new CollectiblesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollectiblesViewHolder holder, int position) {
        Collectible collectible = list.get(position);
        holder.nameTextView.setText(collectible.getName());

        // Cargar la imagen (simulado con un recurso drawable)
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(collectible.getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.imageImageView.setImageResource(imageResId);


        holder.itemView.setOnClickListener(v -> {
            if (collectible.getName().equals("Gemas")) {

                clickCount++; // Incrementar el contador de clics
                System.out.println("Gemas pulsadas! " + clickCount);
                if (clickCount == MAX_CLICKS) {
                    // Reproducir vídeo cuando el contador llega a 4 clics
                    playVideo();
                    clickCount = 0; // Reiniciar el contador
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void playVideo() {
        // Lanza la actividad para reproducir el vídeo en pantalla completa
        Intent intent = new Intent(context, VideoActivity.class);
        context.startActivity(intent);
    }

    public static class CollectiblesViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageImageView;

        public CollectiblesViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
        }
    }
}
