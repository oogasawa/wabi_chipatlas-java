package jp.ac.nig.ddbj.wabi.validator.chipatlas;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class QsubOptions {
	
	Options options = new Options();

	public void createOptions() {
		
		options.addOption(Option.builder("a")
				.hasArg()
				.argName("date_time")
				.build());

		options.addOption(Option.builder("ac")
				.hasArgs()
				.argName("variable")
				.build());
		
		options.addOption(Option.builder("ar")
				.hasArg()
				.argName("ar_id")
				.build());
		
		options.addOption(Option.builder("A")
				.hasArg()
				.argName("account_string")
				.build());
		
		options.addOption(Option.builder("l")
				.hasArgs()
				.argName("resource")
				.build());
	
	}
	
}
