package PGAalgorithm;

public class FunctionBox implements Comparable{
	String function;
	int priority;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((function == null) ? 0 : function.hashCode());
		result = prime * result + priority;
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
		FunctionBox other = (FunctionBox) obj;
		if (function == null) {
			if (other.function != null)
				return false;
		} else if (!function.equals(other.function))
			return false;
		if (priority != other.priority)
			return false;
		return true;
	}
	public FunctionBox(String function, int priority) {
		super();
		this.function = function;
		this.priority = priority;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		FunctionBox func = (FunctionBox) o;
		if (this.priority == func.priority) {
			return 0;
		} else if (this.priority > func.priority) {
			return 1;
		}
		return -1;
	}
	@Override
	public String toString() {
		return "FunctionBox [function=" + function + "]";
	}
}
