package info.fetter.rrdclient.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/*
 * Copyright 2014 Didier Fetter
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

/**
 * 
 * Abstract class used to mimic a real RRDTool server.
 * Must be derived to respond to the different types of request.
 * 
 * @author Didier Fetter
 *
 */
public abstract class PseudoServer implements Runnable {
	private static Logger logger = Logger.getLogger(PseudoServer.class);
	protected int port;
	protected ServerSocketChannel serverChannel;
	private Executor threadPool = Executors.newCachedThreadPool();
	
	public PseudoServer(int port) throws IOException {
		logger.debug("Creating new server instance");
		this.port = port;
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.socket().bind(new InetSocketAddress(port));
		Thread acceptThread = new Thread(this,"accept-thread:port:"+port);
		acceptThread.start();
	}
	
	public void run() {
		while(true) {
			try {
				acceptLoop();
			} catch (IOException e) {
				if(logger.isDebugEnabled())
					e.printStackTrace();
			}
		}
	}
	
	private void acceptLoop() throws IOException {
		SocketChannel clientChannel = serverChannel.accept();
		threadPool.execute(new Responder(clientChannel));
	}
	
	private class Responder implements Runnable {
		private SocketChannel clientChannel;
		
		public Responder(SocketChannel clientChannel) {
			this.clientChannel = clientChannel;
		}

		public void run() {
			//TODO : parse request
			String request = null;
			respond(request);
		}
		
	}
	
	protected abstract void respond(String request);
}
