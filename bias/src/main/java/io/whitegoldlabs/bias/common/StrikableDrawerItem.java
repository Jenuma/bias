package io.whitegoldlabs.bias.common;

import android.view.View;
import android.widget.TextView;

import com.mikepenz.materialdrawer.model.AbstractBadgeableDrawerItem;
import com.mikepenz.materialdrawer.model.BaseViewHolder;

import java.util.List;

//TODO: document this
public class StrikableDrawerItem extends AbstractBadgeableDrawerItem<StrikableDrawerItem>
{
    private boolean isCrossed = false;

    public StrikableDrawerItem withCrossed(boolean crossed)
    {
        this.isCrossed = crossed;
        return this;
    }

    public static class ViewHolder extends BaseViewHolder
    {
        private View badgeContainer;
        private TextView badge;

        public ViewHolder(View view)
        {
            super(view);
            this.badgeContainer = view.findViewById(com.mikepenz.materialdrawer.R.id.material_drawer_badge_container);
            this.badge = (TextView)view.findViewById(com.mikepenz.materialdrawer.R.id.material_drawer_badge);
        }
    }

    @Override
    public void bindView(ViewHolder holder, List payloads)
    {
        super.bindView(holder, payloads);

        
    }
}
