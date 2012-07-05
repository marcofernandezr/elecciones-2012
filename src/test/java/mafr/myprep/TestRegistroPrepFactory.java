package mafr.myprep;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestRegistroPrepFactory {

	@Test
	public void shouldCreateRegistroCompleto() throws Exception{
		RegistroPrep acta = RegistroPrepFactory.create("1|1|338|1|B|0|2|2|127|65|19|8|1|2|31|20|6|1|1|0|0|6|287|505| |1|null|2012-07-02 01:25:00.0|2012-07-02 04:28:04.0|2012-07-02 04:28:15.608");
		assertNotNull(acta);
		assertTrue(acta.isCompleto());
	}
	
	@Test
	public void shouldCreateRegistroIncompleto() throws Exception {
		RegistroPrep acta = RegistroPrepFactory.create("11|4|894|1|C|0|2|2|61|95|23|11|8|2|7|18|3|1|Sin dato|Sin dato|Sin dato|6|235|480| |1|null|2012-07-01 23:46:00.0|2012-07-02 12:36:11.0|2012-07-02 12:36:18.046");
		assertNotNull(acta);
		assertFalse(acta.isCompleto());
	}
	
}
