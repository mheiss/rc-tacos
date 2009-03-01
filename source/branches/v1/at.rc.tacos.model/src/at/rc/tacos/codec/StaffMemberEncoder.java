/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;

public class StaffMemberEncoder implements MessageEncoder {

	@Override
	public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException {
		// Cast the object to a item
		StaffMember member = (StaffMember) message;

		// assert valid
		if (member == null) {
			System.out.println("WARNING: Object staffMember is null and cannot be encoded");
			return;
		}

		// start
		writer.writeStartElement(StaffMember.ID);
		// do we have a funtion, then write it as attribute
		if (member.function != null)
			writer.writeAttribute("function", member.function);

		// write the elements and attributes
		writer.writeStartElement("staffMemberId");
		writer.writeCharacters(String.valueOf(member.getStaffMemberId()));
		writer.writeEndElement();
		// get the encoder for the location
		if (member.getPrimaryLocation() != null) {
			MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(Location.ID);
			encoder.doEncode(member.getPrimaryLocation(), writer);
		}
		// write the elements and attributes
		writer.writeStartElement("lastName");
		writer.writeCharacters(member.getLastName());
		writer.writeEndElement();
		// write the elements and attributes
		writer.writeStartElement("firstName");
		writer.writeCharacters(member.getFirstName());
		writer.writeEndElement();
		// write the elements and attributes
		if (member.getUserName() != null) {
			writer.writeStartElement("userName");
			writer.writeCharacters(member.getUserName());
			writer.writeEndElement();
		}
		// write the elements and attributes
		if (member.getStreetname() != null) {
			writer.writeStartElement("streetname");
			writer.writeCharacters(member.getStreetname());
			writer.writeEndElement();
		}
		// write the elements and attributes
		if (member.getCityname() != null) {
			writer.writeStartElement("cityname");
			writer.writeCharacters(member.getCityname());
			writer.writeEndElement();
		}
		// write the elements and attributes
		if (member.getEMail() != null) {
			writer.writeStartElement("eMail");
			writer.writeCharacters(member.getEMail());
			writer.writeEndElement();
		}

		if (member.getPhone1() != null) {
			writer.writeStartElement("phone1");
			writer.writeCharacters(member.getPhone1());
			writer.writeEndElement();
		}
		if (member.getPhone2() != null) {
			writer.writeStartElement("phone2");
			writer.writeCharacters(member.getPhone2());
			writer.writeEndElement();
		}
		writer.writeStartElement("sex");
		writer.writeCharacters(String.valueOf(member.isMale()));
		writer.writeEndElement();
		// write the elements and attributes
		if (member.getBirthday() != null) {
			writer.writeStartElement("birthday");
			writer.writeCharacters(member.getBirthday());
			writer.writeEndElement();
		}
		// get the encoder for the phone and write the list
		if (!member.getPhonelist().isEmpty()) {
			MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(MobilePhoneDetail.ID);
			for (MobilePhoneDetail detail : member.getPhonelist())
				encoder.doEncode(detail, writer);
		}

		// get the encoder for the competence and write the list
		if (!member.getCompetenceList().isEmpty()) {
			MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(Competence.ID);
			for (Competence comp : member.getCompetenceList())
				encoder.doEncode(comp, writer);
		}
		// end
		writer.writeEndElement();
	}

}
