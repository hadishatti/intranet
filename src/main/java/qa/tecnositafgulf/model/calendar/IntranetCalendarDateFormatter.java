package qa.tecnositafgulf.model.calendar;

import org.zkoss.calendar.api.DateFormatter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class IntranetCalendarDateFormatter implements DateFormatter, Serializable {
    private String _dayFormat = "EEEE MMMM, dd";
    private String _weekFormat = "EEEE";
    private String _timeFormat = "HH:mm";
    private String _ppFormat = "EEEE MMMM, dd";
    private SimpleDateFormat _df, _wf, _tf, _pf;
    public String getCaptionByDate(Date date, Locale locale, TimeZone timezone) {
        if (_df == null) {
            _df = new SimpleDateFormat(_dayFormat, locale);
        }
        _df.setTimeZone(timezone);
        return _df.format(date);
    }
    public String getCaptionByDateOfMonth(Date date, Locale locale, TimeZone timezone) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTimeZone(timezone);
        if (cal.get(Calendar.DAY_OF_MONTH) == 1) {
            SimpleDateFormat sd = new SimpleDateFormat("MMMM, dd", locale);
            sd.setTimeZone(timezone);
            return sd.format(date);
        }
        return Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
    }

    public String getCaptionByDayOfWeek(Date date, Locale locale, TimeZone timezone) {
        if (_wf == null) {
            _wf = new SimpleDateFormat(_weekFormat, locale);
        }
        _wf.setTimeZone(timezone);
        return _wf.format(date);
    }

    public String getCaptionByTimeOfDay(Date date, Locale locale, TimeZone timezone) {
        if (_tf == null) {
            _tf = new SimpleDateFormat(_timeFormat, locale);
        }
        _tf.setTimeZone(timezone);
        return _tf.format(date);
    }
    public String getCaptionByPopup(Date date, Locale locale, TimeZone timezone) {
        if (_pf == null) {
            _pf = new SimpleDateFormat(_ppFormat, locale);
        }
        _pf.setTimeZone(timezone);
        return _pf.format(date);
    }
    public String getCaptionByWeekOfYear(Date date, Locale locale,
                                         TimeZone timezone) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTimeZone(timezone);
        return String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));
    }
}
