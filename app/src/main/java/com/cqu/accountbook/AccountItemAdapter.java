package com.cqu.accountbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cqu.accountbook.database.MyDatabase;

import java.util.List;
import java.util.Locale;

public class AccountItemAdapter extends RecyclerView.Adapter<AccountItemAdapter.ViewHolder> {
    private List<MyDatabase> itemsList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemIcon;
        TextView itemType;
        TextView itemDate;
        TextView itemMoney;

        public ViewHolder(View view) {
            super(view);
            itemIcon = view.findViewById(R.id.itemIcon);
            itemType = view.findViewById(R.id.itemType);
            itemDate = view.findViewById(R.id.itemDate);
            itemMoney = view.findViewById(R.id.moneyAcc);
        }
    }

    public AccountItemAdapter(List<MyDatabase> itemsList) {
        this.itemsList = itemsList;
    }

    public void setData(List<MyDatabase> itemsList) {
        this.itemsList = itemsList;
    }

    public interface OnItemClickListener {
        void onClick(int position, View v);

        void onLongClick(int position, View v);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyDatabase cd = itemsList.get(position);
        holder.itemMoney.setText((cd.getInOut() ? "-" : "+") + String.format("%.2f", (double) cd.getMoney() / 100));
        holder.itemDate.setText(String.format(Locale.CHINA, "%d年%d月%d日", cd.getYear(), cd.getMonth(), cd.getDay()));
        holder.itemType.setText(cd.getRemark().equals("") ? cd.getType() : cd.getRemark());

        switch (cd.getType()) {
            case "餐饮":
                holder.itemIcon.setImageResource(R.drawable.ic_expenditure_catering);
                break;
            case "日用":
                holder.itemIcon.setImageResource(R.drawable.ic_expenditure_daily);
                break;
            case "服饰":
                holder.itemIcon.setImageResource(R.drawable.ic_expenditure_clothes);
                break;
            case "购物":
                holder.itemIcon.setImageResource(R.drawable.ic_expenditure_shopping);
                break;
            case "交通":
                holder.itemIcon.setImageResource(R.drawable.ic_expenditure_traffic);
                break;
            case "医药":
                holder.itemIcon.setImageResource(R.drawable.ic_expenditure_medicine);
                break;
            case "办公":
                holder.itemIcon.setImageResource(R.drawable.ic_expenditure_work);
                break;
            case "工资":
                holder.itemIcon.setImageResource(R.drawable.ic_income_salary);
                break;
            case "理财":
                holder.itemIcon.setImageResource(R.drawable.ic_income_wealth_management);
                break;
            case "礼金":
                holder.itemIcon.setImageResource(R.drawable.ic_income_gift);
                break;
            default:
                holder.itemIcon.setImageResource(cd.getInOut() ? R.drawable.ic_expenditure_other : R.drawable.ic_income_other);
                break;
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> mOnItemClickListener.onClick(holder.getAdapterPosition(), v));
            holder.itemView.setOnLongClickListener(v -> {
                mOnItemClickListener.onLongClick(holder.getAdapterPosition(), v);
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}
