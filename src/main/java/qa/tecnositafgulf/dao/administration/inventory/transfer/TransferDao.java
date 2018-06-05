package qa.tecnositafgulf.dao.administration.inventory.transfer;

import qa.tecnositafgulf.model.administration.inventory.Transfer;

import java.util.List;

/**
 * Created by ameljo on 6/3/18.
 */
public interface TransferDao {

    void saveTransfer(Transfer transfer);

    void deleteTransfer(Transfer transfer);

    List<Transfer> getAll();
}
