package mafr.myprep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrepAnalysis {

	private static final Comparator<TipoRegistro> TIPO_REGISTRO_ORDER_COMPARATOR = new Comparator<TipoRegistro>() {
		public int compare(TipoRegistro o1, TipoRegistro o2) {
			if(o1.toString().split(" ").length == o2.toString().split(" ").length) {
				return o1.toString().compareTo(o2.toString());
			}
			return new Integer(o1.toString().split(" ").length).compareTo(o2.toString().split(" ").length);
		};
	};

	private final Map<TipoRegistro, MyPrepStatImpl> stats;

	public PrepAnalysis() {
		stats = new HashMap<TipoRegistro, PrepAnalysis.MyPrepStatImpl>();
	}

	public PrepStats getStats(TipoRegistro tipoRegistro) {
		return stats.get(tipoRegistro);
	}

	public void addRegistro(TipoRegistro tipoRegistro, RegistroPrep registro) {
		if (!stats.containsKey(tipoRegistro)) {
			stats.put(tipoRegistro, new MyPrepStatImpl());
		}
		MyPrepStatImpl stat = stats.get(tipoRegistro);

		stat.registros++;
		if (registro.isCompleto()) {
			incrementRegistroCompleto(registro, stat);
		} else {
			incrementeRegistroIncompleto(registro, stat);
		}
		incrementErrores(registro, stat);
	}

	private void incrementeRegistroIncompleto(RegistroPrep registro, MyPrepStatImpl stat) {
		stat.votos += registro.getTotalVotosCalculado();
		if (registro.getListaNominal() != null) {
			stat.listaNominal += registro.getListaNominal();
		}
		if (registro.getJVM() == null) {
			stat.jvmIncompleto++;
		} else {
			stat.jvm += registro.getJVM();
		}
		if (registro.getGQT() == null) {
			stat.gqtIncompleto++;
		} else {
			stat.gqt += registro.getGQT();
		}
		if (registro.getNulos() == null) {
			stat.nulosIncompleto++;
		} else {
			stat.nulos += registro.getNulos();
		}
		if (registro.getNoRegistrados() == null) {
			stat.noRegistradosIncompleto++;
		} else {
			stat.noRegistrados += registro.getNoRegistrados();
		}
		if (registro.isAMLOIncompleto()) {
			stat.amloIncompleto++;
			stat.amlo += registro.getAMLO(true);
		} else {
			stat.amlo += registro.getAMLO();
		}
		if (registro.isEPNIncompleto()) {
			stat.epnIncompleto++;
			stat.epn += registro.getEPN(true);
		} else {
			stat.epn += registro.getEPN();
		}
	}

	private void incrementRegistroCompleto(RegistroPrep registro, MyPrepStatImpl stat) {
		stat.listaNominal += registro.getListaNominal();
		stat.votos += registro.getTotalVotosCalculado();
		stat.epn += registro.getEPN();
		stat.amlo += registro.getAMLO();
		stat.jvm += registro.getJVM();
		stat.gqt += registro.getGQT();
		stat.nulos += registro.getNulos();
		stat.noRegistrados += registro.getNoRegistrados();
	}

	private void incrementErrores(RegistroPrep registro, MyPrepStatImpl stat) {
		for (TipoError error : registro.getErrores()) {
			switch (error) {
			case ERROR_TIPO_ACTA:
				stat.errorTipoActa++;
				break;
			case ERROR_SUMA_VOTOS:
				stat.errorSumaVotos++;
				break;
			case ERROR_EXCEDE_LISTA_NOMINAL:
				stat.errorExcedeListaNominal++;
				break;
			case ERROR_VALOR_NEGATIVO:
				stat.errorValorNegativo++;
				break;
			}
		}
	}

	public void finish() {
		stats.put(new TipoRegistro(Criterio.TODOS),
				addStats(
						new TipoRegistro(Criterio.COMPLETO), 
						new TipoRegistro(Criterio.INCOMPLETO)));
		
		stats.put(new TipoRegistro(Criterio.CONTABILIZADO),
				addStats(
						new TipoRegistro(Criterio.COMPLETO, Criterio.CONTABILIZADO), 
						new TipoRegistro(Criterio.INCOMPLETO, Criterio.CONTABILIZADO)));
		stats.put(
				new TipoRegistro(Criterio.SIN_CONTABILIZAR),
				addStats(
						new TipoRegistro(Criterio.COMPLETO, Criterio.SIN_CONTABILIZAR), 
						new TipoRegistro(Criterio.INCOMPLETO, Criterio.SIN_CONTABILIZAR)));

		stats.put(
				new TipoRegistro(Criterio.CASILLA_ESPECIAL, Criterio.CONTABILIZADO),
				addStats(
						new TipoRegistro(Criterio.COMPLETO, Criterio.CONTABILIZADO, Criterio.CASILLA_ESPECIAL), 
						new TipoRegistro(Criterio.INCOMPLETO, Criterio.CONTABILIZADO, Criterio.CASILLA_ESPECIAL)));

		stats.put(
				new TipoRegistro(Criterio.CASILLA_EXTRAORDINARIA, Criterio.CONTABILIZADO),
				addStats(
						new TipoRegistro(Criterio.COMPLETO, Criterio.CONTABILIZADO, Criterio.CASILLA_EXTRAORDINARIA), 
						new TipoRegistro(Criterio.INCOMPLETO, Criterio.CONTABILIZADO, Criterio.CASILLA_EXTRAORDINARIA)));

		stats.put(new TipoRegistro(Criterio.SIN_CONTABILIZAR, Criterio.INVALIDO),
				addStats(
						new TipoRegistro(Criterio.COMPLETO, Criterio.SIN_CONTABILIZAR, Criterio.CASILLA_NORMAL, Criterio.INVALIDO), 
						new TipoRegistro(Criterio.COMPLETO, Criterio.SIN_CONTABILIZAR, Criterio.CASILLA_EXTRAORDINARIA, Criterio.INVALIDO), 
						new TipoRegistro(Criterio.COMPLETO, Criterio.SIN_CONTABILIZAR, Criterio.CASILLA_ESPECIAL, Criterio.INVALIDO), 
						new TipoRegistro(Criterio.INCOMPLETO, Criterio.SIN_CONTABILIZAR, Criterio.CASILLA_NORMAL, Criterio.INVALIDO), 
						new TipoRegistro(Criterio.INCOMPLETO, Criterio.SIN_CONTABILIZAR, Criterio.CASILLA_EXTRAORDINARIA, Criterio.INVALIDO), 
						new TipoRegistro(Criterio.INCOMPLETO, Criterio.SIN_CONTABILIZAR, Criterio.CASILLA_ESPECIAL, Criterio.INVALIDO)));

	}

	private MyPrepStatImpl addStats(TipoRegistro... tiposRegistro) {
		MyPrepStatImpl result = new MyPrepStatImpl();
		for (TipoRegistro tipoRegistro : tiposRegistro) {
			if (!stats.containsKey(tipoRegistro)) {
				continue;
			}
			MyPrepStatImpl stat = stats.get(tipoRegistro);
			result.registros += stat.registros;
			result.listaNominal += stat.listaNominal;
			result.votos += stat.votos;
			result.epn += stat.epn;
			result.amlo += stat.amlo;
			result.jvm += stat.jvm;
			result.gqt += stat.gqt;
			result.nulos += stat.nulos;
			result.noRegistrados += stat.noRegistrados;

			result.epnIncompleto += stat.epnIncompleto;
			result.amloIncompleto += stat.amloIncompleto;
			result.jvmIncompleto += stat.jvmIncompleto;
			result.gqtIncompleto += stat.gqtIncompleto;
			result.nulosIncompleto += stat.nulosIncompleto;
			result.noRegistradosIncompleto += stat.noRegistradosIncompleto;

			result.errorExcedeListaNominal += stat.errorExcedeListaNominal;
			result.errorSumaVotos += stat.errorSumaVotos;
			result.errorTipoActa += stat.errorTipoActa;
			result.errorValorNegativo += stat.errorValorNegativo;

		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		List<TipoRegistro> tipos = new ArrayList<TipoRegistro>(stats.keySet());
		Collections.sort(tipos, TIPO_REGISTRO_ORDER_COMPARATOR);
		for (TipoRegistro tipoRegistro : tipos) {
			sb.append("\n******************** " + tipoRegistro + " *******************\n\n");
			sb.append(getStats(tipoRegistro).toString());
		}

		return sb.toString();
	}

	private final class MyPrepStatImpl implements PrepStats {
		public long errorValorNegativo;
		public long errorExcedeListaNominal;
		public long errorSumaVotos;
		public long errorTipoActa;
		public long epnIncompleto;
		public long amloIncompleto;
		public long noRegistradosIncompleto;
		public long nulosIncompleto;
		public long gqtIncompleto;
		public long jvmIncompleto;
		public long listaNominal;
		public long votos;
		public long registros;
		public long epn;
		public long amlo;
		public long jvm;
		public long gqt;
		public long nulos;
		public long noRegistrados;

		public final long getListaNominal() {
			return listaNominal;
		}

		public final long getVotos() {
			return votos;
		}

		public final double getParticipacionCiudadana() {
			if (listaNominal != 0) {
				return votos * 100.0 / listaNominal;
			}
			return 0.0;
		}

		public final long getRegistros() {
			return registros;
		}

		public final long getEPN() {
			return epn;
		}

		public final long getAMLO() {
			return amlo;
		}

		public final long getJVM() {
			return jvm;
		}

		public final long getGQT() {
			return gqt;
		}

		public final long getNulos() {
			return nulos;
		}

		public final long getNoRegistrados() {
			return noRegistrados;
		}

		public double getPorcentajeEPN() {
			return votos <= 0 ? 0 : epn * 100.0 / votos;

		}

		public double getPorcentajeAMLO() {
			return votos <= 0 ? 0 : amlo * 100.0 / votos;
		}

		public double getPorcentajeJVM() {
			return votos <= 0 ? 0 : jvm * 100.0 / votos;
		}

		public double getPorcentajeGQT() {
			return votos <= 0 ? 0 : gqt * 100.0 / votos;
		}

		public double getPorcentajeNulos() {
			return votos <= 0 ? 0 : nulos * 100.0 / votos;
		}

		public double getPorcentajeNoRegistrados() {
			return votos <= 0 ? 0 : noRegistrados * 100.0 / votos;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("\n\nRegistros  = " + registros);
			sb.append("\nVotos  = " + votos);
			if (listaNominal > 0) {
				sb.append("\nListaNominal  = " + listaNominal);
			}
			if (getParticipacionCiudadana() > 0.0) {
				sb.append("\nParticipacion ciudadana  = " + getParticipacionCiudadana() + "%");
			}
			sb.append("\n\nEPN  \t" + epn + "\t\t" + getPorcentajeEPN() + "%");
			sb.append("\nAMLO \t" + amlo + "\t\t" + getPorcentajeAMLO() + "%");
			sb.append("\nJVM  \t" + jvm + "\t\t" + getPorcentajeJVM() + "%");
			sb.append("\nGQT  \t" + gqt + "\t\t" + getPorcentajeGQT() + "%");
			sb.append("\nNulos\t" + nulos + "\t\t" + getPorcentajeNulos() + "%");
			sb.append("\nNoReg\t" + noRegistrados + "\t\t" + getPorcentajeNoRegistrados() + "%\n");
			if (epnIncompleto > 0) {
				sb.append("\nEPN  Ilegible\t" + epnIncompleto + " casos.");
			}
			if (amloIncompleto > 0) {
				sb.append("\nAMLO Ilegible\t" + amloIncompleto + " casos.");
			}
			if (jvmIncompleto > 0) {
				sb.append("\nJVM Ilegible\t" + jvmIncompleto + " casos.");
			}
			if (gqtIncompleto > 0) {
				sb.append("\nGQT Ilegible\t" + gqtIncompleto + " casos.");
			}
			if (nulosIncompleto > 0) {
				sb.append("\nNulos Ilegible\t" + nulosIncompleto + " casos.");
			}
			if (noRegistradosIncompleto > 0) {
				sb.append("\nNoReg Ilegible\t" + noRegistradosIncompleto + " casos.");
			}
			if (errorTipoActa > 0) {
				sb.append("\n\nError TipoActa\t" + errorTipoActa + " casos.");
			}
			if (errorExcedeListaNominal > 0) {
				sb.append("\n\nError ExcedeListaNominal\t" + errorExcedeListaNominal + " casos.");
			}
			if (errorSumaVotos > 0) {
				sb.append("\n\nError sumaVotos\t" + errorSumaVotos + " casos.");
			}
			if (errorValorNegativo > 0) {
				sb.append("\n\nError valorNegativo\t" + errorValorNegativo + " casos.");
			}
			sb.append("\n\n\n");
			return sb.toString();
		}

	}

}
