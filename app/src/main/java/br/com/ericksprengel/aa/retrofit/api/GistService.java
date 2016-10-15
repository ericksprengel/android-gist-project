package br.com.ericksprengel.aa.retrofit.api;


import java.util.List;

import br.com.ericksprengel.aa.retrofit.model.Gist;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GistService {
    // https://developer.github.com/v3/gists/

    // https://api.github.com/users/ericksprengel/gists
    @GET("users/{user}/gists")
    Call<List<Gist>> listGists(@Path("user") String user, @Query("since") String since);

    // https://api.github.com/gists/public
    @GET("gists/public")
    Call<List<Gist>> listGists(@Query("since") String since);


    // https://api.github.com/gists/0b7cde81d74b4bb9a4b8d99dfe54b424
    @GET("gist/{id}")
    Call<Gist> getGist(@Path("id") String id);
}
