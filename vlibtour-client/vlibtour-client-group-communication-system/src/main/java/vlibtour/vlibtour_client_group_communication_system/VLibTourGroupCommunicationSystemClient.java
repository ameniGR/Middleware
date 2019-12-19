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
package vlibtour.vlibtour_client_group_communication_system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;

import vlibtour.vlibtour_lobby_room_api.VLibTourLobbyService;



/**
 * This class is the client application of the tourists.
 * 
 * @author sana bouchahoua
 * @author amani graja
 */
public class VLibTourGroupCommunicationSystemClient {

	/**
	 * the user id.
	 */
	private String userId;

	/**
	 * the Tour id.
	 * 
	 */
	private String tid;

	/**
	 * the group id.
	 */
	private String groupId;

	/**
	 * the connection to the RabbitMQ broker.
	 */
	private Connection connection;

	/**
	 * the channel for consuming.
	 */
	private Channel channel;

	/**
	 * the name of the exchange.
	 */
	private final String exchangeName;

	/**
	 * the consumer thread.
	 */
	private Consumer consumer;

	/**
	 * the name of the queue.
	 */
	private String queueName;


	/**
	 * the routing key for producing..removeMapMarker(mapMarker).
	 */
	private String routingKey;

	/**
	 * the binding key for producing.
	 */
	private String bindingKey;
	/**
	 * the number of messages that have been received by this consumer.
	 */
	private int nbMsgReceived = 0;

	/**
	 * constructs a producer that produces log messages with the given routing key.
	 * 
	 * @param url
	 *          the path
	 * @throws IOException
	 *             the communication problems.
	 * @throws TimeoutException
	 *             broker to long to connect to.
	 * @throws URISyntaxException 
	 * 			URISyntaxException 
	 * @throws NoSuchAlgorithmException 
	 * 			NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 * 			KeyManagementException
	 */
	public VLibTourGroupCommunicationSystemClient(final String url) throws IOException, TimeoutException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(url);

		this.userId =  factory.getUsername();
		this.groupId = VLibTourLobbyService.computeGroupId(url);
		String[] sentences = this.groupId.split("_");
		this.tid = sentences[0];
		this.queueName = userId;
		this.bindingKey = "*." + userId;
		this.exchangeName = tid;
		connection = factory.newConnection();
		channel = connection.createChannel();
		channel.exchangeDeclare(exchangeName, "topic");
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, "*.all");

	}


	/**
	 * publishes a message.
	 * 
	 * @param message
	 * 			the published message
	 * @param routingKey
	 * 			the routing key
	 * @throws UnsupportedEncodingException
	 *             problem when encoding the message.
	 * @throws IOException
	 *             communication problem with the broker.
	 */
	public void publish(final String message, final String routingKey) throws UnsupportedEncodingException, IOException {
		channel.basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
		System.out.println("message published : " + message);
	}

	/**
	 * adds a default consumer.
	 * 
	 * @param consumer
	 * 			the consumer
	 * @param queueName 
	 *			 the queue name
	 * @param bindingKey
	 * 			 the binding key
	 * @throws IOException 
	 * 				communication problem with the broker.
	 */
	public void addConsumer(final Consumer consumer, final String queueName, final String bindingKey) throws IOException {
		channel.queueBind(queueName, exchangeName, bindingKey);	
		this.consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(final String consumerTag, final Envelope envelope,
					final AMQP.BasicProperties properties, final byte[] body) throws IOException {
				consumer.handleDelivery(consumerTag, envelope, properties, body);
				nbMsgReceived++;
			}
		};

	}

	/**
	 * starts consuming and consume.
	 * @param channel
	 * 			the channel
	 * 
	 * @return the AMQP response.
	 * @throws IOException
	 *             communication problem with the broker.
	 */
	public String startConsumption(final Channel channel) throws IOException {

		return channel.basicConsume(queueName, true, consumer);
	}


	/**
	 * closes the channel and the connection with the broker.
	 * 
	 * @throws IOException
	 *             communication problem.
	 * @throws TimeoutException
	 *             broker to long to communicate with.
	 */
	public void close() throws IOException, TimeoutException {
		if (channel != null) {
			channel.close();
		}
		if (connection != null) {
			connection.close();
		}
	}
	/**
	 * 
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * 
	 * @param channel
	 * 			the channel
	 */
	public void setChannel(final Channel channel) {
		this.channel = channel;
	}


	/**
	 * gets the number of messages received by this consumer.
	 * 
	 * @return the number of messages.
	 */
	public int getNbMsgReceived() {
		return nbMsgReceived;
	}


}
