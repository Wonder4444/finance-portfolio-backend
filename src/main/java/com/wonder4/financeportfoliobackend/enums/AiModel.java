package com.wonder4.financeportfoliobackend.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AiModel {
    LLAMA_3_2("llama3.2"),
    GPT_OSS("gpt-oss:120b-cloud"),
    KIMI_K2_5("kimi-k2.5:cloud");

    private final String modelName;

    AiModel(String modelName) {
        this.modelName = modelName;
    }

    @JsonValue
    public String getModelName() {
        return modelName;
    }

    @JsonCreator
    public static AiModel fromString(String text) {
        for (AiModel model : AiModel.values()) {
            if (model.modelName.equalsIgnoreCase(text)) {
                return model;
            }
        }
        throw new IllegalArgumentException("Unsupported AI model: " + text);
    }
}
