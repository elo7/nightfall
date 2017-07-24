package com.elo7.nightfall.di.providers.reporter.statsd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

class UDPSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(UDPSender.class);
	private static String CHAR_SET = "utf-8";

	private final InetSocketAddress address;
	private DatagramChannel channel;

	UDPSender(String host, int port) throws IOException {
		address = new InetSocketAddress(InetAddress.getByName(host), port);
		channel = DatagramChannel.open();
	}

	void send(String stat) {
		try {
			final byte[] data = stat.getBytes(CHAR_SET);
			if (channel.isOpen()) {
				final int nbSentBytes = channel.send(ByteBuffer.wrap(data), address);

				if (data.length != nbSentBytes) {
					LOGGER.error(
							"Could not send entirely stat {} to host {}:{}. Only sent {} bytes out of {} bytes",
							stat, address.getHostName(), address.getPort(), nbSentBytes, data.length);
				}
			} else {
				LOGGER.error("StatsD socket is closed! Dropped metrics: {}", stat);
				channel = DatagramChannel.open();
				LOGGER.debug("Channel re-opened");
			}
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Error generating StatsD Payload. Couldn't generate byte array", e);
		} catch (IOException e) {
			LOGGER.error(
					"Could not send stat {} to host {}:{}",
					stat, address.getHostName(), address.getPort(), e);
		}
	}
}
