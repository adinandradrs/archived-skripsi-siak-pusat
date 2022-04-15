package org.siak.data;

// Generated Jul 9, 2012 9:43:41 PM by Hibernate Tools 3.2.1.GA

/**
 * KotaId generated by hbm2java
 */
public class KotaId implements java.io.Serializable {

	private int kotaId;
	private String username;

	public KotaId() {
	}

	public KotaId(int kotaId, String username) {
		this.kotaId = kotaId;
		this.username = username;
	}

	public int getKotaId() {
		return this.kotaId;
	}

	public void setKotaId(int kotaId) {
		this.kotaId = kotaId;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof KotaId))
			return false;
		KotaId castOther = (KotaId) other;

		return (this.getKotaId() == castOther.getKotaId())
				&& ((this.getUsername() == castOther.getUsername()) || (this
						.getUsername() != null
						&& castOther.getUsername() != null && this
						.getUsername().equals(castOther.getUsername())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getKotaId();
		result = 37 * result
				+ (getUsername() == null ? 0 : this.getUsername().hashCode());
		return result;
	}

}
