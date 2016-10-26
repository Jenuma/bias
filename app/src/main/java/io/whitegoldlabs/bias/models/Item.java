package io.whitegoldlabs.bias.models;

/**
 * Represents a shopping list item which has a name and is either crossed-off or not.
 */
public class Item
{
    // Fields -------------------------------------------------------------------------//
    private int id;                                                                    //
    private String name;                                                               //
    private boolean crossed;                                                           //
    // --------------------------------------------------------------------------------//

    /**
     * Default constructor, left blank for Firebase.
     */
    public Item() {}

    // Accessors ----------------------------------------------------------------------//
    public int getId() {return this.id;}                                               //
    public String getName() {return this.name;}                                        //
    public boolean isCrossed() {return this.crossed;}                                  //
    // --------------------------------------------------------------------------------//

    // Mutators -----------------------------------------------------------------------//
    public void setName(String newName) {this.name = newName;}                         //
    public void setId(int newId) {this.id = newId;}                                    //
    // --------------------------------------------------------------------------------//

    /**
     * Toggles the "crossed" state of the item.
     *
     * @return true if the item is now crossed, false otherwise.
     */
    public boolean toggleCrossed()
    {
        this.crossed = !this.crossed;
        return this.crossed;
    }
}
