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
    private List<GuideStep> steps = Arrays.asList();

    public GuideManager(Activity activity) {
        this.activity = activity;
        container = activity.findViewById(R.id.main_container);
    }

    public void startGuide() {

        steps = Arrays.asList(
                new GuideStep(R.id.nav_characters, activity.getString(R.string.guide_step1)),
                new GuideStep(R.id.nav_worlds, activity.getString(R.string.guide_step2)),
                new GuideStep(R.id.nav_collectibles, activity.getString(R.string.guide_step3)),
                new GuideStep(R.id.action_info, activity.getString(R.string.guide_step4))
        );


        guideOverlay = activity.getLayoutInflater().inflate(R.layout.guide_overlay, container, false);
        container.addView(guideOverlay);

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



        btnNext.setOnClickListener(v -> {
            if (activity instanceof MainActivity && currentStep<3) {  // Comprueba que sea MainActivity
                System.out.println("Current step: " +  currentStep);
                ((MainActivity) activity).playSound(soundContinue);  // Llamada a playSound() de la MainActivity
            } else if (activity instanceof MainActivity && currentStep ==3) {
                soundEnd = soundPool.load(activity,R.raw.end_guide,1);
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

        // Posicionar el anillo sobre el botón
        View targetButton = activity.findViewById(step.targetViewId);
        if (targetButton != null) {


            if (step.targetViewId == R.id.action_info) {
                description.setY(100);
                Button btnNext = guideOverlay.findViewById(R.id.btnNext);
                btnNext.setEnabled(false);
                // En vez de bloquear la interacción, simulamos el clic después de unos instantes
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

                // Convertir coordenadas de pantalla a coordenadas del contenedor
                ringHighlight.setX(centerX - ringHighlight.getWidth() / 2f);
                ringHighlight.setY(centerY - ringHighlight.getHeight() / 1.8f);


                ringHighlight.setAlpha(0f);
                description.setAlpha(0f);
                ringHighlight.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);

                ringHighlight.animate().alpha(1f).setDuration(1000).start();
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
        // Quitar la vista anterior
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




