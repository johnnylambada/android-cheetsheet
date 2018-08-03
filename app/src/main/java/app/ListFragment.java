package app;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import app.databinding.FragmentListBinding;
import app.databinding.ItemRepoBinding;
import app.model.Repo;
import app.model.User;

public class ListFragment extends Fragment {

    private FragmentListBinding binder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        binder.getRoot().setOnClickListener(__->
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,new DetailFragment())
                        .addToBackStack(null)
                        .commit()
        );
        binder.list.setAdapter(new RepoListAdapter());
        return binder.getRoot();
    }

    private class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.RepoViewHolder> {

        private final List<Repo> data = new ArrayList<Repo>() {{
            add(new Repo(1, "arepo", "A really good repo", new User("fred"), 1, 2));
            add(new Repo(2, "another-repo", "A very good repo", new User("wilma"), 10, 20));
            add(new Repo(3, "fine-repo", "A fine repo", new User("betty"), 13, 24));
        }};

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

        @Override public long getItemId(int position) {
            return data.get(position).id;
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
