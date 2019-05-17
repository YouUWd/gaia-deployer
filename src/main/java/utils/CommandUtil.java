package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.http.Consts;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;

@CommonsLog
public class CommandUtil {
	private static final Gson GSON = new Gson();

	public static void main(String[] args) throws Exception {
		CommandUtil.exec("47240b4e268e940eeef33ab1b9d35e45", "16c7efe1c0e5e61c9c2c8bb124a1b339", "11.193.180.1",
			"ls -la");
	}

	public static CommandResult exec(String key, String code, String ip, String cmd) throws Exception {
		Map<String, String> params = Maps.newHashMap();
		params.put("key", key);
		params.put("ip", ip);
		String _cmd = "cmd://" + cmd;
		params.put("exeurl", _cmd);
		String timestamp = String.valueOf(DateTime.now().getMillis() / 1000);
		params.put("timestamp", timestamp);
		String sign = Sign.buildSign(params, code);

		String template
			= "http://staragent.alibaba-inc.com/api/task?key=%s&ip=%s&exeurl=%s"
			+ "&timestamp=%s&sign=%s";
		String url = Strings.lenientFormat(template, key, ip, _cmd, timestamp, sign);

		log.info("URL:" + url);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		CloseableHttpResponse response = httpClient.execute(
			new HttpGet("http://staragent.alibaba-inc.com/api/task?" + paramEncode(params) + "sign=" + sign));
		String result = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
		CommandResult commandResult = GSON.fromJson(result, CommandResult.class);
		return commandResult;
	}

	public static String paramEncode(Map<String, String> params) {
		StringBuilder builder = new StringBuilder();
		params.forEach((k, v) -> {
			try {
				builder.append(k).append("=").append(URLEncoder.encode(v, Consts.UTF_8.toString())).append("&");
			} catch (UnsupportedEncodingException e) {
			}
		});
		return builder.toString();
	}
}
