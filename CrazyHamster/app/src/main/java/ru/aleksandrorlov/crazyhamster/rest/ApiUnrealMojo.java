package ru.aleksandrorlov.crazyhamster.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import ru.aleksandrorlov.crazyhamster.model.Hamster;

/**
 * Created by alex on 19.05.17.
 */

public interface ApiUnrealMojo {
    @POST("test3/")
    Call<List<Hamster>> getHamsters();
}
