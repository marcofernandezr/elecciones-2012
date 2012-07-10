package mafr.myprep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestPrepAnalyzer {

	private PrepAnalyzer prepAnalyzer = new PrepAnalyzer();
	
	@Test
	public void shouldMatchContabilizadosWithOfficialPREPresults() throws Exception {
		PrepAnalysis prepAnalysis = prepAnalyzer.execute("src/test/resources");
		assertNotNull(prepAnalysis);
		
		PrepStats stats = prepAnalysis.getStats(new TipoRegistro(Criterio.CONTABILIZADO));
		assertEquals(63.14, stats.getParticipacionCiudadana(), 0.01);
		assertEquals(38.15, stats.getPorcentajeEPN(), 0.01);
		assertEquals(31.64, stats.getPorcentajeAMLO(), 0.01);
		assertEquals(25.40, stats.getPorcentajeJVM(), 0.01);
		assertEquals(2.30, stats.getPorcentajeGQT(), 0.01);
		assertEquals(2.42, stats.getPorcentajeNulos(), 0.01);
		assertEquals(0.06, stats.getPorcentajeNoRegistrados(), 0.01);
	}
		
}
