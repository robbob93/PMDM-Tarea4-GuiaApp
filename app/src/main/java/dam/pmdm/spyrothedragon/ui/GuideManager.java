package dam.pmdm.spyrothedragon.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.transition.TransitionManager;

import java.util.Arrays;
import java.util.List;

import dam.pmdm.spyrothedragon.R;

public class GuideManager {
    private final Activity activity;
    private final ViewGroup container; // Por ejemplo, el "main_container" de la Activity
    private View guideView;
    private int currentStep = 0;

    // Arrays para los recursos y datos de cada paso:
    private final int[] guideLayouts = {
            R.layout.guia_paso_1

    };
    private final int[] targetButtonIds = {
            R.id.nav_characters,  // Botón a resaltar en el paso 1
            R.id.nav_worlds,      // Botón a resaltar en el paso 2
            R.id.nav_collectibles // Botón a resaltar en el paso 3
    };
    private final String[] guideTexts = {
            "Aquí podrás conocer a todos los personajes del mundo de Spyro",
            "Este botón muestra los mundos.",
            "Este botón te permite ver los coleccionables."
    };

    public GuideManager(Activity activity) {
        this.activity = activity;
        container = activity.findViewById(R.id.main_container);
    }

    public void startGuide() {
        showStep(currentStep);
    }

    private void showStep(int stepIndex) {
        if (stepIndex >= guideLayouts.length) {
            finishGuide();
            return;
        }
        // Remover el layout anterior, si existe
        if (guideView != null) {
            container.removeView(guideView);
        }
        // Inflar el layout correspondiente al paso actual
        guideView = activity.getLayoutInflater().inflate(guideLayouts[stepIndex], container, false);
        container.addView(guideView);

        // Actualizar el texto descriptivo
        TextView guideTextView = guideView.findViewById(R.id.guideText);
        if (guideTextView != null) {
            guideTextView.setText(guideTexts[stepIndex]);
        }

        // Reposicionar el contenedor del anillo según el botón a resaltar
        final FrameLayout ringContainer = guideView.findViewById(R.id.ringContainer);
        final int targetId = targetButtonIds[stepIndex];
        final View targetButton = activity.findViewById(targetId);
        if (ringContainer != null && targetButton != null) {
            // Esperar a que targetButton se mida para obtener su posición
            targetButton.post(new Runnable() {
                @Override
                public void run() {
                    int[] location = new int[2];
                    targetButton.getLocationOnScreen(location);
                    float centerX = location[0] + targetButton.getWidth() / 2f;
                    float centerY = location[1] + targetButton.getHeight() / 2f;

                    // Posicionar ringContainer de modo que su centro se alinee con el botón
                    ringContainer.setX(centerX - ringContainer.getWidth() / 2f);
                    ringContainer.setY(centerY - ringContainer.getHeight() / 1.8f);
                    ringContainer.setVisibility(View.VISIBLE);
                }
            });
        }

        // Configurar los botones de navegación de la guía
        Button btnNext = guideView.findViewById(R.id.btnNextGuide);
        Button btnSkip = guideView.findViewById(R.id.btnSkipGuide);
        if (btnNext != null) {
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentStep++;
                    showStep(currentStep);
                }
            });
        }
        if (btnSkip != null) {
            btnSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishGuide();
                }
            });
        }
    }

    private void finishGuide() {
        if (guideView != null) {
            container.removeView(guideView);
            guideView = null;
        }
    }
}


