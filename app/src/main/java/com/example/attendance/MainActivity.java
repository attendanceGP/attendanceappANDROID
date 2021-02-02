package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private String queryUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        // when we click login, one of these scenarios will happen:
        // 1- incorrect username or password, the app will let the user try again
        // 2- correct credentials, the user will be logged in and sent to the home activity
        // and their detail will be persisted in the app
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAPI userAPI = APIClient.getClient().create(UserAPI.class);

                Call<User> call = userAPI.login(username.getText().toString(), password.getText().toString());

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User test = response.body();
                        if(test.getErrorCode() == 1){
                            Toast.makeText(getApplicationContext(), "you can only login once every 30 days", Toast.LENGTH_LONG).show();
                        }else if(test.getErrorCode() == 2){
                            Toast.makeText(getApplicationContext(), "incorrect username or password", Toast.LENGTH_SHORT).show();
                        }else {
                            //TODO open another activity and persist
                            startActivity(new Intent(MainActivity.this, Home.class));
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        username.setText(t.getMessage());
                    }
                });
            }
        });
    }
}