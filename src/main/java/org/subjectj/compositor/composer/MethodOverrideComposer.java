package org.subjectj.compositor.composer;

import org.objectweb.asm.tree.MethodNode;

/**
 * Composes two methods so that one overrides the other.
 * 
 * @author John Rivera
 */
public class MethodOverrideComposer implements Composer<MethodNode> {

	private final MethodNode methodNode1;
	private final MethodNode methodNode2;
	private final String newMethodName;

	/**
	 * Constructs the method overriding composer.
	 * 
	 * @param methodNode1
	 *            The {@link MethodNode} to be overridden.
	 * @param methodNode2
	 *            The overriding {@link MethodNode}.
	 * @param newMethodName
	 *            The name of the composed method.
	 */
	public MethodOverrideComposer(MethodNode methodNode1, MethodNode methodNode2, String newMethodName) {
		this.methodNode1 = methodNode1;
		this.methodNode2 = methodNode2;
		this.newMethodName = newMethodName;
	}

	/**
	 * Composes the two methods so that the instructions in the first method
	 * replaces the instructions in the second..
	 * 
	 * @return The composed {@link MethodNode}.
	 */
	@Override
	public MethodNode compose() {
		MethodNode composed = methodNode1;
		composed.name = newMethodName;
		composed.instructions.clear();
		composed.instructions.add(methodNode2.instructions);
		return composed;
	}
}
