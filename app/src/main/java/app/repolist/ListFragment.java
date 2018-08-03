package app.repolist;

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
        binder.list.setAdapter(adapter);
        return binder.getRoot();
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binder.progress.setVisibility(VISIBLE);
        getRepositoriesBackendCall = RepoApi.getRepoService().getRepositories("Google");
        getRepositoriesBackendCall.enqueue(new Callback<List<Repo>>() {
            @Override public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                binder.progress.setVisibility(GONE);
                adapter.setRepos(response.body());
                getRepositoriesBackendCall = null;
            }

            @Override public void onFailure(Call<List<Repo>> call, Throwable t) {
                binder.progress.setVisibility(GONE);
                binder.error.setVisibility(VISIBLE);
                binder.error.setText("An BE error has occured");
                getRepositoriesBackendCall = null;
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
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container,DetailFragment.build(repo))
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
