package org.btc4j.daemon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Logger;

public class BtcNotificationSource extends Observable implements Runnable {
	private static final Logger LOG = Logger
			.getLogger(BtcNotificationSource.class.getName());
	private int port;

	public BtcNotificationSource(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		Thread currentThread = Thread.currentThread();
		try (ServerSocket server = new ServerSocket(port);) {
			LOG.info("thread " + currentThread.getName()
					+ " started server socket " + port);
			while (!currentThread.isInterrupted()) {
				LOG.info("thread " + currentThread.getName()
						+ " waiting to accept " + port);
				try (Socket socket = server.accept();
						BufferedReader in = new BufferedReader(
								new InputStreamReader(socket.getInputStream()));) {
					String line;
					while ((line = in.readLine()) != null) {
						LOG.info("thread " + currentThread.getName()
								+ " received " + line);
						setChanged();
						notifyObservers(line);
					}
				} catch (Throwable t) {
					LOG.warning(String.valueOf(t));
				}
			}
		} catch (Throwable t) {
			LOG.severe(String.valueOf(t));
		}
	}
}
