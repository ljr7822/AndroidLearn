package cn.iwenddg.animation.fragment;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RemoteViews;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwenddg.animation.R;
import cn.iwenddg.animation.utils.QlNotificationUtil;

/**
 * 通知
 */
public class MineFragment extends SupportFragment {

    private static final String TAG = "MineFragment";

    @BindView(R.id.btn_char)
    Button mCharBtn;

    @BindView(R.id.btn_sub)
    Button mSubBtn;

    View mRoot;

    public MineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, root);
        mRoot = root;
        // 通知栏初始化（适配8.0）
        QlNotificationUtil.setNotificationChannel(getContext());
        return mRoot;
    }

    /**
     * 发送聊天通知
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.btn_char)
    void onClickChar(){
        // 发送同一个通知
        QlNotificationUtil.show(getContext(),"收到一条聊天消息", "今天中午吃什么？",null);

        //发送自定义通知
//        RemoteViews mViews = new RemoteViews(getContext().getPackageName(), R.layout.custom_notify);
//        mViews.setTextViewText(R.id.tv_custom_notify,"正在下载");
//        mViews.setTextViewText(R.id.tv_custom_notify_number,++progress+"0%");
//        mViews.setProgressBar(R.id.pb_custom_notify,10,progress,false);
//        QlNotificationUtil.show(getContext(), StringUtils.getString(et_notification_title), StringUtils.getString(etNotificationContent),mViews, null);
    }

    /**
     * 发送订阅通知
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.btn_sub)
    void onClickSub(){
        // 发送多个通知
        QlNotificationUtil.showMuch(getContext(),"收到一条订阅消息", "今天中午吃什么？",null);
    }

}