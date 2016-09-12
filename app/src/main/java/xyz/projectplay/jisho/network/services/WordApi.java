package xyz.projectplay.jisho.network.services;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface WordApi {

    @GET("/word/{word}")
    Observable<ResponseBody> getWordDetails(@Path("word") String reading);
}
