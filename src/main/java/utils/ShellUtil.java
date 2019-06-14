package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class ShellUtil {

	private static final Logger LOGGER = Logger.getLogger(ShellUtil.class.getName());

	public static void exec(String cmd) throws Exception {
		ProcessBuilder builder = new ProcessBuilder();
		builder.command("sh", "-c", cmd);
		builder.directory(new File(System.getProperty("user.home")));
		Process process = builder.start();
		InputStream inputStream = process.getInputStream();
		new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(LOGGER::info);
		int exitCode = process.waitFor();
		process.destroy();
		assert exitCode == 0;
		if (exitCode != 0) {
			throw new RuntimeException("Exec Cmd Fail \"" + cmd + "\"");
		}
	}
}