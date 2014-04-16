package com.lew.scott.hanoi;

public class PlateStack {
	protected int[] data;
	protected int ptr;

	public PlateStack(int size) {
		this.data = new int[size];
		this.ptr = 0;
	}

	public void push(int x) {
		if (this.ptr < this.data.length)
			this.data[(this.ptr++)] = x;
	}

	public int pop() {
		if (this.ptr > 0) {
			return this.data[(--this.ptr)];
		}
		return -2147483648;
	}

	public int top() {
		if (this.ptr > 0) {
			return this.data[(this.ptr - 1)];
		}
		return -2147483648;
	}

	public int size() {
		return this.ptr;
	}
}
