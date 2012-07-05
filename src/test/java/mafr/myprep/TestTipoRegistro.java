package mafr.myprep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TestTipoRegistro {

	@Test
	public void shouldBeEqual() {
		assertEquals(new TipoRegistro(Criterio.COMPLETO, Criterio.CONTABILIZADO), new TipoRegistro(
				Criterio.CONTABILIZADO, Criterio.COMPLETO));
	}

	@Test
	public void shouldNotBeEqual() {
		assertFalse(new TipoRegistro(Criterio.COMPLETO, Criterio.SIN_CONTABILIZAR).equals(new TipoRegistro(
				Criterio.CONTABILIZADO, Criterio.COMPLETO)));
	}

}
