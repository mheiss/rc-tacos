package at.redcross.tacos.dbal.entity;

public enum Gender {
	UNKNOWN, MALE, FEMALE;

	public String getName() {
		return name().toLowerCase();
	}
}
