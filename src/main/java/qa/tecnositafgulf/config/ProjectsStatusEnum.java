package qa.tecnositafgulf.config;

/**
 * Created by ameljo on 5/19/18.
 */
public enum  ProjectsStatusEnum {
    ONGOING("Ongoing"),
    CLOSED("Closed");

    private final String status;

    ProjectsStatusEnum (String status){
        this.status = status;
    }

    @Override
    public String toString(){
        return status;
    }
}
