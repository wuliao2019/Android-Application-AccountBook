package com.cqu.accountbook.ui.notifications;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cqu.accountbook.R;
import com.cqu.accountbook.database.DatabaseAction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class YearView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int COMPLETED = -1;
    private LineChartView lineChartView;
    private int yy;
    private long yi;
    private long yo;
    private TextView im;
    private TextView om;
    private TextView am;
    Axis axisX = new Axis();
    Axis axisY = new Axis();
    private final LineChartData data = new LineChartData();
    private final List<AxisValue> monthList = new ArrayList<>();

    public YearView() {
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
    public static YearView newInstance(String param1, String param2) {
        YearView fragment = new YearView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 12; i++)
            monthList.add(new AxisValue(i).setLabel((i + 1) + "月"));
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.year_view, container, false);
        im = root.findViewById(R.id.yinM);
        om = root.findViewById(R.id.youtM);
        am = root.findViewById(R.id.yallM);
        lineChartView = root.findViewById(R.id.yearChart);
        Button button = root.findViewById(R.id.changeYear);
        yy = Calendar.getInstance().get(Calendar.YEAR);
        button.setText(yy + "年");
        TimePickerView pvTime = new TimePickerBuilder(getContext(), (date, v) -> {
            yy = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
            button.setText(yy + "年");
            update(yy);
        }).setType(new boolean[]{true, false, false, false, false, false}).build();
        button.setOnClickListener(v -> pvTime.show());
        axisX.setValues(monthList);
        axisX.setName("月份");
        axisY.setName("结余");
        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);
        update(yy);
        return root;
    }

    private void update(int year) {
        new Thread(() -> {
            List<Line> lines = new ArrayList<>();
            List<PointValue> values = new ArrayList<>();
            yi = 0;
            yo = 0;
            for (int i = 0; i < 12; i++) {
                long yit = DatabaseAction.getInstance(getContext()).getAllIncomesDao().getMonthI(year, i + 1);
                long yot = DatabaseAction.getInstance(getContext()).getAllIncomesDao().getMonthO(year, i + 1);
                yi += yit;
                yo += yot;
                values.add(new PointValue(i, (yit - yot) / 100));
            }
            lines.add(new Line(values));
            data.setLines(lines);
            Message msg = new Message();
            msg.what = COMPLETED;
            handler.sendMessage(msg);
        }).start();
    }

    public final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                im.setText(String.format("%.2f", (double) yi / 100));
                om.setText(String.format("%.2f", (double) yo / 100));
                am.setText(String.format("%.2f", (double) yi / 100 - (double) yo / 100));
                lineChartView.setLineChartData(data);
            }
        }
    };
}
