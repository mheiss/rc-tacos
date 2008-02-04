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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.RosterEntry;



public class PrinterJobRoster implements Printable{
	
	private String station = null;
	private List<AbstractMessage> resultList = null;
	private List<AbstractMessage> resultListFilter = null;
	public PrinterJobRoster() {}

	public PrinterJobRoster(List<AbstractMessage> resultList, String doStation) {
		this.setStation(doStation);
		this.setResultList(resultList);
		resultListFilter = new ArrayList<AbstractMessage>();
		
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		Graphics2D g2 = (Graphics2D) graphics;
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("E, dd.MM.yyyy");
		SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");	
		int maxPage = 1;
		
		if(resultList.size() / 8 >=1){
			maxPage = (resultList.size() / 8) + 1; 
		}
		
		if (pageIndex >= maxPage) {
			return Printable.NO_SUCH_PAGE;
		} else {	
			if(pageIndex<1){
				g2.setColor(new Color(182,16,0));
				g2.setFont(new Font("Verdana", Font.BOLD, 15)); 
				
				g2.drawString("Dienstplanübersicht für die Ortstelle: ", (int)((pageFormat.getImageableY())+10), (int)((pageFormat.getImageableX())+20));
				g2.drawString(this.getStation(), (int)((pageFormat.getImageableY())+10), (int)((pageFormat.getImageableX())+40));
			}
			int i = 80;
			int siteRight = 20;
			g2.setFont(new Font("Verdana", Font.PLAIN, 12));
			g2.setColor(new Color(0,0,0));

			g2.drawString("Seite " + (pageIndex+1) + " / " + maxPage,(int) (pageFormat.getImageableWidth() +
					pageFormat.getImageableY() - 100), (int) (pageFormat.getImageableHeight() + pageFormat.getImageableX() - 10));
			
			int j = 0;
			//Set right data to filterArray
			//Max entries -> 8/site
			for(j = pageIndex*8; j < (pageIndex+1)*8 ; j++)
			{
				if(j<resultList.size()){
					resultListFilter.add(resultList.get(j));
				}
			}
			System.out.println("LSIT: " + resultListFilter.size());
			for(AbstractMessage message:resultListFilter)
			{		
					if(i<pageFormat.getImageableHeight()-50){
						RosterEntry entry = (RosterEntry)message;
							g2.drawString(" >>> Datum: " + format2.format(entry.getPlannedStartOfWork()),(int)((pageFormat.getImageableY())+siteRight), (int)((pageFormat.getImageableY())+i));
							i+=18;
							g2.drawString("Mitarbeiter: " + entry.getStaffMember().getFirstName() + " " + entry.getStaffMember().getLastName()  ,(int)((pageFormat.getImageableY())+siteRight), (int)((pageFormat.getImageableY())+i));
							i+=15;
							g2.drawString("Dienstzeit: (plan) " + formatHour.format(entry.getPlannedStartOfWork())+ " - " + formatHour.format(entry.getPlannedEndOfWork()) + " | (real) "+ formatHour.format(entry.getRealStartOfWork()) + " - " + formatHour.format(entry.getRealEndOfWork()), (int)((pageFormat.getImageableY())+siteRight), (int)((pageFormat.getImageableY())+i));
							i+=15;
							g2.drawString("Dienstart: " + entry.getServicetype().getServiceName(),(int)((pageFormat.getImageableY())+siteRight), (int)((pageFormat.getImageableY())+i));
							i+=30;
					} 
					j++;
				
				
			}
			resultListFilter.clear();
			i+=50;
			g2.setFont(new Font("Verdana", Font.ITALIC, 10));
			g2.setColor(new Color(171,171,171));
			g2.drawString("[ Dokument wurde erstellt am: " + new Date().toString() + " ]", (int)((pageFormat.getImageableY())+10), (int)((pageFormat.getImageableY())+i));

			return Printable.PAGE_EXISTS; 
		}
		
		
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
