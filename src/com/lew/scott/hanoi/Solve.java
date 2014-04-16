package com.lew.scott.hanoi;

public class Solve {

	protected int step = 0;
	protected StringBuffer text = new StringBuffer(256);

	public String solveOfHanoi(int N, int from, int to, int spare) {
		if (N < 1) {
			return "";
		}
		if (N == 1) {
			moveOne(from, to);
		} else {
			solveOfHanoi(N - 1, from, spare, to);
			moveOne(from, to);
			solveOfHanoi(N - 1, spare, to, from);
		}
		return this.text.toString();
	}

	private void moveOne(int from, int to) {
		step++;
		this.text.append("      µÚ" + step + "²½:" + from + "-->" + to + "\n");
	}
}
