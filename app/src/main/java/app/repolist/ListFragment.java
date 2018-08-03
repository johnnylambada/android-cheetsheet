package app.repolist;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.R;
import app.databinding.FragmentListBinding;
import app.databinding.ItemRepoBinding;
import app.model.Repo;
import app.networking.RepoApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ListFragment extends Fragment {

    private FragmentListBinding binder;
    private Call<List<Repo>> getRepositoriesBackendCall;
    private RepoListAdapter adapter;
    private ListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        binder.getRoot().setOnClickListener(__ ->
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new DetailFragment())
                        .addToBackStack(null)
                        .commit()
        );
        adapter = new RepoListAdapter();
        viewModel = ViewModelProviders.of(getActivity()).get(ListViewModel.class);
        binder.list.setAdapter(adapter);
        return binder.getRoot();
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel.getRepos().observe(this, repos-> {
            binder.list.setVisibility(View.VISIBLE);
            if (repos!=null){
                adapter.setRepos(repos);
            }
        });
        viewModel.getRepoLoadError().observe(this, isError -> {
            //noinspection ConstantConditions
            if (isError) {
                binder.list.setVisibility(View.GONE);
                binder.error.setVisibility(View.VISIBLE);
                binder.error.setText("A BE error has occured");
            } else {
                binder.error.setVisibility(View.GONE);
                binder.error.setText(null);
            }
        });
        viewModel.getRepoLoading().observe(this, isLoading ->{
            //noinspection ConstantConditions
            binder.progress.setVisibility(isLoading?View.VISIBLE:View.GONE);
            if (isLoading){
                binder.error.setVisibility(View.GONE);
                binder.list.setVisibility(View.GONE);
            }
        });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if (getRepositoriesBackendCall!=null) {
            getRepositoriesBackendCall.cancel();
            getRepositoriesBackendCall = null;
        }
    }

    private class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.RepoViewHolder> {

        private final List<Repo> data = new ArrayList<>();

        public void setRepos(List<Repo> repos) {
            data.clear();
            data.addAll(repos);
            notifyDataSetChanged();
        }

        @NonNull @Override
        public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemRepoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_repo, parent, false);
            return new RepoViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
            holder.bind(data.get(position));
        }

        @Override public int getItemCount() {
            return data.size();
        }

        class RepoViewHolder extends RecyclerView.ViewHolder {

            private final ItemRepoBinding binding;
            private Repo repo;

            RepoViewHolder(ItemRepoBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                binding.getRoot().setOnClickListener(__->{
                    ViewModelProviders.of(getActivity()).get(SelectedRepoViewModel.class)
                            .setSelectedRepo(repo);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container,new DetailFragment())
                            .addToBackStack(null)
                            .commit();
                });
            }

            public void bind(Repo repo) {
                this.repo = repo;
                binding.repoName.setText(repo.name);
                binding.repoDescription.setText(repo.description);
                binding.repoForks.setText(String.valueOf(repo.forks));
                binding.repoStars.setText(String.valueOf(repo.stars));
            }
        }
    }

}
