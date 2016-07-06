package org.subjectj.compiler.ast.visitor;

import java.nio.file.Path;

import org.subjectj.compiler.ast.ASTNode;
import org.subjectj.compiler.ast.DirectoryExcludeNode;
import org.subjectj.compiler.ast.DirectoryNode;
import org.subjectj.compiler.ast.DirectoryRecursiveExcludeNode;
import org.subjectj.compiler.ast.DirectoryRecursiveNode;
import org.subjectj.compiler.ast.ExcludeNode;
import org.subjectj.compiler.ast.FileNode;
import org.subjectj.compiler.ast.IncludeNode;
import org.subjectj.compiler.ast.SubjectJNode;

public class SimplePrintingVisitor implements ASTVisitor {

	@Override
	public void visit(DirectoryNode node) {
		System.out.println("Including directory " + node.getPath().toString());
	}

	@Override
	public void visit(DirectoryExcludeNode node) {
		System.out.println("Including directory " + node.getPath().toString());
		node.getExcludeNode().accept(this);

	}

	@Override
	public void visit(DirectoryRecursiveNode node) {
		System.out.println("Including directory " + node.getPath().toString() + " recursively");

	}

	@Override
	public void visit(DirectoryRecursiveExcludeNode node) {
		System.out.println("Including directory " + node.getPath().toString() + " recursively");
		node.getExcludeNode().accept(this);
	}

	@Override
	public void visit(FileNode node) {
		System.out.println("Including file " + node.getPath().toString());
	}

	@Override
	public void visit(ExcludeNode node) {
		for (Path path : node.getPaths())
			System.out.println("Excluding " + path.toString());
	}

	@Override
	public void visit(IncludeNode node) {
		for (ASTNode path : node.getIncludes())
			path.accept(this);
	}

	@Override
	public void visit(SubjectJNode node) {
		System.out.println("Completed!");
	}
}
