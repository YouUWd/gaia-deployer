package utils;

import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class Sign {

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
