package tech.typeof.backend.gateway.channel.infrastructure.codec.crypto.sign.js;

import org.apache.commons.lang3.StringUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Component;
import tech.typeof.backend.gateway.channel.infrastructure.codec.crypto.sign.Signaturer;

@Component
public class JsSignaturer implements Signaturer {
    private static final String JS = "js";

    protected String exec(String funcName, String funcCode, String... args) {
        if (StringUtils.isBlank(funcCode)) {
            return args.length > 0 ? args[0] : null;
        }

        try (Context context = Context.create()) {
            context.eval(JS, defineFunction(funcName, funcCode));
            var jsFunc = context.getBindings(JS).getMember(funcName);

            // 创建一个新的 JavaScript 数组并将 Java 参数插入到该数组中, 以便在 js 中调用
            Value jsArray = context.eval(JS, "new Array()");
            for (Object arg : args) {
                jsArray.setArrayElement(jsArray.getArraySize(), arg);
            }

            var result = jsFunc.execute(jsArray);
            return result.asString();
        }
    }

    @Override
    public String encrypt(String encryptFunc, String data) {
        return exec("encryptFunc", encryptFunc, data);
    }

    @Override
    public String decrypt(String decryptFunc, String encryptedData) {
        return exec("decryptFunc", decryptFunc, encryptedData);
    }

    @Override
    public String sign(String signFunc, String data) {
        return exec("signFunc", signFunc, data);
    }

    @Override
    public boolean verifySignature(String verifySignFunc, String signedData, String signature) {
        var result = exec("verifySignFunc", verifySignFunc, signedData, signature);
        return "OK".equalsIgnoreCase(result);
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
