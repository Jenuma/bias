package io.whitegoldlabs.bias.common;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import com.google.common.collect.HashBiMap;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import static com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener;
import static com.mikepenz.materialdrawer.Drawer.OnDrawerListener;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import io.whitegoldlabs.bias.Bias;
import io.whitegoldlabs.bias.R;
import io.whitegoldlabs.bias.models.Item;

public class DrawerWrapper
{
    private static int primaryColor;
    private static int secondaryColor;
    private static int accentColor;

    /**
     * Builds a custom navigation drawer for the calling activity with the given toolbar.
     *
     * @param activity The activity building the drawer.
     * @param toolbar The toolbar of the activity that needs the hamburger button.
     */
    public static Drawer build
    (
        Activity activity,
        Toolbar toolbar,
        HashBiMap<String, Integer> idMap,
        OnDrawerItemClickListener clickListener,
        OnDrawerListener drawerListener
    )
    {
        AccountHeader headerResult = new AccountHeaderBuilder()
            .withActivity(activity)
            .withHeaderBackground(R.color.colorPrimary)
            .addProfiles
            (
                //TODO: Adapt this to real profiles
                new ProfileDrawerItem().withEmail("jenuma@live.com")
            )
            .build();

        Context context = activity.getBaseContext();
        primaryColor = ContextCompat.getColor(context, R.color.colorPrimary);
        secondaryColor = ContextCompat.getColor(context, R.color.colorSecondaryText);
        accentColor = ContextCompat.getColor(context, R.color.colorAccent);

        return new DrawerBuilder()
            .withAccountHeader(headerResult)
            .withActivity(activity)
            .withToolbar(toolbar)
            .addDrawerItems
            (
                buildDrawerItem
                (
                    idMap.get("MainActivity"),
                    FontAwesome.Icon.faw_home,
                    "Home",
                    ""
                ),
                new ExpandableDrawerItem()
                    .withIdentifier(idMap.get("MyList"))
                    .withIcon(FontAwesome.Icon.faw_list)
                    .withName("My List")
                    .withDescription("X of Y items crossed.")
                    .withSubItems(getListItems(activity))
                    .withTextColor(primaryColor)
                    .withIconColor(accentColor)
                    .withDescriptionTextColor(secondaryColor),
                buildDrawerItem
                (
                    idMap.get("ItemLocatorActivity"),
                    FontAwesome.Icon.faw_map_marker,
                    "Item Locator",
                    "Walmart Supercenter #853"
                ),
                buildDrawerItem
                (
                    idMap.get("ManageSharingActivity"),
                    FontAwesome.Icon.faw_users,
                    "Manage Sharing",
                    ""
                ),
                buildDrawerItem
                (
                    idMap.get("EditCartActivity"),
                    FontAwesome.Icon.faw_shopping_cart,
                    "Edit Cart",
                    ""
                ),
                buildDrawerItem
                (
                    idMap.get("EditMapActivity"),
                    FontAwesome.Icon.faw_map,
                    "Edit Map",
                    ""
                ),
                buildDrawerItem
                (
                    idMap.get("SettingsActivity"),
                    FontAwesome.Icon.faw_cog,
                    "Settings",
                    ""
                ),
                buildDrawerItem
                (
                    idMap.get("SignOut"),
                    FontAwesome.Icon.faw_sign_out,
                    "Sign Out",
                    ""
                )
            )
            .withSelectedItem(idMap.get(activity.getClass().getSimpleName()))
            .withCloseOnClick(false)
            .withOnDrawerItemClickListener(clickListener)
            .withOnDrawerListener(drawerListener)
            .build();
    }

    /**
     * Builds a primary drawer item with a description.
     *
     * @param id The ID for the new drawer item.
     * @param icon The icon for the new drawer item.
     * @param name The name for the new drawer item.
     * @param desc The description for the new drawer item.
     * @return The newly built drawer item.
     */
    private static PrimaryDrawerItem buildDrawerItem
    (
        long id,
        IIcon icon,
        String name,
        String desc
    )
    {
        PrimaryDrawerItem result = new PrimaryDrawerItem()
            .withIdentifier(id)
            .withIcon(icon)
            .withIconColor(accentColor)
            .withName(name)
            .withTextColor(primaryColor)
            .withSelectable(false);

        if(desc.length() > 0)
        {
            result
                .withDescription(desc)
                .withDescriptionTextColor(secondaryColor);
        }

        return result;
    }

    //TODO: Document this.
    private static List<IDrawerItem> getListItems(Activity activity)
    {
        ArrayList<Item> items = ((Bias)activity.getApplication()).getItems();
        List<IDrawerItem> subItems = new ArrayList<>();

        for(Item item : items)
        {
            subItems.add
            (
                new PrimaryDrawerItem()
                    .withName(item.getName())
                    .withSelectable(false)
            );
        }

        return subItems;
    }
}
