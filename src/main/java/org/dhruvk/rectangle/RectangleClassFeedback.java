package org.dhruvk.rectangle;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

class ShouldHaveConstructor extends VoidVisitorAdapter<AtomicBoolean> {
    @Override
    public void visit(ConstructorDeclaration constructorDeclaration, AtomicBoolean arg) {
        super.visit(constructorDeclaration, arg);
        arg.set(true);
    }
}


public class RectangleClassFeedback implements Rule {

    private final String sourceCode;

    public RectangleClassFeedback(String sourceCode) { // TODO - think about interface again, simple enough to start with though
        this.sourceCode = sourceCode;
    }

    @Override
    public Set<String> suggestionKey() {
        Set<String> feedbacks = new HashSet<>();
        CompilationUnit compilationUnit = StaticJavaParser.parse(sourceCode);

        if(!hasConstructor(compilationUnit)) feedbacks.add("NO_CONSTRUCTOR_FOUND");

        return feedbacks.isEmpty() ? Set.of("UNKNOWN_SCENARIO") : feedbacks;
    }

    private Boolean hasConstructor(CompilationUnit compilationUnit) {
        AtomicBoolean hasConstructor = new AtomicBoolean(false);
        new ShouldHaveConstructor().visit(compilationUnit,hasConstructor);
        return hasConstructor.get();
    }
}
