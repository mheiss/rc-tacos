package at.redcross.tacos.web.security;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import at.redcross.tacos.dbal.entity.SecuredAction;

/**
 * The {@code WebActionDefinitionRegistry} is the global registry where all
 * available {@linkplain SecuredAction actions} are registered.
 */
@ManagedBean(name = "actionDefinitionRegistry")
@ApplicationScoped
public class WebActionDefinitionRegistry {

	/** the list of available actions */
	private Set<WebActionDefinition> definitions = new HashSet<WebActionDefinition>();

	@PostConstruct
	protected void registerActions() {
		// delete a roster entry
		{
			WebActionDefinition definition = new WebActionDefinition("roster-deleteEntry");
			definition.setDescription("Dienstplaneinträge löschen");
			definitions.add(definition);
		}
		// edit a roster entry
		{
			WebActionDefinition definition = new WebActionDefinition("roster-editEntry");
			definition.setDescription("Dienstplaneinträge bearbeiten");
			definitions.add(definition);
		}
		// assign a car to a roster entry
		{
			WebActionDefinition definition = new WebActionDefinition("roster-assignCar");
			definition.setDescription("Fahrzeugzuweisung durchführen");
			definitions.add(definition);
		}
	}

	/**
	 * Returns a list of all available definitions
	 * 
	 * @return the available action definitions.
	 */
	public Set<WebActionDefinition> getDefinitions() {
		return definitions;
	}
}
