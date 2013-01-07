package net.ulfheim.brainfuck;

import java.util.EmptyStackException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author mdriscoll
 */
public class IntStackTest
{
	@Test
	public void testEmptyStack()
	{
		IntStack stack = new IntStack(0);
		stack.push(2);
		assertEquals(2, stack.pop());
	}

	@Test
	public void testPushAndPop()
	{
		IntStack stack = new IntStack();
		stack.push(1);
		assertEquals(1, stack.pop());
	}

	@Test
	public void testPeek()
	{
		IntStack stack = new IntStack();
		stack.push(99);
		assertEquals(99, stack.peek());
	}

	@Test(expected = EmptyStackException.class)
	public void testPeekEmpty()
	{
		IntStack stack = new IntStack();
		stack.peek();
	}

	@Test
	public void testSize()
	{
		IntStack stack = new IntStack();
		assertEquals(0, stack.size());
		stack.push(1);
		assertEquals(1, stack.size());
		stack.pop();
		assertEquals(0, stack.size());
	}

	@Test(expected = EmptyStackException.class)
	public void testPopEmpty()
	{
		IntStack stack = new IntStack();
		stack.push(1);
		stack.pop();
		stack.pop();
	}

	@Test
	public void testResize()
	{
		IntStack stack = new IntStack(5);
		for (int i = 0; i < 100; i++) {
			stack.push(i);
		}
		for (int i = 0; i < 100; i++) {
			assertEquals(100-i-1, stack.pop());
		}
		assertEquals(0, stack.size());
	}
}
