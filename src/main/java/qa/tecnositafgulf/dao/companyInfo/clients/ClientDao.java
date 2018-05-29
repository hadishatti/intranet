package qa.tecnositafgulf.dao.companyInfo.clients;

import org.hibernate.exception.ConstraintViolationException;
import qa.tecnositafgulf.model.companyInfo.Client;
import qa.tecnositafgulf.searchcriteria.ClientsSearchCriteira;

import java.util.List;

/**
 * Created by klajdi on 05/05/18.
 */
public interface ClientDao {

    void save(Client client) throws ConstraintViolationException;

    void remove(Client client) throws ConstraintViolationException;

    List<Client> listAll();

    List<Client> listAllOrderByChartId();

    Integer getClientCount(ClientsSearchCriteira clientsSearchCriteira);

    List<Client> getClientList(ClientsSearchCriteira clientsSearchCriteira);

    int countAll();

}
