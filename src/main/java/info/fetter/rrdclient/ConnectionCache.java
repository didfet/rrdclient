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
	private Map<InetSocketAddress,SocketChannel> channelMap = new HashMap<InetSocketAddress,SocketChannel>();
	
	public void put(InetSocketAddress server, SocketChannel channel) {
		channelMap.put(server, channel);
	}
	
	/**
	 * 
	 * Returns a connection to the server. If the connection doesn't exist, it is created and put in the cache.
	 * 
	 * @param server
	 * @return a connection to the server
	 * @throws IOException
	 */
	public SocketChannel get(InetSocketAddress server) throws IOException {
		SocketChannel channel = channelMap.get(server);
		if(channel == null) {
			channel = SocketChannel.open(server);
			put(server, channel);
		}
		return channel;
	}
}
