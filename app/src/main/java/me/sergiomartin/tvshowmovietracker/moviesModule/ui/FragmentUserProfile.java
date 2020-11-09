package me.sergiomartin.tvshowmovietracker.moviesModule.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.OnClick;
import me.sergiomartin.tvshowmovietracker.R;

public class FragmentUserProfile extends Fragment {

    @BindView(R.id.tb_user_profile)
    Toolbar tbUserProfile;
    @BindView(R.id.ctl_user_profile)
    CollapsingToolbarLayout ctlUserProfile;
    @BindView(R.id.fab_user_profile)
    FloatingActionButton fabUserProfile;

    public FragmentUserProfile() {
        // Required empty public constructor
    }

    @Override
    public void onStop() {
        super.onStop();
        // mostrar la Toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onResume() {
        super.onResume();

        // ocultar la Toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ocultar la Toolbar principal al iniciar View
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        /*ctlUserProfile.setTitle("test");
        ctlUserProfile.setCollapsedTitleTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        ctlUserProfile.setExpandedTitleColor(ContextCompat.getColor(getContext(), R.color.colorAccent));*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //((AppCompatActivity)getActivity()).setSupportActionBar(tbUserProfile);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        //ProfileHelper.with(getActivity()).loadProfile();
    }

    @OnClick(R.id.fab_user_profile)
    public void onClick() {

    }
}