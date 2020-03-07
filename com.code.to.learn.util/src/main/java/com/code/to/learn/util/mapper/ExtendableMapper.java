package com.code.to.learn.util.mapper;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ExtendableMapper<IN, OUT> {

    private final ModelMapper modelMapper;

    protected ExtendableMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public IN toInput(OUT output) {
        return modelMapper.map(output, getInputClass());
    }

    public List<IN> toInput(List<OUT> outputs) {
        return outputs.stream()
                .map(this::toInput)
                .collect(Collectors.toList());
    }

    public OUT toOutput(IN input) {
        return modelMapper.map(input, getOutputClass());
    }

    public List<OUT> toOutput(List<IN> inputs) {
        return inputs.stream()
                .map(this::toOutput)
                .collect(Collectors.toList());
    }

    protected ModelMapper getMapper() {
        return modelMapper;
    }

    protected abstract Class<IN> getInputClass();

    protected abstract Class<OUT> getOutputClass();

}
