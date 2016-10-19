package io.whitegoldlabs.bias.common;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.whitegoldlabs.bias.models.Item;

public class ItemAdapter extends ArrayAdapter
{
    private ArrayList<Item> items;

    public ItemAdapter(ArrayList<Item> items, Context context)
    {
        super(context, android.R.layout.simple_list_item_1, items);

        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        TextView textView = (TextView)inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        Item item = items.get(position);

        textView.setText(item.getName());

        if(item.isCrossed())
        {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        return textView;
    }
}
