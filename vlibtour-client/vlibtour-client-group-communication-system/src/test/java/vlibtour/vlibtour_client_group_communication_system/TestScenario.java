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
package vlibtour.vlibtour_client_group_communication_system;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.http.client.Client;

import vlibtour.vlibtour_lobby_room_api.InAMQPPartException;
/**
 * @author sana bouchahoua
 * @author graja_am
 *
 */
public class TestScenario {
	/**
	 * the client.
	 */
	private static Client c;
	
	/**
	 * 
	 * @throws IOException
	 * 			IOExcption
	 * @throws InterruptedException
	 * 			InterruptedException
	 * @throws URISyntaxException
	 * 			URISyntaxException
	 */
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
		new ProcessBuilder("rabbitmqctl", "add_vhost", "2").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "add_vhost", "3").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "add_user","1", "passwd1").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "add_user","2", "passwd2").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "add_user","3", "passwd3").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "add_user","4", "passwd4").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "add_user","5", "passwd5").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "set_permissions" ,"-p","2","1", ".*", ".*", ".*").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "set_permissions" ,"-p","2","2", ".*", ".*", ".*").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "set_permissions" ,"-p","2","3", ".*", ".*", ".*").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "set_permissions" ,"-p","2","4", ".*", ".*", ".*").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "set_permissions" ,"-p","3","5", ".*", ".*", ".*").inheritIO().start().waitFor();

		c = new Client("http://127.0.0.1:15672/api/", "guest", "guest");
	}

	@Test
	public void test()
			throws IOException, TimeoutException, InterruptedException, ExecutionException, InAMQPPartException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
		VLibTourGroupCommunicationSystemClient v1 = new VLibTourGroupCommunicationSystemClient("amqp://1:passwd1@localhost:5672/2");
		VLibTourGroupCommunicationSystemClient v2 = new VLibTourGroupCommunicationSystemClient("amqp://2:passwd2@localhost:5672/2");
		VLibTourGroupCommunicationSystemClient v3 = new VLibTourGroupCommunicationSystemClient("amqp://3:passwd3@localhost:5672/2");
		VLibTourGroupCommunicationSystemClient v4 = new VLibTourGroupCommunicationSystemClient("amqp://4:passwd4@localhost:5672/2");
		VLibTourGroupCommunicationSystemClient v5 = new VLibTourGroupCommunicationSystemClient("amqp://5:passwd5@localhost:5672/3");

		Consumer consumer2 = new DefaultConsumer(v2.getChannel()) {
			public int nbMsgReceived=0;
			@Override
			public void handleDelivery(final String consumerTag, final Envelope envelope,
					final AMQP.BasicProperties properties, final byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + "2" + " " + envelope.getRoutingKey() + "':'" + message + "'");
				nbMsgReceived++;
			}
		};

		Consumer consumer4 = new DefaultConsumer(v4.getChannel()) {
			public int nbMsgReceived=0;
			@Override
			public void handleDelivery(final String consumerTag, final Envelope envelope,
					final AMQP.BasicProperties properties, final byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + "4" + " " + envelope.getRoutingKey() + "':'" + message + "'");
				nbMsgReceived++;
			}
		};

		Consumer consumer5 = new DefaultConsumer(v5.getChannel()) {
			public int nbMsgReceived=0;
			@Override
			public void handleDelivery(final String consumerTag, final Envelope envelope,
					final AMQP.BasicProperties properties, final byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + "5" + " " + envelope.getRoutingKey() + "':'" + message + "'");
				nbMsgReceived++;
			}
		};

		v2.addConsumer(consumer2,"2","2");
		v4.addConsumer(consumer4,"4","4");
		v5.addConsumer(consumer5,"5","5");

		v1.publish("message from V1","*.all" );
		v3.publish("message from V3","*.all" );

		v2.startConsumption(v2.getChannel());
		v4.startConsumption(v4.getChannel());
		v5.startConsumption(v5.getChannel());

		Thread.sleep(5000);
		System.out.println("user 2 has "+ v2.getNbMsgReceived()+" messages");
		System.out.println("user 4 has "+ v4.getNbMsgReceived()+" messages");
		System.out.println("user 5 has "+ v5.getNbMsgReceived()+ " messages");
		Assert.assertEquals(2, v2.getNbMsgReceived());
		Assert.assertEquals(2, v4.getNbMsgReceived());
		Assert.assertEquals(0, v5.getNbMsgReceived());

		Thread.sleep(5000);

		v1.close();
		v2.close();
		v3.close();
		v4.close();
		v5.close();

	}

	@AfterClass
	public static void tearDown() throws InterruptedException, IOException {
		new ProcessBuilder("rabbitmqctl", "stop_app").inheritIO().start().waitFor();
		new ProcessBuilder("rabbitmqctl", "stop").inheritIO().start().waitFor();
	}
}