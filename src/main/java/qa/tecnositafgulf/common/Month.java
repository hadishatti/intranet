package qa.tecnositafgulf.common;


import java.util.Arrays;
import java.util.List;

public enum Month {

    JANUARY("JANUARY"),
    FEBURARY("FEBURARY"),
    MARCH("MARCH"),
    APRIL("APRIL"),
    MAY("MAY"),
    JUNE("JUNE"),
    JULY("JULY"),
    AUGUST("AUGUST"),
    SEPTEMBUR("SEPTEMBUR"),
    OCTOBER("OCTOBER"),
    NOVEMBER("NOVEMBER"),
    DECEMBER("DECEMBER");

    private String month;


    Month(String month){
        this.month = month;
    }

    public String getMonth(){
        return month;
    }

    public static List<Month> getMonthList(){
        return Arrays.asList(Month.values());
    }

}
