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
package vlibtour.vlibtour_client_scenario;

import static vlibtour.vlibtour_visit_emulation.Log.EMULATION;
import static vlibtour.vlibtour_visit_emulation.Log.LOG_ON;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import javax.naming.NamingException;

import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import com.google.gson.*;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.tools.jsonrpc.JsonRpcException;

import vlibtour.vlibtour_client_emulation_visit.VLibTourVisitEmulationClient;
import vlibtour.vlibtour_client_group_communication_system.VLibTourGroupCommunicationSystemClient;
import vlibtour.vlibtour_client_lobby_room.VLibTourLobbyRoomClient;
import vlibtour.vlibtour_client_scenario.map_viewer.BasicMap;
import vlibtour.vlibtour_client_scenario.map_viewer.MapHelper;
import vlibtour.vlibtour_lobby_room_api.InAMQPPartException;
import vlibtour.vlibtour_lobby_room_server.VLibTourLobbyServer;
import vlibtour.vlibtour_tour_management.entity.VlibTourTourManagementException;
import vlibtour.vlibtour_visit_emulation.ExampleOfAVisitWithTwoTourists;
import vlibtour.vlibtour_visit_emulation.GPSPosition;
import vlibtour.vlibtour_visit_emulation.Position;

/**
 * This class is the client application of the tourists. The access to the lobby
 * room server, to the group communication system, and to the visit emulation
 * server visit should be implemented using the design pattern Delegation (see
 * https://en.wikipedia.org/wiki/Delegation_pattern).
 * 
 * A client creates two queues to receive messages from the broker; the binding
 * keys are of the form "{@code *.*.identity}" and "{@code *.*.location}" while
 * the routing keys are of the form "{@code sender.receiver.identity|location}".
 * 
 * This class uses the classes {@code MapHelper} and {@code BasicMap} for
 * displaying the tourists on the map of Paris. Use the attributes for the
 * color, the map, the map marker dot, etc.
 * 
 * @author Denis Conan
 */
public class VLibTourVisitTouristApplication {
	/**
	 * Other dot.
	 */
	private static MapMarkerDot otherDot;

	/**
	 * the map to manipulate. Not all the clients need to have a map; therefore we
	 * use an optional.
	 */
	@SuppressWarnings("unused")
	private Optional<BasicMap> map = Optional.empty();
	/**
	 * the dot on the map for the first tourist.
	 */
	@SuppressWarnings("unused")
	private static MapMarkerDot mapDotJoe;
	/**
	 * the dot on the map for the second tourist.
	 */
	@SuppressWarnings("unused")
	private static MapMarkerDot mapDotAvrel;
	/**
	 * delegation to the client of type
	 * {@link vlibtour.vlibtour_client_emulation_visit.VLibTourVisitEmulationClient}.
	 */
	@SuppressWarnings("unused")
	private VLibTourVisitEmulationClient emulationVisitClient;
	/**
	 * delegation to the client of type
	 * {@link vlibtour.vlibtour_client_group_communication_system.VLibTourGroupCommunicationSystemClient}.
	 */
	@SuppressWarnings("unused")
	private VLibTourLobbyRoomClient lobbyRoomClient;
	/**
	 * delegation to the client of type
	 * {@link vlibtour.vlibtour_client_group_communication_system.VLibTourGroupCommunicationSystemClient}.
	 * The identifier of the user is obtained from this reference.
	 */
	@SuppressWarnings("unused")
	private VLibTourGroupCommunicationSystemClient groupCommClient;

	/**
	 * the url.
	 */
	private static String url = "";
	/**
	 * the message to publish.
	 */
	private static String messageToPublish;

	/**
	 * Font.
	 */
	private static Font font;

	/**
	 * creates a client application, which will join a group that must already
	 * exist. The group identifier is optional whether this is the first user of the
	 * group ---i.e. the group identifier is built upon the user identifier.
	 * Concerning the tour identifier, it must be provided by the calling method.
	 * 
	 * @param tourId  the tour identifier of this application.
	 * @param groupId the group identifier of this client application.
	 * @param userId  the user identifier of this client application.
	 * @throws InAMQPPartException             the exception thrown in case of a
	 *                                         problem in the AMQP part.
	 * @throws VlibTourTourManagementException problem in the name or description of
	 *                                         POIs.
	 * @throws IOException                     problem when setting the
	 *                                         communication configuration with the
	 *                                         broker.
	 * @throws TimeoutException                problem in creation of connection,
	 *                                         channel, client before the RPC to the
	 *                                         lobby room.
	 * @throws JsonRpcException                problem in creation of connection,
	 *                                         channel, client before the RPC to the
	 *                                         lobby room.
	 * @throws InterruptedException            thread interrupted in call sleep.
	 * @throws NamingException                 the EJB server has not been found
	 *                                         when getting the tour identifier.
	 */

	public VLibTourVisitTouristApplication(final String tourId, final String groupId, final String userId)
			throws InAMQPPartException, VlibTourTourManagementException, IOException, JsonRpcException,
			TimeoutException, InterruptedException, NamingException {
		//throw new UnsupportedOperationException("No implemented, yet");

		emulationVisitClient = new VLibTourVisitEmulationClient();

	}

	/**
	 * function splits a message and shows the dot on the map.
	 * @param message
	 * 		the message recieved
	 */
	public void splitAndShow(final String message) {
		
		String longitude = message.split(" ")[1].split("=")[1];
		String latitude = message.split(" ")[3].split("=")[1];
		String name = message.split(" ")[0];	
		System.out.println(" Longitude : " + longitude);
		System.out.println(" Latitude : " + latitude);
		GPSPosition gpsPosition = new GPSPosition(Double.parseDouble(latitude), Double.parseDouble(longitude));
		Position position = new Position("position ", gpsPosition);
		otherDot = new MapMarkerDot(Double.parseDouble(latitude), Double.parseDouble(longitude));
		otherDot = MapHelper.addTouristOnMap(this.map.get(), Color.BLUE, font, name, position);
		this.map.get().repaint();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.map.get().removeMarker(otherDot);
	}


	/**
	 * executes the tourist application. <br>
	 * We prefer insert comments in the method instead of detailing the method here.
	 * 
	 * @param args the command line arguments.
	 * @throws Exception all the potential problems (since this is a demonstrator,
	 *                   apply the strategy "fail fast").
	 */
	public static void main(final String[] args) throws Exception {
		@SuppressWarnings("unused")
		String usage = "USAGE: " + VLibTourVisitTouristApplication.class.getCanonicalName()
		+ " userId (either Joe or Avrel) + Tour Id";
		String userId = args[0];
		String tid = "1";
		String groupId = tid;
		final	VLibTourVisitTouristApplication client = new VLibTourVisitTouristApplication("The unusual Paris", tid, userId);
		if (args.length != 3) {
			client.lobbyRoomClient = new VLibTourLobbyRoomClient(tid, userId);	
			url = client.lobbyRoomClient.createGroupAndJoinIt(groupId, userId);
			System.out.println("url : " + url);

		}
		if (args.length != 2) {
			client.lobbyRoomClient = new VLibTourLobbyRoomClient(tid, userId);	
			url = client.lobbyRoomClient.joinGroup(groupId, userId);
			System.out.println("url : " + url);
		}


		if (LOG_ON && EMULATION.isInfoEnabled()) {
			EMULATION.info(userId + "'s application is starting");
		}
		// the tour is empty, this client gets it from the data base (first found)
		// TODO
		// start the VLibTourVisitTouristApplication applications
		// TODO
		// set the map viewer of the scenario (if this is this client application that
		// has created the group [see #VLibTourVisitTouristApplication(...)])
		// the following code should be completed
		// FIXME

		if (client == null) {
			throw new UnsupportedOperationException("No implemented, yet");
		}

		client.map = Optional.of(MapHelper.createMapWithCenterAndZoomLevel(48.851412, 2.343166, 14));


		font  = new Font("name", Font.BOLD, 20);
		client.map.ifPresent(m -> {
			MapHelper.addMarkerDotOnMap(m, 48.871799, 2.342355, Color.BLACK, font, "Musée Grevin");
			MapHelper.addMarkerDotOnMap(m, 48.860959, 2.335757, Color.BLACK, font, "Pyramide du Louvres");
			MapHelper.addMarkerDotOnMap(m, 48.833566, 2.332416, Color.BLACK, font, "Les catacombes");
			// all the tourists start at the same position
			Position positionOfJoe = client.emulationVisitClient.getCurrentPosition(userId);
			mapDotJoe = MapHelper.addTouristOnMap(m, Color.RED, font, userId, positionOfJoe);	
			client.map.get().repaint();
			// wait for painting the map
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


		});

		client.groupCommClient = new VLibTourGroupCommunicationSystemClient(url);

		Gson gson = new Gson();
		Consumer consumer = new DefaultConsumer(client.groupCommClient.getChannel()) {
			@Override 
			public void handleDelivery(final String consumerTag, final Envelope envelope, final AMQP.BasicProperties properties, final byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" Position recieved  : " + message + " routing key = " + envelope.getRoutingKey());
				if (!message.contains(userId)) {
				
				client.splitAndShow(message);
				}
				}
		};


		client.groupCommClient.addConsumer(consumer, userId, userId);
		client.groupCommClient.startConsumption(client.groupCommClient.getChannel());


		// repainting # approximately 3s => delay only non-leader clients => not all
		// the clients at the same speed
		// TODO
		// loop until the end of the visit is detected
		// FIXME
		while (true) {
			// step in the current path
			Position nextStep = client.emulationVisitClient.stepsInVisit(userId);
			Position positionOfClient = client.emulationVisitClient.stepInCurrentPath(userId);
			MapHelper.moveTouristOnMap(mapDotJoe, positionOfClient);
			// repainting # approximately 2x2s => delay only non-leader clients => not all the clients at the same speed



			client.map.get().repaint();
			// wait for painting the map

			// start the consumption of messages (e.g. positions of group members) from the group communication system 
			// TODO
			String json = positionOfClient.getGpsPosition().toString();
			client.groupCommClient.publish(userId + " " + json, "*.all");

			System.out.println("position to publish " + json);	

			System.out.println("nbr of msgs recieved : " + client.groupCommClient.getNbMsgReceived());

			// Thread.sleep...
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (positionOfClient.getName().equals(nextStep.getName())) {
				break;
			}

		}
		// closes the channel and the connection.
		// TODO
		// exit this main
		System.exit(0);
	}

}
