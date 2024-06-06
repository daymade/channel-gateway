package tech.typeof.backend.gateway.channel.infrastructure.codec.crypto.sign.js;

import lombok.SneakyThrows;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;
import org.junit.jupiter.api.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsSignaturerTest {
    public static final String SOURCE = """
            var N = 2000;
            var EXPECTED = 17393;

            function Natural() {
                x = 2;
                return {
                    'next' : function() { return x++; }
                };
            }

            function Filter(number, filter) {
                var self = this;
                this.number = number;
                this.filter = filter;
                this.accept = function(n) {
                  var filter = self;
                  for (;;) {
                      if (n % filter.number === 0) {
                          return false;
                      }
                      filter = filter.filter;
                      if (filter === null) {
                          break;
                      }
                  }
                  return true;
                };
                return this;
            }

            function Primes(natural) {
                var self = this;
                this.natural = natural;
                this.filter = null;

                this.next = function() {
                    for (;;) {
                        var n = self.natural.next();
                        if (self.filter === null || self.filter.accept(n)) {
                            self.filter = new Filter(n, self.filter);
                            return n;
                        }
                    }
                };
            }

            function primesMain() {
                var primes = new Primes(Natural());
                var primArray = [];
                for (var i=0;i<=N;i++) { primArray.push(primes.next()); }
                if (primArray[N] != EXPECTED) { throw new Error('wrong prime found: '+primArray[N]); }
            }
            """;

    public static final int WARMUP = 30;
    public static final int ITERATIONS = 10;

    private static long benchScriptEngineIntl(ScriptEngine eng) throws IOException {
        long sum = 0L;
        try {
            eng.eval(SOURCE);
            Invocable inv = (Invocable) eng;
            System.out.println("warming up ...");
            for (int i = 0; i < WARMUP; i++) {
                inv.invokeFunction("primesMain");
            }
            System.out.println("warmup finished, now measuring");
            for (int i = 0; i < ITERATIONS; i++) {
                long start = System.currentTimeMillis();
                inv.invokeFunction("primesMain");
                long took = System.currentTimeMillis() - start;
                sum += took;
                System.out.println("iteration: " + (took));
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return sum;
    }

    @SneakyThrows
    @Test
    public void testbenchGraalScriptEngine() {
        System.out.println("=== Graal.js via javax.script.ScriptEngine ===");
        ScriptEngine graaljsEngine = new ScriptEngineManager().getEngineByName("Graal.js");
        if (graaljsEngine == null) {
            System.out.println("*** Graal.js not found ***");
        } else {
            benchScriptEngineIntl(graaljsEngine);
        }
    }

    @Test
    public void testEncrypt() {
        JsSignaturer jsSignaturer = new JsSignaturer();
        String data = "Test Data";
        String result = jsSignaturer.encrypt("(str) => { return JSON.stringify(str); }", data);

        assertEquals("ataD tseT", result, "Encryption output did not match input");
    }

    @Test
    void testEncrypt2() {
        try (Context context = Context.create()) {
            Map<String, Object> params = new HashMap<>();
            params.put("key1", "value1");
            params.put("key2", "value2");

            ProxyObject proxyParams = ProxyObject.fromMap(params);
            context.getBindings("js").putMember("params", proxyParams);
            Value result = context.eval("js", "params.key1 + '-' + params.key2");

            System.out.println(result.asString()); // 输出: value1-value2
            assertEquals("value1-value2", result.asString());
        }
    }

    @Test
    void exec() {
        var jsSignaturer = new JsSignaturer();

        var result = jsSignaturer.exec("testFunc",
                "(args) => { return JSON.stringify(args); }",
                "ttt1", "ttt2");

        assertEquals("""
                ["ttt1","ttt2"]""", result, "Encryption output did not match input");
    }

    @Test
    void sign() {
        var jsSignaturer = new JsSignaturer();

        var result = jsSignaturer.sign("args => args[0]", "data");

        assertEquals("data", result, "Encryption output did not match input");
    }
}