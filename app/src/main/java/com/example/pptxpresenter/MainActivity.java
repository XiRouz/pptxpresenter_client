package com.example.pptxpresenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements UserNameDialogFragment.UserNameDialogListener {

    static private RestAPI restService;
    SharedPreferences sharedPref;
    private String user = "";
    private String serverUrl = "";

    static private Retrofit retrofitInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.0.111:10050/") // 83.69.213.178
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        user = sharedPref.getString(getString(R.string.user_name_key), "None");
        serverUrl = sharedPref.getString(getString(R.string.server_url_key), "None");
        if (!URLUtil.isValidUrl(serverUrl)) {
            serverUrl = "http://127.0.0.1:10050/";
        }
        retrofitInstance = new Retrofit.Builder()
                .baseUrl(serverUrl) // 83.69.213.178 // "http://192.168.0.111:10050/"
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restService = retrofitInstance.create(RestAPI.class);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                restService.heartBeat(user).enqueue(new Callback<ServerAns>() {
                    @Override
                    public void onResponse(Call<ServerAns> call, Response<ServerAns> response) {
                        int color = Color.DKGRAY;
                        if (response.body() != null) {
                            switch (response.body().message) {
                                case "Active":
                                    color = Color.GREEN;
                                    break;
                                case "Not active":
                                    color = Color.YELLOW;
                                    break;
                                case "Presenter not found":
                                    color = Color.RED;
                                    break;
                            }
                        }
                        MenuItem item = myToolbar.getMenu().findItem(R.id.state);
                        item.getIcon().setTint(color);
                    }

                    @Override
                    public void onFailure(Call<ServerAns> call, Throwable t) {
                    }
                });
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitems, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {// User chooses the "Settings" item. Show the app settings UI.
            Bundle arguments = new Bundle();
            arguments.putString("userName", user);
            arguments.putString("serverUrl", serverUrl);
            UserNameDialogFragment dialogFragment = new UserNameDialogFragment();
            dialogFragment.setArguments(arguments);
            dialogFragment.show(getSupportFragmentManager(), "Ssettings");
            return true;
        } else if (itemId == R.id.state) {// User chooses the "Favorite" action. Mark the current item as a
            // favorite.
            return true;
        }// The user's action isn't recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    public void pressButton(View view) {
        int id = view.getId();
        if (id == R.id.nextButton) {
            restService.nextSlide(user).enqueue(new Callback<ServerAns>() {
                @Override
                public void onResponse(Call<ServerAns> call, Response<ServerAns> response) {

                }

                @Override
                public void onFailure(Call<ServerAns> call, Throwable t) {

                }
            });
        }
        else if (id == R.id.prevButton) {
            restService.prevSlide(user).enqueue(new Callback<ServerAns>() {
                @Override
                public void onResponse(Call<ServerAns> call, Response<ServerAns> response) {

                }

                @Override
                public void onFailure(Call<ServerAns> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onDialogPositiveClick(UserNameDialogFragment dialog) {
        if (dialog.getArguments() != null) {
            user = dialog.getArguments().getString("userName");
            serverUrl = dialog.getArguments().getString("serverUrl");
            if (!URLUtil.isValidUrl(serverUrl)) {
                serverUrl = "http://127.0.0.1:10050/";
            }
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.user_name_key), user);
            editor.putString(getString(R.string.server_url_key), serverUrl);
            editor.apply();

            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(serverUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restService = retrofitInstance.create(RestAPI.class);
        }
    }
}