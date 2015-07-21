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

import static org.apache.log4j.Level.*;
import info.fetter.rrdclient.util.FetchServer;

import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.RootLogger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class FetchCommandTest {
	Logger logger = Logger.getLogger(FetchCommandTest.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		BasicConfigurator.configure();
		RootLogger.getRootLogger().setLevel(DEBUG);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		BasicConfigurator.resetConfiguration();
	}

	@Test
	public void testFetchCommandOK() {
		try {
			String fileName = "toto.rrd";
			String CF = "AVERAGE";
			String[] args = new String[] {"--start" , "-1m", "--resolution", "86400"};
			FetchCommand command = new FetchCommand(fileName, CF, args);
			@SuppressWarnings("unused")
			FetchServer server = new FetchServer(13900,new File(FetchServer.class.getClassLoader().getResource("FetchResponse1.txt").toURI()));
			//command.execute("localhost", 13900);
			command.setServerAddress("localhost");
			command.setServerPort(13900);
			command.execute();
			System.out.println(command.getDataTable().toJSON());

		} catch(Exception e) {
			if(logger.isDebugEnabled())
				e.printStackTrace();
		}
	}

	@Test(expected = RRDToolError.class)
	public void testFetchCommandKO() throws IOException, URISyntaxException {
		String fileName = "toto.rrd";
		String CF = "AVERAGE";
		String[] args = new String[] {"--start" , "-1m", "--resolution", "86400"};
		FetchCommand command = new FetchCommand(fileName, CF, args);
		@SuppressWarnings("unused")
		FetchServer server = new FetchServer(13902,new File(FetchServer.class.getClassLoader().getResource("FetchResponse2.txt").toURI()));
		command.execute("localhost", 13902);
	}

	@Test
	public void dummyTest() {}
}
