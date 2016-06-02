package edu.rit.cs.weaver.traversal;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * An utility class that allows for easy retrieval of members from a
 * {@link ClassNode}.
 * 
 * @author John Rivera
 */
public class NodeGetter {

	private final ClassNode classNode;

	/**
	 * Constructs a new {@link NodeGetter} for a {@link ClassNode}.
	 * 
	 * @param classNode
	 *            The {@link ClassNode} to use for this {@link NodeGetter}.
	 */
	public NodeGetter(ClassNode classNode) {
		this.classNode = classNode;
	}

	/**
	 * Gets an inner class from the {@link ClassNode}.
	 * 
	 * @return The {@link ClassNode} representing the inner class.
	 */
	public ClassNode getClassNode() {
		return this.classNode;
	}

	/**
	 * Gets a method from the {@link ClassNode}.
	 * 
	 * @param name
	 *            The name of the method to get.
	 * @return The {@link MethodNode} representing the method.
	 */
	public MethodNode getMethodNode(String name) {
		@SuppressWarnings("unchecked")
		List<MethodNode> methods = classNode.methods;

		for (MethodNode method : methods) {
			if (name.equals(method.name))
				return method;
		}

		return null;
	}

	/**
	 * Gets a field from the {@link ClassNode}.
	 * 
	 * @param name
	 *            The name of the field to get.
	 * @return The {@link FieldNode} representing the field.
	 */
	public FieldNode getFieldNode(String name) {
		@SuppressWarnings("unchecked")
		List<FieldNode> fields = classNode.fields;

		for (FieldNode field : fields) {
			if (name.equals(field.name))
				return field;
		}

		return null;
	}
}
