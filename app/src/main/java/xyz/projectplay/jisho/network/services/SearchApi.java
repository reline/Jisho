package xyz.projectplay.jisho.network.services;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import xyz.projectplay.jisho.network.responses.SearchResponse;

public interface SearchApi {

    @GET("search/words")
    Observable<SearchResponse> searchQuery(@Query("keyword") String keyword);
}
