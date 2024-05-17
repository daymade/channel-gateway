package tech.typeof.backend.gateway.channel.infrastructure.codec.crypto.sign.js;

import org.graalvm.polyglot.Context;
import org.springframework.stereotype.Component;
import tech.typeof.backend.gateway.channel.infrastructure.codec.crypto.sign.Signaturer;

@Component
public class JsSignaturer implements Signaturer {
    private static final String JS = "js";

    @Override
    public String encrypt(String encryptFunc, String data) {
        return exec("encryptFunc", encryptFunc, new String[]{data});
    }

    @Override
    public String decrypt(String decryptFunc, String encryptedData) {
        return exec("decryptFunc", decryptFunc, new String[]{encryptedData});
    }

    @Override
    public String sign(String signFunc, String data) {
        return exec("signFunc", signFunc, new String[]{data});
    }

    @Override
    public boolean verifySignature(String verifySignFunc, String signedData, String signature) {
        var result = exec("verifySignFunc", verifySignFunc, new String[]{signedData, signature});
        return result.equalsIgnoreCase("OK");
    }

    protected static String exec(String funcName, String funcCode, String[]... args) {
        try (Context context = Context.create()) {

            context.eval(JS, defineFunction(funcName, funcCode));
            var jsFunc = context.getBindings(JS).getMember(funcName);

            // 将 Java 字符串自动转换为 JavaScript 字符串
            context.getBindings(JS).putMember("params", args);
            var jsParams = context.getBindings(JS).getMember("params");

            var result = jsFunc.execute(jsParams);
            return result.asString();
        }
    }

    /**
     * define a function in js engine
     * @param funcName the name of the function is a pre-defined protocol between callee and caller
     * @param funcCode an arrow method accept string returns string, e.g. (str) => str;
     * @return a js function definition, will be eval in js engine, e.g. `let encryptFunc = (str) => str`;
     */
    private static String defineFunction(String funcName, String funcCode) {
        return "let %s = %s".formatted(funcName, funcCode);
    }

}
