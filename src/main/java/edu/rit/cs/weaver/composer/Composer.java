package edu.rit.cs.weaver.composer;

/**
 * An interface for a composer that composes two objects in some way. Uses the
 * Command pattern for ability to separate creation and invocation.
 * 
 * @author John Rivera
 * @param <T>
 *            The object to be composed.
 */
public interface Composer<T> {

	/**
	 * Composes an object in some way and returns the composed object.
	 * 
	 * @return The composed object.
	 */
	public T compose();
}
