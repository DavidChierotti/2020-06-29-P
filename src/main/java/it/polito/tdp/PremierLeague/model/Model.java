package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Map<Integer,Match> partite;
	private SimpleWeightedGraph<Match,DefaultWeightedEdge> grafo;
	private List<Match> migliore;
	private double pesototale;
	
	
	public Model() {
		this.dao=new PremierLeagueDAO();
		this.partite=new HashMap<>();
		for(Match m:dao.listAllMatches()) {
			partite.put(m.getMatchID(), m);
		}
		
	}
	
	public void creaGrafo(int m,int n) {
		this.grafo=new SimpleWeightedGraph<Match,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo,dao.getVertici(partite, m));
		
		for(Adiacenza a:dao.getArchi(partite, m, n)) {
			Graphs.addEdge(grafo,a.getM1(),a.getM2(),a.getPeso());
		}
		
		
		
	}
	
	public List<Match> cercaCammino(Match m1,Match m2){
		this.migliore=new ArrayList<>();
		this.pesototale=0;
		List<Match> parziale=new ArrayList<>();
		parziale.add(m1);
		cerca(parziale,m2);
		
		this.pesototale=this.calcolaPeso(migliore);
		
		return migliore;
	}
	
	
	
	
	
	private void cerca(List<Match> parziale, Match m2) {
		if(parziale.get(parziale.size()-1).equals(m2)) {
			if(this.calcolaPeso(parziale)>this.calcolaPeso(migliore)) {
				migliore=new ArrayList<>(parziale);
			}
			return;
		}
		else {
			for(Match m:this.possibili(parziale)) {
				parziale.add(m);
				this.cerca(parziale, m2);
				parziale.remove(m);
			}
		}
		
	}

	private List<Match> possibili(List<Match> parziale) {
		Match m=parziale.get(parziale.size()-1);
		List<Match> possibili=new ArrayList<>();
		for(Match mm:Graphs.neighborListOf(grafo,m)) {
			String s=mm.getTeamHomeID()+mm.getTeamAwayID()+"";
			if(!this.isPresente(s,parziale)) {
				possibili.add(mm);
			}
		}
		
		
		return possibili;
	}

	private boolean isPresente(String s,List<Match> parziale) {
		boolean p=false;
		for(Match m:parziale) {
			if(s.compareTo(m.getTeamHomeID()+m.getTeamAwayID()+"")==0||s.compareTo(m.getTeamAwayID()+m.getTeamHomeNAME()+"")==0) {
				p=true;
			}
		}
		return p;
	}

	private double calcolaPeso(List<Match> parziale) {
        double p=0;
        if(parziale.size()>1) {
		for(int i=0;i<parziale.size()-2;i++) {
			DefaultWeightedEdge e=grafo.getEdge(parziale.get(i),parziale.get(i+1));
			p+=grafo.getEdgeWeight(e);
		 }
        }
        return p;
	}

	public List<Adiacenza> massimo(){
		List<Adiacenza> massimi=new ArrayList<>();
		double max=0;
		for(DefaultWeightedEdge e:grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)>max) {
				max=grafo.getEdgeWeight(e);
			}
		}
		for(DefaultWeightedEdge e:grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)==max) {
				Adiacenza a=new Adiacenza(grafo.getEdgeSource(e),grafo.getEdgeTarget(e),grafo.getEdgeWeight(e));
				massimi.add(a);
			}
		}
		return massimi;
	}
	
	
	
	public int nVert() {
		return this.grafo.vertexSet().size();
	}
	public int nArch() {
		return this.grafo.edgeSet().size();
	}

	public SimpleWeightedGraph<Match, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public double getPesototale() {
		return pesototale;
	}
	
	
	
}
