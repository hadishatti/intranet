package qa.tecnositafgulf.dao.companyInfo.clients;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.companyInfo.Client;
import qa.tecnositafgulf.searchcriteria.ClientsSearchCriteira;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by klajdi on 05/05/18.
 */

@Repository
public class ClientDaoImpl implements ClientDao{

    @PersistenceContext
    private EntityManager em;

    public void save(Client client) throws ConstraintViolationException{
        em.merge(client);
    }

    public void remove(Client client) throws ConstraintViolationException{
        em.remove(em.contains(client) ? client : em.merge(client));
    }

    public List<Client> listAll(){
        TypedQuery<Client> query = em.createNamedQuery("Client.listAll", Client.class);
        return query.getResultList();
    }

    public List<Client> listAllOrderByChartId() {
        TypedQuery<Client> query = em.createNamedQuery("Client.listAllOrderByChartId", Client.class);
        return query.getResultList();
    }

    @Override
    public Integer getClientCount(ClientsSearchCriteira clientsSearchCriteira) {
        Query query = clientsSearchCriteira.getCountQuery(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    @Override
    public List<Client> getClientList(ClientsSearchCriteira clientsSearchCriteira) {
        Query query = clientsSearchCriteira.toQuery(em);
        return query.getResultList();
    }

    public int countAll() {
        Query query = em.createNamedQuery("Client.countAll");
        return ((Long)query.getSingleResult()).intValue();
    }

}
