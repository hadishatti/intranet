package qa.tecnositafgulf.dao.administration.inventory.transfer;

import qa.tecnositafgulf.model.administration.inventory.Transfer;

import java.util.List;

/**
 * Created by ledio on 6/5/18.
 */
public interface TransferDao {

    void save(Transfer transfer);

    void remove(Transfer transfer);

    List<Transfer> getAllTransfers();

    Integer countAllTransfers();
}
