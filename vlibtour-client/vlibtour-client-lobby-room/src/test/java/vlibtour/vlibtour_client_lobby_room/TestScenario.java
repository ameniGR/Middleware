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
// CHECKSTYLE:OFF
package vlibtour.vlibtour_client_lobby_room;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rabbitmq.http.client.Client;
import com.rabbitmq.tools.jsonrpc.JsonRpcException;
import vlibtour.vlibtour_lobby_room_api.InAMQPPartException;
import vlibtour.vlibtour_lobby_room_server.VLibTourLobbyServer;

public class TestScenario {

	@SuppressWarnings("unused")
	private static Client c;

	@BeforeClass
	public static void setUp() throws IOException, InterruptedException, URISyntaxException {
		try {
			new ProcessBuilder("rabbitmqctl", "stop").inheritIO().start().waitFor();
		} catch (IOException | InterruptedException e) {
		}
		Thread.sleep(1000);
		new ProcessBuilder("rabbitmq-server", "-detached").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "stop_app").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "reset").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "start_app").inheritIO().start().waitFor();
		c = new Client("http://127.0.0.1:15672/api/", "guest", "guest");
	}

	
	@Test
	public void test() throws IOException, TimeoutException, InterruptedException, ExecutionException,
			InAMQPPartException, JsonRpcException {
		
		String tourId = "tour";
		String userId1 = "user1";
		String userId2 = "user2";
		String userId3 = "user3";
		String groupId1 = tourId+"_"+userId1;
		String groupId2 = tourId+"_"+userId3;
		String url1 = null, url2= null , url3=null;
		
		
		VLibTourLobbyServer lobbyRoomServer = new VLibTourLobbyServer();
		new Thread(lobbyRoomServer).start();
		 
		VLibTourLobbyRoomClient client1 = new VLibTourLobbyRoomClient(tourId,userId1);
		VLibTourLobbyRoomClient client2 = new VLibTourLobbyRoomClient(groupId1,tourId,userId2);
		VLibTourLobbyRoomClient client3 = new VLibTourLobbyRoomClient(tourId,userId3);
		
		url1=client1.createGroupAndJoinIt(groupId1, userId1);
		url2=client2.joinGroup(groupId1, userId2);
		url3=client3.createGroupAndJoinIt(groupId2, userId3);
		
		Assert.assertNotNull(url1);
		Assert.assertNotNull(url2);
		Assert.assertNotNull(url3);

		
		System.out.println("url1 : "+url1);
		System.out.println("url2 : "+url2);
		System.out.println("url3 : "+url3);
		
		
		
		
	}

	@AfterClass
	public static void tearDown() throws InterruptedException, IOException {
		new ProcessBuilder("rabbitmqctl", "stop_app").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "stop").inheritIO().start().waitFor();
	}
}
