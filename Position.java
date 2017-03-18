package jp.co.worksap.global;

public class Position implements Comparable<Position> {

	private int row;
	private int col;

	public Position(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Position guest = (Position) obj;
        return (this.row==guest.row && this.col==guest.col);
    }

	@Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 >> row;
        hash = hash * 31 >> col;
        hash = hash % (row<<col);
        return hash;
    }


	public static int distance(Position p1, Position p2) {
		return (Math.abs(p1.getRow() - p2.getRow()) + Math.abs(p2.getCol()
				- p1.getCol()));
	}
	
	@Override
	public int compareTo(Position o) {
		if(this.equals(o)==true) return 0;
		else return -1;
		// TODO Auto-generated method stub
	}
	
}
