package qa.tecnositafgulf.searchcriteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSearchCriteria {

    private String likeEmployeeId;
    private String likeFirstName;
    private String likeFamilyName;
    private String likeAddress;
    private String likeOfficePhoneNumber;
    private String likePersonalPhoneNumber1;
    private String likePersonalPhoneNumber2;
    private String likeRole;
    private String likeDepartment;
    private String likeCompany;

    private static final int DEFAULTPAGESIZE = 5;
    private static final int DEFAULTPAGESTARTINDEX = 0;
    private static final String DEFAULTPAGEORDER = "employeeNumber";
    private static final String DEFAULTPAGEORDERMODE = "desc";

    private Integer startIndex = DEFAULTPAGESTARTINDEX;
    private Integer pageSize = DEFAULTPAGESIZE;
    private String pageOrderMode = DEFAULTPAGEORDERMODE;
    private String orderByFieldName = DEFAULTPAGEORDER;

    public Query toQuery(EntityManager entityManager){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT employee FROM Employee employee, Role role, Department dept, Company comp where employee.role = role.id " +
                " and employee.department = dept.id and employee.department.company = comp.id ");
        Query query = addConstraint(entityManager, sb,
                new StringBuilder(" ORDER BY employee.").append(getOrderByFieldName()).append(" ").append(getPageOrderMode()).toString());

        if (this.getStartIndex() > 0) {
            query.setFirstResult(this.getStartIndex() * this.getPageSize());
        }
        if (this.getPageSize() > 0) {
            query.setMaxResults(this.getPageSize());
        }

        return query;
    }


    public Query getCountQuery(EntityManager em) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(*) FROM Employee employee, Role role, Department dept, Company comp where employee.role = role.id " +
                " and employee.department = dept.id and employee.department.company = comp.id ");
        Query query = addConstraint(em, sb, null);
        return query;
    }

    private Query addConstraint(EntityManager entityManager, StringBuilder sb, String orderBy){

        List<String> conditions = new ArrayList<>();
        boolean isEmployeeId = (likeEmployeeId != null && !likeEmployeeId.isEmpty());
        boolean isFirstName = (likeFirstName != null && !likeFirstName.isEmpty());
        boolean isFamilyName = (likeFamilyName != null && !likeFamilyName.isEmpty());
        boolean isAddress = (likeAddress != null && !likeAddress.isEmpty());
        boolean isOfficePhoneNumber = (likeOfficePhoneNumber != null && !likeOfficePhoneNumber.isEmpty());
        boolean isPersonalPhoneNumber1 = (likePersonalPhoneNumber1 != null && !likePersonalPhoneNumber1.isEmpty());
        boolean isPersonalPhoneNumber2 = (likePersonalPhoneNumber2 != null && !likePersonalPhoneNumber2.isEmpty()) ;
        boolean isRole = (likeRole != null && !likeRole.isEmpty());
        boolean isDepartment = (likeDepartment != null && !likeDepartment.isEmpty());
        boolean isCompany = (likeCompany != null && !likeCompany.isEmpty());
        if(isEmployeeId)
            conditions.add("employee.employeeNumber = :likeEmployeeId");
        if(isFirstName)
            conditions.add("employee.name like :likeFirstName");
        if(isFamilyName)
            conditions.add("employee.familyName like :likeFamilyName");
        if(isAddress)
            conditions.add("employee.address like :likeAddress");
        if(isOfficePhoneNumber)
            conditions.add("employee.officePhoneNumber like :likeOfficePhoneNumber");
        if(isPersonalPhoneNumber1)
            conditions.add("employee.personalPhoneNumber1 like :likePersonalPhoneNumber1");
        if(isPersonalPhoneNumber2)
            conditions.add("employee.personalPhoneNumber2 like :likePersonalPhoneNumber2");
        if(isRole)
            conditions.add("role.description like :likeRole");
        if(isDepartment)
            conditions.add("dept.name like :likeDepartment");
        if(isCompany)
            conditions.add("comp.name like :likeCompany");

        for (int i = 0; i < conditions.size(); i++) {
                sb.append(" and  ");
                sb.append(conditions.get(i));
        }

        //order By condition
        if (orderBy != null && !orderBy.isEmpty())
            sb.append(orderBy);

        // create query
        Query query = entityManager.createQuery(sb.toString());
        if(isEmployeeId)
            query.setParameter("likeEmployeeId", likeEmployeeId);
        if(isFirstName)
            query.setParameter("likeFirstName", "%"+likeFirstName+"%");
        if(isFamilyName)
            query.setParameter("likeFamilyName","%"+likeFamilyName+"%");
        if(isAddress)
            query.setParameter("likeAddress", "%"+likeAddress+"%");
        if(isOfficePhoneNumber)
            query.setParameter("likeOfficePhoneNumber","%"+likeOfficePhoneNumber+"%");
        if(isPersonalPhoneNumber1)
            query.setParameter("likePersonalPhoneNumber1", "%"+likePersonalPhoneNumber1+"%");
        if(isPersonalPhoneNumber2)
            query.setParameter("likePersonalPhoneNumber2","%"+likePersonalPhoneNumber2+"%");
        if(isRole)
            query.setParameter("likeRole", "%"+likeRole+"%");
        if(isDepartment)
            query.setParameter("likeDepartment","%"+likeDepartment+"%");
        if(isCompany)
            query.setParameter("likeCompany","%"+likeCompany+"%");

        return query;
    }


    public void clear() {
        startIndex = DEFAULTPAGESTARTINDEX;
        pageSize = DEFAULTPAGESIZE;
        pageOrderMode = DEFAULTPAGEORDERMODE;
        orderByFieldName = DEFAULTPAGEORDER;
    }

    public void clearFilters() {
        likeEmployeeId = null;
        likeFirstName = null;
        likeFamilyName = null;
        likeAddress = null;
        likeOfficePhoneNumber = null;
        likePersonalPhoneNumber1 = null;
        likePersonalPhoneNumber2 = null;
        likeRole = null;
        likeDepartment = null;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageOrderMode() {
        return pageOrderMode;
    }

    public void setPageOrderMode(String pageOrderMode) {
        this.pageOrderMode = pageOrderMode;
    }

    public String getOrderByFieldName() {
        return orderByFieldName;
    }

    public void setOrderByFieldName(String orderByFieldName) {
        this.orderByFieldName = orderByFieldName;
    }

    public String getLikeEmployeeId() {
        return likeEmployeeId;
    }

    public void setLikeEmployeeId(String likeEmployeeId) {
        this.likeEmployeeId = likeEmployeeId;
    }

    public String getLikeFirstName() {
        return likeFirstName;
    }

    public void setLikeFirstName(String likeFirstName) {
        this.likeFirstName = likeFirstName;
    }

    public String getLikeFamilyName() {
        return likeFamilyName;
    }

    public void setLikeFamilyName(String likeFamilyName) {
        this.likeFamilyName = likeFamilyName;
    }

    public String getLikeAddress() {
        return likeAddress;
    }

    public void setLikeAddress(String likeAddress) {
        this.likeAddress = likeAddress;
    }

    public String getLikeOfficePhoneNumber() {
        return likeOfficePhoneNumber;
    }

    public void setLikeOfficePhoneNumber(String likeOfficePhoneNumber) {
        this.likeOfficePhoneNumber = likeOfficePhoneNumber;
    }

    public String getLikePersonalPhoneNumber1() {
        return likePersonalPhoneNumber1;
    }

    public void setLikePersonalPhoneNumber1(String likePersonalPhoneNumber1) {
        this.likePersonalPhoneNumber1 = likePersonalPhoneNumber1;
    }

    public String getLikePersonalPhoneNumber2() {
        return likePersonalPhoneNumber2;
    }

    public void setLikePersonalPhoneNumber2(String likePersonalPhoneNumber2) {
        this.likePersonalPhoneNumber2 = likePersonalPhoneNumber2;
    }

    public String getLikeRole() {
        return likeRole;
    }

    public void setLikeRole(String likeRole) {
        this.likeRole = likeRole;
    }

    public String getLikeDepartment() {
        return likeDepartment;
    }

    public void setLikeDepartment(String likeDepartment) {
        this.likeDepartment = likeDepartment;
    }

    public String getLikeCompany() {
        return likeCompany;
    }

    public void setLikeCompany(String likeCompany) {
        this.likeCompany = likeCompany;
    }

    @Override
    public String toString() {
        return "EmployeeSearchCriteria{" +
                "likeEmployeeId='" + likeEmployeeId + '\'' +
                ", likeFirstName='" + likeFirstName + '\'' +
                ", likeFamilyName='" + likeFamilyName + '\'' +
                ", likeAddress='" + likeAddress + '\'' +
                ", likeOfficePhoneNumber='" + likeOfficePhoneNumber + '\'' +
                ", likePersonalPhoneNumber1='" + likePersonalPhoneNumber1 + '\'' +
                ", likePersonalPhoneNumber2='" + likePersonalPhoneNumber2 + '\'' +
                ", likeRole='" + likeRole + '\'' +
                ", likeDepartment='" + likeDepartment + '\'' +
                '}';
    }
}
