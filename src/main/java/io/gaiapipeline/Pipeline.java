package io.gaiapipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Stream;

import io.gaiapipeline.javasdk.Handler;
import io.gaiapipeline.javasdk.InputType;
import io.gaiapipeline.javasdk.Javasdk;
import io.gaiapipeline.javasdk.PipelineArgument;
import io.gaiapipeline.javasdk.PipelineJob;
import utils.CommandResult;
import utils.CommandUtil;

public class Pipeline {
	private static final Logger LOGGER = Logger.getLogger(Pipeline.class.getName());

	private static Handler MyAwesomeJob = (gaiaArgs) -> {
		for (PipelineArgument arg : gaiaArgs) {
			LOGGER.info("Key: " + arg.getKey() + "; Value: " + arg.getValue());
		}

		String[] ips = gaiaArgs.get(3).getValue().split(",");
		Stream.of(ips).parallel().forEach(ip -> {
			CommandResult result = null;
			try {
				result = CommandUtil.exec(gaiaArgs.get(0).getValue(), gaiaArgs.get(1).getValue(),
					gaiaArgs.get(2).getValue(),
					gaiaArgs.get(3).getValue(), gaiaArgs.get(4).getValue());
			} catch (Exception e) {
				LOGGER.info("ERROR: " + ip);
			}
			LOGGER.info(ip + " Result: " + result);
		});

	};

	public static void main(String[] args) {
		PipelineJob myjob = new PipelineJob();
		myjob.setTitle("命令通道");
		myjob.setDescription("在指定机器上执行指令。");
		myjob.setHandler(MyAwesomeJob);

		PipelineArgument vaultDomain = new PipelineArgument();
		vaultDomain.setType(InputType.VaultInp);
		vaultDomain.setKey("domain");

		PipelineArgument vaultKey = new PipelineArgument();
		vaultKey.setType(InputType.VaultInp);
		vaultKey.setKey("key");

		PipelineArgument vaultCode = new PipelineArgument();
		vaultCode.setType(InputType.VaultInp);
		vaultCode.setKey("code");

		PipelineArgument argUsernameIP = new PipelineArgument();
		// Instead of InputType.TextFieldInp you can also use InputType.TextAreaInp
		// for a text area or InputType.BoolInp for boolean input.
		argUsernameIP.setType(InputType.TextFieldInp);
		argUsernameIP.setKey("ip");
		argUsernameIP.setDescription("输入指令执行机器（多个ip使用英文,分割）:");

		PipelineArgument argUsernameCmd = new PipelineArgument();
		// Instead of InputType.TextFieldInp you can also use InputType.TextAreaInp
		// for a text area or InputType.BoolInp for boolean input.
		argUsernameCmd.setType(InputType.TextFieldInp);
		argUsernameCmd.setKey("cmd");
		argUsernameCmd.setDescription("输入指令:");

		myjob.setArgs(new ArrayList<>(Arrays.asList(vaultDomain, vaultKey, vaultCode, argUsernameIP, argUsernameCmd)));

		Javasdk sdk = new Javasdk();
		try {
			sdk.Serve(new ArrayList<>(Arrays.asList(myjob)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}