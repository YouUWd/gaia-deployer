package utils;

import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Hex;
import org.joda.time.DateTime;

public class Sign {
	public static void main(String[] args) throws Exception {
		String ip = "11.193.180.1";
		Map<String, String> params = Maps.newHashMap();
		params.put("key", "47240b4e268e940eeef33ab1b9d35e45");
		params.put("ip", ip);
		params.put("exeurl", "cmd://ls;");
		params.put("timestamp", String.valueOf(DateTime.now().getMillis() / 1000));
		System.out.println(params);
		String sign = buildSign(params, "16c7efe1c0e5e61c9c2c8bb124a1b339");
		System.out.println(sign);

		String ss
			= "http://staragent.alibaba-inc.com/api/task?key=47240b4e268e940eeef33ab1b9d35e45&ip=%s&exeurl=%s"
			+ "&timestamp=%s&sign=%s";
		String url = String.format(ss, ip, params.get("exeurl"), params.get("timestamp"), sign);
		System.out.println("curl -i '" + url + "'");
	}

	public static String buildSign(Map<String, String> params, String code) throws Exception {
		StringBuffer buffer = new StringBuffer();
		Map<String, String> sortedMap = new TreeMap<>(params);
		for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
			buffer.append(entry.getKey());
			buffer.append(entry.getValue());
		}     //参数排序
		Mac hmac = Mac.getInstance("HmacSHA1");
		SecretKeySpec sec = new SecretKeySpec(code.getBytes(), "HmacSHA1");
		hmac.init(sec);
		byte[] digest = hmac.doFinal(buffer.toString().getBytes("UTF-8"));
		return new String(new Hex().encode(digest), "UTF-8");  //sign加密
	}
}
