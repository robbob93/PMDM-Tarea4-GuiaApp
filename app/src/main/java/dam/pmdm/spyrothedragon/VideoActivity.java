package dam.pmdm.spyrothedragon;

import android.os.Bundle;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;


    public class VideoActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.video_layout);

            VideoView videoView = findViewById(R.id.videoView);
            videoView.setVideoPath("android.resource://"
                    + this.getPackageName()+ "/"
                    + R.raw.easter_video);

            // Iniciar reproducción
            videoView.start();

            videoView.setOnCompletionListener(mp -> {
                finish();  // al terminar el vídeo cierra la actividad
            });
        }

    }
