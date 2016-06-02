package edu.rit.cs.weaver.build;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import edu.rit.cs.weaver.composer.Composer;
import edu.rit.cs.weaver.composer.FieldOverrideComposer;
import edu.rit.cs.weaver.composer.MethodMergeComposer;
import edu.rit.cs.weaver.composer.MethodOverrideComposer;
import edu.rit.cs.weaver.composer.NoMergeClassComposer;
import edu.rit.cs.weaver.map.ClassMap;
import edu.rit.cs.weaver.traversal.NodeGetter;

/**
 * Allows for the composition of a new class file from the contents of other
 * class files. Uses the Builder design pattern so the steps could easily be
 * chained, i.e. into a composite function.
 * 
 * @author John Rivera
 */
public class ClassBuilder {

	private final ClassMap classMap;
	private ClassNode newClass;
	private String newClassName;

	/**
	 * Constructs a new class builder for building a new Java class file.
	 * 
	 * @param classMap
	 *            The {@link ClassMap} containing mapped {@link ClassNode}s to
	 *            be used for composing.
	 * @param newClassName
	 *            The name of the composed class that will result from the
	 *            build.
	 */
	public ClassBuilder(ClassMap classMap, String newClassName) {
		this.classMap = classMap;
		this.newClassName = newClassName;
	}

	/**
	 * Composes two class files into the new composed class. The behavior is
	 * simple; all contents from the second class file will be appended onto the
	 * first. In other words, this method performs a concatenation operation on
	 * the two classes.
	 * 
	 * @param className1
	 *            The mapped name of the first class file to be composed.
	 * @param className2
	 *            The mapped name of the second class file to be composed.
	 * @return A {@link ClassBuilder} with the updated composed class.
	 */
	public ClassBuilder composeClass(String className1, String className2) {
		ClassNode classNode1 = classMap.get(className1);
		ClassNode classNode2 = classMap.get(className2);
		Composer<ClassNode> composer = new NoMergeClassComposer(classNode1, classNode2, this.newClassName);
		newClass = composer.compose();

		return this;
	}

	/**
	 * Merges all methods in two class files by name. In other words, all
	 * methods with the same names in the first and second class files will be
	 * merged into a new method with the same name.
	 * 
	 * This method calls mergeMethods in the implementations; see the
	 * documentation for that method for more details.
	 * 
	 * Use with caution; this method is under development and likely to change.
	 * 
	 * @param className1
	 *            The mapped name of the first class file merged.
	 * @param className2
	 *            The mapped name of the second class file merged.
	 * @return A {@link ClassBuilder} with the updated composed class.
	 */
	@SuppressWarnings("unchecked")
	public ClassBuilder mergeByName(String className1, String className2) {
		ClassNode classNode1 = classMap.get(className1);
		ClassNode classNode2 = classMap.get(className2);

		Set<String> methodNameSet = new HashSet<String>();
		Set<String> otherMethodNameSet = new HashSet<String>();

		for (MethodNode method : (List<MethodNode>) classNode1.methods)
			methodNameSet.add(method.name);

		for (MethodNode method : (List<MethodNode>) classNode2.methods)
			otherMethodNameSet.add(method.name);

		methodNameSet.retainAll(otherMethodNameSet);

		for (String methodName : methodNameSet)
			this.mergeMethods(className1, methodName, className2, methodName, methodName);

		return this;
	}

	/**
	 * Merges two methods into one with a new name. The second method will be
	 * appended onto the first; in other words, this means all instructions in
	 * the first method will be called, followed by the instructions in the
	 * second. This method is then appended to the composed class.
	 * 
	 * @param className1
	 *            The mapped name of the class file containing the first method
	 *            to be merged.
	 * @param methodName1
	 *            The name of the first method to be merged.
	 * @param className2
	 *            The mapped name of the class file containing the second method
	 *            to be merged.
	 * @param methodName2
	 *            The name of the second method to be merged.
	 * @param newMethodName
	 *            The name of the resulting method. Normally would be the name
	 *            of one of the methods above, but could optionally be anything.
	 * @return A {@link ClassBuilder} with the updated composed class.
	 */
	@SuppressWarnings("unchecked")
	public ClassBuilder mergeMethods(String className1, String methodName1, String className2, String methodName2,
			String newMethodName) {
		ClassNode classNode1 = classMap.get(className1);
		ClassNode classNode2 = classMap.get(className2);
		NodeGetter nodeGetter1 = new NodeGetter(classNode1);
		NodeGetter nodeGetter2 = new NodeGetter(classNode2);
		MethodNode methodNode1 = nodeGetter1.getMethodNode(methodName1);
		MethodNode methodNode2 = nodeGetter2.getMethodNode(methodName2);
		Composer<MethodNode> composer = new MethodMergeComposer(methodNode1, methodNode2, newMethodName);
		newClass.methods.add(composer.compose());
		return this;
	}

	/**
	 * Overrides a method with another. This method simply replaces the
	 * instructions from the first method with the instructions from the second
	 * and places this into a new method. This method is then appended to the
	 * composed class.
	 * 
	 * @param className1
	 *            The mapped name of the class file containing the method to be
	 *            overridden.
	 * @param methodName1
	 *            The name of the method to be overridden.
	 * @param className2
	 *            The mapped name of the class file containing the overriding
	 *            method.
	 * @param methodName2
	 *            The name of the overriding method.
	 * @param newMethodName
	 *            The name of the resulting method. Normally would be the name
	 *            of one of the methods above, but could optionally be anything.
	 * @return A {@link ClassBuilder} with the updated composed class.
	 */
	@SuppressWarnings("unchecked")
	public ClassBuilder overrideMethods(String className1, String methodName1, String className2, String methodName2,
			String newMethodName) {
		ClassNode classNode1 = classMap.get(className1);
		ClassNode classNode2 = classMap.get(className2);
		NodeGetter nodeGetter1 = new NodeGetter(classNode1);
		NodeGetter nodeGetter2 = new NodeGetter(classNode2);
		MethodNode methodNode1 = nodeGetter1.getMethodNode(methodName1);
		MethodNode methodNode2 = nodeGetter2.getMethodNode(methodName2);
		Composer<MethodNode> composer = new MethodOverrideComposer(methodNode1, methodNode2, newMethodName);
		newClass.methods.add(composer.compose());
		return this;
	}

	/**
	 * Overrides the contents of a field with another, and stores this in a new
	 * field. This field is appended to the composed class.
	 * 
	 * @param className1
	 *            The mapped name of the class containing the field to be
	 *            overridden.
	 * @param fieldName1
	 *            The name of the field to be overridden.
	 * @param className2
	 *            The mapped name of the class containing the overriding field.
	 * @param fieldName2
	 *            The name of the overriding field.
	 * @param newFieldName
	 *            The name of the resulting field. Normally would be the name of
	 *            one of the fields above, but could optionally be anything.
	 * @return A {@link ClassBuilder} with the updated composed class.
	 */
	@SuppressWarnings("unchecked")
	public ClassBuilder overrideFields(String className1, String fieldName1, String className2, String fieldName2,
			String newFieldName) {
		ClassNode classNode1 = classMap.get(className1);
		ClassNode classNode2 = classMap.get(className2);
		NodeGetter nodeGetter1 = new NodeGetter(classNode1);
		NodeGetter nodeGetter2 = new NodeGetter(classNode2);
		FieldNode fieldNode1 = nodeGetter1.getFieldNode(fieldName1);
		FieldNode fieldNode2 = nodeGetter2.getFieldNode(fieldName2);
		Composer<FieldNode> composer = new FieldOverrideComposer(fieldNode1, fieldNode2, newFieldName);
		newClass.fields.add(composer.compose());
		return this;
	}

	/**
	 * Finalizes and writes the composed class to a file. Finalizes the composed
	 * class by removing all duplicate members -- only the last occurring
	 * duplicate members in the file are retained. Then writes this to the file
	 * specified by the parameter.
	 * 
	 * @param classFile
	 *            The path to the file to be written to. If the file already
	 *            exists, it will be overwritten without prompt.
	 * @return The {@link Path} representing the new file.
	 * @throws IOException
	 *             if an I/O error occurs writing to the specified file.
	 */
	public Path write(String classFile) throws IOException {
		cleanup();
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		newClass.accept(writer);
		byte[] buffer = writer.toByteArray();
		Path newClassPath = Paths.get(classFile);
		Files.write(newClassPath, buffer);

		return newClassPath;
	}

	@SuppressWarnings("unchecked")
	private void cleanup() {
		Map<String, MethodNode> methodMap = new LinkedHashMap<String, MethodNode>();
		Map<String, FieldNode> fieldMap = new LinkedHashMap<String, FieldNode>();

		for (MethodNode method : (List<MethodNode>) newClass.methods)
			methodMap.put(method.name, method);

		for (FieldNode field : (List<FieldNode>) newClass.fields)
			fieldMap.put(field.name, field);

		newClass.methods.clear();
		newClass.fields.clear();
		newClass.methods.addAll(methodMap.values());
		newClass.fields.addAll(fieldMap.values());
	}
}
