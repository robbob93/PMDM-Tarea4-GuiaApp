package dam.pmdm.spyrothedragon;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.transition.TransitionManager;


import java.util.Arrays;
import java.util.List;

public class GuideManager {
    private final Activity activity;
    private final ViewGroup container; // El contenedor principal de la actividad (por ejemplo, main_container)
    private View guideOverlay;
    private int currentStep = 0;

    // Lista de pasos de la guía
    private final List<GuideStep> steps = Arrays.asList(
            new GuideStep(R.id.nav_characters, "Aquí podrás conocer a todos los personajes del mundo de Spyro."),
            new GuideStep(R.id.nav_worlds, "En este lugar podrás explorar las distintas áreas del mundo de Spyro"),
            new GuideStep(R.id.nav_collectibles, "Aquí podrás saber cuáles son los coleccionables del mundo de Spyro"),
            new GuideStep(R.id.action_info, "Aqui podras encontrar información sobre los desarrolladores")
    );

    public GuideManager(Activity activity) {
        this.activity = activity;
        container = activity.findViewById(R.id.main_container);
    }

    public void startGuide() {
        guideOverlay = activity.getLayoutInflater().inflate(R.layout.guide_overlay, container, false);
        container.addView(guideOverlay);

        guideOverlay.setClickable(true);
        guideOverlay.setClickable(true);
        guideOverlay.setFocusable(true);
        guideOverlay.setFocusableInTouchMode(true);
        guideOverlay.requestFocus();

        Button btnNext = guideOverlay.findViewById(R.id.btnNext);
        Button btnSkip = guideOverlay.findViewById(R.id.btnSkip);

        btnNext.setOnClickListener(v -> nextStep());
        btnSkip.setOnClickListener(v -> finishGuide());

        showStep(currentStep);
    }

    private void showStep(int stepIndex) {
        if (stepIndex >= steps.size()) {
            finishGuide();
            return;
        }
        GuideStep step = steps.get(stepIndex);
        TextView description = guideOverlay.findViewById(R.id.guideText);
        ImageView ringHighlight = guideOverlay.findViewById(R.id.ringContainer);
        ringHighlight.setVisibility(View.VISIBLE);

        description.setText(step.description);

        // Posicionar el anillo sobre el botón
        View targetButton = activity.findViewById(step.targetViewId);
        if (targetButton != null) {

            if (step.targetViewId != R.id.action_info) {
                targetButton.performClick();
            }else{
                float altura = guideOverlay.getHeight();
                System.out.println("Altura " +  altura);
                description.setY(100);
            }
            // Esperar a que se mida la vista
            targetButton.post(() -> {
                int[] location = new int[2];
                targetButton.getLocationOnScreen(location);

                // Calcula el centro del botón y ajusta el anillo
                float centerX = location[0] + targetButton.getWidth() / 2f;
                float centerY = location[1] + targetButton.getHeight() / 2f;

                // Convertir coordenadas de pantalla a coordenadas del contenedor (puede variar según tu layout)
                ringHighlight.setX(centerX - ringHighlight.getWidth() / 2f);
                ringHighlight.setY(centerY - ringHighlight.getHeight() / 1.8f);
                ringHighlight.setVisibility(View.VISIBLE);

                // Opcional: animar el anillo (por ejemplo, con un fade-in)
                ringHighlight.setAlpha(0f);
                ringHighlight.animate().alpha(1f).setDuration(100).start();
            });


        }
    }

    private void nextStep() {
        currentStep++;
        // Usa una transición para suavizar el cambio (opcional)
        TransitionManager.beginDelayedTransition((ViewGroup) guideOverlay);
        showStep(currentStep);
    }

    private void finishGuide() {
        // Quita el overlay
        if (guideOverlay != null) {
            ((ViewGroup) guideOverlay.getParent()).removeView(guideOverlay);
            guideOverlay = null;
        }
    }

    // Clase para definir cada paso
    static class GuideStep {
        int targetViewId;
        String description;

        GuideStep(int targetViewId, String description) {
            this.targetViewId = targetViewId;

            this.description = description;
        }
    }
}




