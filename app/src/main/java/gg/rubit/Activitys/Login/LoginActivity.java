package gg.rubit.Activitys.Login;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import gg.rubit.Activitys.MensajeLoginActivity;
import gg.rubit.database.DatabaseManager;
import gg.rubit.Entidades.Usuarios;
import gg.rubit.R;
import gg.rubit.api.ApiService;
import gg.rubit.api.request.RequestUser;
import gg.rubit.api.response.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText userEmail, userPassword;
    int type;
    DatabaseManager database;
    MediaPlayer click, music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseManager(getApplicationContext());

        userEmail = (EditText) findViewById(R.id.email);
        userPassword = (EditText) findViewById(R.id.password);

        click = MediaPlayer.create(this, R.raw.click);
        music = MediaPlayer.create(this, R.raw.menumusic);
        music.start();

        verifyUserSession();
    }

    @Override
    protected void onStart() {
        super.onStart();
        music.start();
    }

    private void verifyUserSession() {
        Usuarios user = database.getUserSession();
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), MensajeLoginActivity.class));
        }
    }

    public void performUserLogin(View v) {
        try {
            click.start();
            String user = userEmail.getText().toString();
            String pass = userPassword.getText().toString();
            RequestUser request = new RequestUser();
            request.setCorreo(user);
            request.setPassword(pass);

            Call<UserResponse> response = ApiService.getApiService().login(request);
            response.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.isSuccessful()) {
                        UserResponse estudiante = response.body();
                        if (estudiante != null) {
                            Usuarios user = new Usuarios(estudiante.getId(), estudiante.getCorreo(), "", estudiante.getNombre());

                            database.saveUserSession(user);

                            Toast.makeText(getApplicationContext(), "Login Exitoso", Toast.LENGTH_LONG).show();
                            estudiante.setTipo(3);

                            Intent i = new Intent(getApplicationContext(), MensajeLoginActivity.class);
                            i.putExtra("Nombre", estudiante.getNombre());
                            i.putExtra("Tipaje", estudiante.getTipo());

                            startActivity(i);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error Al Iniciar Sesión", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error Al Iniciar Sesión", Toast.LENGTH_LONG).show();
                    int x = 1;
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error Al Iniciar Sesión", Toast.LENGTH_LONG).show();
            int x = 1;
        }
    }

    public void registerUser(View v) {
        click.start();
        Intent i = new Intent(getApplicationContext(), RegistrarseActivity.class);
        i.putExtra("num", 1);
        startActivity(i);
    }

    public void recoverUserPassword(View view) {
        click.start();
        startActivity(new Intent(getApplicationContext(), RecuperarContrasenaActivity.class));
    }

    public void utpLogo(View view) {
        click.start();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://utp.ac.pa/")));
    }

    public void fiscLogo(View view) {
        click.start();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://fisc.utp.ac.pa/")));
    }

    @Override
    protected void onPause() {
        super.onPause();
        music.pause();
    }
}