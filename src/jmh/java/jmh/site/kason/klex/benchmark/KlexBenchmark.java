package jmh.site.kason.klex.benchmark;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import site.kason.klex.StringCharStream;
import site.kason.klex.dfa.DFA;
import site.kason.klex.dfa.DFAMatchResult;
import site.kason.klex.dfa.DFAUtil;
import site.kason.klex.nfa.NFAMatchResult;
import site.kason.klex.nfa.NFA;
import site.kason.klex.nfa.NFAUtil;

/**
 *
 * @author Kason Yang
 */
@Fork(5)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class KlexBenchmark {

  private NFA nfa;

  private DFA dfa;

  private final String content = "abcdefghijklmnopqrstuvwxyz0123456789";

  @Setup
  public void setup() {
    nfa = NFAUtil.range('a', 'z').or(NFAUtil.oneOf('_')).concat(
            NFAUtil.range('a', 'z').or(NFAUtil.range('0', '9')).or(NFAUtil.oneOf('_')).closure()
    );
    dfa = DFAUtil.buildFromNFA(nfa);
    int contentLen = content.length();
    DFAMatchResult dfaResult = this.benchmarkDFA();
    NFAMatchResult nfaResult = this.benchmarkNFA();
    if(dfaResult.getMatchedLength()!=contentLen || nfaResult.getMatchedLength()!=contentLen){
      throw new RuntimeException("unexpected result");
    }
  }

  @Benchmark
  public DFAMatchResult benchmarkDFA() {
    return dfa.match(new StringCharStream(content));
  }

  @Benchmark
  public NFAMatchResult benchmarkNFA() {
    return nfa.match(new StringCharStream(content));
  }

}
