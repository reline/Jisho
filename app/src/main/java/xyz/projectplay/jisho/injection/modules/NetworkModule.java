/*
 * Copyright 2016 Nathaniel Reline
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.projectplay.jisho.injection.modules;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.schedulers.Schedulers;
import xyz.projectplay.jisho.BuildConfig;
import xyz.projectplay.jisho.network.services.ConceptApi;
import xyz.projectplay.jisho.network.services.SearchApi;

@Module
public class NetworkModule {

    private static final String BASE_URL = "http://jisho.org";
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    @Provides
    @Singleton
    static OkHttpClient provideHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
                httpClientBuilder.addInterceptor(loggingInterceptor);
            }
            okHttpClient = httpClientBuilder.build();
        }
        return okHttpClient;
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit(@NonNull OkHttpClient okHttpClient) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .callFactory(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    @Provides
    SearchApi provideServerApi() {
        return provideRetrofit(provideHttpClient()).create(SearchApi.class);
    }

    @Provides
    ConceptApi provideWordApi() {
        return provideRetrofit(provideHttpClient()).create(ConceptApi.class);
    }
}
