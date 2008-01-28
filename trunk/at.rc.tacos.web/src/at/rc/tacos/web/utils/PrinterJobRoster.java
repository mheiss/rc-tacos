package at.rc.tacos.web.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.RosterEntry;



public class PrinterJobRoster implements Printable{
	
	private String station = null;
	private List<AbstractMessage> resultList = null;

	public PrinterJobRoster() {}

	public PrinterJobRoster(List<AbstractMessage> resultList, String doStation) {
		this.setStation(doStation);
		this.setResultList(resultList);
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		
		Graphics2D g2 = (Graphics2D) graphics;
		if (pageIndex >= 1) {
			return Printable.NO_SUCH_PAGE;
		} else {	
			drawPageContents(g2, pageIndex+1, pageFormat);
			return Printable.PAGE_EXISTS; 
		}
		
		
	}

	public void drawPageContents(Graphics2D g2, int pageNo, PageFormat pageFormat) {
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("E, dd.MM.yyyy");
		SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");

		g2.setColor(new Color(182,16,0));
		g2.setFont(new Font("Verdana", Font.BOLD, 15)); 
		
		g2.drawString("Dienstplanübersicht für die Ortstelle: ", (int)((pageFormat.getImageableY())+10), (int)((pageFormat.getImageableX())+20));
		g2.drawString(this.getStation(), (int)((pageFormat.getImageableY())+10), (int)((pageFormat.getImageableX())+40));
		int i = 80;
		int siteRight = 20;
		g2.setFont(new Font("Verdana", Font.PLAIN, 12));
		g2.setColor(new Color(0,0,0));
		for(AbstractMessage message:resultList)
		{
			RosterEntry entry = (RosterEntry)message;
//			if(entry.getStation().equals(this.getStation()))
//			{
				g2.drawString(" >>> Datum: " + format2.format(entry.getPlannedStartOfWork()),(int)((pageFormat.getImageableY())+siteRight), (int)((pageFormat.getImageableY())+i));
				i+=18;
				g2.drawString("Mitarbeiter: " + entry.getStaffMember().getFirstName() + " " + entry.getStaffMember().getLastName()  ,(int)((pageFormat.getImageableY())+siteRight), (int)((pageFormat.getImageableY())+i));
				i+=15;
				g2.drawString("Dienstzeit: (plan) " + formatHour.format(entry.getPlannedStartOfWork())+ " - " + formatHour.format(entry.getPlannedEndOfWork()) + " | (real) "+ formatHour.format(entry.getRealStartOfWork()) + " - " + formatHour.format(entry.getRealEndOfWork()), (int)((pageFormat.getImageableY())+siteRight), (int)((pageFormat.getImageableY())+i));
				i+=15;
				g2.drawString("Dienstart: " + entry.getServicetype().getServiceName(),(int)((pageFormat.getImageableY())+siteRight), (int)((pageFormat.getImageableY())+i));
				i+=30;
//			}
		}
		i+=50;
		g2.setFont(new Font("Verdana", Font.ITALIC, 10));
		g2.setColor(new Color(171,171,171));
		g2.drawString("[ Dokument wurde erstellt am: " + new Date().toString() + " ]", (int)((pageFormat.getImageableY())+10), (int)((pageFormat.getImageableY())+i));

		i=0;
		
	}

//  set PageFormat fix to "A4"
	private PageFormat getPaperSize()
	{
		PageFormat pf = new PageFormat();
		Paper a4 = new Paper(); 
		double reso = 72.0;
  	           
//  	 size (inch)
		double a4Width  =  8.26;
		double a4Height = 11.69;
		a4.setSize(a4Width * reso, a4Height * reso);

//  	 margin (inch)
		double a4LeftMargin   = 0.78; 
		double a4RightMargin  = 0.78;
		double a4TopMargin    = 0.78;
		double a4BottomMargin = 0.78;

		a4.setImageableArea(a4LeftMargin * reso, a4TopMargin * reso,
  	                   (a4Width - a4LeftMargin - a4RightMargin)*reso,
  	                   (a4Height - a4TopMargin - a4BottomMargin)*reso);
		pf.setPaper(a4);
	  	
		return pf;
	}
	
	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public List<AbstractMessage> getResultList() {
		return resultList;
	}

	public void setResultList(List<AbstractMessage> resultList) {
		this.resultList = resultList;
	}
}
