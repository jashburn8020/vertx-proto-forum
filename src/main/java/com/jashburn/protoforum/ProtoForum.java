package com.jashburn.protoforum;

import com.jashburn.protoforum.storage.InMemoryStorage;
import com.jashburn.protoforum.storage.Storage;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class ProtoForum extends AbstractVerticle {

	private static final int PORT = 8000;

	private static final String PATH_APP_V1 = "/forum/v1";
	private static final String PATH_FORUM = "/";

	@Override
	public void start(Future<Void> future) {
		Storage storage = new InMemoryStorage();
		ForumHandlers forumHandlers = new ForumHandlers(storage);
		
		Router apiRouter = Router.router(vertx);
		apiRouter.get(PATH_FORUM).handler(forumHandlers::getForum);

		Router mainRouter = Router.router(vertx);
		mainRouter.mountSubRouter(PATH_APP_V1, apiRouter);
		
		vertx.createHttpServer().requestHandler(mainRouter::accept).listen(PORT, result -> {
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
