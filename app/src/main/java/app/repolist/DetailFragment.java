package app.repolist;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.R;
import app.databinding.FragmentDetailBinding;

public class DetailFragment extends Fragment {

    private FragmentDetailBinding binder;
    private SelectedRepoViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        return binder.getRoot();
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(getActivity()).get(SelectedRepoViewModel.class);
        viewModel.getSelectedRepo().observe(this, repo -> {
            if (repo!=null) {
                binder.repoName.setText(repo.name());
                binder.repoDescription.setText(repo.description());
                binder.repoForks.setText(String.valueOf(repo.forks()));
                binder.repoStars.setText(String .valueOf(repo.stars()));
            }
        });
        viewModel.restoreFromBundle(savedInstanceState);
    }

    @Override public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.saveToBundle(outState);
    }
}
