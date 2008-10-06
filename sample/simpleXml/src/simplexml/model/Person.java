package simplexml.model;

/**
 * Sample class for xml testing
 * 
 * @author Michael
 */
public class Person {

	public int customerNumber;
	public String firstName;
	public String lastName;

	/**
	 * Default class constructor
	 */
	public Person() {
	}

	/**
	 * Default clas constructor for a complete person object
	 */
	public Person(int customerNumber, String firstName, String lastName) {
		this.customerNumber = customerNumber;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	// GETTERS AND SETTERS
	public int getCustomerNumber() {
		return customerNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setCustomerNumber(int customerNumber) {
		this.customerNumber = customerNumber;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
