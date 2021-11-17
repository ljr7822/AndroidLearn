package cn.iwenddg.animation.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

import butterknife.BindView;
import cn.iwenddg.animation.R;
import cn.iwenddg.animation.ui.flowlayout.FlowLayout;
import cn.iwenddg.animation.ui.flowlayout.RecordsDao;
import cn.iwenddg.animation.ui.flowlayout.TagAdapter;
import cn.iwenddg.animation.ui.flowlayout.TagFlowLayout;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends SupportFragment {

    private static final String TAG = "HomeFragment";

    private RecordsDao mRecordsDao;
    // 默然展示词条个数
    private final int DEFAULT_RECORD_NUMBER = 10;
    private List<String> recordList = new ArrayList<>();
    private TagAdapter mRecordsAdapter;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.edit_query)
    EditText editText;
    @BindView(R.id.iv_clear_search)
    ImageView clearSearch;
    @BindView(R.id.iv_search)
    TextView ivSearch;
    @BindView(R.id.cl_toolbar)
    ConstraintLayout clToolbar;
    @BindView(R.id.tv_history_hint)
    TextView tvHistoryHint;
    @BindView(R.id.clear_all_records)
    ImageView clearAllRecords;
    @BindView(R.id.fl_search_records)
    TagFlowLayout tagFlowLayout;
    @BindView(R.id.iv_arrow)
    ImageView moreArrow;
    @BindView(R.id.ll_history_content)
    LinearLayout mHistoryContent;

    public String mContent;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_search_new, container, false);
        ButterKnife.bind(this, v);
        // 默认账号
        String username = "a";
        // 初始化数据库
        mRecordsDao = new RecordsDao(getContext(), username);
        initView();
        initData();
        return v;
    }

    private void initView() {
        //创建历史标签适配器
        //为标签设置对应的内容
        mRecordsAdapter = new TagAdapter<String>(recordList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tv_history, tagFlowLayout, false);
                // 为标签设置对应的内容
                tv.setText(s);
                return tv;
            }
        };

        tagFlowLayout.setAdapter(mRecordsAdapter);
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public void onTagClick(View view, int position, FlowLayout parent) {
                // 清空editText之前的数据
                editText.setText("");
                // 将获取到的字符串传到搜索结果界面,点击后搜索对应条目内容
                editText.setText(recordList.get(position));
                editText.setSelection(editText.length());
            }
        });
        // 删除某个历史条目
        tagFlowLayout.setOnLongClickListener(new TagFlowLayout.OnLongClickListener() {
            @Override
            public void onLongClick(View view, final int position) {
                showDialog("确定要删除该条历史记录？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 删除某一条记录
                        mRecordsDao.deleteRecord(recordList.get(position));
                    }
                });
            }
        });

        // view加载完成时回调
        tagFlowLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean isOverFlow = tagFlowLayout.isOverFlow();
                boolean isLimit = tagFlowLayout.isLimit();
                if (isLimit && isOverFlow) {
                    moreArrow.setVisibility(View.VISIBLE);
                } else {
                    moreArrow.setVisibility(View.GONE);
                }
            }
        });

        // 加载更多按钮
        moreArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagFlowLayout.setLimit(false);
                mRecordsAdapter.notifyDataChanged();
            }
        });

        // 清除所有记录
        clearAllRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("确定要删除全部历史记录？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tagFlowLayout.setLimit(true);
                        //清除所有数据
                        mRecordsDao.deleteUsernameAllRecords();
                    }
                });
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String record = editText.getText().toString();
                if (!TextUtils.isEmpty(record)) {
                    // 添加数据
                    mRecordsDao.addRecords(record);
                    // 把内容拿到
                    mContent = record;
                    Log.i(TAG, "input content == " + mContent);
                    // 点击搜索后清空输入框
                    editText.setText("");
                }
            }
        });

        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清除搜索历史
                editText.setText("");
            }
        });

        mRecordsDao.setNotifyDataChanged(new RecordsDao.NotifyDataChanged() {
            @Override
            public void notifyDataChanged() {
                initData();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//搜索按键action
                    hideSoftInput();
                    mContent = editText.getText().toString();
                    if (TextUtils.isEmpty(mContent)) {
                        return true;
                    }
                    Log.i(TAG, "开始搜索" + mContent);
                    return true;
                }
                return false;
            }
        });
    }


    private void setClearIconVisible() {

    }

    private void showDialog(String dialogTitle, @NonNull DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(dialogTitle);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    private void initData() {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                emitter.onNext(mRecordsDao.getRecordsByNumber(DEFAULT_RECORD_NUMBER));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> s) throws Exception {
                        recordList.clear();
                        recordList = s;
                        if (null == recordList || recordList.size() == 0) {
                            mHistoryContent.setVisibility(View.GONE);
                        } else {
                            mHistoryContent.setVisibility(View.VISIBLE);
                        }
                        if (mRecordsAdapter != null) {
                            mRecordsAdapter.setData(recordList);
                            mRecordsAdapter.notifyDataChanged();
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        mRecordsDao.closeDatabase();
        mRecordsDao.removeNotifyDataChanged();
        super.onDestroyView();
    }
}