package qa.tecnositafgulf.config.inventoryEnums;

/**
 * Created by ledio on 6/3/18.
 */
public enum ItemStatusEnum {
    STOCK("In Stock"),
    IN_USE("In Use"),
    DEFECTIVE("Defective"),
    DELETED("Deleted");

    private final String status;

    ItemStatusEnum (String status){
        this.status = status;
    }

    @Override
    public String toString(){
        return status;
    }
}
