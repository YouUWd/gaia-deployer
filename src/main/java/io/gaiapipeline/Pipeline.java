package io.gaiapipeline;

import io.gaiapipeline.javasdk.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class Pipeline
{
	private static final Logger LOGGER = Logger.getLogger(Pipeline.class.getName());

	private static Handler MyAwesomeJob = (gaiaArgs) -> {
		for (PipelineArgument arg: gaiaArgs) {
			LOGGER.info("Key: " + arg.getKey() + "; Value: " + arg.getValue());
		}
	};

	public static void main( String[] args )
	{
		PipelineJob myjob = new PipelineJob();
		myjob.setTitle("MyAwesomeJob");
		myjob.setDescription("Do something awesome.");
		myjob.setHandler(MyAwesomeJob);
		PipelineArgument vaultParam = new PipelineArgument();
		vaultParam.setType(InputType.VaultInp);
		vaultParam.setKey("dbpassword");
		myjob.setArgs(new ArrayList<>(Arrays.asList(vaultParam)));

		Javasdk sdk = new Javasdk();
		try {
			sdk.Serve(new ArrayList<>(Arrays.asList(myjob)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}