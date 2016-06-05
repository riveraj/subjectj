package org.subjectj.compositor;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassUtil {

	@SuppressWarnings("unchecked")
	public final static void print(ClassNode node) {
		System.out.println("name: " + node.name);
		System.out.println("access: " + node.access);

		if (node.attrs != null)
			for (Object o : node.attrs)
				System.out.println("attrs: " + o.toString());

		for (FieldNode o : (List<FieldNode>) node.fields)
			System.out.println("field: " + o.name);

		for (Object o : node.innerClasses)
			System.out.println("inner class: " + o.toString());

		for (Object o : node.interfaces)
			System.out.println("interface: " + o.toString());

		if (node.invisibleAnnotations != null)
			for (Object o : node.invisibleAnnotations)
				System.out.println("invisible annotation: " + o.toString());

		if (node.invisibleTypeAnnotations != null)
			for (Object o : node.invisibleTypeAnnotations)
				System.out.println("invisible type annotation: " + o.toString());

		for (MethodNode o : (List<MethodNode>) node.methods)
			System.out.println("method: " + o.name);

		System.out.println("outer class: " + node.outerClass);
		System.out.println("outer method: " + node.outerMethod);
		System.out.println("outer method desc: " + node.outerMethodDesc);
		System.out.println("signature: " + node.signature);
		System.out.println("source debug: " + node.sourceDebug);
		System.out.println("source file: " + node.sourceFile);
		System.out.println("super name: " + node.superName);
		System.out.println("version: " + node.version);

		if (node.visibleAnnotations != null)
			for (Object o : node.visibleAnnotations)
				System.out.println("visible annotation: " + o.toString());

		if (node.visibleTypeAnnotations != null)
			for (Object o : node.visibleTypeAnnotations)
				System.out.println("visible type annotation: " + o.toString());
	}
}
