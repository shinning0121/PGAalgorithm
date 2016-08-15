package PGAalgorithm;
import java.util.HashMap;
import java.util.Map;

public class PolicyGraph {
	Map<EPGPair, Edge> graph;
	Map<Integer, CptConstraint> constraints;   //PORT and its composition constraints, null when default
	public PolicyGraph(Map<EPGPair, Edge> graph) {
		super();
		this.graph = graph;
		this.constraints = new HashMap<Integer, CptConstraint>();
	}
	public PolicyGraph(Map<EPGPair, Edge> graph, Map<Integer, CptConstraint> constraints) {
		super();
		this.graph = graph;
		this.constraints = constraints;
	}
	public Map<EPGPair, Edge> getGraph() {
		return graph;
	}
	public void setGraph(Map<EPGPair, Edge> graph) {
		this.graph = graph;
	}
	public Map<Integer, CptConstraint> getConstraints() {
		return constraints;
	}
	public void setConstraints(Map<Integer, CptConstraint> constraints) {
		this.constraints = constraints;
	}
	
}
