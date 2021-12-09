package com.InterviewTask.FileTransferApi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {

    @JsonProperty("source")
    public Source source;

    @JsonProperty("target")
    public Target target;

}
