package org.subjectj.compiler.ast;

import java.nio.file.Path;

import org.subjectj.compiler.ast.visitor.ASTVisitor;

public class DirectoryRecursiveNode implements ASTNode {

	private final Path path;

	public DirectoryRecursiveNode(Path path) {
		this.path = path;
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}

	public Path getPath() {
		return this.path;
	}
}
