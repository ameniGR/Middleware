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

import java.util.List;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import vlibtour.vlibtour_tour_management.api.VlibTourTourManagement;
import vlibtour.vlibtour_tour_management.entity.POI;
import vlibtour.vlibtour_tour_management.entity.Tour;

public class TestVlibTourTourManagementBean {
	
	private static EJBContainer ec;
	private static Context ctx;
	private static VlibTourTourManagement vlib;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		 Map<String, Object> properties = new HashMap<String, Object>();
		 properties.put(EJBContainer.MODULES, new File("target/classes"));
		 ec = EJBContainer.createEJBContainer(properties);
		 ctx = ec.getContext();
		 vlib =  (VlibTourTourManagement) ctx.lookup("vlibtour.vlibtour_tour_management.api.VlibTourTourManagement");
	}


	
	@Test
	public void createPOITest1() throws Exception {
		System.out.println("........Creating POI......");
		POI poi = new POI("1", "POI", "First POI", 15, 87);
		vlib.createPOI(poi);
		Assert.assertTrue(poi!=null);
		System.out.println("..........POI created : " + poi.getName()+"......");
		
	}

	@Test
	public void createTourTest1() throws Exception {
		System.out.println("........Creating Tour......"); 	 	
		Tour tour = new Tour("1","Tour","Tor test", null);
		vlib.createTour(tour);
		Assert.assertTrue(tour!=null);
		System.out.println("......Tour created : "+tour.getName() + "......");
	}
	
	@Test
	public void findPOIWithPIDTest1() throws Exception {
		POI p = new POI("2","Poi2","Poi to be found ", 0, 0);
		vlib.createPOI(p);
		System.out.println("......Finding POI with ID........");
		System.out.println("POI found with id 2: "+ vlib.findPOIById("2").getDescription());
	}
	
	@Test
	public void findAllPOIsWithNameTest1() throws Exception {
		POI poi1  = new POI("1","name","Poi 1 to be found by name", 0, 0);
		POI poi2  = new POI("2","name","Poi 2 to be found by name", 0, 0);
		POI poi3  = new POI("3","poi3","Poi to be found by name", 0, 0);		
		
		vlib.createPOI(poi1);
		vlib.createPOI(poi2);
		vlib.createPOI(poi3);
		
		List<POI> pois=  vlib.findPOI("name");
		
		System.out.println("......Finding POI by name........");
		Assert.assertTrue(pois.size() == 2);
		System.out.println("POIs found : "+ pois.get(0).getDescription()+ " / "+ pois.get(1).getDescription());
	}

	
	@Test
	public void findAllPOIsTest1() throws Exception {
		POI poi1  = new POI("1","name","Poi to be found by name", 0, 0);
		POI poi2  = new POI("2","name","Poi to be found by name", 0, 0);
		POI poi3  = new POI("3","name2","Poi to be found by name", 0, 0);		
		
		vlib.createPOI(poi1);
		vlib.createPOI(poi2);
		vlib.createPOI(poi3);
		System.out.println("....Listing tours.....");
		List<POI> pois=  vlib.listPois();
		Assert.assertTrue(pois.size() == 3);
		System.out.println("......All POIs listed.......");
	}

	
	

	
	@Test
	public void findTourWithTIDTest1() throws Exception {
		Tour tour = new Tour("1","Tour","Tor test", null);
		vlib.createTour(tour);
		
		System.out.println("created : "+tour);
		Tour found = vlib.findTourById("1");
		System.out.println("found : "+found);
		System.out.println("....Finding tour with Id...");
		Assert.assertTrue(vlib.findTourById("1").equals(tour));
		System.out.println("....Tour found ..."+vlib.findTourById("1").getName());

	}

	
	@Test
	public void findAllToursWithNameTest1() throws Exception {
		Tour tour = new Tour("1","Tour","Tor test", null);
		Tour tour2 = new Tour("2","Tour","Tor test", null);
		Tour tour3 = new Tour("3","Tour2","Tor test", null);
		
		vlib.createTour(tour);
		vlib.createTour(tour2);
		vlib.createTour(tour3);
	List<Tour> tours = vlib.findTour("Tour");
	System.out.println(".....finding tours by name....");
	Assert.assertTrue(tours.size() == 2);
	System.out.println(".....all tours found by name......");
	}

	
	@Test
	public void findAllToursTest1() throws Exception {
		Tour tour1 = new Tour("1","Tour","Tor test", null);
		Tour tour2 = new Tour("2","Tour","Tor test", null);
		Tour tour3 = new Tour("3","Tour2","Tor test", null);
		
		vlib.createTour(tour1);
		vlib.createTour(tour2);
		vlib.createTour(tour3);
		System.out.println(".....List tours.....");
		List<Tour> tours =  vlib.listTours();
		
		Assert.assertTrue(tours.size() == 3);
		System.out.println(".....All tours listes.....");
	}

	@After
	public void tearDown() throws Exception {
		List<Tour> tours = vlib.listTours();	
		for(int i=0 ; i<tours.size() ;i++) {
		vlib.deleteTour(tours.get(i).getTid());
		}
		
		List<POI> pois=  vlib.listPois();
		for(int i=0 ; i<pois.size() ;i++) {
		vlib.deletePoi(pois.get(i).getPoid());
		}
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		if(ec != null) {
			ec.close();
		}
	}

}
