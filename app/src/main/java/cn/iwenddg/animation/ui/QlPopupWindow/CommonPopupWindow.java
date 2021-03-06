package cn.iwenddg.animation.ui.QlPopupWindow;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

import cn.iwenddg.animation.utils.UIUtils;

/**
 * @author iwen大大怪
 * @create 2021/11/09 0:07
 */
public class CommonPopupWindow extends PopupWindow {

    public final PopupController controller;

    public CommonPopupWindow(PopupController controller) {
        this.controller = controller;
    }

    /**
     * 获取宽
     */
    @Override
    public int getWidth() {
        return controller.mPopupView.getMeasuredWidth();
    }

    /**
     * 获取高
     */
    @Override
    public int getHeight() {
        return controller.mPopupView.getMeasuredHeight();
    }

    /**
     * 隐藏
     */
    @Override
    public void dismiss() {
        super.dismiss();
        controller.setBackGroundLevel(1.0f);
    }

    /**
     * 查看界面
     */
    public interface ViewInterface {
        void getChildView(View view, int layoutResId);
    }

    /**
     * 构造方法
     *
     * @param context 上下文
     */
    private CommonPopupWindow(Context context) {
        controller = new PopupController(context, this);
    }

    /**
     * 使用建造者模式
     */
    public static class Builder {

        private final PopupController.PopupParams params;

        private ViewInterface listener;

        public Builder(Context context) {
            params = new PopupController.PopupParams(context);
        }

        /**
         * 设置PopupWindow
         *
         * @param layoutResId 布局ID
         * @return Builder
         */
        public Builder setView(int layoutResId) {
            params.mView = null;
            params.layoutResId = layoutResId;
            return this;
        }

        /**
         * 设置PopupWindow布局
         *
         * @param view View
         * @return Builder
         */
        public Builder setView(View view) {
            params.mView = view;
            params.layoutResId = 0;
            return this;
        }

        /**
         * 设置子View
         *
         * @param listener ViewInterface
         * @return Builder
         */
        public Builder setViewOnclickListener(ViewInterface listener) {
            this.listener = listener;
            return this;
        }

        /**
         * 设置宽度和高度 如果不设置 默认是wrap_content
         *
         * @param width 宽
         * @return Builder
         */
        public Builder setWidthAndHeight(int width, int height) {
            params.mWidth = width;
            params.mHeight = height;
            return this;
        }

        /**
         * 设置背景灰色程度
         *
         * @param level 取值范围:0.0f-1.0f 值越小越暗
         * @return Builder
         */
        public Builder setBackGroundLevel(float level) {
            params.isShowBg = true;
            params.bg_level = level;
            return this;
        }

        /**
         * 是否可点击Outside消失
         *
         * @param touchable 是否可点击
         * @return Builder
         */
        public Builder setOutsideTouchable(boolean touchable) {
            params.isTouchable = touchable;
            return this;
        }

        /**
         * 设置动画
         *
         * @return Builder
         */
        public Builder setAnimationStyle(int animationStyle) {
            params.isShowAnim = true;
            params.animationStyle = animationStyle;
            return this;
        }

        public CommonPopupWindow builder(){
            final CommonPopupWindow popupWindow = new CommonPopupWindow(params.mContext);
            params.apply(popupWindow.controller);
            if (listener != null && params.layoutResId != 0){
                listener.getChildView(popupWindow.controller.mPopupView,params.layoutResId);
            }
            UIUtils.measureWidthAndHeight(popupWindow.controller.mPopupView);
            return popupWindow;
        }

    }
}
