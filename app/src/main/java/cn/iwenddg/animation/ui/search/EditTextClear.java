package cn.iwenddg.animation.ui.search;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import cn.iwenddg.animation.R;

/**
 * 自定义EdiText，丰富了自定义样式 & 一键删除
 *
 * @author iwen大大怪
 * @create 2021/11/08 20:31
 */
public class EditTextClear extends AppCompatEditText {
    /**
     * 定义左侧搜索图标 & 一键删除图标
     */
    private Drawable clearDrawable,searchDrawable;

    public EditTextClear(@NonNull Context context) {
        super(context);
        init();
    }

    public EditTextClear(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextClear(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 步骤2：初始化 图标资源
     */
    private void init() {
        clearDrawable = getResources().getDrawable(R.drawable.ic_close);
        searchDrawable = getResources().getDrawable(R.drawable.ic_search);

        setCompoundDrawablesWithIntrinsicBounds(searchDrawable, null, null, null);
        // setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom)介绍
        // 作用：在EditText上、下、左、右设置图标（相当于android:drawableLeft=""  android:drawableRight=""）
        // 注1：setCompoundDrawablesWithIntrinsicBounds（）传入的Drawable的宽高=固有宽高（自动通过getIntrinsicWidth（）& getIntrinsicHeight（）获取）
        // 注2：若不想在某个地方显示，则设置为null
        // 此处设置了左侧搜索图标

        // 另外一个相似的方法：setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom)介绍
        // 与setCompoundDrawablesWithIntrinsicBounds（）的区别：可设置图标大小
        // 传入的Drawable对象必须已经setBounds(x,y,width,height)，即必须设置过初始位置、宽和高等信息
        // x:组件在容器X轴上的起点 y:组件在容器Y轴上的起点 width:组件的长度 height:组件的高度
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setClearIconVisible(hasFocus() && text.length() > 0);
        // hasFocus()返回是否获得EditTEXT的焦点，即是否选中
        // setClearIconVisible（） = 根据传入的是否选中 & 是否有输入来判断是否显示删除图标->>关注1
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        setClearIconVisible(focused && length() > 0);
        // focused = 是否获得焦点
        // 同样根据setClearIconVisible（）判断是否要显示删除图标
    }

    /**
     * 判断是否显示删除图标
     */
    private void setClearIconVisible(boolean visible) {
        setCompoundDrawablesWithIntrinsicBounds(searchDrawable, null,
                visible ? clearDrawable : null, null);
    }

    /**
     * 对删除图标区域设置点击事件，即"点击 = 清空搜索框内容"
     * 原理：当手指抬起的位置在删除图标的区域，即视为点击了删除图标 = 清空搜索框内容
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            // 原理：当手指抬起的位置在删除图标的区域，即视为点击了删除图标 = 清空搜索框内容
            case MotionEvent.ACTION_UP:
                Drawable drawable = clearDrawable;
                if (drawable != null && event.getX() <= (getWidth() - getPaddingRight())
                        && event.getX() >= (getWidth() - getPaddingRight() - drawable.getBounds().width())) {
                    setText("");
                }
                // 判断条件说明
                // event.getX() ：抬起时的位置坐标
                // getWidth()：控件的宽度
                // getPaddingRight():删除图标图标右边缘至EditText控件右边缘的距离
                // 即：getWidth() - getPaddingRight() = 删除图标的右边缘坐标 = X1
                // getWidth() - getPaddingRight() - drawable.getBounds().width() = 删除图标左边缘的坐标 = X2
                // 所以X1与X2之间的区域 = 删除图标的区域
                // 当手指抬起的位置在删除图标的区域（X2=<event.getX() <=X1），即视为点击了删除图标 = 清空搜索框内容
                // 具体示意图请看下图
                break;
        }
        return super.onTouchEvent(event);
    }
}
