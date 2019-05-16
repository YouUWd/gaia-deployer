package io.gaiapipeline;

import io.gaiapipeline.javasdk.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class Pipeline {
	private static final Logger LOGGER = Logger.getLogger(Pipeline.class.getName());

	//下载代码
	private static Handler Download = (gaiaArgs) -> {
		LOGGER.info("Download project started!");
		Thread.sleep(5000);
		LOGGER.info("Download project finished!");
	};
	//适配代码和环境
	private static Handler Fit = (gaiaArgs) -> {
		LOGGER.info("Fit project started!");
		Thread.sleep(5000);
		LOGGER.info("Fit project finished!");
	};

	//启动(重启)
	private static Handler Start = (gaiaArgs) -> {
		LOGGER.info("Start project started!");
		Thread.sleep(5000);
		LOGGER.info("Start project finished!");
	};
	//清理
	private static Handler Cleanup = (gaiaArgs) -> {
		LOGGER.info("Cleanup has been started!");
		Thread.sleep(5000);
		LOGGER.info("Cleanup has been finished!");
	};

	public static void main(String[] args) {
		PipelineJob download = new PipelineJob();
		download.setTitle("下载代码");
		download.setDescription("下载项目代码。");
		download.setHandler(Download);

		PipelineJob fit = new PipelineJob();
		fit.setTitle("适配环境");
		fit.setDescription("适配环境信息。");
		fit.setHandler(Fit);
		fit.setDependsOn(new ArrayList<>(Arrays.asList("下载代码")));

		PipelineJob start = new PipelineJob();
		start.setTitle("启动");
		start.setDescription("启动或重启");
		start.setHandler(Start);
		start.setDependsOn(new ArrayList<>(Arrays.asList("适配环境")));

		PipelineJob cleanup = new PipelineJob();
		cleanup.setTitle("收尾");
		cleanup.setDescription("收尾工作，检查是否发布成功。");
		cleanup.setHandler(Cleanup);
		cleanup.setDependsOn(new ArrayList<>(Arrays.asList("启动")));

		Javasdk sdk = new Javasdk();
		try {
			sdk.Serve(new ArrayList<>(Arrays.asList(download, fit, start, cleanup)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}