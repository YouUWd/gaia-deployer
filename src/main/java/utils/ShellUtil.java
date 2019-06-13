package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShellUtil {

	public static void exec(String cmd) throws Exception {
		ProcessBuilder builder = new ProcessBuilder();
		builder.command("sh", "-c", cmd);
		builder.directory(new File(System.getProperty("user.home")));
		Process process = builder.start();
		InputStream inputStream = process.getInputStream();
		new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(System.out::println);
		int exitCode = process.waitFor();
		process.destroy();
		assert exitCode == 0;
		if (exitCode != 0) {
			throw new RuntimeException("Exec Cmd Fail \"" + cmd + "\"");
		}
	}
}