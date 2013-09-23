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

import java.util.Date;

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
		RootLogger.getRootLogger().setLevel(TRACE);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		BasicConfigurator.resetConfiguration();
	}

	@Test
	public void testFetchCommand() {
		try {
			//String fileName = "up0te003_-_serveur_cacti_load_15min_13526.rrd";
			String fileName = "up0te003_-_serveur_cacti_apache_total_kbytes_13642.rrd";
			//String fileName = "toto";
			String CF = "AVERAGE";
			//String[] args = new String[] {"--start" , "-1d", "--resolution", "3600"};
			String[] args = new String[] {"--start" , "-1m", "--resolution", "86400"};
			FetchCommand command = new FetchCommand(fileName, CF, args);
			command.execute("UP0TE003", 13900);

			DataTable<Double> data = command.getDataTable();
			//Double value = data.getData("apache_idle_workers", new Date(1375833600000L));
			Double value = data.getData("apache_idle_workers", 3);
			logger.debug("Extracted value : " + value);
			value = data.getData("apache_total_kbytes", data.getNumberOfRows()-3);
			logger.debug("Extracted value : " + value);
			
			command = new FetchCommand(fileName, CF, args);
			command.execute("UP0TE003", 13900);
			
		} catch(Exception e) {
			if(logger.isDebugEnabled())
				e.printStackTrace();
		}
	}
}
