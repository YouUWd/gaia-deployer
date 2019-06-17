package utils;

public interface Commands {
	//@formatter:off
	String DOWNLOAD_BACKUP
		= "osscmd get oss://drds-manager-project-repo/drds-manager/manager_backup.sh /home/admin/auto_deploy/manager_backup.sh";
	String DOWNLOAD_DEPLOY
		= "osscmd get oss://drds-manager-project-repo/drds-manager/manager_deploy.sh /home/admin/auto_deploy/manager_deploy.sh";

	String DOWNLOAD_RESTART
		= "osscmd get oss://drds-manager-project-repo/drds-manager/manager_restart.sh /home/admin/auto_deploy/manager_restart.sh";

	String DOWNLOAD_CHECK
		= "osscmd get oss://drds-manager-project-repo/drds-manager/manager_check.sh /home/admin/auto_deploy/manager_check.sh";
	String DOWNLOAD_WAR
		= "osscmd get oss://drds-manager-project-repo/drds-manager/drds-manager-web-2.0.0-SNAPSHOT.war drds-manager-web-2.0.0-SNAPSHOT.war";
	//@formatter:on
	String BACKUP = "sh /home/admin/auto_deploy/manager_backup.sh";
	String DEPLOY = "sh /home/admin/auto_deploy/manager_deploy.sh";
	String RESART = "sh /home/admin/auto_deploy/manager_restart.sh";
	String CHECK = "sh /home/admin/auto_deploy/manager_check.sh";
}