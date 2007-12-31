package at.rc.tacos.codec;

import java.util.ArrayList;

import javax.xml.stream.XMLStreamException;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.factory.XMLFactory;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.Transport;

public class TransportDecoderTest 
{
	//the string to test
	String source = "<?xml version=\"1.0\" ?><message><header><userid>user3</userid><timestamp>1198844728203</timestamp><contentType>transport</contentType><queryString>message.add</queryString></header><content><transport><transportId>0</transportId><fromStreet>asdf</fromStreet><fromCity>asf</fromCity><patient><patientId>0</patientId><firstname>asdf</firstname><lastname>sadf</lastname></patient><backTransport>false</backTransport><accompanyingPerson>false</accompanyingPerson><emergencyPhone>false</emergencyPhone><responsibleStation>Breitenau</responsibleStation><dateOfTransport>1198844728203</dateOfTransport><plannedStartOfTransportTime>1198844728203</plannedStartOfTransportTime><transportPriority>C</transportPriority><emergencyDoctoralarming>false</emergencyDoctoralarming><helicopterAlarmingTime>false</helicopterAlarmingTime><blueLightToGoal>false</blueLightToGoal><dfAlarming>false</dfAlarming><brkdtAlarming>false</brkdtAlarming><firebrigadeAlarming>false</firebrigadeAlarming><mountainRescueServiceAlarming>false</mountainRescueServiceAlarming><policeAlarming>false</policeAlarming><direction>1</direction></transport></content></message>";

	@BeforeClass
	public static void oneTimeSetUp() throws XMLStreamException
	{
		//Register the needed additional encoders
		ProtocolCodecFactory.getDefault().registerDecoder(Transport.ID,new TransportDecoder());
		ProtocolCodecFactory.getDefault().registerDecoder(Patient.ID,new PatientDecoder());
	}

	@Test
	public void testDecode()
	{
		XMLFactory factory = new XMLFactory();
		factory.setupDecodeFactory(source);
		ArrayList<AbstractMessage> result = factory.decode();
		Transport transport = (Transport)result.get(0);
		System.out.println(transport);
		Assert.assertEquals(Transport.ID, factory.getContentType());
	}
}
