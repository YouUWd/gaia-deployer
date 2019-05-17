package utils;

import lombok.Data;

@Data
public class CommandResult {
	boolean SUCCESS;
	String JOBRESULT, ERRORMSG;

	@Override
	public String toString() {
		return
			"SUCCESS=" + SUCCESS + System.getProperty("line.separator") +
				JOBRESULT + System.getProperty("line.separator") +
				"ERRORMSG='" + ERRORMSG + "'";
	}
}
