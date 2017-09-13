package com.jashburn.protoforum;

import com.jashburn.protoforum.storage.InMemoryStorage;
import com.jashburn.protoforum.storage.Storage;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class ProtoForum extends AbstractVerticle {

	public static final int PORT = 8000;

	public static final String PATH_APP_V1 = "/forum/v1";
	public static final String PATH_FORUM = "/";
	public static final String PATH_THREADS = "/threads";

	@Override
	public void start(Future<Void> future) {
		Storage storage = new InMemoryStorage();
		ForumHandlers forumHandlers = new ForumHandlers(storage);
		ThreadHandlers threadHandlers = new ThreadHandlers(storage);

		Router apiRouter = Router.router(vertx);
		apiRouter.get(PATH_FORUM).handler(forumHandlers::getForum);

		apiRouter.route(PATH_THREADS).handler(BodyHandler.create());
		apiRouter.post(PATH_THREADS).consumes(HttpHeaderValues.APPLICATION_JSON.toString())
			.produces(HttpHeaderValues.APPLICATION_JSON.toString()).handler(threadHandlers::createThread);

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
