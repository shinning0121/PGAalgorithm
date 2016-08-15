package PGAalgorithm;

public class EPGPair {
	EPG src;
	EPG dst;
	public EPGPair(EPG src, EPG dst) {
		super();
		this.src = src;
		this.dst = dst;
	}
	public EPG getSrc() {
		return src;
	}
	public void setSrc(EPG src) {
		this.src = src;
	}
	public EPG getDst() {
		return dst;
	}
	public void setDst(EPG dst) {
		this.dst = dst;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dst == null) ? 0 : dst.hashCode());
		result = prime * result + ((src == null) ? 0 : src.hashCode());
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
		EPGPair other = (EPGPair) obj;
		if (dst == null) {
			if (other.dst != null)
				return false;
		} else if (!dst.equals(other.dst))
			return false;
		if (src == null) {
			if (other.src != null)
				return false;
		} else if (!src.equals(other.src))
			return false;
		return true;
	}
	
}
