package utils;

import java.io.File;

import lombok.extern.log4j.Log4j;

@Log4j
public class ShellUtil {

	public static void exec(String cmd) throws Exception {
		log.info("exec " + cmd);
		ProcessBuilder builder = new ProcessBuilder();
		builder.command("sh", "-c", cmd);
		builder.directory(new File(System.getProperty("user.home")));
		Process process = builder.start();
		int exitCode = process.waitFor();
		process.destroy();
		assert exitCode == 0;
		if (exitCode != 0) {
			log.error("exec " + cmd + " fail!!!");
			throw new RuntimeException("Exec Cmd Fail \"" + cmd + "\"");
		}
	}
}