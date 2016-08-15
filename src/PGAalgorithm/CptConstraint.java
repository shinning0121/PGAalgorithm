package PGAalgorithm;

public class CptConstraint {
	boolean addClassifier;
	boolean removeClassifier;
	boolean DropFB;
	public CptConstraint() {
		this.addClassifier = true;
		this.removeClassifier = false;
		DropFB = false;		
	}
	public CptConstraint(Boolean addClassifier, Boolean removeClassifier, Boolean dropFB) {
		super();
		this.addClassifier = addClassifier;
		this.removeClassifier = removeClassifier;
		DropFB = dropFB;
	}
	public Boolean getAddClassifier() {
		return addClassifier;
	}
	public void setAddClassifier(Boolean addClassifier) {
		this.addClassifier = addClassifier;
	}
	public Boolean getRemoveClassifier() {
		return removeClassifier;
	}
	public void setRemoveClassifier(Boolean removeClassifier) {
		this.removeClassifier = removeClassifier;
	}
	public Boolean getDropFB() {
		return DropFB;
	}
	public void setDropFB(Boolean dropFB) {
		DropFB = dropFB;
	}
	
}
