package qa.tecnositafgulf.service;

import org.hibernate.exception.ConstraintViolationException;
import qa.tecnositafgulf.model.companyInfo.Client;
import qa.tecnositafgulf.model.companyInfo.Contact;
import qa.tecnositafgulf.model.companyInfo.Partner;
import qa.tecnositafgulf.model.companyInfo.Project;
import qa.tecnositafgulf.searchcriteria.ClientsSearchCriteira;
import qa.tecnositafgulf.searchcriteria.ContactSearchCriteria;
import qa.tecnositafgulf.searchcriteria.PartnerSearchCriteria;
import qa.tecnositafgulf.searchcriteria.ProjectSearchCriteria;

import java.util.List;

/**
 * Created by ameljo on 05/05/18.
 */

public interface CompanyInfoService {

    void savePartner(Partner partner);

    void remove(Partner partner);

    List<Partner> getAllPartners();

    List<Partner> getPartners(PartnerSearchCriteria searchCriteria);

    int getPartnersCount(PartnerSearchCriteria searchCriteria);

    int getAllPartnersCount();

    //Client
    void saveClient(Client client) throws ConstraintViolationException;

    void removeClient(Client client) throws ConstraintViolationException;

    List<Client> listClients();

    List<Client> listAllOrderByChartId();

    Integer getClientCount(ClientsSearchCriteira clientsSearchCriteira);

    List<Client> getClientList(ClientsSearchCriteira clientsSearchCriteira);

    int countClients();

    void saveContact (Contact contact);

    void removeContact(Contact contact);

    List<Contact> listContacts();

    List<Contact> getContactsList(ContactSearchCriteria contactSearchCriteria);

    Integer getContactsCount(ContactSearchCriteria contactSearchCriteria);

    void saveProject (Project project);

    void removeProject (Project project);

    List<Project> listProjects ();

    List<Project> getProjectsList(ProjectSearchCriteria searchCriteria);

    int getProjectsCount(ProjectSearchCriteria searchCriteria);

    List<Project> getActiveProjects();

    List<Project> getLatestActiveProjects(int limit);
}
