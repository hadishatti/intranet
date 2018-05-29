package qa.tecnositafgulf.dao.companyInfo.contacts;

import qa.tecnositafgulf.model.companyInfo.Contact;
import qa.tecnositafgulf.searchcriteria.ContactSearchCriteria;

import java.util.List;

/**
 * Created by ameljo on 5/13/18.
 */
public interface ContactDao {

    void saveContact(Contact contact);

    void removeContact(Contact contact);

    List<Contact> listAll();

    List<Contact> getContactsList(ContactSearchCriteria contactSearchCriteria);

    int getContactsCount(ContactSearchCriteria contactSearchCriteria);
}
