package at.redcross.tacos.dbal.entity;

import javax.persistence.Embeddable;

@Embeddable
public enum Gender {
	UNKNOWN, MALE, FEMALE;
}
