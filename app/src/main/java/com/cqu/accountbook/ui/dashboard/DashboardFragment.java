package com.cqu.accountbook.ui.dashboard;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cqu.accountbook.AccountItemAdapter;
import com.cqu.accountbook.R;
import com.cqu.accountbook.database.DatabaseAction;
import com.cqu.accountbook.database.MyDatabase;
import com.cqu.accountbook.databinding.FragmentDashboardBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {
    public static List<MyDatabase> itemList = new ArrayList<>();
    public static AccountItemAdapter adapter = new AccountItemAdapter(itemList);
    private final List<String> ioList = new ArrayList<>(Arrays.asList("全部", "支出", "收入"));
    private final List<String> iList = new ArrayList<>(Arrays.asList("所有类别", "工资", "理财", "礼金", "其他"));
    private final List<String> oList = new ArrayList<>(Arrays.asList("所有类别", "餐饮", "日用", "服饰", "购物", "交通", "医药", "办公", "其他"));
    private final List<String> iaoList = new ArrayList<>(Arrays.asList("所有类别", "工资", "理财", "礼金", "餐饮", "日用", "服饰", "购物", "交通", "医药", "办公", "其他"));
    private final List<List<String>> typeList = new ArrayList<>(Arrays.asList(iaoList, oList, iList));
    private int inOut = 0;
    public String type = "所有类别";
    private long fn = 0;
    private long tn = 9999999999L;
    private String yyyy = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    private String mm = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
    boolean allDt = false;
    public static final int COMPLETED = -1;

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView recyclerView = root.findViewById(R.id.searchList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AccountItemAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, View v) {
                Dialog dialog = new Dialog(getContext());
                //去掉标题线
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.detail, null, false);
                MyDatabase mdb = itemList.get(position);
                TextView txt = view.findViewById(R.id.detailInOut);
                txt.setText((mdb.getInOut() ? "支出" : "收入"));
                txt = view.findViewById(R.id.detailType);
                txt.setText("：" + mdb.getType());
                txt = view.findViewById(R.id.detailDate);
                txt.setText(String.format(Locale.CHINA, "%d年%d月%d日", mdb.getYear(), mdb.getMonth(), mdb.getDay()));
                txt = view.findViewById(R.id.detailMoney);
                txt.setText(String.format("%.2f", (double) mdb.getMoney() / 100));
                txt = view.findViewById(R.id.detailRemark);
                txt.setText(mdb.getRemark());
                dialog.setContentView(view);
                //背景透明
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }

            @Override
            public void onLongClick(int position, View v) {
                editMenu(position, v);
            }
        });
        TextView fromN = root.findViewById(R.id.fromN);
        TextView toN = root.findViewById(R.id.toN);
        ImageButton button = root.findViewById(R.id.searchBt);
        button.setOnClickListener(v -> {
            if (!String.valueOf(fromN.getText()).equals("")) {
                fn = (long) (Double.parseDouble(String.valueOf(fromN.getText())) * 100);
            } else {
                fn = 0;
            }
            if (!String.valueOf(toN.getText()).equals("")) {
                tn = (long) (Double.parseDouble(String.valueOf(toN.getText())) * 100);
            } else {
                tn = 9999999999L;
            }
            refreshList();
        });
        TextView yTxt = root.findViewById(R.id.yyyy);
        TextView mTxt = root.findViewById(R.id.mm);
        yTxt.setText(yyyy);
        mTxt.setText(mm + "月");
        TimePickerView pvTime = new TimePickerBuilder(getContext(), (date, v) -> {
            yyyy = new SimpleDateFormat("yyyy").format(date);
            mm = new SimpleDateFormat("MM").format(date);
            yTxt.setText(yyyy);
            mTxt.setText(mm + "月");
            refreshList();
        }).setType(new boolean[]{true, true, false, false, false, false}).build();
        LinearLayout chm = root.findViewById(R.id.chooseYM);
        chm.setOnClickListener(v -> {
            pvTime.setDate(Calendar.getInstance());
            pvTime.show();
        });
        Switch allDate = root.findViewById(R.id.allDate);
        allDate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                allDt = true;
                yTxt.setText("----");
                mTxt.setText("--月");
                chm.setEnabled(false);
            } else {
                allDt = false;
                yTxt.setText(yyyy);
                mTxt.setText(mm + "月");
                chm.setEnabled(true);
            }
            refreshList();
        });
        TextView ioType = root.findViewById(R.id.chooseIoType);
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (options1, option2, option3, v) -> {
            //返回的分别是三个级别的选中位置
            inOut = options1;
            type = typeList.get(options1).get(option2);
            String tx = ioList.get(options1) + "：" + typeList.get(options1).get(option2);
            ioType.setText(tx);
            refreshList();
        }).build();
        pvOptions.setPicker(ioList, typeList);
        ioType.setOnClickListener(v -> pvOptions.show());
        refreshList();
        return root;
    }

    private void editMenu(int position, View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.long_click_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.delete) {
                new Thread(() -> {
                    DatabaseAction.getInstance(getContext()).getAllIncomesDao().delete(itemList.get(position));
                    refreshList();
                    Looper.prepare();
                    Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }).start();
            }
            return false;
        });
        popupMenu.show();
    }

    public static final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                adapter.setData(itemList);
                adapter.notifyDataSetChanged();
            }
        }
    };

    private void refreshList() {
        new Thread(() -> {
            itemList = DatabaseAction.getInstance(getContext()).getAllIncomesDao().search(inOut, type, fn, tn, Integer.parseInt(yyyy), Integer.parseInt(mm), allDt);
            Message msg = new Message();
            msg.what = COMPLETED;
            handler.sendMessage(msg);
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}