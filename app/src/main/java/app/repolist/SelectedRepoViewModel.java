package app.repolist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;

import app.model.Repo;

public class SelectedRepoViewModel extends ViewModel {

    public static final String REPO_DETAILS = "repo_details";
    private final MutableLiveData<Repo> selectedRepo = new MutableLiveData<>();

    public LiveData<Repo> getSelectedRepo() {
        return selectedRepo;
    }

    public void setSelectedRepo(Repo repo) {
        selectedRepo.setValue(repo);
    }

    public void saveToBundle(Bundle outState) {
        final Repo repo = selectedRepo.getValue();
        if (repo != null) {
            outState.putSerializable(REPO_DETAILS, repo);
        }
    }

    public void restoreFromBundle(Bundle savedInstanceState){
        if (selectedRepo.getValue()==null){
            if (savedInstanceState != null && savedInstanceState.containsKey(REPO_DETAILS)){
                final Repo repo = (Repo) savedInstanceState.getSerializable(REPO_DETAILS);
                setSelectedRepo(repo);
            }
        }
    }
}
