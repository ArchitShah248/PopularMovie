package com.portfolio.archit.popularmovie.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Archit Shah on 5/1/2016.
 */
public abstract class BaseFragment extends Fragment {

    public Context mContext;
    protected View mView;

    abstract protected void initView(Bundle savedInstanceState);

    abstract protected void setListeners();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView(savedInstanceState);
        setListeners();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
