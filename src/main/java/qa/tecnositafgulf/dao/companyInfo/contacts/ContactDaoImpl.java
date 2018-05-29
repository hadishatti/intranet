package qa.tecnositafgulf.dao.companyInfo.contacts;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.companyInfo.Contact;
import qa.tecnositafgulf.searchcriteria.ContactSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by ameljo on 5/13/18.
 */
@Repository
public class ContactDaoImpl implements ContactDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void saveContact(Contact contact) {
        em.merge(contact);
    }

    @Override
    public void removeContact(Contact contact) {
        em.remove(em.contains(contact) ? contact : em.merge(contact));
    }

    @Override
    public List<Contact> listAll() {
        TypedQuery<Contact> query = em.createNamedQuery("Contact.listAll", Contact.class);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsList(ContactSearchCriteria contactSearchCriteria) {
        Query query = contactSearchCriteria.toQuery(em);
        return query.getResultList();
    }

    @Override
    public int getContactsCount(ContactSearchCriteria contactSearchCriteria) {
        Query query = contactSearchCriteria.getCountQuery(em);
        Number number = (Number) query.getSingleResult();
        return number.intValue();
    }
}
