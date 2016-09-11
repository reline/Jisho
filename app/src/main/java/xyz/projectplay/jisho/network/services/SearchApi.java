package xyz.projectplay.jisho.network.services;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface SearchApi {

    @GET("/search/{query}")
    Observable<ResponseBody> searchQuery(@Path("query") String query);
}
