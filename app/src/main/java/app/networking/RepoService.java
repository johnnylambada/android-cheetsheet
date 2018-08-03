package app.networking;

import java.util.List;

import app.model.Repo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RepoService {
    @GET("orgs/{login}/repos") Call<List<Repo>> getRepositories(@Path("login") String login);
}
