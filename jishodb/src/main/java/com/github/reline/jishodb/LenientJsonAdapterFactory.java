package com.github.reline.jishodb;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

public class LenientJsonAdapterFactory implements JsonAdapter.Factory {
    public static final LenientJsonAdapterFactory INSTANCE = new LenientJsonAdapterFactory();

    private LenientJsonAdapterFactory() {}

    @Override
    public JsonAdapter<?> create(@NotNull Type type, @NotNull Set<? extends Annotation> annotations, Moshi moshi) {
        return moshi.nextAdapter(this, type, annotations).lenient();
    }
}
