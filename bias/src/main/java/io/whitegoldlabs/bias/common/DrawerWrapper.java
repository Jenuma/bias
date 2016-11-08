package io.whitegoldlabs.bias.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import static com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener;

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
import io.whitegoldlabs.bias.features.EditCartActivity;
import io.whitegoldlabs.bias.features.ItemLocatorActivity;
import io.whitegoldlabs.bias.features.MainActivity;
import io.whitegoldlabs.bias.models.Item;

public class DrawerWrapper
{
    private static final int HOME_ID = 1;
    private static final int MY_LIST_ID = 2;
    private static final int ITEM_LOCATOR_ID = 3;
    private static final int MANAGE_SHARING_ID = 4;
    private static final int EDIT_CART_ID = 5;
    private static final int EDIT_MAP_ID = 6;
    private static final int SETTINGS_ID = 7;
    private static final int SIGN_OUT_ID = 8;

    private static int primaryColor;
    private static int secondaryColor;
    private static int accentColor;

    /**
     * Builds a custom navigation drawer for the calling activity with the given toolbar.
     *
     * @param activity The activity building the drawer.
     * @param toolbar The toolbar of the activity that needs the hamburger button.
     */
    public static Drawer build(Activity activity, Toolbar toolbar)
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
                    HOME_ID,
                    FontAwesome.Icon.faw_home,
                    "Home",
                    ""
                ),
                new ExpandableDrawerItem()
                    .withIdentifier(MY_LIST_ID)
                    .withIcon(FontAwesome.Icon.faw_list)
                    .withName("My List")
                    .withDescription("X of Y items crossed.")
                    .withSubItems(getListItems(activity))
                    .withTextColor(primaryColor)
                    .withIconColor(accentColor)
                    .withDescriptionTextColor(secondaryColor),
                buildDrawerItem
                (
                    ITEM_LOCATOR_ID,
                    FontAwesome.Icon.faw_map_marker,
                    "Item Locator",
                    "Walmart Supercenter #853"
                ),
                buildDrawerItem
                (
                    MANAGE_SHARING_ID,
                    FontAwesome.Icon.faw_users,
                    "Manage Sharing",
                    ""
                ),
                buildDrawerItem
                (
                    EDIT_CART_ID,
                    FontAwesome.Icon.faw_shopping_cart,
                    "Edit Cart",
                    ""
                ),
                buildDrawerItem
                (
                    EDIT_MAP_ID,
                    FontAwesome.Icon.faw_map,
                    "Edit Map",
                    ""
                ),
                buildDrawerItem
                (
                    SETTINGS_ID,
                    FontAwesome.Icon.faw_cog,
                    "Settings",
                    ""
                ),
                buildDrawerItem
                (
                    SIGN_OUT_ID,
                    FontAwesome.Icon.faw_sign_out,
                    "Sign Out",
                    ""
                )
            )
            .withSelectedItem(getSelectedItem(activity))
            .withCloseOnClick(true)
            .withOnDrawerItemClickListener(getOnDrawerItemClickListener(activity))
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
        return new PrimaryDrawerItem()
            .withIdentifier(id)
            .withIcon(icon)
            .withIconColor(accentColor)
            .withName(name)
            .withTextColor(primaryColor)
            .withDescription(desc)
            .withDescriptionTextColor(secondaryColor)
            .withSelectable(false);
    }

    /**
     * Gets the ID of the item that should appear selected by looking at the calling
     * activity.
     *
     * @param activity The activity building the drawer.
     * @return The ID of the item that should appear selected.
     */
    private static int getSelectedItem(Activity activity)
    {
        switch(activity.getClass().getSimpleName())
        {
            case "MainActivity":
                return HOME_ID;
            case "ItemLocatorActivity":
                return ITEM_LOCATOR_ID;
            case "ManageSharingActivity":
                return MANAGE_SHARING_ID;
            case "EditCartActivity":
                return EDIT_CART_ID;
            case "EditMapActivity":
                return EDIT_MAP_ID;
            case "SettingsActivity":
                return SETTINGS_ID;
            default:
                return -1;
        }
    }

    /**
     * Creates a new OnDrawerItemClickListener for clicking an item in the drawer.
     *
     * @param activity The activity the drawer resides in.
     * @return The new OnDrawerItemClickListener.
     */
    private static OnDrawerItemClickListener getOnDrawerItemClickListener
    (
            final Activity activity
    )
    {
        return new OnDrawerItemClickListener()
        {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem)
            {
                if(!drawerItem.isSelected())
                {
                    Class dest;
                    switch((int)drawerItem.getIdentifier())
                    {
                        case HOME_ID:
                            dest = MainActivity.class;
                            break;
                        case ITEM_LOCATOR_ID:
                            dest = ItemLocatorActivity.class;
                            break;
                        case MANAGE_SHARING_ID:
                            return false;
                        case EDIT_CART_ID:
                            dest = EditCartActivity.class;
                            break;
                        case EDIT_MAP_ID:
                            return false;
                        case SETTINGS_ID:
                            return false;
                        case SIGN_OUT_ID:
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            auth.signOut();
                            return false;
                        default:
                            return false;
                    }

                    Intent intent = new Intent(activity, dest);
                    activity.startActivity(intent);
                }
                return false;
            }
        };
    }

    //TODO: Replace this with actual cart items.
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
