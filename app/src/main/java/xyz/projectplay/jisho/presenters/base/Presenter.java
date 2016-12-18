package xyz.projectplay.jisho.presenters.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

public abstract class Presenter<V> {
    protected final String TAG = getClass().getSimpleName();
    private WeakReference<V> view;

    protected void onBind() {}
    protected void onUnbind() {}

    public final void takeView(@NonNull V view) {
        if (this.view != null) {
            if (this.view.get() != view) {
                this.view.clear();
                this.view = new WeakReference<>(view);
            }
        } else {
            this.view = new WeakReference<>(view);
        }
        this.onBind();
    }

    public final void dropView(@NonNull V view) {
        this.onUnbind();
        if (this.view != null && this.view.get() == view) {
            this.view = null;
        }
    }

    @Nullable
    protected final V view() {
        if (view == null) {
            return null;
        } else {
            return view.get();
        }
    }

    public boolean hasView() {
        return view() != null;
    }
}