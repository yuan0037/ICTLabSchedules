package domain;

/**
 * Model an Algonquin College <em>Lab</em>.
 *
 * A lab has the following attributes:
 * 1) a room
 * 2) a description
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 * @Version 1.0
 */
public class Lab {
	private String description;
	private String room;

	@SuppressWarnings("unused")
	private Lab() {
		// NOOP
	}

	public Lab( String room, String description ) {
		super();

		this.room = room;
		this.description = description;
	}

	public String getDescription() { return description; }
	public String getRoom()        { return room; }

	public void setDescription( String description ) { this.description = description; }
	public void setRoom( String room )               { this.room = room; }

	@Override
	public String toString() {
		return room;
	}
}
