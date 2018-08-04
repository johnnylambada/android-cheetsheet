package app.networking;

import com.squareup.moshi.Moshi;

import app.model.ModelAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RepoApi {

    private static final String BASE_URL = "https://api.github.com/";

    private static Retrofit retrofit;
    private static RepoService repoService;

    private RepoApi() { }

    public static RepoService getRepoService() {
        if (repoService != null) {
            return repoService;
        }
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(
                            MoshiConverterFactory.create(
                                    new Moshi.Builder()
                                        .add(ModelAdapterFactory.create())
                                        .build()
                            )
                    )
                    .build();
        }
        repoService = retrofit.create(RepoService.class);
        return repoService;
    }
}

