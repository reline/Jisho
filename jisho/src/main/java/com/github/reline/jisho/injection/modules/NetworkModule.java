/*
 * Copyright 2017 Nathaniel Reline
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

package com.github.reline.jisho.injection.modules;

import android.support.annotation.NonNull;

import com.github.reline.jisho.BuildConfig;
import com.github.reline.jisho.models.RealmString;
import com.github.reline.jisho.models.RealmStringListTypeAdapter;
import com.github.reline.jisho.network.services.SearchApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmList;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    private static final String BASE_URL = "http://jisho.org/api/v1/";

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(new TypeToken<RealmList<RealmString>>(){}.getType(), RealmStringListTypeAdapter.INSTANCE)
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient provideHttpClient() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            httpClientBuilder.addInterceptor(loggingInterceptor);
        }
        return httpClientBuilder.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(@NonNull OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callFactory(okHttpClient)
                .build();
    }

    @Provides
    SearchApi provideServerApi(Retrofit retrofit) {
        return retrofit.create(SearchApi.class);
    }
}
