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
package vlibtour.vlibtour_tour_management.bean;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import vlibtour.vlibtour_tour_management.api.VlibTourTourManagement;
import vlibtour.vlibtour_tour_management.entity.POI;
import vlibtour.vlibtour_tour_management.entity.Tour;
import vlibtour.vlibtour_tour_management.entity.VlibTourTourManagementException;

/**
 * This class defines the EJB Bean of the VLibTour tour management.
 * 
 * @author Sana Bouchahoua
 * @author Amani Graja
 */
@Stateless
public class VlibTourTourManagementBean implements VlibTourTourManagement {
	/**
	 * the reference to the entity manager, which persistence context is "pu1".
	 */
	@PersistenceContext(unitName = "pu1")
	private EntityManager em;

	@Override
	public Tour createTour(Tour tour) throws VlibTourTourManagementException {
		// Create a new TOUR
			if(tour == null) {
				throw new VlibTourTourManagementException("TOI must not be null");
			}
				// Persist the tour
				em.persist(tour);	
		
				return tour;
	}
	
	@Override
	public POI createPOI(POI poi) throws VlibTourTourManagementException {
		if(poi == null) {
			throw new VlibTourTourManagementException("POI must not be null");
		}
		em.persist(poi);
		return poi;
	}
	
	
	@Override 
	public String associate(Tour tour, POI poi) {
		// Associate poi with the tours. The association
						tour.getpois().add(poi);
						em.persist(poi);
						em.persist(tour);
		return "Ok";
	}

	

	@Override
	public List<Tour> findTour(String name) {
		Query q = em.createQuery("select t from Tour t where t.name = :name");
		q.setParameter("name", name);
		@SuppressWarnings("unchecked")
		List<Tour> list= (List<Tour>)q.getResultList();
		return list;
	}

	@Override
	public List<POI> findPOI(String name) {
		Query q = em.createQuery("select poi from  POI poi where poi.name = :name");
		q.setParameter("name", name);
		 @SuppressWarnings("unchecked")
		List<POI> list= (List<POI>)q.getResultList();
		return list;
	}

	@Override
	public List<Tour> listTours() {
		Query q = em.createQuery("select t from Tour t ");
		@SuppressWarnings("unchecked")
		List<Tour> list= (List<Tour>) q.getResultList();
		return list ;
	}


	@Override
	public void deleteTour(String id) {
		Tour tour =this.findTourById(id);
		em.remove(tour);
		
	}

	@Override
	public void deletePoi(String id) {
		POI poi = this.findPOIById(id);
		em.remove(poi);
	}

	@Override
	public List<POI> listPois() {
		Query q = em.createQuery("select poi from  POI poi ");
		@SuppressWarnings("unchecked")
		List<POI> list= (List<POI>) q.getResultList();
		return list ;		
	}

	@Override
	public POI findPOIById(String poid) {
		Query q = em.createQuery("select poi from POI poi where poi.poid = :poid");
		q.setParameter("poid",poid);
		POI p = (POI)q.getSingleResult();
		return p;
	}

	@Override
	public Tour findTourById(String tid) {
		Query q = em.createQuery("select t from Tour t where t.tid = :tid");
		q.setParameter("tid",tid);
		Tour t = (Tour)q.getSingleResult();
		return t;
	}
	
	
}
