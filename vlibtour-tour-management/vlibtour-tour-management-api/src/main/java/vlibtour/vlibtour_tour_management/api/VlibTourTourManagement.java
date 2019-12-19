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
package vlibtour.vlibtour_tour_management.api;

import java.util.List;

import javax.ejb.Remote;

import vlibtour.vlibtour_tour_management.entity.POI;
import vlibtour.vlibtour_tour_management.entity.Tour;
import vlibtour.vlibtour_tour_management.entity.VlibTourTourManagementException;

/**
 * This interface defines the operation for managing POIs and Tours.
 * 
 * @author Amani Graja
 * @author Sana Bouchahoua
 */
@Remote
public interface VlibTourTourManagement {
	/**
	 * 
	 * @param tour
	 * 			the tour to create
	 * @return
	 * 			the tour created
	 * @throws VlibTourTourManagementException
	 * 			Exception
	 */
	Tour createTour(Tour tour) throws VlibTourTourManagementException;
	
	/**
	 * 
	 * @param poi 
	 * 			POI
	 * @return
	 * 		the POI created
	 * @throws VlibTourTourManagementException
	 * 			Exception
	 */
	POI createPOI(POI poi) throws VlibTourTourManagementException;
	
	/**
	 * 
	 * @param tour
	 * 		the tour
	 * @param poi
	 * 		the POI to associate
	 * @return
	 * 			a String
	 */
	String associate(Tour tour, POI poi);
	/**
	 * 
	 * @param id
	 * 			the Tour id
	 */
	void deleteTour(String id);
	/**
	 * 
	 * @param id
	 * 			the POI id
	 */
	void deletePoi(String id);
	/**
	 * 
	 * @param name
	 * 			the tour name
	 * @return
	 * 			a list of tours
	 */
	List<Tour> findTour(String name);
	/**
	 * 
	 * @param name
	 * 			POI name
	 * @return
	 * 			List of POIs
	 */
	List<POI> findPOI(String name);
	/**
	 * 
	 * @param id
	 * 			the Id of POI to find
	 * @return
	 * 			the POI
	 */
	POI findPOIById(String id);
	/**
	 * 
	 * @param id
	 * 		The Tour Id
	 * @return
	 * 		The tour
	 */
	Tour findTourById(String id);
	/**
	 * 
	 * @return list of tours
	 */
	List<Tour> listTours();
	/**
	 * 
	 * @return list of POIs
	 */
	List<POI> listPois();
	
}
