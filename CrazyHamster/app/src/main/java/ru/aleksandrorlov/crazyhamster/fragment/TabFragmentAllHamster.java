package ru.aleksandrorlov.crazyhamster.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.aleksandrorlov.crazyhamster.R;
import ru.aleksandrorlov.crazyhamster.controllers.ApiController;
import ru.aleksandrorlov.crazyhamster.controllers.ControllerTabFragmentAllHamster;
import ru.aleksandrorlov.crazyhamster.model.Hamster;
import ru.aleksandrorlov.crazyhamster.rest.ApiUnrealMojo;

/**
 * Created by alex on 19.05.17.
 */

public class TabFragmentAllHamster extends Fragment {
    private ApiUnrealMojo apiUnrealMojo;
    private ControllerTabFragmentAllHamster controller;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_tab_all_hamster , container, false);

        init();

        return view;
    }

    private void init(){
        controller = new ControllerTabFragmentAllHamster(getActivity());

        apiUnrealMojo = ApiController.getApi();
        apiUnrealMojo.getHamsters().enqueue(new Callback<List<Hamster>>(){

            @Override
            public void onResponse(Call<List<Hamster>> call, Response<List<Hamster>> response) {
                try {
                    if (response.isSuccessful()){
                        List<Hamster> hamsters = response.body();
                        controller.checkDataBase(hamsters);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Hamster>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
