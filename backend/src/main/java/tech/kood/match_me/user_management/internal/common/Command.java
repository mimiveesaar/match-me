package tech.kood.match_me.user_management.internal.common;

import java.util.concurrent.CompletableFuture;

public class Command<Request, Result> {
    private final Request request;
    private final CompletableFuture<Result> resultFuture;

    public Command(Request request) {
        this.request = request;
        this.resultFuture = new CompletableFuture<>();
    }

    public Request getRequest() {
        return request;
    }

    public CompletableFuture<Result> getResultFuture() {
        return resultFuture;
    }
}