package org.subjectj.compositor.map;

import static org.objectweb.asm.Opcodes.ASM5;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

/**
 * Allows for the mapping of user-defined names to an actual {@link ClassNode}.
 * Makes for easy storage and retrieval of {@link ClassNode}s for composing.
 * 
 * @author John Rivera
 */
public class ClassMap {

	private final Map<String, ClassNode> classMap = new HashMap<String, ClassNode>();

	/**
	 * Adds a new name-{@link ClassNode} mapping.
	 * 
	 * @param className
	 *            The name to map the {@link ClassNode} to.
	 * @param filePath
	 *            The path to the file to turn into a {@link ClassNode}.
	 * @throws IOException
	 *             If an error occurs reading the file.
	 */
	public void add(String className, String filePath) throws IOException {
		Path classPath = new File(filePath).toPath();
		ClassNode classNode = new ClassNode(ASM5);

		try (InputStream input = Files.newInputStream(classPath)) {
			ClassReader reader = new ClassReader(input);
			reader.accept(classNode, 0);
		}

		classMap.put(className, classNode);
	}

	/**
	 * Gets the mapped {@link ClassNode}.
	 * 
	 * @param className
	 *            The mapped name.
	 * @return The {@link ClassNode}.
	 */
	public ClassNode get(String className) {
		return this.classMap.get(className);
	}

	/**
	 * Removes a mapping.
	 * 
	 * @param className
	 *            The name of the mapping to remove.
	 */
	public void remove(String className) {
		this.classMap.remove(className);
	}

	/**
	 * Removes all mappings from the map.
	 */
	public void clear() {
		this.classMap.clear();
	}
}
