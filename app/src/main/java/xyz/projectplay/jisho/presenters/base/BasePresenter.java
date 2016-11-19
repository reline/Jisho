package xyz.projectplay.jisho.presenters.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<M, V> {
    protected M model;
    @Nullable
    private WeakReference<V> view;

    public void setModel(M model) {
        this.model = model;
    }

    protected void resetState() {

    }

    public void bindView(@NonNull V view) {
        this.view = new WeakReference<>(view);
    }

    public void unbindView() {
        this.view = null;
    }

    protected V view() {
        if (view == null) {
            return null;
        } else {
            return view.get();
        }
    }

    protected boolean setupDone() {
        return view() != null && model != null;
    }
}