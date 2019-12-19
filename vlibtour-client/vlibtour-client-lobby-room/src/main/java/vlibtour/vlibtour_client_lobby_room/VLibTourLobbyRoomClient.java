/**
This file is part of the course CSC5002.

Copyright (C) 2019 Télécom SudParis

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
package vlibtour.vlibtour_client_lobby_room;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.jsonrpc.JsonRpcClient;
import com.rabbitmq.tools.jsonrpc.JsonRpcException;

import vlibtour.vlibtour_lobby_room_api.VLibTourLobbyService;

/**
 * This class is the client application of the tourists.
 * 
 * @author Graja Amani
 * @author sana Bouchahoua
 */
public class VLibTourLobbyRoomClient {
	
	/**
	 * the delimiter
	 * 
	 */
	static VLibTourLobbyService GROUP_TOUR_USER_DELIMITER;
	
	/**
	 * Connection factory 
	 */
	ConnectionFactory factory ;
	
	/**
	 * the channel for producing and consuming.
	 */
	Channel channel;
	/**
	 * the connection to the RabbitMQ broker.
	 */
	Connection connection;
	/**
	 * The jsonRpc Client
	 */
	JsonRpcClient  jsonRpcClient;
	/**
	 * The Client
	 */
	VLibTourLobbyService  client;
	
	/**
	 * Constructor
	 * @param tourId
	 * 			the tour Id
	 * @param userId
	 * 			the user Id
	 * @throws JsonRpcException
	 * 			JsonRpc Exception 
	 * @throws TimeoutException
	 * 			Timeout exception 
	 * @throws IOException 
	 * 			IOException
	 */
	public VLibTourLobbyRoomClient(String tourId,String userId) throws JsonRpcException, IOException, TimeoutException {
		if((userId == null || userId.equals("")) && (tourId == null || tourId.equals(""))) {
			throw new IllegalArgumentException("UserId or TourId cannot be null");
		}
			factory = new ConnectionFactory();
			factory.setHost("localhost");
			connection = factory.newConnection();
			channel = connection.createChannel();
			jsonRpcClient = new JsonRpcClient(channel, VLibTourLobbyService.EXCHANGE_NAME,VLibTourLobbyService.BINDING_KEY);
			client = (VLibTourLobbyService) jsonRpcClient.createProxy(VLibTourLobbyService.class);		
		
	}
	/**
	 * 
	 * @param groupId
	 * 			the group ID
	 * @param tourId
	 * 			the tour Id
	 * @param userId
	 * 			the user id
	 * @throws IOException
	 * 			IOException
	 * @throws TimeoutException
	 * 			Timeout Exception
	 * @throws JsonRpcException
	 * 			JsonRpc Exception
	 */
	public VLibTourLobbyRoomClient(String groupId,String tourId,String userId) throws IOException, TimeoutException, JsonRpcException {
		if((userId == null || userId.equals("")) && (tourId == null || tourId.equals(""))&& (groupId == null || groupId.equals(""))) {
			throw new IllegalArgumentException("UserId or GroupId or TourId cannot be null");
		}
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			connection = factory.newConnection();
			channel = connection.createChannel();
			jsonRpcClient = new JsonRpcClient(channel, VLibTourLobbyService.EXCHANGE_NAME,VLibTourLobbyService.BINDING_KEY);
			client = (VLibTourLobbyService) jsonRpcClient.createProxy(VLibTourLobbyService.class);
		
	
		}
	
	public String createGroupAndJoinIt(final String groupId, final String userId) {
		if((userId == null || userId.equals("")) && (userId == null || userId.equals(""))) {
			throw new IllegalArgumentException("UserId or GroupId cannot be null");
		}
		String url = client.createGroupAndJoinIt(groupId, userId);
		return url;
		
	}
	
	public String joinGroup(final String groupId, final String userId) {
		if((userId == null || userId.equals("")) && (userId == null || userId.equals(""))) {
			throw new IllegalArgumentException("UserId or GroupId cannot be null");
		}
		String url = client.joinAGroup(groupId, userId);
		
		return url;
		
	}
	
	private void close() throws IOException, TimeoutException {
		jsonRpcClient.close();
		if (channel != null) {
			channel.close();
		}
		if (connection != null) {
			connection.close();
		}
		
	}

	
	/**
	 * the main method of the example.
	 * 
	 * @param argv
	 *            the command line argument is the number of the call.
	 * @throws Exception
	 *             communication problem with the broker.
	 */
	public static void main(final String[] argv) throws Exception {
		String tourId= argv[0];
		String userId= argv[1];
		String groupId = tourId+ GROUP_TOUR_USER_DELIMITER+ userId;
		
		VLibTourLobbyRoomClient rpcClient1 = new VLibTourLobbyRoomClient(tourId,userId);

		rpcClient1.createGroupAndJoinIt(groupId, userId);
		
		rpcClient1.close();
	}

}




