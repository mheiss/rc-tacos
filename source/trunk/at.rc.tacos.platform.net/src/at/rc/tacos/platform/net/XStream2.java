package at.rc.tacos.platform.net;

import java.io.Writer;

import at.rc.tacos.platform.model.Address;
import at.rc.tacos.platform.model.CallerDetail;
import at.rc.tacos.platform.model.Competence;
import at.rc.tacos.platform.model.DayInfoMessage;
import at.rc.tacos.platform.model.DialysisPatient;
import at.rc.tacos.platform.model.Disease;
import at.rc.tacos.platform.model.Job;
import at.rc.tacos.platform.model.Link;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.Lock;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.model.Patient;
import at.rc.tacos.platform.model.Period;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.model.SickPerson;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.model.SystemMessage;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.message.AddMessage;
import at.rc.tacos.platform.net.message.ExecMessage;
import at.rc.tacos.platform.net.message.GetMessage;
import at.rc.tacos.platform.net.message.RemoveMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Enhanced {@link XStream} implementation for common usage in the project
 * 
 * @author Michael
 */
public class XStream2 extends XStream {

	/**
	 * Default class constructor
	 */
	public XStream2() {
		super(new XppDriverImpl());
	}

	@Override
	protected boolean useXStream11XmlFriendlyMapper() {
		return true;
	}

	/**
	 * Initializes alias for common used classes
	 */
	@Override
	protected void setupAliases() {
		super.setupAliases();
		// the messages types
		alias("addMessage", AddMessage.class);
		alias("updateMessage", UpdateMessage.class);
		alias("removeMessage", RemoveMessage.class);
		alias("getMessage", GetMessage.class);
		alias("execMessage", ExecMessage.class);

		// the model classes
		alias("address", Address.class);
		alias("callerDetail", CallerDetail.class);
		alias("competence", Competence.class);
		alias("dayInfo", DayInfoMessage.class);
		alias("dialysePatient", DialysisPatient.class);
		alias("disease", Disease.class);
		alias("job", Job.class);
		alias("link", Link.class);
		alias("location", Location.class);
		alias("lock", Lock.class);
		alias("login", Login.class);
		alias("mobilePhone", MobilePhoneDetail.class);
		alias("patient", Patient.class);
		alias("period", Period.class);
		alias("rosterEntry", RosterEntry.class);
		alias("serviceType", ServiceType.class);
		alias("sickPerson", SickPerson.class);
		alias("staffMember", StaffMember.class);
		alias("system", SystemMessage.class);
		alias("transport", Transport.class);
		alias("vehicle", VehicleDetail.class);
	}

	/**
	 * Specialized implemenation of the xpp driver to use a compact writer
	 * instead of the pretty printer
	 * 
	 * @author Michael
	 */
	public static class XppDriverImpl extends XppDriver {

		@Override
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new CompactWriter(out);
		}
	}
}
