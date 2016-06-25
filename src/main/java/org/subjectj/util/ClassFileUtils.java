package org.subjectj.util;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Utility class for gathering class files on the classpath.
 * 
 * @author John Rivera
 */
public final class ClassFileUtils {

	// Prevent class instantiation
	private ClassFileUtils() {
	}

	/**
	 * Gets all .class files contained within the given path. This function does
	 * not allow for recursive searching of sub-directories.
	 * 
	 * Passing a path to a directory to this function will return a
	 * {@link Collection} containing all .class files contained within that
	 * directory.
	 * 
	 * Passing a path to a file to this function will return a
	 * {@link Collection} containing only that file.
	 * 
	 * @param path
	 *            The path to search for .class files in.
	 * @return A {@link Collection} of all .class files found within the path.
	 * @throws IOException
	 *             If an error occurs accessing the path.
	 */
	public static final Collection<Path> getClassFiles(Path path) throws IOException {
		return getClassFiles(path, false);
	}

	/**
	 * Gets all .class files contained within the given path. Allows for the
	 * user to specify whether to search sub-directories recursively.
	 * 
	 * Passing a path to a directory to this function will return a
	 * {@link Collection} containing all .class files contained within that
	 * directory.
	 * 
	 * Passing a path to a file to this function will return a
	 * {@link Collection} containing only that file.
	 * 
	 * @param path
	 *            The path to search for .class files in.
	 * @param recursive
	 *            Boolean value to set whether to search recursively.
	 * @return A {@link Collection} of all .class files found within the path.
	 * @throws IOException
	 *             If an error occurs accessing the path.
	 */
	public static final Collection<Path> getClassFiles(Path path, boolean recursive) throws IOException {
		return getClassFiles(path, recursive, new ArrayList<Path>());
	}

	/**
	 * Gets all .class files contained within the given path. Allows for the
	 * user to specify files and/or directories to exclude from the returned
	 * {@link Collection}. This function does not allow for recursive searching
	 * of sub-directories.
	 * 
	 * Passing a path to a directory to this function will return a
	 * {@link Collection} containing all .class files contained within that
	 * directory.
	 * 
	 * Passing a path to a file to this function will return a
	 * {@link Collection} containing only that file.
	 * 
	 * @param path
	 *            The path to search for .class files in.
	 * @param excludes
	 *            The {@link Collection} of paths to exclude from the returned
	 *            {@link Collection}.
	 * @return A {@link Collection} of all .class files found within the path.
	 * @throws IOException
	 *             If an error occurs accessing the path.
	 */
	public static final Collection<Path> getClassFiles(Path path, Collection<Path> excludes) throws IOException {
		return getClassFiles(path, false, excludes);
	}

	/**
	 * Gets all .class files contained within the given path. Allows for the
	 * user to specify whether to search sub-directories recursively and to
	 * specify files and/or directories to exclude from the returned
	 * {@link Collection}.
	 * 
	 * Passing a path to a directory to this function will return a
	 * {@link Collection} containing all .class files contained within that
	 * directory.
	 * 
	 * Passing a path to a file to this function will return a
	 * {@link Collection} containing only that file.
	 * 
	 * @param path
	 *            The path to search for .class files in.
	 * @param recursive
	 *            Boolean value to set whether to search recursively.
	 * @param excludes
	 *            The {@link Collection} of paths to exclude from the returned
	 *            {@link Collection}.
	 * @return A {@link Collection} of all .class files found within the path.
	 * @throws IOException
	 *             If an error occurs accessing the path.
	 */
	public static final Collection<Path> getClassFiles(Path path, boolean recursive, Collection<Path> excludes)
			throws IOException {
		Collection<Path> result = new ArrayList<Path>();

		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

			private final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.class");

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) {
				if (dir.equals(path))
					return CONTINUE;

				if (recursive && !excludes.contains(dir))
					return CONTINUE;

				return SKIP_SUBTREE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
				if (matcher.matches(file) && !excludes.contains(file))
					result.add(file);

				return CONTINUE;
			}
		});

		return result;
	}
}
