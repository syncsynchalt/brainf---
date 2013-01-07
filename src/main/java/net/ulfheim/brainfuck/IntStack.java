package net.ulfheim.brainfuck;

import java.util.EmptyStackException;

/**
 * Stack which works around the performance shortcomings of the Stack&lt;Integer&gt;
 * class:
 * <ul>
 * <li> only supports Integer
 * <li> synchronized
 * </ul>
 * 
 * This stack uses an array of int internally.
 *
 * @author mdriscoll
 */
public class IntStack {
	private int[] stack;
	private int cur;

	IntStack(int size) {
		if (size <= 0) {
			size = 1;
		}
		stack = new int[size];
	}

	IntStack() {
		this(16);
	}

	public void push(int i) {
		if (cur >= stack.length) {
			int[] newStack = new int[stack.length * 2];
			System.arraycopy(stack, 0, newStack, 0, stack.length);
			stack = newStack;
		}
		stack[cur] = i;
		cur++;
	}

	public int pop() {
		if (cur <= 0) {
			throw new EmptyStackException();
		}
		--cur;
		return stack[cur];
	}

	public int peek() {
		if (cur <= 0) {
			throw new EmptyStackException();
		}
		return stack[cur - 1];
	}

	public int size() {
		return cur;
	}
}
