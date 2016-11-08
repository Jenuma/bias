package io.whitegoldlabs.bias.common;

import java.util.ArrayList;
import io.whitegoldlabs.bias.models.Item;

//TODO: Document this.
public interface IObserver
{
    //TODO: Probably gonna need to make this more complex with various kinds of updates.
    //TODO: For example, updateListChanging, updateListChanged;
    //TODO: Lock the form when list changing, maybe hide list and show ProgressBar
    //TODO: Unlock form and show list/hide Progress bar when list changed
    void update(ArrayList<Item> newItems);
}
