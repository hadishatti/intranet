package qa.tecnositafgulf.service;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.tecnositafgulf.config.ProjectsStatusEnum;
import qa.tecnositafgulf.dao.companyInfo.PartnerDao;
import qa.tecnositafgulf.dao.companyInfo.clients.ClientDao;
import qa.tecnositafgulf.dao.companyInfo.contacts.ContactDao;
import qa.tecnositafgulf.dao.companyInfo.projects.ProjectDao;
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

@Service
public class CompanyInfoServiceImpl implements CompanyInfoService {

    @Autowired
    private PartnerDao partnerDao;

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private ProjectDao projectDao;

    @Transactional
    public void savePartner(Partner partner) {
        partnerDao.save(partner);
    }

    @Transactional
    public void remove(Partner partner) {
        partnerDao.remove(partner);
    }

    @Transactional
    public List<Partner> getAllPartners() {
        return partnerDao.listAll();
    }

    @Override
    public List<Partner> getPartners(PartnerSearchCriteria searchCriteria) {
        return partnerDao.getPartnersList(searchCriteria);
    }

    @Override
    public int getPartnersCount(PartnerSearchCriteria searchCriteria) {
        return partnerDao.getPartnersCount(searchCriteria);
    }

    @Transactional
    public int getAllPartnersCount() {
        return partnerDao.countAll();
    }

    @Transactional
    public void saveClient(Client client) throws ConstraintViolationException {
        clientDao.save(client);
    }

    @Transactional
    public void removeClient(Client client) throws ConstraintViolationException {
        clientDao.remove(client);
    }

    @Transactional
    public List<Client> listClients(){
        return clientDao.listAll();
    }

    public List<Client> listAllOrderByChartId(){
        return clientDao.listAllOrderByChartId();
    }

    public Integer getClientCount(ClientsSearchCriteira clientsSearchCriteira){
        return clientDao.getClientCount(clientsSearchCriteira);
    }

    public List<Client> getClientList(ClientsSearchCriteira clientsSearchCriteira){
        return clientDao.getClientList(clientsSearchCriteira);
    }

    public int countClients(){
        return clientDao.countAll();
    }

    @Transactional
    public void saveContact(Contact contact) {
        contactDao.saveContact(contact);
    }

    @Transactional
    public void removeContact(Contact contact) {
        contactDao.removeContact(contact);
    }

    @Transactional
    public List<Contact> listContacts() {
        return contactDao.listAll();
    }

    @Override
    public List<Contact> getContactsList(ContactSearchCriteria contactSearchCriteria) {
        return contactDao.getContactsList(contactSearchCriteria);
    }

    @Override
    public Integer getContactsCount(ContactSearchCriteria contactSearchCriteria) {
        return contactDao.getContactsCount(contactSearchCriteria);
    }

    @Transactional
    public void saveProject(Project project) {
        projectDao.saveProject(project);
    }

    @Transactional
    public void removeProject(Project project) {
        projectDao.removeProject(project);
    }

    @Override
    public List<Project> listProjects() {
        return projectDao.listAll();
    }

    @Override
    public List<Project> getProjectsList(ProjectSearchCriteria searchCriteria) {
        return projectDao.getProjectList(searchCriteria);
    }

    @Override
    public int getProjectsCount(ProjectSearchCriteria searchCriteria) {
        return projectDao.getProjectCount(searchCriteria);
    }

    @Override
    public List<Project> getActiveProjects() {
        return projectDao.projectListByStatus(ProjectsStatusEnum.CLOSED.ONGOING);
    }

    @Override
    public List<Project> getLatestActiveProjects(int limit) {
        return projectDao.projectListByStatusOrderDate(ProjectsStatusEnum.ONGOING, limit);
    }
}
