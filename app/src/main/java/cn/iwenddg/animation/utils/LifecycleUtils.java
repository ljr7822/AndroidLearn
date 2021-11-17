package cn.iwenddg.animation.utils;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

/**
 * @author iwen大大怪
 * @create 2021/11/05 17:44
 */
public class LifecycleUtils {
    private LifecycleUtils() {}

    public static <T> AutoDisposeConverter<T> bindLifecycle(
            LifecycleOwner lifecycleOwner) {
        if (lifecycleOwner == null) {
            lifecycleOwner = CommonModule.gAppLifeCycle;
        }

        return AutoDispose.autoDisposable(
                AndroidLifecycleScopeProvider.from(lifecycleOwner, Lifecycle.Event.ON_DESTROY));
    }

    public static <T> AutoDisposeConverter<T> bindLifecycle(
            LifecycleOwner lifecycleOwner, Lifecycle.Event untilEvent) {
        if (lifecycleOwner == null) {
            lifecycleOwner = CommonModule.gAppLifeCycle;
        }

        return AutoDispose.autoDisposable(
                AndroidLifecycleScopeProvider.from(lifecycleOwner, untilEvent));
    }

    public static <L> AutoDisposeConverter<L> bindContext(Context context) {
        if (!(context instanceof LifecycleOwner)) {
            return LifecycleUtils.bindLifecycle(null);
        }

        return LifecycleUtils.bindLifecycle((LifecycleOwner)context, Lifecycle.Event.ON_DESTROY);
    }
}
