package domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model an Algonquin College <em>Lab</em>.
 *
 * A lab has the following attributes:
 * 1) a room
 * 2) a description
 *
 * @author yuan0037@algonquinlive.com
 * @Version 1.0
 */
public class Lab implements Parcelable{
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Lab(Parcel in) {
		description=in.readString();
		room=in.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(description);
		dest.writeString(room);
		
	}
	

	public static final Parcelable.Creator<Lab> CREATOR = new Parcelable.Creator<Lab>() {
		@Override
		public Lab createFromParcel(Parcel source) {
			return new Lab(source);
		};
		
		@Override
		public Lab[] newArray(int size) {
			return new Lab[size];
		}
	};
	
}
