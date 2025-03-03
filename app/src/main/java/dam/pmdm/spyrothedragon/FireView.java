package dam.pmdm.spyrothedragon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;

public class FireView extends View {
    private Paint paintRed, paintOrange, paintYellow;
    private Path pathRed, pathOrange, pathYellow;
    private float originX, originY;
    private float fireHeight = 50;  // Altura inicial de la llama
    private boolean isGrowing = true;
    private ValueAnimator animator;
    private MediaPlayer fireSound;

    public FireView(Context context, float x, float y) {
        super(context);
        this.originX = x;
        this.originY = y;
        init();
        startGrowing();
        startSound();
    }

    private void init() {
        // Pinceles para cada color
        paintRed = new Paint();
        paintRed.setColor(Color.RED);
        paintRed.setStyle(Paint.Style.FILL);
        paintRed.setAntiAlias(true);

        paintOrange = new Paint();
        paintOrange.setColor(Color.parseColor("#FFA500")); // Naranja
        paintOrange.setStyle(Paint.Style.FILL);
        paintOrange.setAntiAlias(true);

        paintYellow = new Paint();
        paintYellow.setColor(Color.YELLOW);
        paintYellow.setStyle(Paint.Style.FILL);
        paintYellow.setAntiAlias(true);

        // Inicializar paths
        pathRed = new Path();
        pathOrange = new Path();
        pathYellow = new Path();
    }

    private void startGrowing() {
        animator = ValueAnimator.ofFloat(10, 900);
        animator.setDuration(5000); // 5 segundos para llegar al máximo
        animator.addUpdateListener(animation -> {
            if (isGrowing) {
                fireHeight = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    private void stopGrowing() {
        isGrowing = false;
        animator.cancel();
        animate().alpha(0f).setDuration(1000).withEndAction(() -> ((ViewGroup) getParent()).removeView(this)).start();
        stopSound();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float baseWidth = fireHeight / 4; // Relación altura/base

        // Dibujar llama roja (más grande)
        pathRed.reset();
        pathRed.moveTo(originX, originY);
        pathRed.lineTo(originX - baseWidth, originY + fireHeight);
        pathRed.lineTo(originX, originY + fireHeight + 100);
        pathRed.lineTo(originX + baseWidth, originY + fireHeight);
        pathRed.close();
        canvas.drawPath(pathRed, paintRed);

        // Dibujar llama naranja (intermedia)
        pathOrange.reset();
        pathOrange.moveTo(originX, originY);
        pathOrange.lineTo(originX - baseWidth / 1.5f, originY + fireHeight - 20);
        pathOrange.lineTo(originX, originY + fireHeight + 60);
        pathOrange.lineTo(originX + baseWidth / 1.5f, originY + fireHeight - 20);
        pathOrange.close();
        canvas.drawPath(pathOrange, paintOrange);

        // Dibujar llama amarilla (más pequeña)
        pathYellow.reset();
        pathYellow.moveTo(originX, originY);
        pathYellow.lineTo(originX - baseWidth / 3, originY + fireHeight - 40);
        pathYellow.lineTo(originX, originY + fireHeight + 10);
        pathYellow.lineTo(originX + baseWidth / 3, originY + fireHeight - 40);
        pathYellow.close();
        canvas.drawPath(pathYellow, paintYellow);
    }

    public void releaseFire() {
        stopGrowing();
    }



    private void startSound() {
        fireSound = MediaPlayer.create(getContext(), R.raw.flare); // 🎵 Archivo de sonido en res/raw/fire_sound.mp3
        if (fireSound != null) {
            fireSound.setOnCompletionListener(mp -> mp.release()); // Libera el recurso al terminar
            fireSound.start(); // 🔥 Reproducción normal (sin bucle)
        }
    }

    private void stopSound() {
        if (fireSound != null) {
            // 🔇 Baja el volumen poco a poco antes de cambiar
            ValueAnimator fadeOut = ValueAnimator.ofFloat(1f, 0f);
            fadeOut.setDuration(1000); // Duración de la transición
            fadeOut.addUpdateListener(animation -> {
                float volume = (float) animation.getAnimatedValue();
                fireSound.setVolume(volume, volume);
            });
            fadeOut.start();
        }
    }



}


