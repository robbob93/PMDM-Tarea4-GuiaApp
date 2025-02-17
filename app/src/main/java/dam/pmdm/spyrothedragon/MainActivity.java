package dam.pmdm.spyrothedragon;

import android.graphics.drawable.AnimationDrawable;
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

    private ActivityMainBinding binding;
    NavController navController = null;
    private BienvenidaLayoutBinding bienvenidaBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        bienvenidaBinding = binding.includeLayout;
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

        initializeGuide();
    }

    private boolean selectedBottomMenu(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_characters)
            navController.navigate(R.id.navigation_characters);
        else if (menuItem.getItemId() == R.id.nav_worlds)
            navController.navigate(R.id.navigation_worlds);
        else
            navController.navigate(R.id.navigation_collectibles);
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

        AnimationDrawable animation;
        bienvenidaBinding.imageView.setBackgroundResource(R.drawable.spyro_jump_animation);
        animation = (AnimationDrawable) bienvenidaBinding.imageView.getBackground();

        binding.constraintLayout.setTouchscreenBlocksFocus(true);


        // Obtén el botón "Comenzar"
        Button btnComenzar = bienvenidaBinding.btnCloseOverlay;
        btnComenzar.setOnClickListener(v -> {
            // Inicia la animación
            animation.start();
            // Programa el cierre del fragmento después de 1350ms (un ciclo completo)
            bienvenidaBinding.getRoot().postDelayed(() -> {
                // Oculta el layout de bienvenida.
                bienvenidaBinding.bienvenidaContainer.setVisibility(View.GONE);
                new GuideManager(MainActivity.this).startGuide();
            }, 1600);


        });

    }
}