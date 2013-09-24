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

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

/**
 * Wrapper for the RRD graph command.
 * 
 * @author Didier Fetter
 *
 */
public class GraphCommand extends RRDCommand {
	private static Logger logger = Logger.getLogger(GraphCommand.class);
	private String[] args;
	private boolean isOutputParsed = false;
	private BufferedImage image;
	
	/**
	 * Create a wrapper object for the RRD graph command.
	 * 
	 * @param args
	 */
	public GraphCommand(String... args) {
		try {
			this.args = args;
			CommandLineParser parser = new PosixParser();
			Options options = new Options();
			options.addOption("a", "imgformat", true, "image format");
			options.addOption("s", "start", true, "start (default end-1day)");
			options.addOption("e", "end", true, "end (default now)");
			options.addOption("t", "title", true, "graph title");
			options.addOption("n", "font", true, "font");
			options.addOption("w", "width", true, "image width");
			options.addOption("h", "height", true, "image height");
			options.addOption("b", "base", true, "base value");
			options.addOption("A", "alt-autoscale-max", false, "alternate scale algorithm");
			options.addOption("l", "lower-limit", true, "lower limit");
			options.addOption("u", "upper-limit", true, "upper limit");
			options.addOption("v", "vertical-label", true, "vertical label");
			options.addOption("E", "slope-mode", false, "enable slope mode");
			
			CommandLine cmd = parser.parse(options, args, false);
			for(Option option : cmd.getOptions()) {
				logger.trace("Parsed option : " + option.getLongOpt());
			}
			for(String arg : cmd.getArgs()) {
				logger.trace("Remaining arg : " + arg);
			}
			
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	@Override
	public void execute(OutputStream out) {
		String command = "graph";
		for(String arg : args ) {
			command += " " + arg;
		}
		try {
			ByteBuffer response = sendCommandToServer(command);
			WritableByteChannel channel = Channels.newChannel(out);
			channel.write(response);
			isOutputParsed = false;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void execute() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		execute(out);
		try {
			parseOutput(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void parseOutput(byte[] output) throws IOException {
		if(isOutputParsed) return;

		ByteArrayInputStream in = new ByteArrayInputStream(output);
		image = ImageIO.read(in);
		if(logger.isTraceEnabled())
			logger.trace("Image size : " + image.getWidth() + "x" + image.getHeight());

		isOutputParsed = true;
	}
	
	public BufferedImage getImage() {
		return image;
	}

}
