package com.jashburn.protoforum;

import java.util.*;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class ProtoForum extends AbstractVerticle {

	private static final int PORT = 8000;

	@Override
	public void start(Future<Void> future) {
		Router router = Router.router(vertx);


		vertx.createHttpServer().requestHandler(router::accept).listen(PORT, result -> {
			if (result.succeeded()) {
				future.complete();
			} else {
				future.fail(result.cause());
			}
		});
	}

	public static void main(String[] args) {
		ProtoForum forum = new ProtoForum();

		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(forum);
	}

}
