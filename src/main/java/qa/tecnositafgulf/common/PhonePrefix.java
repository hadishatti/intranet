package qa.tecnositafgulf.common;

import javax.persistence.Transient;

/**
 * Created by hadi on 5/20/18.
 */
public class PhonePrefix {
    @Transient
    private String country;

    private String code;

    public PhonePrefix(String country, String code) {
        this.country = country;
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return country+" ("+code+")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PhonePrefix that = (PhonePrefix) o;

        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        return code != null ? code.equals(that.code) : that.code == null;

    }

    @Override
    public int hashCode() {
        int result = country != null ? country.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }
}
