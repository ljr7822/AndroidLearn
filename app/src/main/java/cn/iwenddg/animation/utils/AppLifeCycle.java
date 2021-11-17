package cn.iwenddg.animation.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

/**
 * @author iwen大大怪
 * @create 2021/11/05 17:47
 */
public class AppLifeCycle implements LifecycleOwner {
    private LifecycleRegistry mLifecycleRegistry;

    public AppLifeCycle() {
        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
    }
    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
}
