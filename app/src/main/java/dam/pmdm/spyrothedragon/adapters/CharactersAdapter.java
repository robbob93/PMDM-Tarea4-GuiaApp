package dam.pmdm.spyrothedragon.adapters;

import android.app.Activity;
import android.media.SoundPool;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import dam.pmdm.spyrothedragon.FireView;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.models.Character;

import java.util.List;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder> {

    private List<Character> list;

    public CharactersAdapter(List<Character> charactersList) {
        this.list = charactersList;
    }

    @Override
    public CharactersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new CharactersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CharactersViewHolder holder, int position) {
        Character character = list.get(position);
        holder.nameTextView.setText(character.getName());

        // Cargar la imagen (simulado con un recurso drawable)
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(character.getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.imageImageView.setImageResource(imageResId);

        holder.itemView.setOnLongClickListener(v -> {
            if(holder.nameTextView.getText().equals( "Spyro")){
                throwFlames(v,holder);
            }

            return true;
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CharactersViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageImageView;

        public CharactersViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
        }
    }


    public void throwFlames(View v, CharactersViewHolder holder){
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        float centerX = location[0] + holder.imageImageView.getWidth() / 2f+25;
        float centerY = location[1] + holder.imageImageView.getHeight() / 2f +85;

        FireView fireView = new FireView(holder.imageImageView.getContext(), centerX, centerY);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        fireView.setLayoutParams(params);

        ViewGroup rootView = (ViewGroup) ((Activity) v.getContext()).getWindow().getDecorView();
        rootView.addView(fireView);

        // Se elimina cuando se suelta el CardView
        v.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                fireView.releaseFire();
            }
            return false;
        });

    }
}
