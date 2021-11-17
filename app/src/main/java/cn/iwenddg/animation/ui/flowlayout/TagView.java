package cn.iwenddg.animation.ui.flowlayout;

import android.content.Context;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;

/**
 * @author iwen大大怪
 * @create 2021/11/12 23:06
 */
public class TagView extends FrameLayout implements Checkable {

    private static final String TAG = "TagView";

    private boolean isChecked;

    private static final int[] CHECK_STATE = new int[]{android.R.attr.state_checked};

    public TagView(Context context) {
        super(context);
    }

    public View getTagView() {
        return getChildAt(0);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        int[] states = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(states, CHECK_STATE);
        }
        return states;
    }


    /**
     * 更改视图的选中状态
     * Change the checked state of the view
     *
     * @param checked The new checked state 新的检查状态
     */
    @Override
    public void setChecked(boolean checked) {
        if (this.isChecked != checked) {
            this.isChecked = checked;
            refreshDrawableState();
        }
    }

    /**
     * @return The current checked state of the view 视图的当前选中状态
     */
    @Override
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * 将视图的选中状态更改为与其当前状态相反的状态
     * Change the checked state of the view to the inverse of its current state
     */
    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}
