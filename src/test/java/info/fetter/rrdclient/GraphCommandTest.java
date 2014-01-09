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
import info.fetter.rrdclient.util.GraphServer;

import java.io.File;

import javax.imageio.ImageIO;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.RootLogger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class GraphCommandTest {
	Logger logger = Logger.getLogger(GraphCommandTest.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		BasicConfigurator.configure();
		RootLogger.getRootLogger().setLevel(TRACE);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		BasicConfigurator.resetConfiguration();
	}

	@Test
	public void testGraphCommandPseudo() {
		try {
			String[] args = new String[] {"-", "--imgformat=PNG", "--start=-86400", "--end=-300", "--title='toto'", "--base=1000", "--height=120", "--width=500", "--alt-autoscale-max", "--lower-limit=0", "--vertical-label='processes'", "--slope-mode", "--font", "TITLE:12:", "--font", "AXIS:8:", "--font", "LEGEND:10:", "--font", "UNIT:8:", "DEF:a=\"toto.rrd\":ucd_hrSystemProcess:AVERAGE AREA:a#F51D30FF:\"Running Processes\"", "GPRINT:a:LAST:\"Current\\:%8.0lf\"", "GPRINT:a:AVERAGE:\"Average\\:%8.0lf\"", "GPRINT:a:MAX:\"Maximum\\:%8.0lf\""};
			GraphCommand command = new GraphCommand(args);
			GraphServer server = new GraphServer(13901, new File(FetchServer.class.getClassLoader().getResource("GraphResponse1.png").toURI()));
			command.execute("localhost", 13901);

			ImageIO.write(command.getImage(), "png", new File("target/test2.png"));
			
			command = new GraphCommand(args);
			command.execute("localhost", 13901);
			
			ImageIO.write(command.getImage(), "gif", new File("target/test2.gif"));
			
		} catch(Exception e) {
			if(logger.isDebugEnabled())
				e.printStackTrace();
		}
	}
	
	@Test
	public void dummyTest() {}
}
