package org.subjectj.compositor.composer;

import java.util.Iterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Composes two methods so that they are merged into one.
 * 
 * @author John Rivera
 */
public class MethodMergeComposer implements Composer<MethodNode> {

	private final MethodNode methodNode1;
	private final MethodNode methodNode2;
	private final String newMethodName;

	/**
	 * Constructs the method merging composer.
	 * 
	 * @param methodNode1
	 *            The first {@link MethodNode} to merge.
	 * @param methodNode2
	 *            The second {@link MethodNode} to merge.
	 * @param newMethodName
	 *            The name of the composed method.
	 */
	public MethodMergeComposer(MethodNode methodNode1, MethodNode methodNode2, String newMethodName) {
		this.methodNode1 = methodNode1;
		this.methodNode2 = methodNode2;
		this.newMethodName = newMethodName;
	}

	/**
	 * Composes the two methods so that the instructions in the second is
	 * appended to the instructions of the first, and returns the composed
	 * method.
	 * 
	 * @return The composed {@link MethodNode}.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MethodNode compose() {
		MethodNode composed = methodNode1;
		composed.name = newMethodName;

		Iterator<AbstractInsnNode> i = composed.instructions.iterator();
		AbstractInsnNode node = null;

		while (i.hasNext()) {
			node = i.next();

			if (node.getOpcode() == Opcodes.ALOAD) {
				AbstractInsnNode temp = node;
				node = i.next();

				if (node.getOpcode() == Opcodes.INVOKESPECIAL)
					if (((MethodInsnNode) node).name.equals("<init>")) {
						composed.instructions.remove(temp);
						composed.instructions.remove(node);
					}
			}

			if (node.getOpcode() == Opcodes.RETURN)
				composed.instructions.remove(node);
		}

		composed.instructions.add(methodNode2.instructions);
		return composed;
	}
}
