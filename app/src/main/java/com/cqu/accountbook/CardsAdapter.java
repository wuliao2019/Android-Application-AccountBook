package com.cqu.accountbook;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class CardsAdapter extends BaseAdapter {
    private final ArrayList<Card> cardsList;
    private final LayoutInflater inflater;
    private int selPos;

    public CardsAdapter(Context context, ArrayList<Card> cardsList, int selPos) {
        super();
        this.cardsList = cardsList;
        this.selPos = selPos;
        inflater = LayoutInflater.from(context);
    }

    public void setSelPos(int selPos) {
        this.selPos = selPos;
    }

    @Override
    public int getCount() {
        return cardsList.size();
    }

    @Override
    public Object getItem(int position) {
        return cardsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.card_item, null);
            holder.cardImage = convertView.findViewById(R.id.typeIcon);
            holder.typeName = convertView.findViewById(R.id.typeName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cardImage.setImageResource(cardsList.get(position).getImageId());
        holder.typeName.setText(cardsList.get(position).getName());
        if (selPos == position)
            holder.cardImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(convertView.getContext(), R.color.yellow)));
        else
            holder.cardImage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(convertView.getContext(), R.color.gray)));
        return convertView;
    }

    public static class ViewHolder {
        ImageView cardImage;
        TextView typeName;
    }
}