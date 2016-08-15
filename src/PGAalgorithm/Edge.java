package PGAalgorithm;
import java.util.List;
import java.util.Set;

public class Edge {
	Set<Integer> port;  //size = 0, any port
	List<FunctionBox> functionBox; //size = 0; no functionbox
	public Edge(Set<Integer> port, List<FunctionBox> functionBox) {
		super();
		this.port = port;
		this.functionBox = functionBox;
	}
	public Set<Integer> getPort() {
		return port;
	}
	public void setPort(Set<Integer> port) {
		this.port = port;
	}
	public List<FunctionBox> getFunctionBox() {
		return functionBox;
	}
	public void setFunctionBox(List<FunctionBox> functionBox) {
		this.functionBox = functionBox;
	}
	
}
