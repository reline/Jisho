package xyz.projectplay.jisho.network.services;

import android.support.annotation.NonNull;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface ConceptApi {

    @NonNull
    @GET("/word/{word}")
    Observable<ResponseBody> getWordDetails(@Path("word") String reading);
}
