package at.redcross.tacos.web.security;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import at.redcross.tacos.dbal.entity.SecuredAction;

/**
 * The {@code WebActionDefinitionRegistry} is the global registry where all
 * available {@linkplain SecuredAction actions} are registered.
 */
public class WebActionDefinitionRegistry {

	/** the one and only instance */
	private static WebActionDefinitionRegistry instance;

	/** the list of available actions */
	private Set<WebActionDefinition> definitions = new HashSet<WebActionDefinition>();

	/**
	 * Creates a new instance and registers the available actions
	 */
	protected WebActionDefinitionRegistry() {
		initializeActions();
	}

	/**
	 * Returns the shared registry instance.
	 * 
	 * @return the registry
	 */
	public static synchronized WebActionDefinitionRegistry getInstance() {
		if (instance == null) {
			instance = new WebActionDefinitionRegistry();
		}
		return instance;
	}

	/**
	 * Returns a list of all available definitions
	 * 
	 * @return the available action definitions.
	 */
	public Set<WebActionDefinition> getDefinitions() {
		return Collections.unmodifiableSet(definitions);
	}

	/**
	 * Initializes all available actions
	 */
	protected void initializeActions() {
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
		// create a roster entry that is in the past
		{
			WebActionDefinition definition = new WebActionDefinition("roster-createOutdated");
			definition.setDescription("Dienstplaneinträge für die Vergangenheit anlegen");
			definitions.add(definition);
		}
		// delete an info entry
		{
			WebActionDefinition definition = new WebActionDefinition("info-deleteEntry");
			definition.setDescription("Informationen löschen");
			definitions.add(definition);
		}
		// edit an info entry
		{
			WebActionDefinition definition = new WebActionDefinition("info-editEntry");
			definition.setDescription("Informationen bearbeiten");
			definitions.add(definition);
		}
		// edit the role of a user
		{
			WebActionDefinition definition = new WebActionDefinition("user-editRole");
			definition.setDescription("Gruppe(n) eines Mitarbeiters zuweisen bzw. ändern");
			definitions.add(definition);
		}
	      // edit the competence of a user
        {
            WebActionDefinition definition = new WebActionDefinition("user-editCompetence");
            definition.setDescription("Kompetenz(n) eines Mitarbeiters zuweisen bzw. ändern");
            definitions.add(definition);
        }
		// edit an user
		{
			WebActionDefinition definition = new WebActionDefinition("user-editEntry");
			definition.setDescription("Mitarbeiter anlegen bzw. bearbeiten");
			definitions.add(definition);
		}
		// edit a vehicle
		{
			WebActionDefinition definition = new WebActionDefinition("vehicle-editEntry");
			definition.setDescription("Fahrzeug anlegen bzw. bearbeiten");
			definitions.add(definition);
		}
		// view history
		{
		    WebActionDefinition definition = new WebActionDefinition("view-history");
		    definition.setDescription("Änderungshistorie eines Datensatzes ansehen");
		    definitions.add(definition);
		}
		// view administrator statistics
		{
		    WebActionDefinition definition = new WebActionDefinition("statistic-viewAdmin");
		    definition.setDescription("Adminstatistik ansehen");
		    definitions.add(definition);
		}
	}
}
