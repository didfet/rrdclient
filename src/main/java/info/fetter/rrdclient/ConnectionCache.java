package info.fetter.rrdclient;

/*
 * Copyright 2013 Didier Fetter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * Cache for the socket channels in order to reuse them instead of creating a new socket for each command.
 * 
 * @author Didier Fetter
 * 
 */
public class ConnectionCache {
	private Map<InetSocketAddress,ServerContext> channelMap = new HashMap<InetSocketAddress,ServerContext>();

	private void put(InetSocketAddress server, ServerContext context) {
		synchronized(channelMap) {
			channelMap.put(server, context);
		}
	}

	public void remove(InetSocketAddress server) {
		try {
			ServerContext context;
			synchronized(channelMap) {
				context = channelMap.get(server);
			}
			if(context != null) {
				context.close();
				synchronized(channelMap) {
					channelMap.remove(context);
				}
			}

		} catch(IOException e) {}
	}

	/**
	 * 
	 * Returns a connection to the server. If the connection doesn't exist, it is created and put in the cache.
	 * 
	 * @param server
	 * @return a connection to the server
	 * @throws IOException
	 */
	public SocketChannel get(InetSocketAddress server, long inactivityTimeout) throws IOException {
		ServerContext context = channelMap.get(server);
		if(context == null) {
			context = new ServerContext(server);
			put(server, context);
		} else {
			long currentTime = System.currentTimeMillis();
			if(context.getLastUsed() < currentTime - inactivityTimeout) {
				context.close();
				context = new ServerContext(server);
			} else {
				context.updateLastUsed(currentTime);
			}
		}
		return context.getChannel();
	}

	private class ServerContext {
		private SocketChannel channel;
		private long lastUsed;

		public ServerContext(InetSocketAddress server) throws IOException {
			channel = SocketChannel.open(server);
			lastUsed = System.currentTimeMillis();
		}

		/**
		 * @return the channel
		 */
		public SocketChannel getChannel() {
			return channel;
		}

		/**
		 * @return the lastUsed
		 */
		public long getLastUsed() {
			return lastUsed;
		}

		public void updateLastUsed(long currentTime) {
			lastUsed = currentTime;
		}

		public void close() throws IOException {
			channel.close();
		}
	}
}
