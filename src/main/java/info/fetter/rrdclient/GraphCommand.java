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

import java.io.OutputStream;

/**
 * Wrapper for the RRD graph command.
 * 
 * @author Didier Fetter
 *
 */
public class GraphCommand extends RRDCommand {

	/**
	 * Create a wrapper object for the RRD graph command.
	 * 
	 * @param fileName
	 * @param args
	 */
	public GraphCommand(String fileName, String... args) {
		
	}
	
	@Override
	public void execute(OutputStream out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

}
