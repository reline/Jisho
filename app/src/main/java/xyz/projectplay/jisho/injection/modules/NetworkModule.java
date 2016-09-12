package xyz.projectplay.jisho.injection.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.schedulers.Schedulers;
import xyz.projectplay.jisho.BuildConfig;
import xyz.projectplay.jisho.network.services.SearchApi;
import xyz.projectplay.jisho.network.services.WordApi;

@Module
public class NetworkModule {

    private static final String BASE_URL = "http://jisho.org";

    @Provides
    @Singleton
    static OkHttpClient provideHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        OkHttpClient.Builder httpClientBuilder = okHttpClient.newBuilder();

        if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(loggingInterceptor);
        }

        // TODO: 9/11/16 either use SimpleXmlConverter to parse HTML or create a JsoupConverterFactory
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .callFactory(httpClientBuilder.build())
                .build();
    }

    @Provides
    SearchApi provideServerApi() {
        return provideRetrofit(provideHttpClient()).create(SearchApi.class);
    }

    @Provides
    WordApi provideWordApi() {
        return provideRetrofit(provideHttpClient()).create(WordApi.class);
    }
}
