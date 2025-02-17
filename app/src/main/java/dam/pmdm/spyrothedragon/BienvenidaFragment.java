package dam.pmdm.spyrothedragon;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class BienvenidaFragment extends Fragment {

    private AnimationDrawable animation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout del fragmento
        return inflater.inflate(R.layout.bienvenida_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Obtén la referencia al ImageView y configura el animation-list
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setBackgroundResource(R.drawable.spyro_jump_animation);
        animation = (AnimationDrawable) imageView.getBackground();

        // Obtén el botón "Comenzar"
        Button btnComenzar = view.findViewById(R.id.btnCloseOverlay);
        btnComenzar.setOnClickListener(v -> {
            // Inicia la animación
            animation.start();
            // Programa el cierre del fragmento después de 1350ms (un ciclo completo)
            view.postDelayed(() -> {
                // Elimina este fragmento
                getParentFragmentManager().beginTransaction()
                        .remove(BienvenidaFragment.this)
                        .commit();
            }, 1600);
        });
    }
}

