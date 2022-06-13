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

public class AddIncome extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final ArrayList<Card> inTypeList = new ArrayList<>();
    public static CardsAdapter adapter;

    public AddIncome() {
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
    public static AddIncome newInstance(String param1, String param2) {
        AddIncome fragment = new AddIncome();
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
        View root = inflater.inflate(R.layout.add_income, container, false);
        GridView gridView = root.findViewById(R.id.inTypeList);
        initCards();
        adapter = new CardsAdapter(this.getContext(), inTypeList, -1);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            HomeFragment.inOut = false;
            HomeFragment.type = inTypeList.get(position).getName();
            adapter.setSelPos(position);
            adapter.notifyDataSetChanged();
            if (AddExpenditure.adapter != null) {
                AddExpenditure.adapter.setSelPos(-1);
                AddExpenditure.adapter.notifyDataSetChanged();
                Handler handler = new Handler();
                handler.postDelayed(() -> AddExpenditure.adapter.notifyDataSetChanged(), 500);
                Log.d("TAG", "update expenditure from income");
            }
        });
        return root;
    }

    private void initCards() {
        inTypeList.add(new Card("工资", R.drawable.ic_income_salary));
        inTypeList.add(new Card("理财", R.drawable.ic_income_wealth_management));
        inTypeList.add(new Card("礼金", R.drawable.ic_income_gift));
        inTypeList.add(new Card("其他", R.drawable.ic_income_other));
    }
}
