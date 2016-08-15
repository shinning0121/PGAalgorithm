package PGAalgorithm;
import java.util.List;

public class EPG {
	List<LabelTreeNode> epg;

	public EPG(List<LabelTreeNode> epg) {
		super();
		this.epg = epg;
	}

	public List<LabelTreeNode> getEPG() {
		return epg;
	}

	public void setEPG(List<LabelTreeNode> epg) {
		this.epg = epg;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("EPG: ");
		for (LabelTreeNode label : epg) {
			sb.append(label.getLabel() + " ");
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
		if (epg == null) return result;
		for (LabelTreeNode label : epg) {
			result = prime * result + label.hashCode();
		}
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
		EPG other = (EPG) obj;
		if (epg == null) {
			if (other.epg != null)
				return false;
		} else if (epg.size() != other.getEPG().size()) {
			return false;
		} else {
			List<LabelTreeNode> other_epg = other.getEPG();
			for (LabelTreeNode label : epg) {
				if (! other_epg.contains(label)) {
					return false;
				}
			}
		}
		return true;
	}	
	
}
