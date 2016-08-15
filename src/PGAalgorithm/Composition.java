package PGAalgorithm;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Composition {
	
	CombinationInit data;
	List<EPG> globalNmlEPGs;
	List<EPG> nmlEPGs;
	List<PolicyGraph> localNmlGraphs;
	PolicyGraph compositionGraph;
	
	public void getData() {
		data = new CombinationInit();
	}
	
	public void localNormalization() {
		getData();
		localNmlGraphs = new LinkedList<PolicyGraph>();
		for (PolicyGraph policyGraph : data.graphs) {
			Iterator<EPGPair> it = policyGraph.getGraph().keySet().iterator();
			Map<EPGPair, Edge> localNmlGraph = new HashMap<EPGPair, Edge>();
			while (it.hasNext()) {
				EPGPair p = it.next();
				EPG src = p.getSrc();
				EPG dst = p.getDst();
				List<EPG> localNmlsrc = getLocalNmlEPG(src);
				List<EPG> localNmldst = getLocalNmlEPG(dst);
				Edge edge = policyGraph.getGraph().get(p);
				for (int i = 0; i < localNmlsrc.size(); i++) {
					for (int j = 0; j < localNmldst.size(); j++) {
						EPGPair localNmlEPGPair = new EPGPair(localNmlsrc.get(i), localNmldst.get(j));
						localNmlGraph.put(localNmlEPGPair, edge);
					}
				}
			}
			PolicyGraph localNmlPolicyGraph = new PolicyGraph(localNmlGraph, policyGraph.getConstraints());
			localNmlGraphs.add(localNmlPolicyGraph);
		}		
	}
	
	public void normalization() {
		localNormalization();
		List<EPG> nmlLabelMappings = new LinkedList<EPG>();		
		for (EPG temp : data.labelMapping) {
			List<EPG> new_temp = getLocalNmlEPG(temp);
			for (EPG NmlLabelMapping : new_temp) {
				nmlLabelMappings.add(NmlLabelMapping);
			}
		}
		globalNmlEPGs = new LinkedList<EPG>();
		List<Integer> combinedIndex = new LinkedList<>();
		for (int i = 0; i < nmlLabelMappings.size() - 1; i++) {
			EPG epg1 = nmlLabelMappings.get(i);
			for (int j = i + 1; j < nmlLabelMappings.size(); j++) {				
				EPG epg2 = nmlLabelMappings.get(j);
				if (Collections.disjoint(epg1.getEPG(), epg2.getEPG())) {
					continue;
				} else {
					List<LabelTreeNode> comb = combineLabelMapping(epg1, epg2);
					if (comb.size() != epg1.getEPG().size()) {
						EPG combinedEPG = new EPG(comb);
						globalNmlEPGs.add(combinedEPG);
						combinedIndex.add(i);
						combinedIndex.add(j);
					}
				}
			}
			if (! combinedIndex.contains(i)) {
				globalNmlEPGs.add(epg1);
			}
		}
		if (! combinedIndex.contains(nmlLabelMappings.size() - 1)) globalNmlEPGs.add(nmlLabelMappings.get(nmlLabelMappings.size() - 1));
		List<LabelTreeNode> dnsList = new LinkedList<LabelTreeNode>();
		dnsList.add(data.dns);
		globalNmlEPGs.add(new EPG(dnsList));
		List<LabelTreeNode> rmdList = new LinkedList<LabelTreeNode>();
		rmdList.add(data.rmd);
		globalNmlEPGs.add(new EPG(rmdList));		
	}
	
	public void graphUnion() {
		normalization();
		List<PolicyGraph> graphs = new LinkedList<PolicyGraph>(localNmlGraphs);
		Map<Integer, Map<EPGPair, Set<EPGPair>>> combinedRecord = new HashMap<>();
		Map<EPGPair, Edge> graph = new HashMap<EPGPair, Edge>();
		Map<Integer, CptConstraint> constraint = new HashMap<Integer, CptConstraint>();
		for (int i = 0; i < graphs.size() - 1; i++) {
			PolicyGraph pg1 = graphs.get(i);
			Set<EPGPair> ep1 = pg1.getGraph().keySet();			
			for (EPGPair epair1 : ep1) {
				Set<EPG> setGlobalNmlEPGSrc1 = belongto(epair1.getSrc(), globalNmlEPGs);
				Set<EPG> setGlobalNmlEPGDst1 = belongto(epair1.getDst(), globalNmlEPGs);
				for (EPG globalNmlEPGSrc1 : setGlobalNmlEPGSrc1) {
					if (globalNmlEPGSrc1.getEPG().contains(data.qn)) {
						continue;
					}
					for (EPG globalNmlEPGDst1 : setGlobalNmlEPGDst1) {
						EPGPair epa = new EPGPair(globalNmlEPGSrc1, globalNmlEPGDst1);
						if (combinedRecord.containsKey(i)) {
							if (combinedRecord.get(i).containsKey(epair1)) {
								if (combinedRecord.get(i).get(epair1).contains(epa)) {
									continue;
								}
							}
						}
						Edge new_edge = pg1.getGraph().get(epair1);
						Map<Integer, CptConstraint> new_constraints = pg1.getConstraints();					
						for (int j = i + 1; j < graphs.size(); j++) {
							PolicyGraph pg2 = graphs.get(j);
							Set<EPGPair> ep2 = pg2.getGraph().keySet();
							for (EPGPair epair2: ep2) {
								Set<EPG> setGlobalNmlEPGSrc2 = belongto(epair2.getSrc(), globalNmlEPGs);
								Set<EPG> setGlobalNmlEPGDst2 = belongto(epair2.getDst(), globalNmlEPGs);
								if (setGlobalNmlEPGSrc2.contains(globalNmlEPGSrc1) && setGlobalNmlEPGDst2.contains(globalNmlEPGDst1)) {
									new_edge = combineEdges(new_edge, pg2.getGraph().get(epair2), new_constraints, pg2.getConstraints());
									new_constraints = combineConstraints(new_constraints, pg2.getConstraints());
									if (combinedRecord.containsKey(j)) {
										if (! combinedRecord.get(j).containsKey(epair2)) {
											combinedRecord.get(j).put(epair2, new HashSet<EPGPair>());
										}
										combinedRecord.get(j).get(epair2).add(epa);
									} else {
										combinedRecord.put(j, new HashMap<EPGPair, Set<EPGPair>>());
										combinedRecord.get(j).put(epair2, new HashSet<EPGPair>());
										combinedRecord.get(j).get(epair2).add(epa);
									}
									break;
								}
							}
						}
						graph.put(epa, new_edge);
						constraint.putAll(new_constraints);
					}
				}

			}

		}
		PolicyGraph pg1 = graphs.get(graphs.size() - 1);
		Set<EPGPair> ep1 = pg1.getGraph().keySet();			
		for (EPGPair epair1 : ep1) {
			Set<EPG> setGlobalNmlEPGSrc1 = belongto(epair1.getSrc(), globalNmlEPGs);
			Set<EPG> setGlobalNmlEPGDst1 = belongto(epair1.getDst(), globalNmlEPGs);
			for (EPG globalNmlEPGSrc1 : setGlobalNmlEPGSrc1) {
				for (EPG globalNmlEPGDst1 : setGlobalNmlEPGDst1) {
					EPGPair epa = new EPGPair(globalNmlEPGSrc1, globalNmlEPGDst1);
					if (combinedRecord.containsKey(graphs.size() - 1)) {
						if (combinedRecord.get(graphs.size() - 1).containsKey(epair1)) {
							if (combinedRecord.get(graphs.size() - 1).get(epair1).contains(epa)) {
								continue;
							}
						}
					}
					Edge new_edge = pg1.getGraph().get(epair1);
					Map<Integer, CptConstraint> new_constraints = pg1.getConstraints();
					graph.put(epa, new_edge);
					constraint.putAll(new_constraints);
				}
			}
		}
		compositionGraph = new PolicyGraph(graph, constraint);
	}
	
	public Set<EPG> belongto(EPG epg, List<EPG> globalNmlEPGs) {
		Set<EPG> rst = new HashSet<EPG>();
		Set<LabelTreeNode> epgSet = new HashSet<LabelTreeNode>(epg.getEPG());
		for (EPG globalNmlEPG : globalNmlEPGs) {
			Set<LabelTreeNode> nmlEpgSet = new HashSet<LabelTreeNode>(globalNmlEPG.getEPG());
			if (hasIntersection(epgSet, nmlEpgSet)) {
				rst.add(globalNmlEPG);
			}
		}
		return rst;
	}
	
	public boolean hasIntersection(Set<LabelTreeNode> set1, Set<LabelTreeNode> set2) {
		for (LabelTreeNode ltn1 : set1) {
			if (set2.contains(ltn1)) return true;
		}
		return false;
	}
			
	public Edge combineEdges(Edge edge1, Edge edge2, Map<Integer, CptConstraint> constraints1, Map<Integer, CptConstraint> constraints2) {
		Edge edge;
		Set<Integer> p1 = edge1.getPort();
		Set<Integer> p2 = edge2.getPort();
		Set<Integer> port = new HashSet<Integer>();
		if (p1.size() == 0) {
			port.addAll(p2);
		} else if (p2.size() == 0) {
			port.addAll(p1);
		} else {
			port.addAll(p1);
			port.retainAll(p2);
		}
		Map<Integer, CptConstraint> constraints = combineConstraints(constraints1, constraints2);
		List<FunctionBox> fb = combineFunctionBox(edge1.getFunctionBox(), edge2.getFunctionBox());
		if (constraints == null || port.size() == 0) {
			edge = new Edge(port, fb);
			return edge;
		}
		Set<Integer> keyset = new HashSet<>(constraints.keySet());
		keyset.retainAll(port);
		for (int p : keyset) {
			if (! constraints.get(p).addClassifier) {
				port.remove(p);
			}
		}
		edge = new Edge(port, fb);
		return edge;
	}
	
	public Map<Integer, CptConstraint> combineConstraints(Map<Integer, CptConstraint> constraints1, Map<Integer, CptConstraint> constraints2) {
		Map<Integer, CptConstraint> constraints = new HashMap<>(constraints1);
		constraints.putAll(constraints2);
		return constraints;
	}
	
	public List<FunctionBox> combineFunctionBox(List<FunctionBox> fb1, List<FunctionBox> fb2) {
		List<FunctionBox> fb = new LinkedList<FunctionBox>();
		if (fb1.size() == 0) return fb2;
		if (fb2.size() == 0) return fb1;
		int i = 0;
		int j = 0;
		while (i < fb1.size() && j < fb2.size()) {
			FunctionBox funcbox1 = fb1.get(i);
			FunctionBox funcbox2 = fb2.get(j);
			int compare = funcbox1.compareTo(funcbox2);
			if (compare < 0) {
				fb.add(funcbox1);
				i++;
			} else if (compare > 0) {
				fb.add(funcbox2);
				j++;
			} else {
				fb.add(funcbox1);
				i++;
				j++;
			}
		}
		if (i < fb1.size()) {
			for (int k = i; k < fb1.size(); k++) {
				fb.add(fb1.get(k));
			}
		}
		if (j < fb2.size()) {
			for (int k = j; k < fb2.size(); k++) {
				fb.add(fb2.get(k));
			}
		}
		return fb;
	}
	
	public List<LabelTreeNode> combineLabelMapping(EPG epg1, EPG epg2) {
		List<LabelTreeNode> l1 = epg1.getEPG();
		List<LabelTreeNode> l2 = epg2.getEPG();
		List<LabelTreeNode> l3 = new LinkedList<LabelTreeNode>(l1);
		List<LabelTreeNode> l4 = new LinkedList<LabelTreeNode>();
		for (int i = 0; i < l2.size(); i++) {
			LabelTreeNode label2 = l2.get(i);
			boolean equal = false;
			for (int j = 0; j < l1.size(); j++) {
				LabelTreeNode label1 = l1.get(j);				
				if (label1.equals(label2)) {
					equal = true;
					break;
				}
				else if (label1.getName().equals(label2.getName())) {
					return l1;
				}
			}
			if (! equal) {
				l4.add(label2);
			}
		}
		l3.addAll(l4);
		return l3;
	}
	
	
	public boolean isLeafLabel(LabelTreeNode ltn) {
		if (ltn.getLeftchild() == null && ltn.getRightchild() == null) {
			return true;
		}
		return false;
	}
	
	public List<EPG> getLocalNmlEPG(EPG ep) {
		List<EPG> l = new LinkedList<EPG>();
		List<List<LabelTreeNode>> ll = new LinkedList<>();
		int size = ep.getEPG().size();
		for (int i = 0; i < size; i++) {
			LabelTreeNode label = ep.getEPG().get(i);
			ll.add(getLeafLabel(label));
		}
		List<LabelTreeNode> temp = new LinkedList<LabelTreeNode>();
		bt(l, temp, ll, 0, size);
		return l;
	}
	
	public void bt(List<EPG> l, List<LabelTreeNode> temp, List<List<LabelTreeNode>> ll, int index, int size) {
		if (temp.size() == size) {
			l.add(new EPG(new LinkedList<LabelTreeNode>(temp)));
			return;
		}
		for (int j = 0; j < ll.get(index).size(); j++) {
			temp.add(ll.get(index).get(j));				
			bt(l, temp, ll, index + 1, size);
			temp.remove(temp.size() - 1);
		}
	}
	
	public List<LabelTreeNode> getLeafLabel(LabelTreeNode ltn) {
		List<LabelTreeNode> l = new LinkedList<LabelTreeNode>();
		traverseHelper(ltn, l);
		return l;
	}
	
	public void traverseHelper(LabelTreeNode ltn, List<LabelTreeNode> l) {
		if (isLeafLabel(ltn)) {
			l.add(ltn);
		} else {
			if (ltn.getLeftchild() != null) {
				LabelTreeNode left = ltn.getLeftchild();
				traverseHelper(left, l);
			}
			if (ltn.getRightchild() != null) {
				LabelTreeNode right = ltn.getRightchild();
				traverseHelper(right, l);
			}
		}
	}	
}
