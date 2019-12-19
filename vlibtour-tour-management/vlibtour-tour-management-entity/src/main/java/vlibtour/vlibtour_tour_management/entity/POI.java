/**
This file is part of the course CSC5002.

Copyright (C) 2017-2019 Télécom SudParis

This is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This software platform is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the muDEBS platform. If not, see <http://www.gnu.org/licenses/>.

Initial developer(s): Denis Conan
Contributor(s):
 */
package vlibtour.vlibtour_tour_management.entity;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import javax.persistence.Table;



/**
 * The entity bean defining a point of interest (POI). A {@link Tour} is a
 * sequence of points of interest.
 * 
 * For the sake of simplicity, we suggest that you use named queries.
 * 
 * @author Sana Bouchahoua
 * @author Amani Graja
 */
@Entity
@Table(name = "POI_TABLE")
public class POI implements Serializable {
	/**
	 * the serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the id of the POI.
	 */
	@Id
	private String  poid;
	
	/**
	 * the name of the POI.
	 */
	private String name;
	
	/**
	 * the description of the POI.
	 */
	private String description;
	
	/**
	 * the longitude of the POI.
	 */
	private int longitude;
	
	/**
	 * the latitude of the POI.
	 */
	private int latitude; 
	
	/**
	 * corresponding tour.
	 */
	private Tour tour;

	/**
	 * the collection of Tours.
	 */
	@ManyToMany(mappedBy = "pois")
	private Collection<Tour> tours;
	
	/**
	 * Constructeur par defaut.
	 */
	public POI() { }
	
	/**
	 * Constructor de POI.
	 * @param id 
	 * 			the POI id
	 * @param name  
	 * 			the POI name
	 * @param desc
	 * 			the POI description
	 * @param longi
	 * 			the POI longitude
	 * @param lat
	 * 			the POI latitude
	 */
	public POI(final String id, final String name, final String desc, final int longi, final int lat) {
		this.poid = id;
		this.name = name;
		this.description = desc;
		this.longitude = longi;
		this.latitude = lat;
	}
	
	
	/**
	 *gets the identifier.
	 * 
	 * @return the identifier.
	 */

	public String getPoid() {
		return poid;
	}

	/**
	 * sets the identifier.
	 * 
	 * @param poid
	 *            the new identifier.
	 */
	public void setPoid(final String poid) {
		this.poid = poid;
	}

	/**
	 *gets the name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * sets the name.
	 * 
	 * @param name
	 *            the new name.
	 */
	public void setName(final String name) {
		this.name = name;
	}
	/**
	 *gets the description.
	 * 
	 * @return the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * sets the Description.
	 * 
	 * @param description
	 *            the new Description.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
	/**
	 *gets the gps location.
	 * 
	 * @return the gps location.
	 */
	public String  getGpslocation() {
		String rslt = "Longitude : " + this.longitude + " latitude : " + this.latitude;
		return rslt;
	}

	/**
	 * sets the gps location.
	 * 
	 * @param longi
	 *            the new longi location.
	 * @param alt
	 * 				the new alt 
	 */
	public void setGpslocation(final int longi, final int alt) {
		this.longitude = longi;
		this.latitude = alt;
	}
	
	/**
	 * gets the tours corresponding to a POI.
	 * 
	 * @return the tour.
	 */

	public Collection<Tour> getTours() {
		return  tours;
	}
	
	
	/**
	 * sets the Tours.
	 * 
	 * @param newValue
	 *            the tours.
	 */
	public void setTours(final List<Tour> newValue) {
		this.tours = (ArrayList<Tour>) newValue;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + latitude;
		result = prime * result + longitude;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((poid == null) ? 0 : poid.hashCode());
		result = prime * result + ((tour == null) ? 0 : tour.hashCode());
		result = prime * result + ((tours == null) ? 0 : tours.hashCode());
		return result;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
			if (obj == null) {
			return false;
			}
		if (getClass() != obj.getClass()) {
			return false;
		}
		POI other = (POI) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (latitude != other.latitude) {
			return false;
		}
		if (longitude != other.longitude) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (poid == null) {
			if (other.poid != null) {
				return false;
			}
		} else if (!poid.equals(other.poid)) {
			return false;
		}
		if (tour == null) {
			if (other.tour != null) {
				return false;
			}
		} else if (!tour.equals(other.tour)) {
			return false;
		}
		if (tours == null) {
			if (other.tours != null) {
				return false;
			}
		} else if (!tours.equals(other.tours)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "POI [poid=" + poid + ", name=" + name + ", description=" + description + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", tour=" + tour + ", tours=" + tours + "]";
	}

	
	
	
}
