package com.code.to.learn.process.steps;

import com.code.to.learn.api.model.UserBindingModel;
import com.code.to.learn.process.constant.Constants;
import com.code.to.learn.process.parser.ParserFactory;
import com.code.to.learn.process.parser.ParserType;
import org.flowable.engine.delegate.DelegateExecution;

public final class StepsUtil {

    private StepsUtil() {
    }

    public static void setUserBindingModel(DelegateExecution execution, UserBindingModel userBindingModel) {
        setValueAsJson(Constants.USER_BINDING_MODEL_KEY, execution, userBindingModel);
    }

    private static <T> void setValueAsJson(String key, DelegateExecution execution, T obj) {
        String serializedObject = ParserFactory.createParser(ParserType.JSON).serialize(obj);
        execution.setVariable(key, serializedObject);
    }

    private static <T> T getValueFromJson(String key, DelegateExecution execution, Class<T> clazz) {
        String deserializedObject = (String) execution.getVariable(key);
        return ParserFactory.createParser(ParserType.JSON).deserialize(deserializedObject, clazz);
    }
}
