package io.whitegoldlabs.bias.common;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import io.whitegoldlabs.bias.R;

public class DrawerBuilder
{
    public static void build(Activity activity, Toolbar toolbar)
    {
        AccountHeader headerResult = new AccountHeaderBuilder()
            .withActivity(activity)
            .withHeaderBackground(R.color.colorPrimary)
            .addProfiles
            (
                new ProfileDrawerItem().withEmail("jenuma@live.com")
            )
            .build();

        Context context = activity.getBaseContext();
        int primaryColor = ContextCompat.getColor(context, R.color.colorPrimary);
        int secondaryColor = ContextCompat.getColor(context, R.color.colorSecondaryText);
        int accentColor = ContextCompat.getColor(context, R.color.colorAccent);

        new com.mikepenz.materialdrawer.DrawerBuilder()
            .withAccountHeader(headerResult)
            .withActivity(activity)
            .withToolbar(toolbar)
            .addDrawerItems
            (
                new PrimaryDrawerItem()
                    .withIdentifier(1)
                    .withIcon(FontAwesome.Icon.faw_home)
                    .withName("Home")
                    .withTextColor(primaryColor)
                    .withIconColor(accentColor)
                    .withSelectable(false),
                new ExpandableDrawerItem()
                    .withIdentifier(2)
                    .withIcon(FontAwesome.Icon.faw_list)
                    .withName("My List")
                    .withDescription("X of Y items crossed.")
                    .withSubItems(getListItems())
                    .withTextColor(primaryColor)
                    .withIconColor(accentColor)
                    .withDescriptionTextColor(secondaryColor)
                    .withSelectable(false),
                new PrimaryDrawerItem()
                    .withIdentifier(3)
                    .withIcon(FontAwesome.Icon.faw_map_marker)
                    .withName("Item Locator")
                    .withDescription("Walmart Supercenter #853")
                    .withTextColor(primaryColor)
                    .withIconColor(accentColor)
                    .withDescriptionTextColor(secondaryColor)
                    .withSelectable(false),
                new PrimaryDrawerItem()
                    .withIdentifier(4)
                    .withIcon(FontAwesome.Icon.faw_users)
                    .withName("Manage Sharing")
                    .withTextColor(primaryColor)
                    .withIconColor(accentColor)
                    .withSelectable(false),
                new PrimaryDrawerItem()
                    .withIdentifier(5)
                    .withIcon(FontAwesome.Icon.faw_shopping_cart)
                    .withName("Edit Cart")
                    .withTextColor(primaryColor)
                    .withIconColor(accentColor)
                    .withSelectable(false),
                new PrimaryDrawerItem()
                    .withIdentifier(6)
                    .withIcon(FontAwesome.Icon.faw_map)
                    .withName("Edit Map")
                    .withTextColor(primaryColor)
                    .withIconColor(accentColor)
                    .withSelectable(false),
                new PrimaryDrawerItem()
                    .withIdentifier(7)
                    .withIcon(FontAwesome.Icon.faw_cog)
                    .withName("Settings")
                    .withTextColor(primaryColor)
                    .withIconColor(accentColor)
                    .withSelectable(false),
                new PrimaryDrawerItem()
                    .withIdentifier(8)
                    .withIcon(FontAwesome.Icon.faw_sign_out)
                    .withName("Sign Out")
                    .withTextColor(primaryColor)
                    .withIconColor(accentColor)
                    .withSelectable(false)
            )
            .withSelectedItem(-1)
            .withCloseOnClick(false)
            .build();
    }

    //TODO: Replace this with actual cart items.
    private static List<IDrawerItem> getListItems()
    {
        List<IDrawerItem> subItems = new ArrayList<>();

        PrimaryDrawerItem item1 = new PrimaryDrawerItem()
            .withName("Item 1")
            .withSelectable(false);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem()
            .withName("Item 2")
            .withSelectable(false);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem()
            .withName("Item 3")
            .withSelectable(false);

        subItems.add(item1);
        subItems.add(item2);
        subItems.add(item3);

        return subItems;
    }
}
