package ru.aleksandrorlov.crazyhamster.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import goldzweigapps.tabs.Builder.EasyTabsBuilder;
import goldzweigapps.tabs.Items.TabItem;
import goldzweigapps.tabs.View.EasyTabs;
import ru.aleksandrorlov.crazyhamster.R;

/**
 * Created by alex on 19.05.17.
 */

public class TabsFragment extends Fragment {
    private EasyTabs easyTabs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs_fragment_layout, container, false);

        initViews(view);
        setTabs();

        return view;
    }

    private void initViews(View view){
        easyTabs = (EasyTabs)view.findViewById(R.id.easyTabs);
    }

    private void setTabs(){
        String nameTabAllHamster = getString(R.string.tab_all_hamster);
        String nameTabLikeHamster = getString(R.string.tab_like_hamster);

        TabFragmentAllHamster fragmentAllHamster = new TabFragmentAllHamster();
        TabFragmentLikeHamster fragmentLikeHamster = new TabFragmentLikeHamster();


        EasyTabsBuilder.with(easyTabs).addTabs(
                new TabItem(fragmentAllHamster, nameTabAllHamster),
                new TabItem(fragmentLikeHamster, nameTabLikeHamster)
        ).Build();
    }
}
