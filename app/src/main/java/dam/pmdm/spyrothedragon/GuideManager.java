package dam.pmdm.spyrothedragon;

import android.app.Activity;
import android.media.SoundPool;
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
    ImageView ringHighlight;
    TextView description;

    private SoundPool soundPool;
    private int soundContinue;
    private int soundEnd;

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
        ringHighlight = guideOverlay.findViewById(R.id.ringContainer);
        description = guideOverlay.findViewById(R.id.guideText);

        soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        soundContinue = soundPool.load(activity, R.raw.continue_guide, 1);
        soundEnd = soundPool.load(activity,R.raw.end_guide,1);


        btnNext.setOnClickListener(v -> {
            if (activity instanceof MainActivity && currentStep<3) {  // Verifica que sea MainActivity
                System.out.println("Current step: " +  currentStep);
                ((MainActivity) activity).playSound(soundContinue);  // Llamada a playSound() de la MainActivity
            } else if (currentStep ==3) {
                ((MainActivity) activity).playSound(soundEnd);
            }
            nextStep();
        });
        btnSkip.setOnClickListener(v -> finishGuide());

        showStep(currentStep);
    }

    private void showStep(int stepIndex) {
        if (stepIndex >= steps.size()) {
            showSummary();
            return;
        }
        GuideStep step = steps.get(stepIndex);

        description.setText(step.description);
        description.setVisibility(View.VISIBLE);

        // Posicionar el anillo sobre el botón
        View targetButton = activity.findViewById(step.targetViewId);
        if (targetButton != null) {


            if (step.targetViewId == R.id.action_info) {
                description.setY(100);
                Button btnNext = guideOverlay.findViewById(R.id.btnNext);
                btnNext.setEnabled(false);
                // En vez de bloquear la interacción, simulamos el clic después de 2 segundos
                targetButton.postDelayed(() -> {
                    targetButton.performClick();

                    btnNext.setEnabled(true);
                }, 1500);
            } else {
                // Para los demás pasos, se simula el clic inmediatamente:
                targetButton.performClick();
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
                ringHighlight.setAlpha(0f);
                ringHighlight.animate().alpha(1f).setDuration(1000).start();
                description.setAlpha(0f);
                description.animate().alpha(1f).setDuration(1000).start();
            });


        }
    }

    private void nextStep() {

        currentStep++;
        ringHighlight.setVisibility(View.GONE);
        description.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition((ViewGroup) guideOverlay);
        showStep(currentStep);
    }
    private void showSummary() {
        // Remover la vista anterior
        if (guideOverlay != null) {
            container.removeView(guideOverlay);
        }
        // Inflar el layout resumen
        guideOverlay = activity.getLayoutInflater().inflate(R.layout.resumen_guia, container, false);
        container.addView(guideOverlay);

        // Configurar el botón para finalizar la guía
        Button btnFinish = guideOverlay.findViewById(R.id.btnFinishGuide);
        btnFinish.setOnClickListener(v -> finishGuide());
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




