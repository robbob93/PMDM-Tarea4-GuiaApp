package dam.pmdm.spyrothedragon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding;
import dam.pmdm.spyrothedragon.databinding.BienvenidaLayoutBinding;

public class MainActivity extends AppCompatActivity {

    private final boolean mostrarGuiaSiempre = true;

    private ActivityMainBinding binding;
    NavController navController = null;
    private BienvenidaLayoutBinding bienvenidaBinding;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_GUIDE_SHOWN = "guide_shown";

    private SoundPool soundPool;
    private int soundContinue;
    private int soundEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        bienvenidaBinding = binding.includeLayout;
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        soundContinue = soundPool.load(this, R.raw.continue_guide, 1);
        soundEnd = soundPool.load(this, R.raw.end_guide, 1);


        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(binding.navView, navController);
            NavigationUI.setupActionBarWithNavController(this, navController);
        }

        binding.navView.setOnItemSelectedListener(this::selectedBottomMenu);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_characters ||
                    destination.getId() == R.id.navigation_worlds ||
                    destination.getId() == R.id.navigation_collectibles) {
                // Para las pantallas de los tabs, no queremos que aparezca la flecha de atrás
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            } else {
                // Si se navega a una pantalla donde se desea mostrar la flecha de atrás, habilítala
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });


        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean guideShown = prefs.getBoolean(KEY_GUIDE_SHOWN, false);

        if (!guideShown || mostrarGuiaSiempre) {
            // Mostrar la guía
            initializeGuide();
            // Marcar la guía como mostrada
            prefs.edit().putBoolean(KEY_GUIDE_SHOWN, true).apply();
        } else {
            bienvenidaBinding.bienvenidaContainer.setVisibility(View.GONE);
        }
    }

    private boolean selectedBottomMenu(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_characters)
            navController.navigate(R.id.navigation_characters);
        else if (menuItem.getItemId() == R.id.nav_worlds)
            navController.navigate(R.id.navigation_worlds);
        else {
            navController.navigate(R.id.navigation_collectibles);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestiona el clic en el ítem de información
        if (item.getItemId() == R.id.action_info) {
            showInfoDialog();  // Muestra el diálogo
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog() {
        // Crear un diálogo de información
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_about)
                .setMessage(R.string.text_about)
                .setPositiveButton(R.string.accept, null)
                .show();
    }

    private void initializeGuide() {


        bienvenidaBinding.spyroJump.setBackgroundResource(R.drawable.spyro_jump_animation);
        AnimationDrawable animationSpyro = (AnimationDrawable) bienvenidaBinding.spyroJump.getBackground();
        bienvenidaBinding.flames.setBackgroundResource(R.drawable.flames_animation);
        AnimationDrawable animationBottonFlames = (AnimationDrawable) bienvenidaBinding.flames.getBackground();

        animationBottonFlames.setColorFilter(Color.parseColor("#70ffff00"), PorterDuff.Mode.SRC_ATOP);
        animationBottonFlames.start();
        //binding.constraintLayout.setTouchscreenBlocksFocus(true);


        Button btnComenzar = bienvenidaBinding.btnStartGuide;
        Button btnCancelar = bienvenidaBinding.btnExit;
        btnComenzar.setOnClickListener(v -> {
            playSound(soundContinue);
            // Inicia la animación
            animationSpyro.start();

            // Programa el cierre del fragmento después de 1350ms (un ciclo completo)
            bienvenidaBinding.getRoot().postDelayed(() ->
                            fadeOutAndStartGuide(bienvenidaBinding.bienvenidaContainer),
                    1600);
        });
        btnCancelar.setOnClickListener(v -> {
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(bienvenidaBinding.bienvenidaContainer, "alpha", 1f, 0f);
            fadeOut.setDuration(1000);

            fadeOut.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    bienvenidaBinding.bienvenidaContainer.setVisibility(View.GONE); // Ocultar vista después de la animación
                }
            });

            fadeOut.start(); // Iniciar animación
        });

    }

    public void playSound(int sound) {
        soundPool.play(sound, 1f, 1f, 0, 0, 1f);
    }

    private void fadeOutAndStartGuide(View view) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        fadeOut.setDuration(1000); // Duración de 500ms

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeOut);
        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE); // Ocultar la vista después del fadeOut
                new GuideManager(MainActivity.this).startGuide(); // Iniciar la guía
            }
        });
    }
}


