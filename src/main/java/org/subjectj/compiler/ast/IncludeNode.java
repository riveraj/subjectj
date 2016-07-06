package org.subjectj.compiler.ast;

import java.util.Collection;

import org.subjectj.compiler.ast.visitor.ASTVisitor;

public class IncludeNode implements ASTNode {

	private final Collection<ASTNode> includes;

	public IncludeNode(Collection<ASTNode> includes) {
		this.includes = includes;
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}

	public Collection<ASTNode> getIncludes() {
		return this.includes;
	}
}
