package qa.tecnositafgulf.dao.administration.inventory.item;

import qa.tecnositafgulf.config.inventoryEnums.ItemStatusEnum;
import qa.tecnositafgulf.model.administration.inventory.Item;

import java.util.List;

/**
 * Created by ledio on 6/3/18.
 */
public interface ItemDao {

    void save(Item item);

    void remove(Item item);

    List<Item> listAllItems();
    Integer countAllItems();

    List<Item> getItemsByStatus(ItemStatusEnum itemStatusEnum);
    Integer countItemsByStatus(ItemStatusEnum itemStatusEnum);

    List<Item> getItemsByName(String name);
    Integer countItemsByName(String name);

    List<Item> getItemsByCategory(String category);
    Integer countItemsByCategory(String category);

    List<Item> getItemsByBrand(String brand);
    Integer countItemsByBrand(String brand);


}
