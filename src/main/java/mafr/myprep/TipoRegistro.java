package mafr.myprep;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TipoRegistro {

	public final Set<Criterio> criterios;

	public TipoRegistro(Criterio... criterios) {
		this(Arrays.asList(criterios));
	}

	public TipoRegistro(Collection<Criterio> criterios) {
		this.criterios = new HashSet<Criterio>();
		for (Criterio criterio : criterios) {
			this.criterios.add(criterio);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((criterios == null) ? 0 : criterios.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoRegistro other = (TipoRegistro) obj;
		if (criterios == null) {
			if (other.criterios != null)
				return false;
		} else if (!criterios.equals(other.criterios))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb =  new StringBuilder();
		for(Criterio criterio: Criterio.values()){
			if(criterios.contains(criterio)) {
				sb.append(criterio.name()+ " ");
			}
		}
		return sb.toString().trim();
	}
}
