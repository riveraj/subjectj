package org.subjectj.compositor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import org.subjectj.util.ClassFileUtils;

public class TestingDriver {

	public static void main(String[] args) throws Throwable {
		Path path = Paths.get("", "org/subjectj/compositor");
		Collection<Path> exceptions = new ArrayList<Path>();
		exceptions.add(Paths.get("", "org/subjectj/compositor/ClassUtil.class"));
		exceptions.add(Paths.get("", "org/subjectj/compositor/composer"));

		Collection<Path> paths = ClassFileUtils.getClassFiles(path, true, exceptions);

		for (Path p : paths) {
			System.out.println(p);
		}

		System.out.println();

		Path file = Paths.get("", "org/subjectj/compositor/TestingDriver.class");
		Collection<Path> files = ClassFileUtils.getClassFiles(file);

		for (Path f : files) {
			System.out.println(f);
		}
	}
}
