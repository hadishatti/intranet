package qa.tecnositafgulf.config.inventoryEnums;

/**
 * Created by ledio on 6/3/18.
 */
public enum InventoryActionEnum {

    ITEM_ADDITION("Item Addition"),
    ITEM_REMOVAL("Item Removal"),
    BULK_ITEM_ADDITION("Bulk Addition"),
    BULK_ITEM_REMOVAL("Bulk Removal"),
    ITEM_STATUS_CHANGE("Item Status Change"),
    TRANSFER("Transfer");

    private final String action;

    InventoryActionEnum (String status){
        this.action = status;
    }

    @Override
    public String toString(){
        return action;
    }
}
