package at.rc.tacos.platform.model;

/**
 * Link
 * @author Payer Martin
 * @version 1.0
 */
public class Link extends AbstractMessage {
	public final static String ID = "link";
	public Link() {
		super(ID);
		id = -1;
		innerText = null;
		href = null;
		title = null;
		username = null;
	}
	
	private int id;
	private String innerText;
	private String href;
	private String title;
	private String username;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInnerText() {
		return innerText;
	}
	public void setInnerText(String innerText) {
		this.innerText = innerText;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
