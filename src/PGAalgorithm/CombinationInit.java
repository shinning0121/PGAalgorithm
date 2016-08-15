package PGAalgorithm;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CombinationInit {
	List<PolicyGraph> graphs;
	List<EPG> labelMapping;
	LabelTreeNode tnt;
	LabelTreeNode dpts;
	LabelTreeNode apps;
	LabelTreeNode stu;
	LabelTreeNode teac;
	LabelTreeNode web;
	LabelTreeNode db;
	LabelTreeNode locn;
	LabelTreeNode cmp;
	LabelTreeNode nc;
	LabelTreeNode zna;
	LabelTreeNode znb;
	LabelTreeNode stats;
	LabelTreeNode dnsp;
	LabelTreeNode nml;
	LabelTreeNode qn;
	LabelTreeNode dns;
	LabelTreeNode rmd;
	
	public CombinationInit() {
		graphs = new LinkedList<PolicyGraph>();
		labelMapping = new LinkedList<EPG>();
		tnt = new LabelTreeNode("Tnt", "Tnt");
		dpts = new LabelTreeNode("Dpts", "Tnt");
		apps = new LabelTreeNode("Apps", "Tnt");
		stu = new LabelTreeNode("Stu", "Tnt");
		teac = new LabelTreeNode("Teac", "Tnt");
		web = new LabelTreeNode("Web", "Tnt");
		db = new LabelTreeNode("DB", "Tnt");
		locn = new LabelTreeNode("Locn", "Locn");
		cmp = new LabelTreeNode("Cmp", "Locn");
		nc = new LabelTreeNode("NC", "Locn");
		zna = new LabelTreeNode("Zn-A", "Locn");
		znb = new LabelTreeNode("Zn-B", "Locn");
		stats = new LabelTreeNode("Stats", "Stats");
		dnsp = new LabelTreeNode("DNSP", "Stats");
		nml = new LabelTreeNode("Nml", "Stats");
		qn = new LabelTreeNode("Qn", "Stats");
		dns = new LabelTreeNode("DNS", "DNS");
		rmd = new LabelTreeNode("Rmd", "DNS");
		initLabelTree();
		initGraphs();
		initLabelMapping();
	}
	
	public void initLabelTree() {
		tnt.setLeftchild(dpts);
		tnt.setRightchild(apps);
		dpts.setLeftchild(stu);
		dpts.setRightchild(teac);
		apps.setLeftchild(web);
		apps.setRightchild(db);
		locn.setLeftchild(cmp);
		locn.setRightchild(nc);
		cmp.setLeftchild(zna);
		cmp.setRightchild(znb);
		stats.setLeftchild(dnsp);
		dnsp.setLeftchild(nml);
		dnsp.setRightchild(qn);
	}
	
	public void initGraph(LabelTreeNode src, LabelTreeNode dst, Set<Integer> port, List<FunctionBox> functionBox, Map<Integer, CptConstraint> constraint) {
		List<LabelTreeNode> s_epg = new LinkedList<LabelTreeNode>();
		List<LabelTreeNode> d_epg = new LinkedList<LabelTreeNode>();
		s_epg.add(src);
		d_epg.add(dst);
		EPG source = new EPG(s_epg);
		EPG destination = new EPG(d_epg);
		EPGPair epair = new EPGPair(source, destination);
		Edge edge = new Edge(port, functionBox);
		Map<EPGPair, Edge> graph = new HashMap<EPGPair, Edge>();
		graph.put(epair, edge);
		PolicyGraph policyGraph;
		if (constraint == null) {
			policyGraph = new PolicyGraph(graph);
		} else {
			policyGraph = new PolicyGraph(graph, constraint);
		}
		graphs.add(policyGraph);		
	}

	public void initGraphs() {
		Map<Integer, CptConstraint> constraint = new HashMap<Integer, CptConstraint>();
		
		Set<Integer> port1 = new HashSet<Integer>();
		List<FunctionBox> functionBox1 = new LinkedList<>();
		functionBox1.add(new FunctionBox("Billing", 3));
		initGraph(stu, web, port1, functionBox1, constraint);
		
		Set<Integer> port2 = new HashSet<Integer>();
		List<FunctionBox> functionBox2 = new LinkedList<>();
		port2.add(80);
		functionBox2.add(new FunctionBox("LB", 2));
		initGraph(dpts, web, port2, functionBox2, constraint);
		
		Set<Integer> port3 = new HashSet<Integer>();
		List<FunctionBox> functionBox3 = new LinkedList<>();
		port3.add(5900);
		initGraph(stu, db, port3, functionBox3, constraint);
		
		Set<Integer> port4 = new HashSet<Integer>();
		List<FunctionBox> functionBox4 = new LinkedList<>();
		port4.add(5900);
		port4.add(22);
		port4.add(23);
		CptConstraint cons = new CptConstraint(false, true, false);
		Map<Integer, CptConstraint> constraint1 = new HashMap<Integer, CptConstraint>();
		constraint1.put(23, cons);
		initGraph(teac, db, port4, functionBox4, constraint1);
		
		Set<Integer> port5 = new HashSet<Integer>();
		List<FunctionBox> functionBox5 = new LinkedList<>();
		functionBox5.add(new FunctionBox("FW", 1));
		initGraph(cmp, nc, port5, functionBox5, constraint);
		
		Set<Integer> port6 = new HashSet<Integer>();
		List<FunctionBox> functionBox6 = new LinkedList<>();
		port6.add(53);
		initGraph(nml, dns, port6, functionBox6, constraint);
		
		Set<Integer> port7 = new HashSet<Integer>();
		List<FunctionBox> functionBox7 = new LinkedList<>();
		initGraph(qn, rmd, port7, functionBox7, constraint);
	}
	
	public void initLabelMapping() {
		List<LabelTreeNode> l1 = new LinkedList<LabelTreeNode>();
		l1.add(stu);
		l1.add(zna);
		List<LabelTreeNode> l2 = new LinkedList<LabelTreeNode>();
		l2.add(teac);
		l2.add(znb);
		List<LabelTreeNode> l3 = new LinkedList<LabelTreeNode>();
		l3.add(dpts);
		l3.add(dnsp);
		List<LabelTreeNode> l4 = new LinkedList<LabelTreeNode>();
		l4.add(apps);
		l4.add(nc);
		EPG lm1 = new EPG(l1);
		EPG lm2 = new EPG(l2);
		EPG lm3 = new EPG(l3);
		EPG lm4 = new EPG(l4);
		labelMapping.add(lm1);
		labelMapping.add(lm2);
		labelMapping.add(lm3);
		labelMapping.add(lm4);
	}
	
}
