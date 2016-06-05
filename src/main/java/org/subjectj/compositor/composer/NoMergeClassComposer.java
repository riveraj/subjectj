package org.subjectj.compositor.composer;

import org.objectweb.asm.tree.ClassNode;

/**
 * Composes two classes so that the composed class contains all members of both
 * classes.
 * 
 * @author John Rivera
 */
public class NoMergeClassComposer implements Composer<ClassNode> {

	private final ClassNode classNode1;
	private final ClassNode classNode2;
	private final String newClassName;

	/**
	 * Constructs a non-merging class composer.
	 * 
	 * @param classNode1
	 *            The first {@link ClassNode} to compose.
	 * @param classNode2
	 *            The second {@link ClassNode} to compose.
	 * @param newClassName
	 *            The name of the composed class.
	 */
	public NoMergeClassComposer(ClassNode classNode1, ClassNode classNode2, String newClassName) {
		this.classNode1 = classNode1;
		this.classNode2 = classNode2;
		this.newClassName = newClassName;
	}

	/**
	 * Composes the classes so that all members of the second class will be
	 * appended into the first.
	 * 
	 * @return The composed {@link ClassNode}.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ClassNode compose() {
		ClassNode composed = classNode1;
		composed.name = newClassName;
		composed.fields.addAll(classNode2.fields);
		composed.methods.addAll(classNode2.methods);
		return composed;
	}
}
