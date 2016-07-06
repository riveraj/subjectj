package org.subjectj.compiler.ast;

import org.subjectj.compiler.ast.visitor.ASTVisitor;

public interface ASTNode {

	public void accept(ASTVisitor visitor);
}
