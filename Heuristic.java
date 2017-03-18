package jp.co.worksap.global;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

public class Heuristic implements Comparator<Position> {

	private Hashtable<Position, Integer> heuristicTable;
	private Position Goal;
	private int goalHeuristic;

	public Hashtable<Position, Integer> getHeuristicTable() {
		return heuristicTable;
	}

	public Heuristic(List<Position> checkpoint, Position Start, Position Goal) {
		heuristicTable = new Hashtable<Position, Integer>();
		this.Goal = Goal;
		for (Position p : checkpoint) {
			heuristicTable.put(p, heuristicFunction(Start, p));
		}
		goalHeuristic = Position.distance(Start, Goal);
	}

	public void setCheckPointHeuristic(Position Start) {
		for (Position p : heuristicTable.keySet()) {
			heuristicTable.put(p, heuristicFunction(Start, p));
		}
	}

	public int getGoalHeuristic() {
		return goalHeuristic;
	}

	public void setGoalHeuristic(Position startpos) {
		this.goalHeuristic = Position.distance(startpos, Goal);
	}

	public int heuristicFunction(Position p1, Position p2) {

		int hsld = (p1.getRow() - p2.getRow()) * (p1.getRow() - p2.getRow())
				+ (p1.getCol() - p2.getCol()) * (p1.getCol() - p2.getCol());

		return hsld;
	}

	public int getHeuristicValue(Position checkpoint) {
		try {
			return heuristicTable.get(checkpoint);
		} catch (NullPointerException e) {
			return -1;
		}
	}

	@Override
	public int compare(Position o1, Position o2) {
		// TODO Auto-generated method stub
		if (heuristicTable.get(o1) > heuristicTable.get(o2))
			return -1;
		else
			return 1;
		// return 0;
	}

}
