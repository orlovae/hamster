package ru.aleksandrorlov.crazyhamster.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.aleksandrorlov.crazyhamster.R;
import ru.aleksandrorlov.crazyhamster.adapter.RecyclerViewAllHamsterAdapter;
import ru.aleksandrorlov.crazyhamster.controllers.ApiController;
import ru.aleksandrorlov.crazyhamster.controllers.ControllerTabFragmentAllHamster;
import ru.aleksandrorlov.crazyhamster.data.Contract;
import ru.aleksandrorlov.crazyhamster.model.Hamster;
import ru.aleksandrorlov.crazyhamster.rest.ApiUnrealMojo;

/**
 * Created by alex on 19.05.17.
 */

public class TabFragmentAllHamster extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private ApiUnrealMojo apiUnrealMojo;
    private ControllerTabFragmentAllHamster controller;
    private RecyclerView recyclerViewAllHamster;
    private RecyclerViewAllHamsterAdapter adapter;

    private int LOADER_ID = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_tab_all_hamster , container, false);

        init();

        initView(view);

        initRecyclerView();

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

    private void initView(View view){
        recyclerViewAllHamster = (RecyclerView)view.findViewById(R.id.recycler_view_all_hamster);
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new RecyclerViewAllHamsterAdapter(getActivity(), null);
        recyclerViewAllHamster.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        recyclerViewAllHamster.setAdapter(adapter);
        recyclerViewAllHamster.setLayoutManager(layoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                Contract.Hamster.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
