package PGAalgorithm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		CombinationInit data = new CombinationInit();
		for (PolicyGraph graph : data.graphs) {
			Iterator<EPGPair> it = graph.graph.keySet().iterator();
			while (it.hasNext()) {
				EPGPair p = it.next();
				Set<Integer> ports = graph.graph.get(p).getPort();
				List<FunctionBox> fbs = graph.graph.get(p).getFunctionBox();
				for (int i : ports) {
					System.out.println(i + " ");
				}
				for (FunctionBox s : fbs) {
					System.out.println(s + " ");
				}
				System.out.println(p.getSrc().toString() + " to " + p.getDst().toString());
			}
		}
		
		Composition c = new Composition();
		c.localNormalization();
		for (PolicyGraph graph : c.localNmlGraphs) {
			Iterator<EPGPair> it = graph.graph.keySet().iterator();
			while (it.hasNext()) {
				EPGPair p = it.next();
				Set<Integer> ports = graph.graph.get(p).getPort();
				List<FunctionBox> fbs = graph.graph.get(p).getFunctionBox();
				for (int i : ports) {
					System.out.println(i + " ");
				}
				for (FunctionBox s : fbs) {
					System.out.println(s + " ");
				}
				System.out.println(p.getSrc().toString() + " to " + p.getDst().toString());
			}
		}		
		
		Composition cn = new Composition();
		cn.normalization();
		for (EPG e : cn.globalNmlEPGs) {
			System.out.println(e.toString() + " ");
		}
		
		Composition ctn = new Composition();
		
		Map<String, EPG> hostMapping = new HashMap<>();
		List<LabelTreeNode> epg1 = new LinkedList<LabelTreeNode>();
		epg1.add(data.stu);
		epg1.add(data.zna);
		epg1.add(data.nml);
		hostMapping.put("10.0.0.1", new EPG(epg1));
		List<LabelTreeNode> epg2 = new LinkedList<LabelTreeNode>();
		epg2.add(data.stu);
		epg2.add(data.zna);
		epg2.add(data.nml);
		hostMapping.put("10.0.0.2", new EPG(epg2));
		List<LabelTreeNode> epg3 = new LinkedList<LabelTreeNode>();
		epg3.add(data.teac);
		epg3.add(data.znb);
		epg3.add(data.nml);
		hostMapping.put("10.0.0.3", new EPG(epg3));
		List<LabelTreeNode> epg4 = new LinkedList<LabelTreeNode>();
		epg4.add(data.dns);
		hostMapping.put("10.0.0.4", new EPG(epg4));
		List<LabelTreeNode> epg5 = new LinkedList<LabelTreeNode>();
		epg5.add(data.rmd);
		hostMapping.put("10.0.0.5", new EPG(epg5));
		List<LabelTreeNode> epg6 = new LinkedList<LabelTreeNode>();
		epg6.add(data.web);
		epg6.add(data.nc);
		hostMapping.put("10.0.0.6", new EPG(epg6));
		List<LabelTreeNode> epg7 = new LinkedList<LabelTreeNode>();
		epg7.add(data.web);
		epg7.add(data.nc);
		hostMapping.put("10.0.0.7", new EPG(epg7));
		List<LabelTreeNode> epg8 = new LinkedList<LabelTreeNode>();
		epg8.add(data.db);
		epg8.add(data.nc);
		hostMapping.put("10.0.0.8", new EPG(epg8));
		
		ctn.graphUnion();
		Iterator<EPGPair> it = ctn.compositionGraph.graph.keySet().iterator();
		while (it.hasNext()) {
			EPGPair p = it.next();
			Set<Integer> ports = ctn.compositionGraph.graph.get(p).getPort();
			List<FunctionBox> fbs = ctn.compositionGraph.graph.get(p).getFunctionBox();
			for (int i : ports) {
				System.out.println(i + " ");
			}
			for (FunctionBox s : fbs) {
				System.out.println(s + " ");
			}
			System.out.println(p.getSrc().toString() + " to " + p.getDst().toString());
		}
	}

}
