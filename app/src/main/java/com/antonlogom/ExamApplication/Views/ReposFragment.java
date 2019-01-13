package com.antonlogom.ExamApplication.Views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.antonlogom.ExamApplication.Interfaces.IReposPresenter;
import com.antonlogom.ExamApplication.Interfaces.IReposView;
import com.antonlogom.ExamApplication.Presenters.ReposPresenter;
import com.antonlogom.ExamApplication.R;
import com.antonlogom.ExamApplication.Models.ReposModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ReposFragment extends Fragment implements IReposView {

    private static final String ARG_TOKEN = "access_token";
    private String access_token;

    private ListView listView;

    private IReposPresenter presenter;

    private String[] from = {"name", "description"};
    private int[] to = {R.id.repoName, R.id.repoDescription};

    public static ReposFragment newInstance(String token) {
        ReposFragment fragment = new ReposFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TOKEN, token);
        fragment.setArguments(bundle);
       return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new ReposPresenter(new ReposModel(), this);
        if (getArguments() != null) {
            access_token = getArguments().getString(ARG_TOKEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repos_view, container, false);

        listView = view.findViewById(R.id.reposList);

        presenter.loadRepos(access_token);
        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
        presenter = null;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setData(ArrayList<HashMap<String, String>> data) {
        listView.setAdapter(new SimpleAdapter(getActivity(), data, R.layout.repo_item, from, to));
    }


}
