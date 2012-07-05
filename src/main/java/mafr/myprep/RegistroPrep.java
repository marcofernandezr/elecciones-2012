package mafr.myprep;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class RegistroPrep {

	private static final int MAX_VOTOS_CASILLA_ESPECIAL = 750;
	private static final String CASILLA_EXTRAORDINARIA = "E";
	private static final String CASILLA_ESPECIAL = "S";
	private static final Integer ACTA_PRESIDENCIAL = 2;
	private final Short estado;
	private final Integer distrito;
	private final Integer seccion;
	private final String idCasilla;
	private final String tipoCasilla;
	private final Short extContigua;
	private final Integer ubicacionCasilla;
	private final Integer tipoActa;
	private final Integer pan;
	private final Integer pri;
	private final Integer prd;
	private final Integer pvem;
	private final Integer pt;
	private final Integer mc;
	private final Integer panal;
	private final Integer cPriPvem;
	private final Integer cPrdPtMc;
	private final Integer cPrdPt;
	private final Integer cPrdMc;
	private final Integer cPtMc;
	private final Integer noRegistrados;
	private final Integer nulos;
	private final Integer totalVotos;
	private final Integer listaNominal;
	private final String observaciones;
	private final Boolean contabilizada;
	private final String cryt;
	private final Date horaAcopio;
	private final Date horaCaptura;
	private final Date horaRegistro;
	private final Set<TipoError> errores = new HashSet<TipoError>();
	private final Boolean valida;
	private final Integer totalVotosCalculado;
	private final Boolean completo;
	private final String registroOriginal;
	public int errorTipoActa;

	public RegistroPrep(Short estado, Integer distrito, Integer seccion, String idCasilla, String tipoCasilla,
			Short extContigua, Integer ubicacionCasilla, Integer tipoActa, Integer pan, Integer pri, Integer prd,
			Integer pvem, Integer pt, Integer mc, Integer panal, Integer cPriPvem, Integer cPrdPtMc, Integer cPrdPt,
			Integer cPrdMc, Integer cPtMc, Integer noRegistrados, Integer nulos, Integer totalVotos,
			Integer listaNominal, String observaciones, Boolean contabilizada, String cryt, Date horaAcopio,
			Date horaCaptura, Date horaRegistro, Boolean completo, String originalString) {
		super();
		this.estado = estado;
		this.distrito = distrito;
		this.seccion = seccion;
		this.idCasilla = idCasilla;
		this.tipoCasilla = tipoCasilla;
		this.extContigua = extContigua;
		this.ubicacionCasilla = ubicacionCasilla;
		this.tipoActa = tipoActa;
		this.pan = pan;
		this.pri = pri;
		this.prd = prd;
		this.pvem = pvem;
		this.pt = pt;
		this.mc = mc;
		this.panal = panal;
		this.cPriPvem = cPriPvem;
		this.cPrdPtMc = cPrdPtMc;
		this.cPrdPt = cPrdPt;
		this.cPrdMc = cPrdMc;
		this.cPtMc = cPtMc;
		this.noRegistrados = noRegistrados;
		this.nulos = nulos;
		this.totalVotos = totalVotos;
		this.listaNominal = listaNominal;
		this.observaciones = observaciones;
		this.contabilizada = contabilizada;
		this.cryt = cryt;
		this.horaAcopio = horaAcopio;
		this.horaCaptura = horaCaptura;
		this.horaRegistro = horaRegistro;
		this.completo = completo;
		this.registroOriginal = originalString;
		this.totalVotosCalculado = sumaVotos(pan, pri, prd, pvem, pt, mc, panal, cPriPvem, cPrdPtMc, cPrdPt, cPrdMc,
				cPtMc, noRegistrados, nulos);
		this.valida = validar();
	}

	private int sumaVotos(Integer... votos) {
		int suma = 0;
		for (Integer voto : votos) {
			if (voto == null) {
				continue;
			}
			suma += voto;
		}
		return suma;
	}

	private Boolean validar() {
		if (!ACTA_PRESIDENCIAL.equals(tipoActa)) {
			errores.add(TipoError.ERROR_TIPO_ACTA);
		}
		if (totalVotos != null && !totalVotosCalculado.equals(totalVotos)) {
			errores.add(TipoError.ERROR_SUMA_VOTOS);
		}
		if (totalVotosCalculado.intValue() > listaNominal.intValue() && !isCasillaEspecial()) {
			errores.add(TipoError.ERROR_EXCEDE_LISTA_NOMINAL);
		}
		if (isCasillaEspecial() && totalVotosCalculado.intValue() > MAX_VOTOS_CASILLA_ESPECIAL) {
			errores.add(TipoError.ERROR_EXCEDE_LISTA_NOMINAL);
		}

		for (Integer votos : Arrays.asList(pan, pri, prd, pvem, pt, mc, panal, cPriPvem, cPrdPtMc, cPrdPt, cPrdMc,
				cPtMc, noRegistrados, nulos, listaNominal)) {
			if (votos != null) {
				int votosTmp = votos;
				if (votosTmp < 0) {
					errores.add(TipoError.ERROR_VALOR_NEGATIVO);
				}
			}
		}
		return errores.isEmpty();
	}

	public Boolean isContabilizado() {
		return contabilizada;
	}

	public Integer getAMLO() {
		return prd + pt + mc + cPrdMc + cPrdPt + cPrdPtMc + cPtMc;
	}

	public Integer getEPN() {
		return pri + pvem + cPriPvem;
	}

	public Integer getJVM() {
		return pan;
	}

	public Integer getGQT() {
		return panal;
	}

	public Integer getNulos() {
		return nulos;
	}

	public Integer getNoRegistrados() {
		return noRegistrados;
	}

	public Boolean isValido() {
		return valida;
	}

	public Set<TipoError> getErrores() {
		return errores;
	}

	public Integer getListaNominal() {
		return listaNominal;
	}

	public Integer getTotalVotosCalculado() {
		return totalVotosCalculado;
	}

	public Boolean isCasillaEspecial() {
		return CASILLA_ESPECIAL.equalsIgnoreCase(tipoCasilla.trim());
	}

	public Boolean isCasillaExtraordinaria() {
		return CASILLA_EXTRAORDINARIA.equalsIgnoreCase(tipoCasilla.trim());
	}

	public Boolean isCompleto() {
		return completo;
	}

	public String getRegistroOriginal() {
		return this.registroOriginal;
	}

	public Integer getAMLO(Boolean nullsafe) {
		if (nullsafe) {
			int totalAmlo = 0;
			for (Integer votos : Arrays.asList(prd, pt, mc, cPrdMc, cPrdPt, cPrdPtMc, cPtMc)) {
				if (votos == null) {
					continue;
				}
				totalAmlo += votos;
			}
			return totalAmlo;
		} else {
			return getAMLO();
		}
	}

	public Boolean isAMLOIncompleto() {
		for (Integer votos : Arrays.asList(prd, pt, mc, cPrdMc, cPrdPt, cPrdPtMc, cPtMc)) {
			if (votos == null) {
				return true;
			}
		}
		return false;
	}

	public Boolean isEPNIncompleto() {
		return pri == null || pvem == null || cPriPvem == null;
	}

	public Integer getEPN(boolean nullsafe) {
		if (nullsafe) {
			int totalEpn = 0;
			for (Integer votos : Arrays.asList(pri, pvem, cPriPvem)) {
				if (votos == null) {
					continue;
				}
				totalEpn += votos;
			}
			return totalEpn;
		}
		return getEPN();
	}

}
