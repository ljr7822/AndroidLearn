package cn.iwenddg.animation;

import android.app.Application;
import android.content.Context;

import cn.iwenddg.animation.utils.CommonModule;

/**
 * @author iwen大大怪
 * @create 2021/11/05 17:49
 */
public class NavApplication extends Application {

    public static Application gContext;

    public static Context getContext() {
        return gContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        CommonModule.init(this);
    }
}
