package org.dice_research.sask.sorookin_ms.sorookin;

/**
 * Represents a triple of subject, predicate and object.
 * 
 * @author Suganya, Kevin Haack
 *
 */
public class Triple {

	private String subject;
	private String predicate;
	private String object;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(this.subject);
		builder.append(" ");
		builder.append(this.predicate);
		builder.append(" ");
		builder.append(this.object);

		return builder.toString();
	}
}
