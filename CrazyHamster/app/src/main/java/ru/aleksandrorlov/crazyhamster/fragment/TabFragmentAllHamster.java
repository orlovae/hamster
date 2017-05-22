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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.aleksandrorlov.crazyhamster.R;
import ru.aleksandrorlov.crazyhamster.adapter.RecyclerViewAllHamsterAdapter;
import ru.aleksandrorlov.crazyhamster.data.Contract;

/**
 * Created by alex on 19.05.17.
 */

public class TabFragmentAllHamster extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String LOG_TAG = getClass().getSimpleName();
    private RecyclerView recyclerViewAllHamster;
    private RecyclerViewAllHamsterAdapter adapter;
    private int height, width;

    private int LOADER_ID = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics;
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = (int)Math.round(metrics.heightPixels * 0.2);//TODO вынести в константы
        height = (int)Math.round(metrics.widthPixels * 0.2);//коэффициент сжатия картинок.
        Log.d(LOG_TAG, "width = " + width + "; height = " + height);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_tab_all_hamster , container, false);

        initView(view);

        initRecyclerView();

        return view;
    }

    private void initView(View view){
        recyclerViewAllHamster = (RecyclerView)view.findViewById(R.id.recycler_view_all_hamster);
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new RecyclerViewAllHamsterAdapter(getActivity(), null, width, height);
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
