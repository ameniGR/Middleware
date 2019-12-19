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


import java.io.Serializable;
import java.util.Collection;
import java.util.SortedSet;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * The entity bean defining a tour in the VLibTour case study. A tour is a
 * sequence of points of interest ({@link POI}).
 * 
 * For the sake of simplicity, we suggest that you use named queries.
 * 
 * @author Sana Bouchahoua
 * @author Amani Graja
 */
@Entity
public class Tour implements Serializable {
	/**
	 * the serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	
	
	/**
	 * the id of the tour.
	 */
	@Id
	private String tid;
	
	/**
	 * the name of the tour.
	 */
	private String name;
	
	/**
	 * the description of the tour.
	 */
	private String description;

	
	/**
	 * default Constructor.
	 */
	public Tour() {
	}


	/**
	 * the collection of POIs.
	 */
	@ManyToMany
	private Collection<POI> pois;
	/**
	 * 
	 * @param id
	 * 			tour Id
	 * @param name
	 * 			tour name
	 * @param desc
	 * 			tour desc
	 * @param pois
	 * 			Tour POIs
	 */
	
	public Tour(final String id, final String name, final String desc, final Collection<POI> pois) {
		this.tid = id;
		this.name = name;
		this.description = desc;
		this.pois = pois;
	}
	
	
	/**
	 * 
	 * @return ID
	 */
	
	public String getTid() {
		return tid;
	}

	/**
	 * @param tid
	 * 			the Tour Id
	 */
	public void setTid(final String tid) {
		this.tid = tid;
	}

	/**
	 * 
	 * @return tour name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 * 
	 * 		the Tour name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return tour description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * 
	 * @param description
	 * 
	 * 			the Tour description
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
	
	/**
	 * gets the collection of orders.
	 * 
	 * @return the collection.
	 */
	
	public Collection<POI> getpois() {
		return pois;
	}

	/**
	 * sets the collection of orders.
	 * 
	 * @param newValue
	 *            the new collection of pois.
	 */
	public void setpois(final SortedSet<POI> newValue) {
		this.pois = newValue;
	}





	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tid == null) ? 0 : tid.hashCode());
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
		Tour other = (Tour) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (tid == null) {
			if (other.tid != null) {
				return false;
			}
		} else if (!tid.equals(other.tid)) {
			return false;
		}
		return true;
	}


	@Override
	public String toString() {
		return "Tour [tid=" + tid + ", name=" + name + ", description=" + description + ", pois=" + pois + "]";
	}

	
	
	
}
