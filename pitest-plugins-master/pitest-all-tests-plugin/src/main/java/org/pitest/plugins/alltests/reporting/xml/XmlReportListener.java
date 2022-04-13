package org.pitest.plugins.alltests.reporting.xml;


import static org.pitest.plugins.alltests.reporting.xml.Tag.index;
import static org.pitest.plugins.alltests.reporting.xml.Tag.killingTest;
import static org.pitest.plugins.alltests.reporting.xml.Tag.lineNumber;
import static org.pitest.plugins.alltests.reporting.xml.Tag.methodDescription;
import static org.pitest.plugins.alltests.reporting.xml.Tag.mutatedClass;
import static org.pitest.plugins.alltests.reporting.xml.Tag.mutatedMethod;
import static org.pitest.plugins.alltests.reporting.xml.Tag.mutation;
import static org.pitest.plugins.alltests.reporting.xml.Tag.mutator;
import static org.pitest.plugins.alltests.reporting.xml.Tag.sourceFile;

import java.io.IOException;
import java.io.Writer;

import org.pitest.functional.Option;
import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.util.ResultOutputStrategy;
import org.pitest.util.StringUtil;
import org.pitest.util.Unchecked;

enum Tag {
    mutation, sourceFile, mutatedClass, mutatedMethod, methodDescription, lineNumber, mutator, index, killingTest;
}

class XMLReportListener implements MutationResultListener {

    private final Writer out;

    public XMLReportListener(final ResultOutputStrategy outputStrategy) {
        this(outputStrategy.createWriterForFile("mutations.xml"));
    }

    public XMLReportListener(final Writer out) {
        this.out = out;
    }

    private void writeResult(final ClassMutationResults metaData) {
        for (final MutationResult mutation : metaData.getMutations()) {
            writeMutationResultXML(mutation);
        }
    }

    private void writeMutationResultXML(final MutationResult result) {
        write(makeNode(makeMutationNode(result), makeMutationAttributes(result),
                mutation) + "\n");
    }

    private String makeMutationAttributes(final MutationResult result) {
        return "detected='" + result.getStatus().isDetected() + "' status='"
                + result.getStatus() + "'";
    }

    private String makeMutationNode(final MutationResult mutation) {
        final MutationDetails details = mutation.getDetails();
        return makeNode(clean(details.getFilename()), sourceFile)
                + makeNode(clean(details.getClassName().asJavaName()), mutatedClass)
                + makeNode(clean(details.getMethod().name()), mutatedMethod)
                + makeNode(clean(details.getId().getLocation().getMethodDesc()), methodDescription)
                + makeNode("" + details.getLineNumber(), lineNumber)
                + makeNode(clean(details.getMutator()), mutator)
                + makeNode("" + details.getFirstIndex(), index)
                + makeNode(createKillingTestDesc(mutation.getKillingTest()),
                killingTest);
    }

    private String clean(final String value) {
        return StringUtil.escapeBasicHtmlChars(value);
    }

    private String makeNode(final String value, final String attributes,
                            final Tag tag) {
        if (value != null) {
            return "<" + tag + " " + attributes + ">" + value + "";
        } else {
            return "<" + tag + attributes + "/>";
        }

    }

    private String makeNode(final String value, final Tag tag) {
        if (value != null) {
            return "<" + tag + ">" + value + "";
        } else {
            return "<" + tag + "/>";
        }
    }

    private String createKillingTestDesc(final Option killingTest) {
        if (killingTest.hasSome()) {
            return clean((String) killingTest.value());
        } else {
            return null;
        }
    }

    private void write(final String value) {
        try {
            this.out.write(value);
        } catch (final IOException e) {
            throw Unchecked.translateCheckedException(e);
        }
    }

    public void runStart() {
        write("\n");
        write("\n");
    }

    public void handleMutationResult(final ClassMutationResults metaData) {
        writeResult(metaData);
    }

    public void runEnd() {
        try {
            write("\n");
            this.out.close();
        } catch (final IOException e) {
            throw Unchecked.translateCheckedException(e);
        }
    }
}