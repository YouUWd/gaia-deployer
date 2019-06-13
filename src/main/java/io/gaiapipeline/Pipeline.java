package io.gaiapipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import io.gaiapipeline.javasdk.Handler;
import io.gaiapipeline.javasdk.InputType;
import io.gaiapipeline.javasdk.Javasdk;
import io.gaiapipeline.javasdk.PipelineArgument;
import io.gaiapipeline.javasdk.PipelineJob;
import utils.CommandUtil;
import utils.Commands;
import utils.ShellUtil;

/**
 * 代码部署工具
 * 0、选择要发布的代码branch
 * 1、前端打包
 * 2、后端打包
 * 3、上传war包
 * 4、下载war包到目标机器
 * 5、备份代码
 * 6、替换代码
 * 7、重启
 * 8、验证是否正常启动
 */
public class Pipeline {
	private static final Logger LOGGER = Logger.getLogger(Pipeline.class.getName());
	private static String HOSTS;

	private static void execute(ArrayList<PipelineArgument> gaiaArgs, String cmd) throws Exception {
		System.out.println("=======" + gaiaArgs);
		CommandUtil.exec(gaiaArgs.get(0).getValue(), gaiaArgs.get(1).getValue(),
			gaiaArgs.get(2).getValue(),
			HOSTS, cmd);
	}

	private static Handler CheckoutHandler = (gaiaArgs) -> {
		ShellUtil.exec("sh /home/youyou.dyy/scripts/manager_checkout.sh");
		LOGGER.info("CheckoutHandler DONE");
	};
	private static Handler NpmBuildHandler = (gaiaArgs) -> {
		ShellUtil.exec("sh /home/youyou.dyy/scripts/manager_build.sh");
		LOGGER.info("NpmBuildHandler DONE");
	};

	private static Handler MvnPackageHandler = (gaiaArgs) -> {
		ShellUtil.exec("sh /home/youyou.dyy/scripts/manager_package.sh");
		LOGGER.info("MvnPackageHandler DONE");
	};
	private static Handler UploadHandler = (gaiaArgs) -> {
		ShellUtil.exec("sh /home/youyou.dyy/scripts/manager_upload.sh");
		LOGGER.info("UploadHandler DONE");
	};

	private static Handler DownloadHandler = (gaiaArgs) -> {
		execute(gaiaArgs, Commands.DOWNLOAD_BACKUP);

		execute(gaiaArgs, Commands.DOWNLOAD_DEPLOY);

		execute(gaiaArgs, Commands.DOWNLOAD_CHECK);

		execute(gaiaArgs, Commands.DOWNLOAD_WAR);
		LOGGER.info("DownloadHandler DONE");
	};

	private static Handler BackupHandler = (gaiaArgs) -> {
		execute(gaiaArgs, Commands.BACKUP);
		LOGGER.info("BackupHandler DONE");
	};

	private static Handler ReplaceHandler = (gaiaArgs) -> {
		LOGGER.info("ReplaceHandler DONE");

	};

	private static Handler RestartHandler = (gaiaArgs) -> {
		LOGGER.info("RestartHandler DONE");

	};

	private static Handler CheckHandler = (gaiaArgs) -> {
		LOGGER.info("CheckHandler DONE");

	};

	public static void main(String[] args) {
		//参数准备
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

		HOSTS = argUsernameIP.getValue();

		//job开始
		PipelineJob checkout = new PipelineJob();
		checkout.setArgs(new ArrayList(Arrays.asList(vaultDomain, vaultKey, vaultCode)));
		checkout.setTitle("拉取代码");
		checkout.setDescription("更新分支最新代码。");
		checkout.setHandler(CheckoutHandler);

		PipelineJob npmBuild = new PipelineJob();
		npmBuild.setArgs(new ArrayList(Arrays.asList(vaultDomain, vaultKey, vaultCode)));
		npmBuild.setTitle("编译前端");
		npmBuild.setDescription("编译前端（npm run build）。");
		npmBuild.setHandler(NpmBuildHandler);

		PipelineJob mvnPackage = new PipelineJob();
		mvnPackage.setArgs(new ArrayList(Arrays.asList(vaultDomain, vaultKey, vaultCode)));
		mvnPackage.setTitle("打包项目");
		mvnPackage.setDescription("打包项目（mvn clean package）。");
		mvnPackage.setHandler(MvnPackageHandler);

		PipelineJob upload = new PipelineJob();
		upload.setArgs(new ArrayList(Arrays.asList(vaultDomain, vaultKey, vaultCode)));
		upload.setTitle("上传WAR包");
		upload.setDescription("上传WAR包到仓库。");
		upload.setHandler(UploadHandler);

		PipelineJob download = new PipelineJob();
		download.setArgs(new ArrayList(Arrays.asList(vaultDomain, vaultKey, vaultCode)));
		download.setTitle("下载WAR包");
		download.setDescription("下载WAR包到指定机器。");
		download.setHandler(DownloadHandler);

		PipelineJob backup = new PipelineJob();
		backup.setArgs(new ArrayList(Arrays.asList(vaultDomain, vaultKey, vaultCode)));
		backup.setTitle("备份代码");
		backup.setDescription("备份当前环境的运行代码。");
		backup.setHandler(BackupHandler);

		PipelineJob replace = new PipelineJob();
		replace.setArgs(new ArrayList(Arrays.asList(vaultDomain, vaultKey, vaultCode)));
		replace.setTitle("更新代码");
		replace.setDescription("更新当前机器运行的代码。");
		replace.setHandler(ReplaceHandler);

		PipelineJob restart = new PipelineJob();
		restart.setArgs(new ArrayList(Arrays.asList(vaultDomain, vaultKey, vaultCode)));
		restart.setTitle("重启服务器");
		restart.setDescription("重启服务器，加载最新代码。");
		restart.setHandler(RestartHandler);

		PipelineJob check = new PipelineJob();
		check.setArgs(new ArrayList(Arrays.asList(vaultDomain, vaultKey, vaultCode)));
		check.setTitle("检查发布情况");
		check.setDescription("检查代码是否成功发布。");
		check.setHandler(CheckHandler);

		Javasdk sdk = new Javasdk();
		try {
			sdk.Serve(new ArrayList(
				Arrays.asList(checkout, npmBuild, mvnPackage, upload, download, backup, replace, restart, check)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}