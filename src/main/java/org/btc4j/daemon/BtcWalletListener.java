package org.btc4j.daemon;

import java.util.logging.Logger;

import org.btc4j.core.BtcTransaction;

public class BtcWalletListener extends BtcNotificationListener {
	private static final Logger LOGGER = Logger
			.getLogger(BtcWalletListener.class.getName());
	private BtcDaemon daemon;
	
	public BtcWalletListener(int port, BtcDaemon daemon) {
		super(port);
		this.daemon = daemon;
	}
	
	@Override
	public void notifyObservers(Object arg) {
		if (hasChanged()) {
			String txId = String.valueOf(arg).trim();
			BtcTransaction transaction;
			try {
				transaction = daemon.getTransaction(txId);
			} catch (Throwable t) {
				LOGGER.warning(String.valueOf(t));
				transaction = new BtcTransaction();
				transaction.setTransaction(txId);
			}
			super.notifyObservers(transaction);
		}
	}
}