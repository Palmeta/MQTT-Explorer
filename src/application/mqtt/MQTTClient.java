package application.mqtt;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import application.main.Launcher;
import application.main.MainViewController;
import application.treeComponents.TreeItems;




public class MQTTClient {

	public static Map<String,String>currentData = new HashMap<String, String>();




	static MQTT mqtt = new MQTT();
	private static CallbackConnection connection;
	private String BROKER_URL = "tcp://127.0.0.1";
	private Topic[] subscribeTopics;
	public static final boolean runAsLocalHost = false;
	
	public static boolean initialized = false;//becomes true after running initialization regardless if server is connected or not.
	public static int installation = 0;
	final static ExecutorService recieveExecutor = Executors.newFixedThreadPool(1);


	public static final String baseTopic = "#" ;
	


	public MQTTClient(){
		
	}






	public void initialize(String serverURL) throws Exception{
		
		
		subscribeTopics = new Topic[1];
		subscribeTopics[0] = new Topic(baseTopic, QoS.EXACTLY_ONCE);

		BROKER_URL = serverURL;
		
		if(runAsLocalHost){
			BROKER_URL = "tcp://localhost:1883";
		}
		System.out.println("MQTTClient.initialize -> Broker URL : " + BROKER_URL);

		

		mqtt.setHost(BROKER_URL);
		mqtt.setCleanSession(true);
		mqtt.setClientId("MQTT_Explorer");

		mqtt.setUserName("MQTT_Explorer");
		mqtt.setPassword("pass");
		
		mqtt.setKeepAlive((short) 60);
		mqtt.setVersion("3.1.1");

		connection = mqtt.callbackConnection();

		connection.listener(new Listener() {

			public void onDisconnected() {
				System.out.println("MQTTClient.initialize -> Listener Disconnected.");
				
				connected = false;
				//	    	connect();

			}

			public void onConnected() {
				System.out.println("MQTTClient.initialize -> Listener Connected.");
				
				
				connected = true;

			}

			public void onPublish(final UTF8Buffer topic, final Buffer payload, Runnable ack) {

				recieveExecutor.submit(new Runnable() {
					public void run() {
						try {
							
							
							String data = URLDecoder.decode(new String(payload.toByteArray()).replaceAll("\\<[^>]*>",""),"UTF-8");
							data = data.trim();
							data.replaceAll("\\r\\n", "");
							
							String top = topic.toString();
							
							TreeItems t = new TreeItems();
							t.setTopic(top);
							t.setData(data);
							MainViewController.addToList(t);
						
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ack.run();
			}

			public void onFailure(Throwable value) {
				//	        connection.kill(null); // a connection failure occured.
				System.out.println("MQTTClient.initialize -> Unable to connect.");
			
				connected = false;
				connect();

			}
		});

		connection.connect(new Callback<Void>() {
			public void onFailure(Throwable value) {
				//	        result.failure(value); // If we could not connect to the server.
				System.out.println("MQTTClient.initialize -> Unable to connect. Cause: " + value.toString());
			
				connected = false;
				connect();	    

			}

			// Once we connect..
			public void onSuccess(Void v) {

				// Subscribe to a topic

				connection.subscribe(subscribeTopics, new Callback<byte[]>() {
					public void onSuccess(byte[] qoses) {
						System.out.println("MQTTClient.initialize -> Subscribed to " + subscribeTopics.length + " topics successfully.");
					
						
						connected = true;

					}
					public void onFailure(Throwable value) {

						System.out.println("MQTTClient.initialize -> Unable to subscribe to " + subscribeTopics.length + " topics.");
					
						connected = false;
						connect();


					}
				});
				//	        connection.transport().getRemoteAddress();



			}
		});
		initialized= true;
	}




	public static void publish(String topic,String message,QoS qos,boolean retain){

		// Send a message to a topic
		String encodedUrl = "";
		try {
			encodedUrl = URLEncoder.encode(message, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final String msg = encodedUrl;
		final String top = topic;
		final String qOS = String.valueOf(qos.ordinal());

		connection.publish(topic, encodedUrl.getBytes(), qos, retain, new Callback<Void>() {


			public void onSuccess(Void v) {

				System.out.println("MQTTClient.publish -> Published: " + msg + " to topic: " + top + " with qos: " + qOS + " successfully." );
			}

			public void onFailure(Throwable value) {

				System.out.println("MQTTClient.publish -> Failed to publish: " + msg + " to topic: " + top + " with qos: " + qOS + " failed." );
				//               connection.kill(null); // publish failed.
			}
		});
	}


	public void disconnect(){
		// To disconnect..
		connection.disconnect(new Callback<Void>() {
			public void onSuccess(Void v) {
				// called once the connection is disconnected.
				System.out.println("MQTTClient.disconect -> Disconected");
			

			}
			public void onFailure(Throwable value) {
				// Disconnects never fail.
				System.out.println("MQTTClient.disconect -> Failed disconect");
			}
		});
	}



	private static final ExecutorService executor = Executors.newFixedThreadPool(1);


	public void submit(String topic, String message, QoS qos, boolean retain) {
		executor.submit(new Runnable() {

			public void run() {
	//			publish(topic, message,qos, retain);
			}
		});
	}
	
	
	private static boolean connected = false;
	private static long lastConnectAttempt;

	private void connect(){
		if(System.currentTimeMillis()-lastConnectAttempt < 5000){
			return;
		}
		lastConnectAttempt = System.currentTimeMillis();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		while (!connected){
			System.out.println("MQTTClient.connect -> Trying to Connect to Broker.");
			initialized = false;
			try {
				Launcher.mqtt.initialize(BROKER_URL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			while(!initialized);

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}


}




