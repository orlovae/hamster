package ru.aleksandrorlov.crazyhamster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.aleksandrorlov.crazyhamster.rest.ApiController;
import ru.aleksandrorlov.crazyhamster.model.Hamster;
import ru.aleksandrorlov.crazyhamster.rest.ApiUnrealMojo;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";
    private ApiUnrealMojo apiUnrealMojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        apiUnrealMojo = ApiController.getApi();
        apiUnrealMojo.getHamsters().enqueue(new Callback<List<Hamster>>(){

            @Override
            public void onResponse(Call<List<Hamster>> call, Response<List<Hamster>> response) {
                try {
                    if (response.isSuccessful()){
                        List<Hamster> hamsterList = response.body();
                        for (Hamster item:hamsterList
                             ) {
                            Log.d(LOG_TAG, "hamster title = " + item.getTitle());
                            Log.d(LOG_TAG, "hamster description = " + item.getDescription());
                            Log.d(LOG_TAG, "hamster imageURL = " + item.getImageURL());
                        }
                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "exeption onResponce " + e.toString());
                    //TODO написать обработку ошибок
                }
            }

            @Override
            public void onFailure(Call<List<Hamster>> call, Throwable t) {
                t.printStackTrace();
                Log.d(LOG_TAG, "exeption onFailure " + t.toString());
                //TODO написать обработку ошибок
            }
        });
    }
}
