package app.repolist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import app.model.Repo;
import app.networking.RepoApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewModel extends ViewModel {

    private final MutableLiveData<List<Repo>> repos = new MutableLiveData<>();
    private final MutableLiveData<Boolean> repoLoadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private Call<List<Repo>> repoCall;

    public ListViewModel(){
        fetchRepos();
    }

    public LiveData<List<Repo>> getRepos(){ return repos; }
    public LiveData<Boolean> getRepoLoadError(){ return repoLoadError; }
    public LiveData<Boolean> getRepoLoading(){ return loading; }

    private void fetchRepos() {
        loading.setValue(true);

        repoCall = RepoApi.getRepoService().getRepositories("google");
        repoCall.enqueue(new Callback<List<Repo>>() {
            @Override public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                repoLoadError.setValue(false);
                loading.setValue(false);
                repos.setValue(response.body());
                repoCall = null;
            }

            @Override public void onFailure(Call<List<Repo>> call, Throwable t) {
                Log.e(getClass().getSimpleName(),"Error loading repos", t);
                loading.setValue(false);
                repoLoadError.setValue(true);
                repoCall = null;
            }
        });
    }

    @Override protected void onCleared() {
        if (repoCall!=null){
            repoCall.cancel();
            repoCall = null;
        }
    }
}
