package utils;

import java.io.File;
import java.util.logging.Logger;

public class ShellUtil {

	private static final Logger LOGGER = Logger.getLogger(ShellUtil.class.getName());

	public static void exec(String cmd) throws Exception {
		LOGGER.info("exec " + cmd);
		ProcessBuilder builder = new ProcessBuilder();
		builder.command("sh", "-c", cmd);
		builder.directory(new File(System.getProperty("user.home")));
		Process process = builder.start();
		int exitCode = process.waitFor();
		process.destroy();
		assert exitCode == 0;
		if (exitCode != 0) {
			LOGGER.warning("exec " + cmd + " fail!!!");
			throw new RuntimeException("Exec Cmd Fail \"" + cmd + "\"");
		}
	}
}