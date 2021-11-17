package cn.iwenddg.animation.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwenddg.animation.R;
import cn.iwenddg.animation.ui.search.ICallBack;
import cn.iwenddg.animation.ui.search.SearchView;
import cn.iwenddg.animation.ui.search.bCallBack;

public class CommunityFragment extends SupportFragment {


    public CommunityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_community, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

}