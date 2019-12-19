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



import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;


import org.junit.Assert;
import org.junit.Test;
/**
 * 
 * @author graja_am
 * @author sana Bouchahoua
 *
 */
public class TestVlibTourTourManagementEntity {
	/**
	 * 	
	 * @throws Exception
	 * 			Exception
	 */
	/**
	 * Longitude.
	 */
	private final int longi = 21;
	/**
	 * altitude.
	 */
	private final int alti = 77;
	/**
	 * Longitude.
	 */
	private final int longi2 = 49;
	/**
	 * altitude.
	 */
	private final int alti2 = 153;

	/**
	 * Test pour creer un POI.
	 * @throws Exception
	 * 		Exception
	 */
	@Test
	public void createPOITest1() throws Exception {
		// Create new POI 
		POI poiTest = new POI("1", "Chatelet", "1ere POI", longi, alti);
		POI poiTest2 = new POI("1", "Chatelet", "1ere POI", longi, alti);
		//print the Id , Name , Description,position
		System.out.println("The Id of this POI : " + poiTest.getPoid());
		System.out.println("The name of this POI : " + poiTest.getName());
		System.out.println("The description of this POI : " + poiTest.getDescription());
		System.out.println("The posiion of this POI : " + poiTest.getGpslocation());

		Assert.assertTrue(poiTest.equals(poiTest2));

	}

	/**
	 * Test pour creer un POI.
	 * @throws Exception
	 * 			Exception
	 */
	@Test
	public void createTourTest1() throws Exception {
		// Create 2 new POIs 
		POI poiTest = new POI("1", "Chatelet", "1ere POI", longi, alti);
		POI poiTest2 = new POI("2", "Notre dame", "2eme POI", longi2, alti2);

		Collection<POI> pois = new ArrayList<POI>();
		pois.add(poiTest);
		pois.add(poiTest2);
		// Create new Tour
		Tour tour = new Tour("1", "Paris tour", "tour de paris", pois);
		Tour tour2 = new Tour("1", "Paris tour", "tour de paris", pois);

		//print the Id , Name , Description
		System.out.println("The Id of this Tour : " + tour.getTid());
		System.out.println("The name of this Tour : " + tour.getName());
		System.out.println("The description of this Tour : " + tour.getDescription());

		assertTrue(tour.equals(tour2));

	}
}
