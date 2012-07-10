package mafr.myprep;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestPrepAnalysis {

	private RegistroPrep registro;
	private RegistroPrep registroIncompleto;
	private RegistroPrep registroExcedeListaNominal;
	private RegistroPrep registroTipoActaIncorrecta;
	private RegistroPrep registroParcialmenteIncompleto;

	@Before
	public void setup() throws Exception {
		registro = RegistroPrepFactory
				.create("1|1|339|1|B|0|2|2|198|195|8|8|6|4|51|11|8|1|0|0|0|7|497|700| |1|null|2012-07-02 01:25:00.0|2012-07-02 04:34:59.0|2012-07-02 04:35:04.336");
		registroIncompleto = RegistroPrepFactory
				.create("2|1|630|1|B|0|2|2| | | | | | | | | | | | | | | |439|Sin acta|0|null|null|2012-07-02 14:18:16.0|2012-07-02 14:34:33.549");
		registroExcedeListaNominal = RegistroPrepFactory
				.create("4|1|104|1|B|0|1|2|60|93|114|30|31|41|10|26|19|7|3|0|1|6|441|404|Excede lista nominal|0|null|2012-07-02 14:43:00.0|2012-07-02 14:54:08.0|2012-07-02 14:54:19.636");
		registroTipoActaIncorrecta = RegistroPrepFactory
				.create("1|1|0|46|B|0|1|1|49|16|10|0|2|2|2|2|6|0|0|1|0|0|90|166| |1|null|null|null|2012-07-02 00:41:44.0");
		registroParcialmenteIncompleto = RegistroPrepFactory
				.create("1|2|141|1|C|0|1|2|73|85|58|4|5|4|20|14|8|Ilegible|2|1|Ilegible|5|279|503| |1|null|2012-07-01 21:21:00.0|2012-07-01 21:26:16.0|2012-07-01 21:26:20.829");
	}

	@Test
	public void shouldAddRegistroCompletoValido() {
		PrepAnalysis prepAnalysis = new PrepAnalysis();
		TipoRegistro tipo = new TipoRegistro(Criterio.COMPLETO);
		assertNull(prepAnalysis.getStats(tipo));
		prepAnalysis.addRegistro(tipo, registro);
		PrepStats stats = prepAnalysis.getStats(tipo);
		assertNotNull(stats);
		assertEquals(registro.getAMLO().longValue(), stats.getAMLO());
		assertEquals(registro.getEPN().longValue(), stats.getEPN());
		assertEquals(registro.getGQT().longValue(), stats.getGQT());
		assertEquals(registro.getJVM().longValue(), stats.getJVM());
		assertEquals(registro.getNulos().longValue(), stats.getNulos());
		assertEquals(registro.getNoRegistrados().longValue(), stats.getNoRegistrados());
		assertEquals(registro.getListaNominal().longValue(), stats.getListaNominal());
		assertEquals(registro.getTotalVotosCalculado().longValue(), stats.getVotos());
		long casosFaltantesEsperados = 0L;
		long erroresEsperados = 0L;
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesAMLO());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesEPN());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesJVM());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesGQT());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesNulos());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesNoRegistrados());
		assertEquals(erroresEsperados, stats.getCasosErrorExcedeListaNominal());
		assertEquals(erroresEsperados, stats.getCasosErrorTipoActa());

	}

	@Test
	public void shouldAddRegistroForSameType() throws Exception {
		PrepAnalysis prepAnalysis = new PrepAnalysis();
		TipoRegistro tipo = new TipoRegistro(Criterio.COMPLETO);
		long casosFaltantesEsperados = 0L;
		long erroresEsperados = 0L;
		int times = 5;
		for (int i = 0; i < times; i++) {
			prepAnalysis.addRegistro(tipo, registro);
		}
		PrepStats stats = prepAnalysis.getStats(tipo);
		assertNotNull(stats);
		assertEquals(registro.getAMLO().longValue() * times, stats.getAMLO());
		assertEquals(registro.getEPN().longValue() * times, stats.getEPN());
		assertEquals(registro.getGQT().longValue() * times, stats.getGQT());
		assertEquals(registro.getJVM().longValue() * times, stats.getJVM());
		assertEquals(registro.getNulos().longValue() * times, stats.getNulos());
		assertEquals(registro.getNoRegistrados().longValue() * times, stats.getNoRegistrados());
		assertEquals(registro.getListaNominal().longValue() * times, stats.getListaNominal());
		assertEquals(registro.getTotalVotosCalculado().longValue() * times, stats.getVotos());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesAMLO());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesEPN());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesJVM());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesGQT());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesNulos());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesNoRegistrados());
		assertEquals(erroresEsperados, stats.getCasosErrorExcedeListaNominal());
		assertEquals(erroresEsperados, stats.getCasosErrorTipoActa());

	}

	@Test
	public void shouldAddRegistroForDifferentType() throws Exception {
		PrepAnalysis prepAnalysis = new PrepAnalysis();
		List<TipoRegistro> tipos = Arrays.asList(new TipoRegistro(Criterio.COMPLETO), new TipoRegistro(
				Criterio.COMPLETO, Criterio.CONTABILIZADO), new TipoRegistro(Criterio.COMPLETO, Criterio.CONTABILIZADO,
				Criterio.CASILLA_NORMAL));
		long casosFaltantesEsperados = 0L;
		long erroresEsperados = 0L;
		for (TipoRegistro tipo : tipos) {
			prepAnalysis.addRegistro(tipo, registro);
		}
		for (TipoRegistro tipo : tipos) {
			PrepStats stats = prepAnalysis.getStats(tipo);
			assertNotNull(stats);
			assertEquals(registro.getAMLO().longValue(), stats.getAMLO());
			assertEquals(registro.getEPN().longValue(), stats.getEPN());
			assertEquals(registro.getGQT().longValue(), stats.getGQT());
			assertEquals(registro.getJVM().longValue(), stats.getJVM());
			assertEquals(registro.getNulos().longValue(), stats.getNulos());
			assertEquals(registro.getNoRegistrados().longValue(), stats.getNoRegistrados());
			assertEquals(registro.getListaNominal().longValue(), stats.getListaNominal());
			assertEquals(registro.getTotalVotosCalculado().longValue(), stats.getVotos());
			assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesAMLO());
			assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesEPN());
			assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesJVM());
			assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesGQT());
			assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesNulos());
			assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesNoRegistrados());
			assertEquals(erroresEsperados, stats.getCasosErrorExcedeListaNominal());
			assertEquals(erroresEsperados, stats.getCasosErrorTipoActa());
		}
	}

	@Test
	public void shouldAddRegistrosTotalmenteIncompletos() throws Exception {
		PrepAnalysis prepAnalysis = new PrepAnalysis();
		TipoRegistro tipo = new TipoRegistro(Criterio.INCOMPLETO);
		long votosEsperados = 0L;
		long times = 5L;
		for (int i = 0; i < times; i++) {
			prepAnalysis.addRegistro(tipo, registroIncompleto);
		}
		PrepStats stats = prepAnalysis.getStats(tipo);
		assertNotNull(stats);
		assertEquals(times, stats.getCasosFaltantesAMLO());
		assertEquals(times, stats.getCasosFaltantesEPN());
		assertEquals(times, stats.getCasosFaltantesJVM());
		assertEquals(times, stats.getCasosFaltantesGQT());
		assertEquals(times, stats.getCasosFaltantesNulos());
		assertEquals(times, stats.getCasosFaltantesNoRegistrados());
		assertEquals(registroIncompleto.getListaNominal() * times, stats.getListaNominal());
		assertEquals(votosEsperados, stats.getAMLO());
		assertEquals(votosEsperados, stats.getEPN());
		assertEquals(votosEsperados, stats.getJVM());
		assertEquals(votosEsperados, stats.getGQT());
		assertEquals(votosEsperados, stats.getNulos());
		assertEquals(votosEsperados, stats.getNoRegistrados());
	}

	@Test
	public void shoudlAddRegistrosQueExcedenListaNominal() throws Exception {
		PrepAnalysis prepAnalysis = new PrepAnalysis();
		TipoRegistro tipo = new TipoRegistro(Criterio.COMPLETO);
		long casosFaltantesEsperados = 0L;
		long times = 5L;
		for (int i = 0; i < times; i++) {
			prepAnalysis.addRegistro(tipo, registroExcedeListaNominal);
		}
		PrepStats stats = prepAnalysis.getStats(tipo);
		assertNotNull(stats);
		assertEquals(times, stats.getCasosErrorExcedeListaNominal());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesAMLO());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesEPN());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesJVM());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesGQT());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesNulos());
		assertEquals(casosFaltantesEsperados, stats.getCasosFaltantesNoRegistrados());
		assertEquals(registroExcedeListaNominal.getListaNominal() * times, stats.getListaNominal());
		assertEquals(registroExcedeListaNominal.getAMLO() * times, stats.getAMLO());
		assertEquals(registroExcedeListaNominal.getEPN() * times, stats.getEPN());
		assertEquals(registroExcedeListaNominal.getJVM() * times, stats.getJVM());
		assertEquals(registroExcedeListaNominal.getGQT() * times, stats.getGQT());
		assertEquals(registroExcedeListaNominal.getNulos() * times, stats.getNulos());
		assertEquals(registroExcedeListaNominal.getNoRegistrados() * times, stats.getNoRegistrados());
	}

	@Test
	public void shouldAddRegistrosConTipoDeActaIncorrecto() throws Exception {
		PrepAnalysis prepAnalysis = new PrepAnalysis();
		TipoRegistro tipo = new TipoRegistro(Criterio.INCOMPLETO);
		long times = 5L;
		for (int i = 0; i < times; i++) {
			prepAnalysis.addRegistro(tipo, registroTipoActaIncorrecta);
		}
		PrepStats stats = prepAnalysis.getStats(tipo);
		assertNotNull(stats);
		long votosFaltantes = 0L;
		assertEquals(votosFaltantes, stats.getCasosFaltantesAMLO());
		assertEquals(votosFaltantes, stats.getCasosFaltantesEPN());
		assertEquals(votosFaltantes, stats.getCasosFaltantesJVM());
		assertEquals(votosFaltantes, stats.getCasosFaltantesGQT());
		assertEquals(votosFaltantes, stats.getCasosFaltantesNulos());
		assertEquals(votosFaltantes, stats.getCasosFaltantesNoRegistrados());
		assertEquals(registroTipoActaIncorrecta.getListaNominal() * times, stats.getListaNominal());
		assertEquals(registroTipoActaIncorrecta.getAMLO() * times, stats.getAMLO());
		assertEquals(registroTipoActaIncorrecta.getEPN() * times, stats.getEPN());
		assertEquals(registroTipoActaIncorrecta.getJVM() * times, stats.getJVM());
		assertEquals(registroTipoActaIncorrecta.getGQT() * times, stats.getGQT());
		assertEquals(registroTipoActaIncorrecta.getNulos() * times, stats.getNulos());
		assertEquals(registroTipoActaIncorrecta.getNoRegistrados() * times, stats.getNoRegistrados());
		assertEquals(0L, stats.getCasosErrorExcedeListaNominal());
		assertEquals(times, stats.getCasosErrorTipoActa());
	}

	@Test
	public void shouldAddRegistrosParcialmenteIncompletos() throws Exception {
		PrepAnalysis prepAnalysis = new PrepAnalysis();
		TipoRegistro tipo = new TipoRegistro(Criterio.INCOMPLETO);
		long times = 5L;
		for (int i = 0; i < times; i++) {
			prepAnalysis.addRegistro(tipo, registroParcialmenteIncompleto);
		}
		PrepStats stats = prepAnalysis.getStats(tipo);
		assertEquals(registroParcialmenteIncompleto.getAMLO(true).longValue() * times, stats.getAMLO());
		assertEquals(registroParcialmenteIncompleto.getEPN().longValue() * times, stats.getEPN());
		assertEquals(registroParcialmenteIncompleto.getGQT().longValue() * times, stats.getGQT());
		assertEquals(registroParcialmenteIncompleto.getJVM().longValue() * times, stats.getJVM());
		assertEquals(registroParcialmenteIncompleto.getNulos().longValue() * times, stats.getNulos());
		assertEquals(0L, stats.getNoRegistrados());
		assertEquals(registroParcialmenteIncompleto.getListaNominal().longValue() * times, stats.getListaNominal());
		assertEquals(registroParcialmenteIncompleto.getTotalVotosCalculado().longValue() * times, stats.getVotos());
		assertEquals(0L, stats.getCasosFaltantesEPN());
		assertEquals(0L, stats.getCasosFaltantesJVM());
		assertEquals(0L, stats.getCasosFaltantesGQT());
		assertEquals(0L, stats.getCasosFaltantesNulos());
		assertEquals(0L, stats.getCasosErrorExcedeListaNominal());
		assertEquals(0L, stats.getCasosErrorTipoActa());
		assertEquals(times, stats.getCasosFaltantesAMLO());
		assertEquals(times, stats.getCasosFaltantesNoRegistrados());
	}
	
	@Test
	public void shouldOverrideToString() throws Exception {
		PrepAnalysis prepAnalysis = new PrepAnalysis();
		TipoRegistro incompleto = new TipoRegistro(Criterio.INCOMPLETO);
		TipoRegistro completo = new TipoRegistro(Criterio.COMPLETO);
		prepAnalysis.addRegistro(completo, registro);
		prepAnalysis.addRegistro(completo, registroExcedeListaNominal);
		prepAnalysis.addRegistro(incompleto, registroExcedeListaNominal);
		prepAnalysis.addRegistro(incompleto, registroIncompleto);
		prepAnalysis.addRegistro(incompleto, registroTipoActaIncorrecta);
		
		String strAnalysis = prepAnalysis.toString();
		assertNotNull(strAnalysis);
		assertFalse(strAnalysis.isEmpty());
	}

}
