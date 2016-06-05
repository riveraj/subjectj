package org.subjectj.compositor.composer;

import org.objectweb.asm.tree.FieldNode;

/**
 * Composes two fields so that one overrides the other.
 * 
 * @author John Rivera
 */
public class FieldOverrideComposer implements Composer<FieldNode> {

	private final FieldNode fieldNode1;
	private final FieldNode fieldNode2;
	private final String newFieldName;

	/**
	 * Constructs the field overriding composer.
	 * 
	 * @param fieldNode1
	 *            The overriding {@link FieldNode}.
	 * @param fieldNode2
	 *            The {@link FieldNode} to be overridden.
	 * @param newFieldName
	 *            The name of the composed field.
	 */
	public FieldOverrideComposer(FieldNode fieldNode1, FieldNode fieldNode2, String newFieldName) {
		this.fieldNode1 = fieldNode1;
		this.fieldNode2 = fieldNode2;
		this.newFieldName = newFieldName;
	}

	/**
	 * Composes the two fields so that one overrides the other, and returns the
	 * newly composed field.
	 * 
	 * @return The composed {@link FieldNode}.
	 */
	@Override
	public FieldNode compose() {
		FieldNode composed = fieldNode1;
		composed.name = newFieldName;
		composed.value = fieldNode2.value;
		return composed;
	}
}
