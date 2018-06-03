package qa.tecnositafgulf.config.inventoryEnums;

/**
 * Created by ledio on 6/3/18.
 */
public enum ItemStatusEnum {
    STOCK("In Stock"),
    INUSE("In use"),
    DEFECTIVE("Defective");

    private final String status;

    ItemStatusEnum (String status){
        this.status = status;
    }

    @Override
    public String toString(){
        return status;
    }
}
