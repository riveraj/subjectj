package org.subjectj.compiler.ast;

import java.nio.file.Path;

import org.subjectj.compiler.ast.visitor.ASTVisitor;

public class DirectoryExcludeNode implements ASTNode {

	private final Path path;
	private final ASTNode exclude;

	public DirectoryExcludeNode(Path path, ASTNode exclude) {
		this.path = path;
		this.exclude = exclude;
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}

	public Path getPath() {
		return this.path;
	}

	public ASTNode getExcludeNode() {
		return this.exclude;
	}
}
