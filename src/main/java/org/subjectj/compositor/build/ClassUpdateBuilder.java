package org.subjectj.compositor.build;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

/**
 * Allows for updating of existing class files so they correctly refer to the
 * newly composed class files at runtime. Uses the Builder design pattern so the
 * steps could easily be chained, i.e. into a composite function.
 * 
 * @author John Rivera
 */
public class ClassUpdateBuilder {

	private final ClassNode classNode;

	/**
	 * Constructs a new {@link ClassUpdateBuilder} for updating a
	 * {@link ClassNode}.
	 * 
	 * @param classNode
	 *            The {@link ClassNode} to update.
	 */
	public ClassUpdateBuilder(ClassNode classNode) {
		this.classNode = classNode;
	}

	/**
	 * Updates all references to an old class to a new class in the
	 * {@link ClassNode} contained in this {@link ClassUpdateBuilder}.
	 * 
	 * @param oldClassName
	 *            The class name references to be updated.
	 * @param newClassName
	 *            The class name to update to.
	 * @return A {@link ClassUpdateBuilder} containing the updated
	 *         {@link ClassNode}.
	 */
	@SuppressWarnings("unchecked")
	public ClassUpdateBuilder updateClass(String oldClassName, String newClassName) {
		for (MethodNode method : (List<MethodNode>) classNode.methods) {
			Iterator<AbstractInsnNode> i = method.instructions.iterator();

			while (i.hasNext()) {
				AbstractInsnNode node = i.next();

				if (node.getType() == AbstractInsnNode.TYPE_INSN)
					if (((TypeInsnNode) node).desc.equals(oldClassName))
						((TypeInsnNode) node).desc = newClassName;

				if (node.getType() == AbstractInsnNode.METHOD_INSN)
					if (((MethodInsnNode) node).owner.equals(oldClassName))
						((MethodInsnNode) node).owner = newClassName;

				if (node.getType() == AbstractInsnNode.FIELD_INSN)
					if (((FieldInsnNode) node).owner.equals(oldClassName))
						((FieldInsnNode) node).owner = newClassName;
			}
		}
		return this;
	}

	/**
	 * Updates all calls to a method to calls to a different method.
	 * 
	 * Not implemented yet.
	 * 
	 * @param oldMethodName
	 *            The name of the method to update the calls to.
	 * @param newMethodName
	 *            The name of the new method.
	 * @return A {@link ClassBuilder} with the updated composed class.
	 */
	@SuppressWarnings("unchecked")
	public ClassUpdateBuilder updateMethod(String oldMethodName, String newMethodName) {
		for (MethodNode method : (List<MethodNode>) classNode.methods) {
			Iterator<AbstractInsnNode> i = method.instructions.iterator();

			while (i.hasNext()) {
				AbstractInsnNode node = i.next();

				if (node.getType() == AbstractInsnNode.METHOD_INSN)
					if (((MethodInsnNode) node).name.equals(oldMethodName))
						((MethodInsnNode) node).name = newMethodName;
			}
		}

		return this;
	}

	/**
	 * Updates all references to a field to calls to a different field.
	 * 
	 * Not implemented yet.
	 * 
	 * @param oldFieldName
	 *            The name of the field to update the references to.
	 * @param newFieldName
	 *            The name of the new field.
	 * @return A {@link ClassBuilder} with the updated composed class.
	 */
	@SuppressWarnings("unchecked")
	public ClassUpdateBuilder updateField(String oldFieldName, String newFieldName) {
		for (MethodNode method : (List<MethodNode>) classNode.methods) {
			Iterator<AbstractInsnNode> i = method.instructions.iterator();

			while (i.hasNext()) {
				AbstractInsnNode node = i.next();

				if (node.getType() == AbstractInsnNode.FIELD_INSN)
					if (((MethodInsnNode) node).name.equals(oldFieldName))
						((MethodInsnNode) node).name = newFieldName;
			}
		}

		return this;
	}

	/**
	 * Writes the updated {@link ClassNode} to a file.
	 * 
	 * @param classFile
	 *            The path to the file to be written to. If the file already
	 *            exists, it will be overwritten without prompt.
	 * @return The {@link Path} representing the new file.
	 * @throws IOException
	 *             if an I/O error occurs writing to the specified file.
	 */
	public Path write(String classFile) throws IOException {
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		byte[] buffer = writer.toByteArray();

		Path classPath = new File(classFile).toPath();
		Files.delete(classPath);
		Files.write(classPath, buffer);

		return classPath;
	}
}
