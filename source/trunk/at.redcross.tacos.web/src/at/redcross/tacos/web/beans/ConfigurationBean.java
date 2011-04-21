package at.redcross.tacos.web.beans;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import at.redcross.tacos.web.faces.FacesUtils;

@SessionScoped
@ManagedBean(name = "configBean")
public class ConfigurationBean implements Serializable {

    private static final long serialVersionUID = 5009671295425284829L;

    /** flag signaling whether or not rich text-editing is allowed */
    /** TinyMCE seems to have troubles with IE9 */
    private boolean richEditor;

    @PostConstruct
    protected void initBean() {
        richEditor = detectEditorSupport();
    }

    public boolean isRichEditor() {
        return richEditor;
    }

    /** Returns whether or not the browser supports rich editor */
    private boolean detectEditorSupport() {
        String userAgent = FacesUtils.getHttpServletRequest().getHeader("user-agent");
        Pattern pattern = Pattern.compile("MSIE ([0-9]{1,}[\\.0-9]{0,})");
        Matcher matcher = pattern.matcher(userAgent);
        if(!matcher.find()) {
            return true;
        }
        String group = matcher.group(1);
        float version = Float.parseFloat(group);
        if (version >= 9) {
            return false;
        }
        return true;
    }

}
