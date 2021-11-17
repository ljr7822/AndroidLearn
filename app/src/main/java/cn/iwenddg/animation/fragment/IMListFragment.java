package cn.iwenddg.animation.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwenddg.animation.R;
import cn.iwenddg.animation.ui.QlPopupWindow.CommonPopupWindow;
import cn.iwenddg.animation.ui.QlPopupWindow.MyOnclickListener;
import cn.iwenddg.animation.ui.QlPopupWindow.PopupAdapter;
import cn.iwenddg.animation.utils.UIUtils;

/**
 * popup window
 */
public class IMListFragment extends SupportFragment implements CommonPopupWindow.ViewInterface {

    private CommonPopupWindow popupWindow;

    @BindView(R.id.showDownPop)
    Button showDownPop;

    @BindView(R.id.showWeChar)
    Button showWeChar;

    @BindView(R.id.showUpPop)
    Button showUpPop;

    @BindView(R.id.showLeftPop)
    Button showLeftPop;

    @BindView(R.id.showRightPop)
    Button showRightPop;

    @BindView(R.id.showAll)
    Button showAll;

    @BindView(R.id.showReminder)
    ImageView showReminder;

    View mRoot;

    public IMListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_imlist, container, false);
        ButterKnife.bind(this, root);
        mRoot = root;
        return mRoot;
    }

    @OnClick(R.id.showDownPop)
    void showDownPop1(){
        showDownPop(showDownPop);
    }

    @OnClick(R.id.showWeChar)
    void showWeChar(){
        showWeChar(showWeChar);
    }

    @OnClick(R.id.showUpPop)
    void showUpPop1(){
        showUpPop(showUpPop);
    }

    @OnClick(R.id.showLeftPop)
    void showLeftPop1(){
        showLeftPop(showLeftPop);
    }

    @OnClick(R.id.showRightPop)
    void showRightPop1(){
        showRightPop(showRightPop);
    }

    @OnClick(R.id.showAll)
    void showAll1(){
        showAll(showAll);
    }

    @OnClick(R.id.showReminder)
    void showReminder1(){
        showReminder(showReminder);
    }

    // 向下弹出
    public void showDownPop(View view) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        popupWindow = new CommonPopupWindow.Builder(getContext())
                .setView(R.layout.popup_down)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimDown)
                .setViewOnclickListener(this)
                .setOutsideTouchable(true)
                .builder();
        popupWindow.showAsDropDown(view);
        //得到button的左上角坐标
//        int[] positions = new int[2];
//        view.getLocationOnScreen(positions);
//        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.NO_GRAVITY, 0, positions[1] + view.getHeight());
    }

    // 右上角
    public void showWeChar(View view) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        popupWindow = new CommonPopupWindow.Builder(getContext())
                .setView(R.layout.pop_window_device)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.pop_add)
                .setViewOnclickListener(this)
                .setOutsideTouchable(true)
                .setBackGroundLevel(0.5f)// 取值范围0.0f-1.0f 值越小越暗
                .builder();
        popupWindow.showAsDropDown(view);
        //得到button的左上角坐标
//        int[] positions = new int[2];
//        view.getLocationOnScreen(positions);
//        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.NO_GRAVITY, 0, positions[1] + view.getHeight());
    }

    //向右弹出
    public void showRightPop(View view) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        popupWindow = new CommonPopupWindow.Builder(getContext())
                .setView(R.layout.popup_left_or_right)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimHorizontal)
                .setViewOnclickListener(this)
                .builder();
        popupWindow.showAsDropDown(view, view.getWidth(), -view.getHeight());
        //得到button的左上角坐标
//        int[] positions = new int[2];
//        view.getLocationOnScreen(positions);
//        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.NO_GRAVITY, positions[0] + view.getWidth(), positions[1]);
    }

    //向左弹出
    public void showLeftPop(View view) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        popupWindow = new CommonPopupWindow.Builder(getContext())
                .setView(R.layout.popup_left_or_right)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimRight)
                .setViewOnclickListener(this)
                .builder();
        popupWindow.showAsDropDown(view, -popupWindow.getWidth(), -view.getHeight());
        //得到button的左上角坐标
//        int[] positions = new int[2];
//        view.getLocationOnScreen(positions);
//        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.NO_GRAVITY, positions[0] - popupWindow.getWidth(), positions[1]);
    }

    //全屏弹出
    public void showAll(View view) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        View upView = LayoutInflater.from(getContext()).inflate(R.layout.popup_up, null);
        //测量View的宽高
        UIUtils.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(getContext())
                .setView(R.layout.popup_up)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.4f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.AnimUp)
                .setViewOnclickListener(this)
                .builder();
        popupWindow.showAsDropDown(mRoot);
        //popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
    }

    //向上弹出
    public void showUpPop(View view) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        popupWindow = new CommonPopupWindow.Builder(getContext())
                .setView(R.layout.popup_left_or_right)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener(this)
                .builder();
        popupWindow.showAsDropDown(view, 0, -(popupWindow.getHeight() + view.getMeasuredHeight()));

        //得到button的左上角坐标
//        int[] positions = new int[2];
//        view.getLocationOnScreen(positions);
//        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.NO_GRAVITY, positions[0], positions[1] - popupWindow.getHeight());
    }


    public void showReminder(View view) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        popupWindow = new CommonPopupWindow.Builder(getContext())
                .setView(R.layout.query_info)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .builder();
        popupWindow.showAsDropDown(view, (int) (-popupWindow.getWidth() + UIUtils.dp2px(getContext(), 20)), -(popupWindow.getHeight() + view.getMeasuredHeight()));
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        //获得PopupWindow布局里的View
        switch (layoutResId) {
            case R.layout.popup_down:
                RecyclerView recycle_view = view.findViewById(R.id.recycle_view);
                recycle_view.setLayoutManager(new GridLayoutManager(getContext(), 3));
                PopupAdapter mAdapter = new PopupAdapter(getContext());
                recycle_view.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new MyOnclickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(), "position is " + position, Toast.LENGTH_SHORT).show();
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                break;
            case R.layout.pop_window_device:
                TextView tv1 = view.findViewById(R.id.tv_1);
                TextView tv2 = view.findViewById(R.id.tv_2);
                TextView tv3 = view.findViewById(R.id.tv_3);
                TextView tv4 = view.findViewById(R.id.tv_4);
                TextView tv5 = view.findViewById(R.id.tv_5);
                tv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "发起群聊", Toast.LENGTH_SHORT).show();
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "添加朋友", Toast.LENGTH_SHORT).show();
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                tv3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "扫一扫", Toast.LENGTH_SHORT).show();
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                tv4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "收付款", Toast.LENGTH_SHORT).show();
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                tv5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "帮助", Toast.LENGTH_SHORT).show();
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                break;
            case R.layout.popup_up:
                Button btn_take_photo =  view.findViewById(R.id.btn_take_photo);
                Button btn_select_photo = view.findViewById(R.id.btn_select_photo);
                Button btn_cancel =  view.findViewById(R.id.btn_cancel);
                btn_take_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "拍照", Toast.LENGTH_SHORT).show();
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                btn_select_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "相册选取", Toast.LENGTH_SHORT).show();
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                        return true;
                    }
                });
                break;
            case R.layout.popup_left_or_right:
                TextView tv_like = (TextView) view.findViewById(R.id.tv_like);
                TextView tv_hate = (TextView) view.findViewById(R.id.tv_hate);
                tv_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "赞一个", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                    }
                });
                tv_hate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "踩一下", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                    }
                });
                break;
        }
    }
}