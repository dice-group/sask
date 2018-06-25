package org.dice_research.sask.open_ie_ms;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * 
 * @author Suganya Kannan
 *
 */

public class OpenIEMSControllerTest {

	@Test
	public void testExtract() {
		OpenIEDTO openIE = new OpenIEDTO();
		String result;
		OpenIEMSController control = new OpenIEMSController();
		openIE.setText("Tony Hall leads the BBC.");
		result = control.extract(openIE);

		String expected = "<http://example.org/Tony%20Hall> <http://example.org/lead> <http://example.org/BBC> .";

		assertEquals(expected, result.trim());

	}

}
