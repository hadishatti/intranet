package qa.tecnositafgulf.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadi on 12/21/17.
 */
public class IntranetProperties {
    public enum TenderTypes{
        Road("Road"),
        Drainage("Drainage"),
        Building("Company"),
        Consultancy("Consultancy"),
        Procurement("Procurement"),
        ICT("ICT"),
        GeneralServices("General Services");

        private final String type;

        private TenderTypes(final String type){
            this.type = type;
        }

        @Override
        public String toString(){
            return this.type;
        }

        public static List<String> getAll(){
            List<String> types = new ArrayList<String>();
            types.add(Road.toString());
            types.add(Drainage.toString());
            types.add(Building.toString());
            types.add(Consultancy.toString());
            types.add(Procurement.toString());
            types.add(ICT.toString());
            types.add(GeneralServices.toString());
            return types;
        }
    }

    public enum PolicyProcedureType{
        Policy("Policy"),
        Procedure("Procedure");

        private final String type;

        private PolicyProcedureType(final String type){
            this.type = type;
        }

        @Override
        public String toString(){
            return this.type;
        }

        public static List<String> getAll(){
            List<String> types = new ArrayList<String>();
            types.add(Policy.toString());
            types.add(Procedure.toString());
            return types;
        }
    }
}
