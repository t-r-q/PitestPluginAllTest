
package org.pitest.plugins.alltests.reporting.csv;

import org.pitest.functional.Option;
import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.util.ResultOutputStrategy;
import org.pitest.util.Unchecked;

import java.io.IOException;
import java.io.Writer;

public class CsvReportListener implements MutationResultListener {

  private final Writer out;

  public CsvReportListener(final ResultOutputStrategy outputStrategy) {
    this(outputStrategy.createWriterForFile("mutations.csv"));
  }

  public CsvReportListener(final Writer out) {
    this.out = out;
  }

  private String createKillingTestDesc(final Option<String> killingTest) {
    if (killingTest.hasSome()) {
      return killingTest.value();
    } else {
      return "none";
    }
  }

  private String makeCsv(final Object... os) {
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i != os.length; i++) {
      sb.append(os[i].toString());
      if (i != (os.length - 1)) {
        sb.append(",");
      }
    }
    return sb.toString();
  }

  public void runStart() {
    
  }

  public void runEnd() {
    try {
      this.out.close();
    } catch (final IOException e) {
      throw Unchecked.translateCheckedException(e);
    }
  }

  public void handleMutationResult(final ClassMutationResults metaData) {
    try {

      for (final MutationResult mutation : metaData.getMutations()) {
        this.out.write(makeCsv(mutation.getDetails().getFilename(), mutation
            .getDetails().getClassName().asJavaName(), mutation.getDetails()
            .getMutator(), mutation.getDetails().getMethod(), mutation
            .getDetails().getLineNumber(), mutation.getStatus(),
            createKillingTestDesc(mutation.getKillingTest()))
            + System.getProperty("line.separator"));
      }

    } catch (final IOException ex) {
      throw Unchecked.translateCheckedException(ex);
    }

  }

}
