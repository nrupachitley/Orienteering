package jp.co.worksap.global;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class BeginGame {

	private static Map map;
	/*
	 * Visited Array of Boolean This array is set to true if 'S' has visited a
	 * point on the map
	 */
	private boolean[][] visited;
	/*
	 * Heuristic Class for calculating the Heuristic Values of Checkpoint and
	 * Goal on each move of 'S'
	 */
	private Heuristic heuristic;
	/*
	 * Number of Steps to Goal
	 */
	private int steps;
	/*
	 * Result Path from Start to Goal
	 */
	LinkedList<Position> resultPath;

	enum Movement {
		UP, LEFT, DOWN, RIGHT
	}

	public BeginGame(int w, int h) {
		map = new Map(w, h);
		visited = new boolean[h][w];
		InitVisited();
		heuristic = new Heuristic(map.getCheckpoint(), map.getStart(),
				map.getGoal());
		steps = 0;
		resultPath = new LinkedList<Position>();
	}

	/*
	 * Marks the Current Position of 'S' as true because it is visited
	 */
	public void visitStart() {
		visited[map.getStart().getRow()][map.getStart().getCol()] = true;
	}

	/*
	 * Re-Initialising Visited Array for the next Checkpoint and subsequently
	 * the Goal
	 */
	public void InitVisited() {
		for (int i = 0; i < map.getHeight(); i++) {
			for (int j = 0; j < map.getWidth(); j++) {
				visited[i][j] = false;
			}
		}
	}

	/*
	 * Move 'S' to the new position. Character in the current position of S is
	 * replaced by '.' If Goal and Start were at the Same position then the
	 * character at current Position of 'S' is replaced by 'G'
	 * 
	 * @param newposition: new Position of 'S'
	 */
	public void move(Position newposition) {

		if (map.getStart().equals(map.getGoal()))
			map.updateMap(new Position(map.getStart().getRow(), map.getStart()
					.getCol()), 'G');
		else {
			map.updateMap(new Position(map.getStart().getRow(), map.getStart()
					.getCol()), '.');
		}
		map.setStart(newposition);
		if (map.getChar(newposition) != 'G')
			map.updateMap(newposition, 'S');
	}

	/*
	 * Returns the Position after the argument Movement m of argument Position p
	 * Movement can be any one of the movement in enum Movement
	 * 
	 * @param p: Initial Position
	 * 
	 * @param m: Movement of Initial Position
	 * 
	 * @return: New Position after Movement
	 */
	public Position getPosition(Position p, Movement m) {
		switch (m) {
		case UP: {
			return new Position(p.getRow() - 1, p.getCol());
		}
		case DOWN: {
			return new Position(p.getRow() + 1, p.getCol());
		}
		case LEFT: {
			return new Position(p.getRow(), p.getCol() - 1);
		}
		case RIGHT: {
			return new Position(p.getRow(), p.getCol() + 1);
		}
		}
		return null;
	}

	/*
	 * Checks if 'S' has reached Position p( of any of the checkpoint or the
	 * Goal)
	 * 
	 * @param p: Position of Goal or Checkpoints
	 * 
	 * @return: boolean true if 'S' has reached
	 */
	public boolean hasReached(Position p) {
		if (getPosition(map.getStart(), Movement.UP).equals(p) == true
				|| getPosition(map.getStart(), Movement.DOWN).equals(p) == true
				|| getPosition(map.getStart(), Movement.LEFT).equals(p) == true
				|| getPosition(map.getStart(), Movement.RIGHT).equals(p) == true) {
			return true;
		} else
			return false;

	}

	/*
	 * A* algorithm for finding optimal path to the checkpoint and then the
	 * Goal. In between if any checkpoint is encountered it is first cleared.If
	 * this move leads to increase in heuristic value than it moves back to its
	 * initial position.
	 * 
	 * Algorithm uses priority queues to store potential path to the
	 * goal/checkpoint and then uses BFS for expanding search space
	 * 
	 * Heuristic Function is the straight Line Distances between different
	 * Positions.
	 * 
	 * Basic idea here is that only when the heuristic value of the next move is
	 * less than the heuristic value of current position then only that move is
	 * made.
	 * 
	 * If there is no path that leads to a decrease in heuristic value than the
	 * next best path is searched. Heuristic value is then set according to this
	 * new value.
	 */

	public int AStarSearch(Position p) throws ArrayIndexOutOfBoundsException {

		PriorityQueue<Position> pq = new PriorityQueue<Position>();
		Position temp = null, newposition;
		int giveUp = 0;
		while (hasReached(p) == false
				&& giveUp++ < Map.getMaxCheckpoint()
						* (map.getHeight() * map.getWidth())) {
			if (pq.isEmpty() == true)
				newposition = map.getStart();
			else
				newposition = pq.remove();
			for (Movement m : Movement.values()) {
				temp = getPosition(newposition, m);
				if ((map.getChar(temp) != '#')
						&& visited[temp.getRow()][temp.getCol()] == false) {
					if (heuristic.heuristicFunction(temp, p) < heuristic
							.getHeuristicValue(p) || map.getChar(temp) == '@') {
						pq.add(temp);
						heuristic.setCheckPointHeuristic(temp);
						heuristic.setGoalHeuristic(temp);
						if (map.getChar(temp) == '@') {
							if (heuristic.heuristicFunction(temp, p) > heuristic
									.heuristicFunction(map.getStart(), p)) {
								Position prev = map.getStart();
								move(temp);
								// map.showMap();
								visitStart();
								steps++;
								resultPath.add(temp);
								move(prev);
								steps++;
								resultPath.add(temp);
							} else {
								move(temp);
								// map.showMap();
								steps++;
								resultPath.add(temp);
							}
							visitStart();
							coverCheckPoint(temp);
							heuristic.getHeuristicTable().remove(temp);
							temp = null;
							break;
						}
					}
				}
			}

			if (pq.isEmpty() == false) {
				if (temp != null) {
					move(pq.element());
					// map.showMap();
					visitStart();
					steps++;
					resultPath.add(pq.element());
				}
			} else {
				Position nextBest = null;
				int nextBestHeuristic = Integer.MAX_VALUE - 8;
				for (Movement m : Movement.values()) {
					Position tempPos = getPosition(map.getStart(), m);
					if (map.getChar(getPosition(map.getStart(), m)) != '#'
							&& visited[getPosition(map.getStart(), m).getRow()][getPosition(
									map.getStart(), m).getCol()] == false) {
						int tempVal = heuristic.heuristicFunction(tempPos, p);
						if (tempVal <= nextBestHeuristic) {
							nextBest = tempPos;
							nextBestHeuristic = tempVal;
						}
					}

				}
				if (nextBest != null) {
					pq.add(nextBest);
					move(nextBest);
					// map.showMap();
					visitStart();
					steps++;
					resultPath.add(nextBest);
					heuristic.setCheckPointHeuristic(nextBest);
					heuristic.setGoalHeuristic(nextBest);
				} else {
					for (Movement m : Movement.values()) {
						visited[getPosition(map.getStart(), m).getRow()][getPosition(
								map.getStart(), m).getCol()] = false;
					}
				}

			}
		}
		move(p);
		// map.showMap();
		resultPath.add(p);
		steps++;
		visitStart();
		coverCheckPoint(map.getStart());
		heuristic.getHeuristicTable().remove(map.getStart());
		heuristic.setCheckPointHeuristic(map.getStart());
		heuristic.setGoalHeuristic(map.getStart());
		if (giveUp > Map.getMaxCheckpoint()
				* (map.getHeight() * map.getWidth()))
			return -1;
		return 1;
	}

	public void coverCheckPoint(Position p) {
		map.getCovered()[p.getRow()][p.getCol()] = true;
	}

	/*
	 * Starts the Game and Prints the result Path if there is any
	 */
	public void start() {

		resultPath.add(map.getStart());
		boolean isValid = true;
		for (Position p : map.getCheckpoint()) {
			if (map.getCovered()[p.getRow()][p.getCol()] == false) {
				InitVisited();
				visitStart();
				if (AStarSearch(p) == -1) {
					isValid = false;
					break;
				}
			}
		}
		InitVisited();
		visitStart();
		if (isValid == true && AStarSearch(map.getGoal()) == 1) {
			System.out.println("Total Steps to Goal= " + (steps));
			System.out.print("Result path (x, y): ");
			int i = resultPath.size();
			for (Position p : resultPath) {
				System.out.print("( " + p.getCol() + ", " + p.getRow() + " )");
				if (--i > 0)
					System.out.print("==>");
			}

		} else {
			System.out.println("There is no valid Path");
		}

	}
}
