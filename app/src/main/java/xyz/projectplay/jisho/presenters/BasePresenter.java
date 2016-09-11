package xyz.projectplay.jisho.presenters;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<M, V> {
    protected M model;
    private WeakReference<V> view;

    public void setModel(M model) {
        this.model = model;

//        if (setupDone()) {
//           updateView(); // do something
//        }
    }

    protected void resetState() {

    }

    public void bindView(@NonNull V view) {
        this.view = new WeakReference<>(view);

//        if (setupDone()) {
//            updateView(); // do something
//        }
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

//    protected abstract void updateView();

    protected boolean setupDone() {
        return view() != null && model != null;
    }
}