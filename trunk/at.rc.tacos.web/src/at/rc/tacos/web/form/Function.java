package at.rc.tacos.web.form;

import at.rc.tacos.model.Competence;

/**
 * Container for special competence
 * @author Payer Martin
 * @version 1.0
 */
public class Function {
	private Competence function;

	public Function(Competence function) {
		this.function = function;
	}
	
	public Competence getFunction() {
		return function;
	}

	public void setFunction(Competence function) {
		this.function = function;
	}
	
}
