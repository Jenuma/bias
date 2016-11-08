package io.whitegoldlabs.bias.common;

import java.util.ArrayList;
import io.whitegoldlabs.bias.models.Item;

//TODO: Document this.
public interface IObserver
{
    void update(ArrayList<Item> newItems);
}
