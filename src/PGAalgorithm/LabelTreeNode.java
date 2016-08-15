package PGAalgorithm;

public class LabelTreeNode {
	LabelTreeNode leftchild;
	LabelTreeNode rightchild;
	String label;
	String name;
	public LabelTreeNode(String label, String name) {
		super();
		this.label = label;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LabelTreeNode getLeftchild() {
		return leftchild;
	}
	public void setLeftchild(LabelTreeNode leftchild) {
		this.leftchild = leftchild;
	}
	public LabelTreeNode getRightchild() {
		return rightchild;
	}
	public void setRightchild(LabelTreeNode rightchild) {
		this.rightchild = rightchild;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
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
		LabelTreeNode other = (LabelTreeNode) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}
	
}
