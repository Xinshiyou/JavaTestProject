package com.test.www.common;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandCMDTest {

	public static void main(String[] args) {

		System.out.println("Begin args:==>");
		for (String item : args)
			System.out.println("Args: " + item);
		System.out.println("End args:<==");

		Options options = new Options();
		options.addOption("job", true, "Job config.");
		options.addOption("jobid", true, "Job unique id.");
		options.addOption("mode", true, "Job runtime mode.");

		BasicParser parser = new BasicParser();
		CommandLine cl = null;
		try {
			cl = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (null != cl) {

			System.out.println("job:" + cl.getOptionValue("job"));
			System.out.println("jobid:" + cl.getOptionValue("jobid"));
			System.out.println("mode:" + cl.getOptionValue("mode"));

		} else {
			System.out.println("CL is null");
		}

	}

}
