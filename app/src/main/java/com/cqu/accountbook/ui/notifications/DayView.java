package com.cqu.accountbook.ui.notifications;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cqu.accountbook.R;
import com.cqu.accountbook.database.DatabaseAction;
import com.cqu.accountbook.database.MyDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class DayView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int COMPLETED = -1;
    private PieChartView pieChart;     //饼状图View
    private PieChartData data;         //存放数据
    private TextView im;
    private TextView om;
    private TextView am;
    private List<MyDatabase> db;
    List<SliceValue> values;
    private long i;
    private long o;

    public DayView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static DayView newInstance(String param1, String param2) {
        DayView fragment = new DayView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.day_view, container, false);
        pieChart = root.findViewById(R.id.pie_chart);
        im = root.findViewById(R.id.inM);
        om = root.findViewById(R.id.outM);
        am = root.findViewById(R.id.allM);
        pieChart.setChartRotationEnabled(false);
        Calendar calendar = Calendar.getInstance();
        update(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        CalendarView calendarView = root.findViewById(R.id.calendarView);
        calendarView.setDate(calendar.getTimeInMillis());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                update(year, month + 1, dayOfMonth);
            }
        });
        return root;
    }

    private void update(int year, int month, int dayOfMonth) {
        new Thread(() -> {
            i = DatabaseAction.getInstance(getContext()).getAllIncomesDao().dayIn(year, month, dayOfMonth);
            o = DatabaseAction.getInstance(getContext()).getAllIncomesDao().dayOut(year, month, dayOfMonth);
            db = DatabaseAction.getInstance(getContext()).getAllIncomesDao().getDayExpense(year, month, dayOfMonth);
            values = new ArrayList<>();
            for (MyDatabase d : db) {
                SliceValue sliceValue = new SliceValue((float) ((double) d.getMoney() / 100), ChartUtils.pickColor());
                values.add(sliceValue);
            }
            data = new PieChartData(values);
            data.setHasLabels(true);
            Message msg = new Message();
            msg.what = COMPLETED;
            handler.sendMessage(msg);
        }).start();
    }

    public final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                im.setText(String.format("%.2f", (double) i / 100));
                om.setText(String.format("%.2f", (double) o / 100));
                am.setText(String.format("%.2f", (double) i / 100 - (double) o / 100));
                pieChart.setPieChartData(data);
            }
        }
    };
}
