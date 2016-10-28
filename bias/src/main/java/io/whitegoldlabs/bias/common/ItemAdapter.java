package io.whitegoldlabs.bias.common;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.whitegoldlabs.bias.models.Item;

/**
 * Custom ArrayAdapter that supports a list of custom Item objects. It is essentially
 * the same, but will print the name of the item and paint it with strike-through if it's
 * state is "crossed".
 */
public class ItemAdapter extends ArrayAdapter<Item>
{
    // Fields -------------------------------------------------------------------------//
    private ArrayList<Item> items;                                                     //
    // --------------------------------------------------------------------------------//

    /**
     * Constructor specifying which list to adapt for and the correct context.
     *
     * @param items The list of items to adapt for.
     * @param context The context concerned with the list of items.
     */
    public ItemAdapter(ArrayList<Item> items, Context context)
    {
        super(context, android.R.layout.simple_list_item_1, items);

        this.items = items;
    }

    /**
     * Facilitates view reuse and cleans up complicated code.
     */
    private static class ViewHolder
    {
        private TextView itemName;
    }

    /**
     * Builds and returns the TextViews that will populate the ListView. The text of each
     * view will be set to the name of the Item object and painted with strike-through if
     * said object is "crossed".
     *
     * @param position The position of the item within the list.
     * @param convertView The view to be reused, if possible.
     * @param parent The ListView that this view will eventually be attached to.
     * @return TextView containing the item's name with strike-through if "crossed".
     */
    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        View view = convertView;
        ViewHolder viewHolder;

        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate
            (
                android.R.layout.simple_list_item_1,
                parent,
                false
            );

            viewHolder = new ViewHolder();
            viewHolder.itemName = (TextView)view.findViewById(android.R.id.text1);

            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)view.getTag();
        }

        Item item = items.get(position);
        viewHolder.itemName.setText(item.getName());

        int flags = item.isCrossed() ?
                viewHolder.itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG :
                viewHolder.itemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG);

        int color = item.isCrossed() ?
                Color.parseColor("#FF0000") : Color.parseColor("#000000");

        viewHolder.itemName.setPaintFlags(flags);
        viewHolder.itemName.setTextColor(color);

        return view;
    }
}
