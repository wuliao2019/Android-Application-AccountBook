package com.cqu.accountbook.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.cqu.accountbook.Card;
import com.cqu.accountbook.CardsAdapter;
import com.cqu.accountbook.R;

import java.util.ArrayList;

public class AddExpenditure extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final ArrayList<Card> outTypeList = new ArrayList<>();
    public static CardsAdapter adapter;

    public AddExpenditure() {
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
    public static AddExpenditure newInstance(String param1, String param2) {
        AddExpenditure fragment = new AddExpenditure();
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
        View root = inflater.inflate(R.layout.add_expenditure, container, false);
        GridView gridView = root.findViewById(R.id.exTypeList);
        initCards();
        adapter = new CardsAdapter(this.getContext(), outTypeList, 0);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            HomeFragment.inOut = true;
            HomeFragment.type = outTypeList.get(position).getName();
            adapter.setSelPos(position);
            if (AddIncome.adapter != null) {
                AddIncome.adapter.setSelPos(-1);
                AddIncome.adapter.notifyDataSetChanged();
                Handler handler = new Handler();
                handler.postDelayed(() -> AddIncome.adapter.notifyDataSetChanged(), 500);
                Log.d("TAG", "update income from expenditure");
            }
            adapter.notifyDataSetChanged();
        });
        return root;
    }


    private void initCards() {
        outTypeList.add(new Card("餐饮", R.drawable.ic_expenditure_catering));
        outTypeList.add(new Card("日用", R.drawable.ic_expenditure_daily));
        outTypeList.add(new Card("服饰", R.drawable.ic_expenditure_clothes));
        outTypeList.add(new Card("购物", R.drawable.ic_expenditure_shopping));
        outTypeList.add(new Card("交通", R.drawable.ic_expenditure_traffic));
        outTypeList.add(new Card("医药", R.drawable.ic_expenditure_medicine));
        outTypeList.add(new Card("办公", R.drawable.ic_expenditure_work));
        outTypeList.add(new Card("其他", R.drawable.ic_expenditure_other));
    }
}
