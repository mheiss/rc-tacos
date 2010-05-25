package at.redcross.tacos.web.beans;

import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name = "localeBean")
public class LocaleBean {

    private TimeZone timeZone;
    private Locale locale;

    @PostConstruct
    protected void init() {
        locale = new Locale("de", "AT");
        timeZone = TimeZone.getTimeZone("Europe/Berlin");
    }

    public Locale getLocale() {
        return locale;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }
}
